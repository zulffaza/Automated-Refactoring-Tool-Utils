package com.finalproject.automated.refactoring.tool.utils.service.implementation;

import com.finalproject.automated.refactoring.tool.utils.model.request.NormalizeGenericsVA;
import com.finalproject.automated.refactoring.tool.utils.service.MergeListHelper;
import com.finalproject.automated.refactoring.tool.utils.service.VariableHelper;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 25 May 2019
 */

@Service
public class VariableHelperImpl implements VariableHelper {

    @Autowired
    private MergeListHelper mergeListHelper;

    private static final Character UNDERSCORE_CHARACTER = '_';

    private static final String WHITESPACE_REGEX = "\\s";
    private static final String SPACE_DELIMITER = " ";
    private static final String ESCAPE = "\\";
    private static final String DOUBLE_QUOTES = "\"";
    private static final String EMPTY_STRING = "";
    private static final String GROUP_ZERO_REGEX = "$0";
    private static final String OPEN_PARENTHESES = "(";
    private static final String CLOSE_PARENTHESES = ")";
    private static final String DOUBLE_COLON = "::";
    private static final String POINT = ".";
    private static final String LESS_THAN = "<";
    private static final String GREATER_THAN = ">";
    private static final String COMMA_SEPARATOR = ", ";
    private static final String OPEN_SQUARE_BRACKETS = "[";
    private static final String CLOSE_SQUARE_BRACKETS = "]";

    private static final Integer FIRST_INDEX = 0;
    private static final Integer SECOND_INDEX = 1;
    private static final Integer SINGLE_LIST_SIZE = 1;
    private static final Integer NEXT_CHAR_INDEX_METHOD_REFERENCE = 2;

    @Override
    public List<String> readVariable(@NonNull String statement) {
        List<String> variables = Arrays.asList(statement.split(WHITESPACE_REGEX));
        variables = new ArrayList<>(variables);

        return doReadVariable(variables);
    }

    private List<String> doReadVariable(List<String> variables) {
        variables = variables.stream()
                .flatMap(this::removeUnusedCharacter)
                .collect(Collectors.toList());

        normalizeString(variables);
        normalizeGenerics(variables);

        return variables.stream()
                .filter(this::isNotString)
                .flatMap(this::splitArrayVariables)
                .filter(this::isVariable)
                .collect(Collectors.toList());
    }

    private Stream<String> removeUnusedCharacter(String variable) {
        variable = variable.replaceAll(Pattern.quote(OPEN_PARENTHESES),
                GROUP_ZERO_REGEX + SPACE_DELIMITER);
        variable = variable.replaceAll(Pattern.quote(CLOSE_PARENTHESES),
                SPACE_DELIMITER + GROUP_ZERO_REGEX + SPACE_DELIMITER);
        variable = changeMethodReferenceIntoMethodCall(variable).replace(DOUBLE_COLON, POINT);
        variable = variable.replaceAll(Pattern.quote(POINT), SPACE_DELIMITER + POINT);
        variable = variable.replaceAll(UNUSED_CHARACTERS_REGEX, EMPTY_STRING);
        variable = variable.replaceAll(OPERATORS_CHARACTERS_REGEX,
                SPACE_DELIMITER + GROUP_ZERO_REGEX + SPACE_DELIMITER);

        return splitMethodParameters(variable);
    }

    private String changeMethodReferenceIntoMethodCall(String variable) {
        Integer index = variable.indexOf(DOUBLE_COLON);

        while (index >= FIRST_INDEX) {
            variable = createMethodCallFromMethodReference(variable, index);
            index = variable.indexOf(DOUBLE_COLON, ++index);
        }

        return variable;
    }

    private String createMethodCallFromMethodReference(String variable, Integer index) {
        index += NEXT_CHAR_INDEX_METHOD_REFERENCE;

        while (isEndOfMethodReferenceFound(variable, index)) {
            index++;
        }

        return variable.substring(FIRST_INDEX, index) + OPEN_PARENTHESES +
                SPACE_DELIMITER + variable.substring(index);
    }

    private Boolean isEndOfMethodReferenceFound(String variable, Integer index) {
        return index < variable.length() && !variable.startsWith(SPACE_DELIMITER, index);
    }

