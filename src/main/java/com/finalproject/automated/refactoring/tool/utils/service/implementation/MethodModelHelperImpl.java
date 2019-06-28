package com.finalproject.automated.refactoring.tool.utils.service.implementation;

import com.finalproject.automated.refactoring.tool.model.MethodModel;
import com.finalproject.automated.refactoring.tool.model.PropertyModel;
import com.finalproject.automated.refactoring.tool.utils.model.request.CreateMethodStringVA;
import com.finalproject.automated.refactoring.tool.utils.service.MethodModelHelper;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 16 June 2019
 */

@Service
public class MethodModelHelperImpl implements MethodModelHelper {

    private static final String SPACE = " ";
    private static final String ENTER = "\n";
    private static final String AT = "@";
    private static final String OPEN_PARENTHESES = "(";
    private static final String CLOSE_PARENTHESES = ")";
    private static final String COMMA = ",";
    private static final String THROWS = "throws";
    private static final String OPEN_CURLY_BRACKETS = "{";
    private static final String CLOSE_CURLY_BRACKETS = "}";
    private static final String TAB = "\t";
    private static final String OPTIONAL_WHITESPACE_REGEX = "(?:\\s)*";
    private static final String REQUIRED_WHITESPACE_REGEX = "(?:\\s)+";
    private static final String PLUS = "+";
    private static final String MINUS = "-";
    private static final String MULTIPLY = "*";
    private static final String OR = "|";
    private static final String POINT = ".";
    private static final String POW_SIGN = "^";
    private static final String DOLLAR_SIGN = "$";
    private static final String OPEN_SQUARE_BRACKETS = "[";
    private static final String CLOSE_SQUARE_BRACKETS = "]";
    private static final String QUESTION_MARK = "?";
    private static final String ESCAPE = "\\";

    private static final Integer FIRST_INDEX = 0;
    private static final Integer ONE = 1;

    private static final List<String> REGEX_KEYWORDS = Arrays.asList(
            ESCAPE,
            OPEN_PARENTHESES,
            CLOSE_PARENTHESES,
            PLUS,
            MINUS,
            MULTIPLY,
            OR,
            POINT,
            POW_SIGN,
            DOLLAR_SIGN,
            OPEN_CURLY_BRACKETS,
            CLOSE_CURLY_BRACKETS,
            OPEN_SQUARE_BRACKETS,
            CLOSE_SQUARE_BRACKETS,
            QUESTION_MARK
    );

    @Override
    public String createMethod(@NonNull MethodModel methodModel) {
        StringBuilder method = new StringBuilder();

        buildMethodKeywords(methodModel, method, Boolean.FALSE);
        buildMethodReturnType(methodModel, method, Boolean.FALSE);

        method.append(methodModel.getName());

        buildMethodParameters(methodModel, method, Boolean.FALSE);
        buildMethodExceptions(methodModel, method, Boolean.FALSE);
        buildMethodBody(methodModel, method, Boolean.FALSE);

        return method.toString();
    }

    @Override
    public String createMethodRegex(@NonNull MethodModel methodModel) {
        StringBuilder method = new StringBuilder();

        buildMethodKeywords(methodModel, method, Boolean.TRUE);
        buildMethodReturnType(methodModel, method, Boolean.TRUE);

        method.append(methodModel.getName());
        method.append(OPTIONAL_WHITESPACE_REGEX);

        buildMethodParameters(methodModel, method, Boolean.TRUE);
        buildMethodExceptions(methodModel, method, Boolean.TRUE);
        buildMethodBody(methodModel, method, Boolean.TRUE);

        return method.toString();
    }

    private void buildMethodKeywords(MethodModel methodModel, StringBuilder method,
                                     Boolean isRegex) {
        Integer maxSize = methodModel.getKeywords().size() - ONE;

        for (Integer index = FIRST_INDEX; index < methodModel.getKeywords().size(); index++) {
            CreateMethodStringVA<String> createMethodStringVA = CreateMethodStringVA.<String>builder()
                    .property(methodModel.getKeywords().get(index))
                    .index(index)
                    .maxSize(maxSize)
                    .isRegex(isRegex)
                    .build();

            method.append(createMethodKeyword(createMethodStringVA));
        }
    }

    private String createMethodKeyword(CreateMethodStringVA<String> createMethodStringVA) {
        String keyword = createMethodStringVA.getProperty();

        if (createMethodStringVA.getIsRegex()) {
            keyword = createRegexWithoutWhitespace(keyword);
        } else {
            keyword = createNonRegexMethodKeyword(createMethodStringVA);
        }

        return keyword;
    }

    private String createRegexWithoutWhitespace(String keyword) {
        keyword = createRegexString(keyword);
        keyword = replaceWhitespace(keyword);

        return keyword;
    }

    private String createRegexString(String keyword) {
        keyword = replaceRegexKeywords(keyword);
        keyword += OPTIONAL_WHITESPACE_REGEX;

        return keyword;
    }

    private String replaceWhitespace(String string) {
        return string.replaceAll(REQUIRED_WHITESPACE_REGEX, Matcher.quoteReplacement(OPTIONAL_WHITESPACE_REGEX));
    }

    private String createNonRegexMethodKeyword(CreateMethodStringVA<String> createMethodStringVA) {
        String keyword = createMethodStringVA.getProperty();

        if (isKeywordNeedEnter(createMethodStringVA)) {
            keyword += ENTER;
        } else {
            keyword += SPACE;
        }

        return keyword;
    }

    private Boolean isKeywordNeedEnter(CreateMethodStringVA<String> createMethodStringVA) {
        return !createMethodStringVA.getIndex().equals(createMethodStringVA.getMaxSize()) &&
                createMethodStringVA.getProperty().startsWith(AT);
    }

