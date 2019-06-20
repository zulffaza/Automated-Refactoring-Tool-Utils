package com.finalproject.automated.refactoring.tool.utils.service.implementation;

import com.finalproject.automated.refactoring.tool.model.MethodModel;
import com.finalproject.automated.refactoring.tool.model.PropertyModel;
import com.finalproject.automated.refactoring.tool.utils.service.MethodModelHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 16 June 2019
 */

public class MethodModelHelperImplTest {

    private MethodModelHelper methodModelHelper;

    private MethodModel methodModel;

    @Before
    public void setUp() {
        methodModelHelper = new MethodModelHelperImpl();

        methodModel = MethodModel.builder()
                .keywords(Arrays.asList(
                        "@GetMapping( value = \"/{filename}/change\", produces = MediaType.APPLICATION_JSON_VALUE )",
                        "@SuppressWarnings()",
                        "public"))
                .returnType("Response<String, String>")
                .name("changeFilename")
                .parameters(Arrays.asList(
                        PropertyModel.builder()
                                .keywords(Collections.singletonList("@RequestParam(required = false, defaultValue = \"null\")"))
                                .type("String")
                                .name("name")
                                .build(),
                        PropertyModel.builder()
                                .keywords(Collections.singletonList("@RequestParam(required = false, defaultValue = \".java\")"))
                                .type("String")
                                .name("extension")
                                .build(),
                        PropertyModel.builder()
                                .keywords(Collections.singletonList("@RequestParam(required = false)"))
                                .type("String")
                                .name("user")
                                .build()))
                .exceptions(Arrays.asList("Exception", "IOException"))
                .body("try {\n" +
                        "            return user + \"-\" + name + extension;\n" +
                        "        } catch (NullPointerException e) {\n" +
                        "            return null;\n" +
                        "        }")
                .build();
    }

    @Test
    public void createMethod_success() {
        String method = methodModelHelper.createMethod(methodModel);
        assertEquals(createExpectedMethodString(), method);
    }

    @Test
    public void createMethodRegex_success() {
        String method = methodModelHelper.createMethodRegex(methodModel);
        assertEquals(createExpectedMethodRegex(), method);
    }

    @Test(expected = NullPointerException.class)
    public void createMethod_failed_methodModelIsNull() {
        methodModelHelper.createMethod(null);
    }

    @Test(expected = NullPointerException.class)
    public void createMethodRegex_failed_methodModelIsNull() {
        methodModelHelper.createMethodRegex(null);
    }

    private String createExpectedMethodString() {
        return "@GetMapping( value = \"/{filename}/change\", produces = MediaType.APPLICATION_JSON_VALUE )\n" +
                "@SuppressWarnings()\n" +
                "public Response<String, String> changeFilename(@RequestParam(required = false, defaultValue = \"null\") String name,@RequestParam(required = false, defaultValue = \".java\") String extension,@RequestParam(required = false) String user) throws Exception, IOException {\n" +
                "\ttry {\n" +
                "            return user + \"-\" + name + extension;\n" +
                "        } catch (NullPointerException e) {\n" +
                "            return null;\n" +
                "        }\n" +
                "}";
    }

    private String createExpectedMethodRegex() {
        return "@GetMapping\\((?:\\s)*value(?:\\s)*=(?:\\s)*\"/\\{filename\\}/change\",(?:\\s)*produces(?:\\s)*=(?:\\s)*MediaType\\.APPLICATION_JSON_VALUE(?:\\s)*\\)(?:\\s)*@SuppressWarnings\\(\\)(?:\\s)*public(?:\\s)*Response<String,(?:\\s)*String>(?:\\s)*changeFilename(?:\\s)*\\((?:\\s)*@RequestParam\\(required(?:\\s)*=(?:\\s)*false,(?:\\s)*defaultValue(?:\\s)*=(?:\\s)*\"null\"\\)(?:\\s)*String(?:\\s)*name(?:\\s)*,(?:\\s)*@RequestParam\\(required(?:\\s)*=(?:\\s)*false,(?:\\s)*defaultValue(?:\\s)*=(?:\\s)*\"\\.java\"\\)(?:\\s)*String(?:\\s)*extension(?:\\s)*,(?:\\s)*@RequestParam\\(required(?:\\s)*=(?:\\s)*false\\)(?:\\s)*String(?:\\s)*user(?:\\s)*\\)(?:\\s)*throws(?:\\s)*Exception(?:\\s)*,(?:\\s)*IOException(?:\\s)*\\{(?:\\s)*try(?:\\s)*\\{(?:\\s)*return(?:\\s)*user(?:\\s)*\\+(?:\\s)*\"\\-\"(?:\\s)*\\+(?:\\s)*name(?:\\s)*\\+(?:\\s)*extension;(?:\\s)*\\}(?:\\s)*catch(?:\\s)*\\(NullPointerException(?:\\s)*e\\)(?:\\s)*\\{(?:\\s)*return(?:\\s)*null;(?:\\s)*\\}(?:\\s)*}";
    }
}