    private Stream<String> splitMethodParameters(String variable) {
        List<String> split = Arrays.asList(variable.split(WHITESPACE_REGEX));
        split = removeEmptyString(split);

        removeCastingType(split);
        removeStaticClass(split);

        return split.stream();
    }

    private List<String> removeEmptyString(List<String> split) {
        split = new ArrayList<>(split);
        split.removeIf(String::isEmpty);

        return split;
    }

    private void removeCastingType(List<String> split) {
        List<Integer> removeIndex = new ArrayList<>();

        for (Integer index = FIRST_INDEX; index < (split.size() - SECOND_INDEX); index++) {
            if (isRemoveIndex(split, index, split.get(index + SECOND_INDEX))) {
                removeIndex.add(index);
            }
        }

        for (Integer index = removeIndex.size() - SECOND_INDEX; index >= FIRST_INDEX; index--) {
            split.remove(removeIndex.get(index).intValue());
        }
    }

    private Boolean isRemoveIndex(List<String> split, Integer index, String nextVariable) {
        return isContainsKeywordsOrClass(split, index) && nextVariable.equals(CLOSE_PARENTHESES);
    }

    private void removeStaticClass(List<String> split) {
        Integer maxSize = split.size() - SECOND_INDEX;
        AtomicBoolean isCallMethod = new AtomicBoolean();

        for (Integer index = FIRST_INDEX; index < maxSize; index++) {
            String variable = split.get(index);
            String nextVariable = split.get(index + SECOND_INDEX);

            if (isStaticMethodCalls(variable, nextVariable)) {
                flagToIgnore(split, index);
            }

            checkIfCallMethod(variable, isCallMethod);
        }

        normalizeCalledMethod(split, isCallMethod);
        normalizeKeywordsCalledMethod(split);
    }

    private Boolean isStaticMethodCalls(String variable, String nextVariable) {
        return isClassName(variable) && nextVariable.contains(POINT);
    }

    private void flagToIgnore(List<String> split, Integer index) {
        split.set(index, POINT + split.get(index));
    }

    private void checkIfCallMethod(String variable, AtomicBoolean isCallMethod) {
        if (variable.contains(OPEN_PARENTHESES)) {
            isCallMethod.set(Boolean.TRUE);
        }
    }

    private void normalizeCalledMethod(List<String> split, AtomicBoolean isCallMethod) {
        if (isNeedNormalization(split, isCallMethod)) {
            doNormalizeCalledMethod(split);
        }
    }

    private Boolean isNeedNormalization(List<String> split, AtomicBoolean isCallMethod) {
        boolean isCallField = split.stream()
                .anyMatch(this::isCalledField);

        return isCallField && !isCallMethod.get();
    }

    private Boolean isCalledField(String variable) {
        return variable.contains(POINT);
    }

    private void doNormalizeCalledMethod(List<String> split) {
        List<String> removeElements = split.subList(SECOND_INDEX, split.size());

        split.set(FIRST_INDEX, split.get(FIRST_INDEX)
                .replaceAll(Pattern.quote(POINT), EMPTY_STRING));
        split.set(FIRST_INDEX, split.get(FIRST_INDEX) + String.join(EMPTY_STRING, removeElements));
        split.removeAll(removeElements);
    }

    private void normalizeKeywordsCalledMethod(List<String> split) {
        if (split.size() > SINGLE_LIST_SIZE) {
            doNormalizeKeywordsCalledMethod(split);
        }
    }

    private void doNormalizeKeywordsCalledMethod(List<String> split) {
        List<Integer> mergeIndex = new ArrayList<>();
        Integer maxSize = split.size() - SINGLE_LIST_SIZE;

        for (Integer index = FIRST_INDEX; index < maxSize; index++) {
            if (isKeywordsCalledMethod(index, split)) {
                saveIndexAndNextIndex(index, mergeIndex);
            }
        }

        mergeListHelper.mergeListOfString(split, mergeIndex, EMPTY_STRING);
    }

    private Boolean isKeywordsCalledMethod(Integer index, List<String> split) {
        String variable = split.get(index);
        String nextVariable = split.get(++index);

        return KEYWORDS.contains(variable) && nextVariable.startsWith(POINT);
    }

    private void saveIndexAndNextIndex(Integer index, List<Integer> mergeIndex) {
        mergeIndex.add(index);
        mergeIndex.add(++index);
    }

