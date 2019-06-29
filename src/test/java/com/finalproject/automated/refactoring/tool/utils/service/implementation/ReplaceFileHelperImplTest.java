package com.finalproject.automated.refactoring.tool.utils.service.implementation;

import com.finalproject.automated.refactoring.tool.utils.model.request.ReplaceFileVA;
import com.finalproject.automated.refactoring.tool.utils.service.ReplaceFileHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 17 June 2019
 */

public class ReplaceFileHelperImplTest {

    private ReplaceFileHelper replaceFileHelper;

    private ReplaceFileVA replaceFileVA;

    private Path path;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        File file = createTemporaryFile();

        replaceFileHelper = new ReplaceFileHelperImpl();
        path = Paths.get(file.getPath());
        replaceFileVA = ReplaceFileVA.builder()
                .filePath(path.toString())
                .target("@GetMapping\\((?:\\s)*value(?:\\s)*=(?:\\s)*\"/\\{filename\\}/change\",(?:\\s)*produces(?:\\s)*=(?:\\s)*MediaType\\.APPLICATION_JSON_VALUE(?:\\s)*\\)(?:\\s)*@SuppressWarnings\\(\\)(?:\\s)*public(?:\\s)*Response<String,(?:\\s)*String>(?:\\s)*changeFilename(?:\\s)*\\((?:\\s)*@RequestParam\\(required(?:\\s)*=(?:\\s)*false,(?:\\s)*defaultValue(?:\\s)*=(?:\\s)*\"null\"\\)(?:\\s)*String(?:\\s)*name(?:\\s)*,(?:\\s)*@RequestParam\\(required(?:\\s)*=(?:\\s)*false,(?:\\s)*defaultValue(?:\\s)*=(?:\\s)*\"\\.java\"\\)(?:\\s)*String(?:\\s)*extension(?:\\s)*,(?:\\s)*@RequestParam\\(required(?:\\s)*=(?:\\s)*false\\)(?:\\s)*String(?:\\s)*user(?:\\s)*\\)(?:\\s)*throws(?:\\s)*Exception(?:\\s)*,(?:\\s)*IOException(?:\\s)*\\{(?:\\s)*try(?:\\s)*\\{(?:\\s)*return(?:\\s)*user(?:\\s)*\\+(?:\\s)*\"\\-\"(?:\\s)*\\+(?:\\s)*name(?:\\s)*\\+(?:\\s)*extension;(?:\\s)*\\}(?:\\s)*catch(?:\\s)*\\(NullPointerException(?:\\s)*e\\)(?:\\s)*\\{(?:\\s)*return(?:\\s)*null;(?:\\s)*\\}(?:\\s)*}")
                .replacement("Hello World")
                .build();
    }

    @Test
    public void replaceFile_success() throws IOException {
        assertTrue(replaceFileHelper.replaceFile(replaceFileVA));
        assertEquals(getExpectedFileContent(), new String(Files.readAllBytes(path)));
    }

    @Test
    public void replaceFile_failed_targetNotFound() throws IOException {
        Files.write(path, getFalseFileContent().getBytes());
        assertFalse(replaceFileHelper.replaceFile(replaceFileVA));
    }

    @Test
    public void replaceFile_failed_fileNotFound() throws IOException {
        Files.delete(path);
        assertFalse(replaceFileHelper.replaceFile(replaceFileVA));
    }

    @Test(expected = NullPointerException.class)
    public void replaceFile_failed() {
        replaceFileHelper.replaceFile(null);
    }

    private File createTemporaryFile() throws IOException {
        File file = temporaryFolder.newFile("Test.java");
        Path path = Paths.get(file.getPath());

        Files.write(path, getFileContent().getBytes());

        return file;
    }

    private String getFileContent() {
        return "package path;\n" +
                "\n" +
                "import java.io.Serializable;\n" +
                "\n" +
                "public class Filename implements Serializable {\n" +
                "\n" +
                "    @GetMapping(\n" +
                "            value = \"/{filename}/change\",\n" +
                "            produces = MediaType.APPLICATION_JSON_VALUE\n" +
                "    )\n" +
                "    @SuppressWarnings()\n" +
                "    public Response<String, String> changeFilename(@RequestParam(required = false, defaultValue = \"null\") String name,\n" +
                "                                                   @RequestParam(required = false, defaultValue = \".java\") String extension,\n" +
                "                                                   @RequestParam(required = false) String user) throws Exception, IOException {\n" +
                "        try {\n" +
                "            return user + \"-\" + name + extension;\n" +
                "        } catch (NullPointerException e) {\n" +
                "            return null;\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    private String getExpectedFileContent() {
        return "package path;\n" +
                "\n" +
                "import java.io.Serializable;\n" +
                "\n" +
                "public class Filename implements Serializable {\n" +
                "\n" +
                "    Hello World\n" +
                "}";
    }

    private String getFalseFileContent() {
        return "False file content";
    }
}