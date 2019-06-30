package com.finalproject.automated.refactoring.tool.utils.service;

import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 25 May 2019
 */

public interface VariableHelper {

    String UNUSED_CHARACTERS_REGEX = "(?:[,:;])+";
    String OPERATORS_CHARACTERS_REGEX = "(?:[+\\-*/%=!<>&|^~])+";
    String VARIABLE_NAME_REGEX = "^(?:[a-zA-Z_$])(?:[a-zA-Z0-9_$<>\\[\\].,?\\s])*";

    List<String> KEYWORDS = Arrays.asList(
            "class",
            "if", "else",
            "switch", "case", "default",
            "continue", "break",
            "try", "catch", "finally",
            "for", "while", "do",
            "super", "this",
            "new", "instanceof", "new", "return",
            "throw", "assert",
            "true", "false", "null"
    );

    List<String> PRIMITIVE_TYPES = Arrays.asList(
            "byte", "char", "short", "int", "long",
            "float", "double",
            "boolean"
    );

    List<String> readVariable(@NonNull String statement);

    Boolean isClassName(@NonNull String variable);
}