    private void normalizeString(List<String> variables) {
        List<Integer> mergeIndex = new ArrayList<>();
        AtomicBoolean isString = new AtomicBoolean();

        for (Integer index = FIRST_INDEX; index < variables.size(); index++) {
            String variable = variables.get(index);

            if (isMergePoint(variable, isString)) {
                mergeIndex.add(index);
            }
        }

        mergeListHelper.mergeListOfString(variables, mergeIndex, SPACE_DELIMITER);
    }

    private Boolean isMergePoint(String variable, AtomicBoolean isString) {
        AtomicBoolean isEscape = new AtomicBoolean();
        Boolean lastIsString = isString.get();

        for (Integer index = FIRST_INDEX; index < variable.length(); index++) {
            String character = variable.substring(index, index + SECOND_INDEX);

            if (isLastEscape(isEscape)) {
                continue;
            }

            ifEscape(character, isEscape);
            ifString(character, isString);
        }

        return isVariableString(lastIsString, isString.get());
    }

    private Boolean isLastEscape(AtomicBoolean isEscape) {
        Boolean isLastEscape = isEscape.get();

        if (isLastEscape) {
            isEscape.set(Boolean.FALSE);
        }

        return isLastEscape;
    }

    private void ifEscape(String character, AtomicBoolean isEscape) {
        if (character.equals(ESCAPE)) {
            isEscape.set(Boolean.TRUE);
        }
    }

    private void ifString(String character, AtomicBoolean isString) {
        if (character.equals(DOUBLE_QUOTES)) {
            isString.set(!isString.get());
        }
    }

    private Boolean isVariableString(Boolean lastIsString, Boolean isString) {
        return lastIsString != isString;
    }

    private void normalizeGenerics(List<String> variables) {
        Integer maxSize = variables.size() - SECOND_INDEX;

        NormalizeGenericsVA normalizeGenericsVA = NormalizeGenericsVA.builder()
                .build();

        for (Integer index = FIRST_INDEX; index < maxSize; index++) {
            normalizeGenericsVA.setVariable(variables.get(index));
            normalizeGenericsVA.setNextVariable(variables.get(index + SECOND_INDEX));

            checkGenerics(index, normalizeGenericsVA);
        }

        beautifyGenerics(variables, normalizeGenericsVA.getMergeIndex());
        mergeListHelper.mergeListOfString(variables, normalizeGenericsVA.getMergeIndex(), EMPTY_STRING);
    }

    private void checkGenerics(Integer index, NormalizeGenericsVA normalizeGenericsVA) {
        if (isOpeningGenerics(normalizeGenericsVA)) {
            saveOpeningGenericsIndex(index, normalizeGenericsVA);
        }

        if (normalizeGenericsVA.getVariable().equals(LESS_THAN)) {
            normalizeGenericsVA.getStack().push(LESS_THAN);
        }

        if (normalizeGenericsVA.getVariable().contains(GREATER_THAN)) {
            normalizeClosingGenerics(index, normalizeGenericsVA);
        }
    }

    private void saveOpeningGenericsIndex(Integer index,
                                          NormalizeGenericsVA normalizeGenericsVA) {
        normalizeGenericsVA.getIsGenerics().set(Boolean.TRUE);
        normalizeGenericsVA.getMergeIndex().add(index);
    }

    private Boolean isOpeningGenerics(NormalizeGenericsVA normalizeGenericsVA) {
        return isClassName(normalizeGenericsVA.getVariable()) &&
                normalizeGenericsVA.getNextVariable().contains(LESS_THAN) &&
                normalizeGenericsVA.getStack().isEmpty() &&
                !normalizeGenericsVA.getIsGenerics().get();
    }

    private void normalizeClosingGenerics(Integer index,
                                          NormalizeGenericsVA normalizeGenericsVA) {
        IntStream.range(FIRST_INDEX, normalizeGenericsVA.getVariable().length())
                .forEach(value -> {
                    if (!normalizeGenericsVA.getStack().isEmpty()) {
                        normalizeGenericsVA.getStack().pop();
                    }
                });

        if (isClosingGenerics(normalizeGenericsVA)) {
            saveClosingGenericsIndex(index, normalizeGenericsVA);
        }
    }

    private Boolean isClosingGenerics(NormalizeGenericsVA normalizeGenericsVA) {
        return normalizeGenericsVA.getStack().isEmpty() &&
                normalizeGenericsVA.getIsGenerics().get();
    }

