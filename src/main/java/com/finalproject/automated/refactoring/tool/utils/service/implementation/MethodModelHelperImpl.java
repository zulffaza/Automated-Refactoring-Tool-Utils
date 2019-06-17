package com.finalproject.automated.refactoring.tool.utils.service.implementation;

import com.finalproject.automated.refactoring.tool.model.MethodModel;
import com.finalproject.automated.refactoring.tool.model.PropertyModel;
import com.finalproject.automated.refactoring.tool.utils.model.request.CreateMethodStringVA;
import com.finalproject.automated.refactoring.tool.utils.service.MethodModelHelper;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    private static final String OPEN_PARENTHESES = "(";
    private static final String CLOSE_PARENTHESES = ")";
    private static final String COMMA = ",";
    private static final String THROWS = "throws";
    private static final String OPEN_CURLY_BRACKETS = "{";
    private static final String CLOSE_CURLY_BRACKETS = "}";
    private static final String TAB = "\t";
    private static final String WHITESPACE_REGEX = "(?:\\s)*";
    private static final String PLUS = "+";
    private static final String OR = "|";
    private static final String REGEX_ESCAPE = "\\\\";

    private static final Integer FIRST_INDEX = 0;
    private static final Integer ONE = 1;

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
        method.append(WHITESPACE_REGEX);

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

        if (createMethodStringVA.getIsRegex())
            keyword = createRegexString(keyword);
        else
            keyword = createNonRegexMethodKeyword(createMethodStringVA);

        return keyword;
    }

    private String createRegexString(String keyword) {
        keyword = replaceRegexKeywords(keyword);
        keyword += WHITESPACE_REGEX;

        return keyword;
    }

    private String createNonRegexMethodKeyword(CreateMethodStringVA<String> createMethodStringVA) {
        String keyword = createMethodStringVA.getProperty();

        if (!createMethodStringVA.getIndex().equals(createMethodStringVA.getMaxSize()))
            keyword += ENTER;
        else
            keyword += SPACE;

        return keyword;
    }

    private void buildMethodReturnType(MethodModel methodModel, StringBuilder method,
                                       Boolean isRegex) {
        if (isHasReturnType(methodModel))
            method.append(createMethodReturnType(methodModel, isRegex));
    }

    private String createMethodReturnType(MethodModel methodModel, Boolean isRegex) {
        String returnType = methodModel.getReturnType();

        if (isRegex)
            returnType = createRegexString(returnType);
        else
            returnType += SPACE;

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

        if (!isRegex)
            method.append(SPACE);
    }

    private void appendString(StringBuilder method, Boolean isRegex, String string) {
        if (isRegex)
            method.append(createRegexString(string));
        else
            method.append(string);
    }

    private String createMethodParameter(CreateMethodStringVA<PropertyModel> createMethodStringVA) {
        StringBuilder parameter = new StringBuilder();

        parameter.append(buildParameterKeywords(
                createMethodStringVA.getProperty().getKeywords(), createMethodStringVA.getIsRegex()));
        appendString(parameter, createMethodStringVA.getIsRegex(), createMethodStringVA.getProperty().getType());

        if (!createMethodStringVA.getIsRegex())
            parameter.append(SPACE);

        appendString(parameter, createMethodStringVA.getIsRegex(), createMethodStringVA.getProperty().getName());

        if (!createMethodStringVA.getIndex().equals(createMethodStringVA.getMaxSize()))
            appendString(parameter, createMethodStringVA.getIsRegex(), COMMA);

        return parameter.toString();
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

            if (!isRegex)
                method.append(SPACE);
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

        if (!createMethodStringVA.getIndex().equals(createMethodStringVA.getMaxSize()))
            exception.append(COMMA);

        if (!createMethodStringVA.getIsRegex())
            exception.append(SPACE);

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

        if (!isRegex)
            method.append(ENTER);

        method.append(CLOSE_CURLY_BRACKETS);
    }

    private String replaceRegexKeywords(String string) {
        string = string.replaceAll(Pattern.quote(OPEN_PARENTHESES), REGEX_ESCAPE + OPEN_PARENTHESES);
        string = string.replaceAll(Pattern.quote(CLOSE_PARENTHESES), REGEX_ESCAPE + CLOSE_PARENTHESES);
        string = string.replaceAll(Pattern.quote(PLUS), REGEX_ESCAPE + PLUS);
        string = string.replaceAll(Pattern.quote(OR), REGEX_ESCAPE + OR);

        return string;
    }
}
