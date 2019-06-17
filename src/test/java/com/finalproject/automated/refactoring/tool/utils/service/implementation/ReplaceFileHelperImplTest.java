package com.finalproject.automated.refactoring.tool.utils.service.implementation;

import com.finalproject.automated.refactoring.tool.utils.model.request.ReplaceFileVA;
import com.finalproject.automated.refactoring.tool.utils.service.ReplaceFileHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 17 June 2019
 */

public class ReplaceFileHelperImplTest {

    private ReplaceFileHelper replaceFileHelper;

    private ReplaceFileVA replaceFileVA;

    @Before
    public void setUp() {
        replaceFileHelper = new ReplaceFileHelperImpl();
        replaceFileVA = ReplaceFileVA.builder()
                .filePath("C:\\Users\\Faza Zulfika\\Videos\\Test.java")
                .target("public(?:\\s)*void(?:\\s)*setName(?:\\s)*\\((?:\\s)*@NonNull(?:\\s)*String(?:\\s)*name(?:\\s)*\\)(?:\\s)*\\{(?:\\s)*this.name = name;(?:\\s)*}")
                .replacement("Berhasil yaw")
                .build();
    }

    @Test
    public void test() {
        assertTrue(replaceFileHelper.replaceFile(replaceFileVA));
    }
}