    private void buildMethodReturnType(MethodModel methodModel, StringBuilder method,
                                       Boolean isRegex) {
        if (isHasReturnType(methodModel)) {
            method.append(createMethodReturnType(methodModel, isRegex));
        }
    }

    private String createMethodReturnType(MethodModel methodModel, Boolean isRegex) {
        String returnType = methodModel.getReturnType();

        if (isRegex) {
            returnType = createRegexWithoutWhitespace(returnType);
        } else {
            returnType += SPACE;
        }

        return returnType;
    }

    private Boolean isHasReturnType(MethodModel methodModel) {
        Optional<String> returnType = Optional.ofNullable(methodModel.getReturnType());
        return returnType.isPresent() && !returnType.get().isEmpty();
    }

    private void buildMethodParameters(MethodModel methodModel, StringBuilder method,
                                       Boolean isRegex) {
        Integer maxSize = methodModel.getParameters().size() - ONE;

        appendString(method, isRegex, OPEN_PARENTHESES);

        for (Integer index = FIRST_INDEX; index < methodModel.getParameters().size(); index++) {
            CreateMethodStringVA<PropertyModel> createMethodStringVA = CreateMethodStringVA.<PropertyModel>builder()
                    .property(methodModel.getParameters().get(index))
                    .index(index)
                    .maxSize(maxSize)
                    .isRegex(isRegex)
                    .build();

            method.append(createMethodParameter(createMethodStringVA));
        }

        appendString(method, isRegex, CLOSE_PARENTHESES);

        if (!isRegex) {
            method.append(SPACE);
        }
    }

    private void appendString(StringBuilder method, Boolean isRegex, String string) {
        if (isRegex) {
            method.append(createRegexWithoutWhitespace(string));
        } else {
            method.append(string);
        }
    }

    private String createMethodParameter(CreateMethodStringVA<PropertyModel> createMethodStringVA) {
        StringBuilder parameter = new StringBuilder();

        parameter.append(buildParameterKeywords(
                createMethodStringVA.getProperty().getKeywords(), createMethodStringVA.getIsRegex()));
        createMethodParameterTypeAndName(createMethodStringVA, parameter);

        if (!createMethodStringVA.getIndex().equals(createMethodStringVA.getMaxSize())) {
            appendString(parameter, createMethodStringVA.getIsRegex(), COMMA);

            if (!createMethodStringVA.getIsRegex()) {
                parameter.append(SPACE);
            }
        }

        return parameter.toString();
    }

    private void createMethodParameterTypeAndName(CreateMethodStringVA<PropertyModel> createMethodStringVA,
                                                  StringBuilder parameter) {
        appendString(parameter, createMethodStringVA.getIsRegex(), createMethodStringVA.getProperty().getType());

        if (!createMethodStringVA.getIsRegex()) {
            parameter.append(SPACE);
        }

        appendString(parameter, createMethodStringVA.getIsRegex(), createMethodStringVA.getProperty().getName());
    }

    private String buildParameterKeywords(List<String> keywords, Boolean isRegex) {
        Integer maxSize = keywords.size() - ONE;
        StringBuilder keyword = new StringBuilder();

        for (Integer index = FIRST_INDEX; index < keywords.size(); index++) {
            CreateMethodStringVA<String> createMethodStringVA = CreateMethodStringVA.<String>builder()
                    .property(keywords.get(index))
                    .index(index)
                    .maxSize(maxSize)
                    .isRegex(isRegex)
                    .build();

            keyword.append(createMethodKeyword(createMethodStringVA));
        }

        return keyword.toString();
    }

    private void buildMethodExceptions(MethodModel methodModel, StringBuilder method,
                                       Boolean isRegex) {
        Integer maxSize = methodModel.getExceptions().size() - ONE;

        if (!methodModel.getExceptions().isEmpty()) {
            appendString(method, isRegex, THROWS);

            if (!isRegex) {
                method.append(SPACE);
            }
        }

        for (Integer index = FIRST_INDEX; index < methodModel.getExceptions().size(); index++) {
            CreateMethodStringVA<String> createMethodStringVA = CreateMethodStringVA.<String>builder()
                    .property(methodModel.getExceptions().get(index))
                    .index(index)
                    .maxSize(maxSize)
                    .isRegex(isRegex)
                    .build();

            method.append(createMethodException(createMethodStringVA));
        }
    }

    private String createMethodException(CreateMethodStringVA<String> createMethodStringVA) {
        StringBuilder exception = new StringBuilder();

        appendString(exception, createMethodStringVA.getIsRegex(), createMethodStringVA.getProperty());

        if (!createMethodStringVA.getIndex().equals(createMethodStringVA.getMaxSize())) {
            appendString(exception, createMethodStringVA.getIsRegex(), COMMA);
        }

        if (!createMethodStringVA.getIsRegex()) {
            exception.append(SPACE);
        }

        return exception.toString();
    }

    private void buildMethodBody(MethodModel methodModel, StringBuilder method,
                                 Boolean isRegex) {
        appendString(method, isRegex, OPEN_CURLY_BRACKETS);

        if (!isRegex) {
            method.append(ENTER);
            method.append(TAB);
        }

        appendString(method, isRegex, methodModel.getBody());

        if (!isRegex) {
            method.append(ENTER);
        }

        method.append(CLOSE_CURLY_BRACKETS);
    }

    private String replaceRegexKeywords(String string) {
        for (String keyword : REGEX_KEYWORDS)
            string = replaceRegexKeyword(string, keyword);

        return string;
    }

    private String replaceRegexKeyword(String string, String regexKeyword) {
        String replacement = ESCAPE + regexKeyword;
        return string.replaceAll(Pattern.quote(regexKeyword), Matcher.quoteReplacement(replacement));
    }
}
