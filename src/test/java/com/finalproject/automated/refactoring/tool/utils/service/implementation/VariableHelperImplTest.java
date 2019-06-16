package com.finalproject.automated.refactoring.tool.utils.service.implementation;

import com.finalproject.automated.refactoring.tool.utils.service.MergeListHelper;
import com.finalproject.automated.refactoring.tool.utils.service.VariableHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 25 May 2019
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class VariableHelperImplTest {

    @Autowired
    private VariableHelper variableHelper;

    @MockBean
    private MergeListHelper mergeListHelper;

    private static final String SPACE_DELIMITER = " ";
    private static final String EMPTY_STRING = "";

    @Before
    public void setUp() {
        mockingNormalizeForStatement();
        mockingNormalizeForGenericsStatement();
        mockingNormalizeForInnerClassStatement();
        mockingNormalizeForMethodReferenceStatement();
        mockingNormalizeForEmptyStatement();
    }

    @Test
    public void readVariable_success() {
        List<String> variables = variableHelper.readVariable(createStatement());
        assertEquals(new ArrayList<>(), variables);

        verifyNormalizeForStatement();
    }

    @Test
    public void readVariable_success_containsGenericsStatement() {
        List<String> variables = variableHelper.readVariable(createGenericsStatement());
        assertEquals(createExpectedGenericsStatementVariables(), variables);

        verifyNormalizeForGenericsStatement();
    }

    @Test
    public void readVariable_success_containsInnerClassStatement() {
        List<String> variables = variableHelper.readVariable(createInnerClassStatement());
        assertEquals(createExpectedInnerClassStatementVariables(), variables);

        verifyNormalizeForInnerClassStatement();
    }

    @Test
    public void readVariable_success_containsMethodReferenceStatement() {
        List<String> variables = variableHelper.readVariable(createMethodReferenceStatement());
        assertEquals(createExpectedMethodReferenceStatementVariables(), variables);

        verifyNormalizeForMethodReferenceStatement();
    }

    @Test
    public void readVariable_success_statementIsEmpty() {
        List<String> variables = variableHelper.readVariable("");
        assertEquals(new ArrayList<>(), variables);

        verifyNormalizeForEmptyStatement();
    }

    @Test(expected = NullPointerException.class)
    public void readVariable_failed_statementIsNull() {
        variableHelper.readVariable(null);
    }

    @Test
    public void isClassName_success() {
        assertTrue(variableHelper.isClassName("Student"));
    }

    @Test
    public void isClassName_success_constVariable() {
        assertFalse(variableHelper.isClassName("STUDENT_NAME"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void isClassName_failed_variableEmpty() {
        variableHelper.isClassName("");
    }

    @Test(expected = NullPointerException.class)
    public void isClassName_failed_variableIsNull() {
        variableHelper.isClassName(null);
    }

    private void mockingNormalizeForStatement() {
        doAnswer(this::mockNormalizeStringStatement)
                .when(mergeListHelper)
                .mergeListOfString(eq(createStatementList()),
                        eq(createStatementMergeIndex()), eq(SPACE_DELIMITER));

        doNothing()
                .when(mergeListHelper)
                .mergeListOfString(eq(createExpectedStatementList()),
                        eq(new ArrayList<>()), eq(EMPTY_STRING));
    }

    private void mockingNormalizeForGenericsStatement() {
        doNothing()
                .when(mergeListHelper)
                .mergeListOfString(eq(createGenericsStatementList()),
                        eq(new ArrayList<>()), eq(SPACE_DELIMITER));

        doAnswer(this::mockNormalizeGenericsGenericsStatement)
                .when(mergeListHelper)
                .mergeListOfString(eq(createBeautyGenericsStatementList()),
                        eq(createGenericsStatementMergeIndex()), eq(EMPTY_STRING));
    }

    private void mockingNormalizeForInnerClassStatement() {
        doNothing()
                .when(mergeListHelper)
                .mergeListOfString(eq(createInnerClassStatementList()),
                        eq(new ArrayList<>()), eq(SPACE_DELIMITER));

        doNothing()
                .when(mergeListHelper)
                .mergeListOfString(eq(createInnerClassStatementList()),
                        eq(new ArrayList<>()), eq(EMPTY_STRING));
    }

    private void mockingNormalizeForMethodReferenceStatement() {
        doAnswer(this::mockNormalizeKeywordsCalledMethodMethodReferenceStatement)
                .when(mergeListHelper)
                .mergeListOfString(eq(createNormalizeKeywordsCalledMethodMethodReferenceStatementList()),
                        eq(createMethodReferenceStatementMergeIndex()), eq(EMPTY_STRING));

        doNothing()
                .when(mergeListHelper)
                .mergeListOfString(eq(createMethodReferenceStatementList()),
                        eq(new ArrayList<>()), eq(SPACE_DELIMITER));

        doNothing()
                .when(mergeListHelper)
                .mergeListOfString(eq(createMethodReferenceStatementList()),
                        eq(new ArrayList<>()), eq(EMPTY_STRING));
    }

    private void mockingNormalizeForEmptyStatement() {
        doNothing()
                .when(mergeListHelper)
                .mergeListOfString(eq(new ArrayList<>()),
                        eq(new ArrayList<>()), eq(SPACE_DELIMITER));

        doNothing()
                .when(mergeListHelper)
                .mergeListOfString(eq(new ArrayList<>()),
                        eq(new ArrayList<>()), eq(EMPTY_STRING));
    }

    private String createStatement() {
        return "// Return string of array list of string declarations\n" +
                "return \"List<String> variables = Arrays.asList(statement.split(\"\\\\s\"));\";";
    }

    private String createGenericsStatement() {
        return "/*\n" +
                "   Declarations of students hash map\n" +
                "*/\n" +
                "Map<Integer, List<String>> students = new HashMap<>();";
    }

    private String createInnerClassStatement() {
        return "Sort.Direction[] direction = new Sort.Direction[directionSize];";
    }

    private String createMethodReferenceStatement() {
        return "variables = (ArrayList) this.variables.stream().flatMap(this::removeUnusedCharacter).collect(Collectors.toList());";
    }

    private List<String> createStatementList() {
        List<String> statementList = Arrays.asList(
                "return", "\"List", "<", "String", ">", "variables", "=",
                ".Arrays", ".asList(", "statement", ".split(", "\"\\\\s\"", ")", ")", "\""
        );
        return new ArrayList<>(statementList);
    }

    private List<String> createGenericsStatementList() {
        List<String> genericsStatementList = Arrays.asList(
                "Map", "<", "Integer", "List", "<", "String", ">>", "students", "=",
                "new", "HashMap", "<>", "(", ")"
        );
        return new ArrayList<>(genericsStatementList);
    }

    private List<String> createInnerClassStatementList() {
        List<String> innerClassStatementList = Arrays.asList(
                "Sort.Direction[]", "direction", "=", "new",
                "Sort.Direction[directionSize]"
        );
        return new ArrayList<>(innerClassStatementList);
    }

    private List<String> createMethodReferenceStatementList() {
        List<String> methodReferenceStatementList = Arrays.asList(
                "variables", "=", "(", ")", "this.variables", ".stream(", ")",
                ".flatMap(", "this.removeUnusedCharacter(", ")",
                ".collect(", ".Collectors", ".toList(", ")", ")"
        );
        return new ArrayList<>(methodReferenceStatementList);
    }

    private List<String> createNormalizeKeywordsCalledMethodMethodReferenceStatementList() {
        List<String> methodReferenceStatementList = Arrays.asList(
                "this", ".variables", ".stream(", ")",
                ".flatMap(", "this", ".removeUnusedCharacter(", ")",
                ".collect(", ".Collectors", ".toList(", ")", ")"
        );
        return new ArrayList<>(methodReferenceStatementList);
    }

    private Object mockNormalizeStringStatement(InvocationOnMock invocationOnMock) {
        List<String> genericsStatementList = invocationOnMock.getArgument(0);

        genericsStatementList.clear();
        genericsStatementList.addAll(createExpectedStatementList());

        return null;
    }

    private Object mockNormalizeGenericsGenericsStatement(InvocationOnMock invocationOnMock) {
        List<String> genericsStatementList = invocationOnMock.getArgument(0);

        genericsStatementList.clear();
        genericsStatementList.addAll(createExpectedGenericsStatementList());

        return null;
    }

    private Object mockNormalizeKeywordsCalledMethodMethodReferenceStatement(InvocationOnMock invocationOnMock) {
        List<String> genericsStatementList = invocationOnMock.getArgument(0);

        genericsStatementList.clear();
        genericsStatementList.addAll(createNormalizeKeywordsCalledMethodExpectedMethodReferenceStatementList());

        return null;
    }

    private List<Integer> createStatementMergeIndex() {
        List<Integer> statementMergeIndex = Arrays.asList(1, 14);
        return new ArrayList<>(statementMergeIndex);
    }

    private List<Integer> createGenericsStatementMergeIndex() {
        List<Integer> genericsStatementMergeIndex = Arrays.asList(0, 6, 10, 12);
        return new ArrayList<>(genericsStatementMergeIndex);
    }

    private List<Integer> createMethodReferenceStatementMergeIndex() {
        List<Integer> methodReferenceStatementMergeIndex = Arrays.asList(0, 1, 5, 6);
        return new ArrayList<>(methodReferenceStatementMergeIndex);
    }

    private List<String> createExpectedStatementList() {
        List<String> statementList = Arrays.asList(
                "return",
                "\"List < String > variables = .Arrays .asList( statement .split( \"\\\\s\" ) ) \""
        );
        return new ArrayList<>(statementList);
    }

    private List<String> createBeautyGenericsStatementList() {
        List<String> genericsStatementList = Arrays.asList(
                "Map", "<", "Integer, ", "List", "<", "String", ">>", "students", "=",
                "new", "HashMap", "<>", "(", ")"
        );
        return new ArrayList<>(genericsStatementList);
    }

    private List<String> createExpectedGenericsStatementList() {
        List<String> genericsStatementList = Arrays.asList(
                "Map<Integer, List<String>>", "students", "=",
                "new", "HashMap<>(", ")"
        );
        return new ArrayList<>(genericsStatementList);
    }

    private List<String> createNormalizeKeywordsCalledMethodExpectedMethodReferenceStatementList() {
        List<String> methodReferenceStatementList = Arrays.asList(
                "this.variables", ".stream(", ")",
                ".flatMap(", "this.removeUnusedCharacter(", ")",
                ".collect(", ".Collectors", ".toList(", ")", ")"
        );
        return new ArrayList<>(methodReferenceStatementList);
    }

    private List<String> createExpectedGenericsStatementVariables() {
        List<String> genericsStatementVariables = Arrays.asList(
                "Map<Integer, List<String>>", "students", "="
        );
        return new ArrayList<>(genericsStatementVariables);
    }

    private List<String> createExpectedInnerClassStatementVariables() {
        List<String> innerClassStatementVariables = Arrays.asList(
                "Sort.Direction[]", "direction", "=", "directionSize"
        );
        return new ArrayList<>(innerClassStatementVariables);
    }

    private List<String> createExpectedMethodReferenceStatementVariables() {
        List<String> methodReferenceStatementVariables = Arrays.asList(
                "variables", "=", "this.variables"
        );
        return new ArrayList<>(methodReferenceStatementVariables);
    }

    private void verifyNormalizeForStatement() {
        verify(mergeListHelper).mergeListOfString(eq(createExpectedStatementList()),
                eq(createStatementMergeIndex()), eq(SPACE_DELIMITER));
        verify(mergeListHelper, times(3))
                .mergeListOfString(anyList(), eq(new ArrayList<>()), eq(EMPTY_STRING));
        verifyNoMoreInteractions(mergeListHelper);
    }

    private void verifyNormalizeForGenericsStatement() {
        verify(mergeListHelper).mergeListOfString(eq(createExpectedGenericsStatementList()),
                eq(new ArrayList<>()), eq(SPACE_DELIMITER));
        verify(mergeListHelper).mergeListOfString(eq(createExpectedGenericsStatementList()),
                eq(createGenericsStatementMergeIndex()), eq(EMPTY_STRING));
        verify(mergeListHelper, times(3))
                .mergeListOfString(anyList(), eq(new ArrayList<>()), eq(EMPTY_STRING));
        verifyNoMoreInteractions(mergeListHelper);
    }

    private void verifyNormalizeForInnerClassStatement() {
        verify(mergeListHelper).mergeListOfString(eq(createInnerClassStatementList()),
                eq(new ArrayList<>()), eq(SPACE_DELIMITER));
        verify(mergeListHelper).mergeListOfString(eq(createInnerClassStatementList()),
                eq(new ArrayList<>()), eq(EMPTY_STRING));
        verifyNoMoreInteractions(mergeListHelper);
    }

    private void verifyNormalizeForMethodReferenceStatement() {
        verify(mergeListHelper, times(2))
                .mergeListOfString(anyList(), eq(new ArrayList<>()), eq(EMPTY_STRING));
        verify(mergeListHelper).mergeListOfString(
                eq(createNormalizeKeywordsCalledMethodExpectedMethodReferenceStatementList()),
                eq(createMethodReferenceStatementMergeIndex()), eq(EMPTY_STRING));
        verify(mergeListHelper).mergeListOfString(eq(createMethodReferenceStatementList()),
                eq(new ArrayList<>()), eq(SPACE_DELIMITER));
        verifyNoMoreInteractions(mergeListHelper);
    }

    private void verifyNormalizeForEmptyStatement() {
        verify(mergeListHelper).mergeListOfString(eq(new ArrayList<>()),
                eq(new ArrayList<>()), eq(SPACE_DELIMITER));
        verify(mergeListHelper).mergeListOfString(eq(new ArrayList<>()),
                eq(new ArrayList<>()), eq(EMPTY_STRING));
        verifyNoMoreInteractions(mergeListHelper);
    }
}