    private void saveClosingGenericsIndex(Integer index,
                                          NormalizeGenericsVA normalizeGenericsVA) {
        if (normalizeGenericsVA.getNextVariable().equals(OPEN_PARENTHESES)) {
            index++;
        }

        normalizeGenericsVA.getIsGenerics().set(Boolean.FALSE);
        normalizeGenericsVA.getMergeIndex().add(index);
    }

    private void beautifyGenerics(List<String> variables, List<Integer> mergeIndex) {
        Integer maxSize = mergeIndex.size() - SECOND_INDEX;

        for (Integer index = FIRST_INDEX; index < maxSize; index++) {
            Integer startPoint = mergeIndex.get(index);
            Integer endPoint = mergeIndex.get(++index);

            beautifyGenericsForRange(variables, startPoint, endPoint);
        }
    }

    private void beautifyGenericsForRange(List<String> variables, Integer startPoint, Integer endPoint) {
        if (variables.get(endPoint).equals(OPEN_PARENTHESES)) {
            endPoint--;
        }

        for (Integer index = startPoint; index < endPoint; index++) {
            String variable = variables.get(index);
            String nextVariable = variables.get(index + SECOND_INDEX);

            if (isNeedCommaSeparator(variable, nextVariable)) {
                variables.set(index, variable + COMMA_SEPARATOR);
            }
        }
    }

    private Boolean isNeedCommaSeparator(String variable, String nextVariable) {
        return (isClassName(variable) || variable.contains(GREATER_THAN)) &&
                !nextVariable.contains(LESS_THAN) && !nextVariable.contains(GREATER_THAN);
    }

    private Boolean isNotString(String variable) {
        return !variable.contains(DOUBLE_QUOTES);
    }

    private Stream<String> splitArrayVariables(String variable) {
        List<String> split = Arrays.asList(variable.split(Pattern.quote(OPEN_SQUARE_BRACKETS)));
        split = removeEmptyString(split);

        if (split.size() > SINGLE_LIST_SIZE) {
            customizeArrayVariables(split);
        }

        return split.stream();
    }

    private void customizeArrayVariables(List<String> split) {
        if (split.get(SECOND_INDEX).equals(CLOSE_SQUARE_BRACKETS)) {
            undoSplitArrayVariables(split);
        } else {
            removeFirstIndex(split);
        }
    }

    private void undoSplitArrayVariables(List<String> split) {
        split.set(FIRST_INDEX, String.join(OPEN_SQUARE_BRACKETS,
                split.get(FIRST_INDEX), split.get(SECOND_INDEX)));
        split.remove(SECOND_INDEX.intValue());
    }

    private void removeFirstIndex(List<String> split) {
        for (Integer index = FIRST_INDEX; index < split.size(); index++) {
            split.set(index, removeCloseSquareBrackets(split.get(index)));
        }

        if (isContainsKeywordsOrClass(split, FIRST_INDEX)) {
            split.remove(FIRST_INDEX.intValue());
        }
    }

    private String removeCloseSquareBrackets(String variable) {
        return variable.replace(CLOSE_SQUARE_BRACKETS, EMPTY_STRING);
    }

    private Boolean isContainsKeywordsOrClass(List<String> split, Integer index) {
        String variable = split.get(index);

        return split.size() > SINGLE_LIST_SIZE &&
                (KEYWORDS.contains(variable) || PRIMITIVE_TYPES.contains(variable) || isClassName(variable));
    }

    private Boolean isVariable(String variable) {
        return (variable.matches(VARIABLE_NAME_REGEX) && !KEYWORDS.contains(variable)) ||
                variable.matches(OPERATORS_CHARACTERS_REGEX);
    }

    public Boolean isClassName(@NonNull String variable) {
        return Character.isUpperCase(variable.charAt(FIRST_INDEX)) && !isConst(variable);
    }

    private Boolean isConst(String variable) {
        Integer index;

        for (index = FIRST_INDEX; index < variable.length(); index++) {
            if (!isComplete(variable.charAt(index))) {
                break;
            }
        }

        return isSuccess(variable, index);
    }

    private Boolean isComplete(Character character) {
        return Character.isUpperCase(character) || character.equals(UNDERSCORE_CHARACTER);
    }

    private Boolean isSuccess(String variable, Integer index) {
        return index == variable.length();
    }
}
