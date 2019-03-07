package com.finalproject.automated.refactoring.tool.utils.model.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 8 March 2019
 */

public class CodeSmellNameTest {

    @Test
    public void getCodeSmellName_success() {
        assertEquals(CodeSmellName.getCodeSmellName("Long Method"), CodeSmellName.LONG_METHOD);
    }

    @Test
    public void getCodeSmellName_notFound() {
        assertNull(CodeSmellName.getCodeSmellName("Not Found"));
    }

    @Test
    public void getName_longMethod() {
        CodeSmellName codeSmellName = CodeSmellName.LONG_METHOD;
        assertEquals(codeSmellName.getName(), CodeSmellName.LONG_METHOD.getName());
    }
}