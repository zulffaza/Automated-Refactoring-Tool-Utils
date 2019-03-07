package com.finalproject.automated.refactoring.tool.utils.model.common;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 8 March 2019
 */

public enum CodeSmellName {

    LONG_METHOD("Long Method");

    private String name;

    private static Map<String, CodeSmellName> codeSmellNames;

    static {
        codeSmellNames = Arrays.stream(CodeSmellName.values())
                .collect(Collectors.toMap(CodeSmellName::getName,
                        CodeSmellName::getCodeSmellName));
    }

    CodeSmellName(String name) {
        this.name = name;
    }

    public static CodeSmellName getCodeSmellName(String value) {
        return codeSmellNames.get(value);
    }

    private static CodeSmellName getCodeSmellName(CodeSmellName codeSmellName) {
        return codeSmellName;
    }

    public String getName() {
        return name;
    }
}
