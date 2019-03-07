package com.finalproject.automated.refactoring.tool.utils.service.implementation;

import com.finalproject.automated.refactoring.tool.utils.model.request.IsStatementVA;
import com.finalproject.automated.refactoring.tool.utils.service.StatementHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 7 March 2019
 */

public class StatementHelperImplTest {

    private static final Integer FIRST_INDEX = 0;
    private static final Integer SECOND_INDEX = 1;

    private static final List<Character> STATEMENTS_KEYWORDS = Arrays.asList(';', '{');

    private StatementHelper statementHelper;

    @Before
    public void setUp() {
        statementHelper = new StatementHelperImpl();
    }

    @Test
    public void isStatement_semicolon_success() {
        Character character = STATEMENTS_KEYWORDS.get(FIRST_INDEX);
        IsStatementVA isStatementVA = new IsStatementVA();

        assertTrue(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertTrue(isStatementVA.getStack().empty());
    }

    @Test
    public void isStatement_semicolonAfterSingleQuote_success() {
        Character character = STATEMENTS_KEYWORDS.get(FIRST_INDEX);
        IsStatementVA isStatementVA = singleQuotedStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('\''));
    }

    @Test
    public void isStatement_semicolonAfterDoubleQuote_success() {
        Character character = STATEMENTS_KEYWORDS.get(FIRST_INDEX);
        IsStatementVA isStatementVA = doubleQuotedStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('\"'));
    }

    @Test
    public void isStatement_semicolonAfterOpenParentheses_success() {
        Character character = STATEMENTS_KEYWORDS.get(FIRST_INDEX);
        IsStatementVA isStatementVA = openParenthesesStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('('));
    }

    @Test
    public void isStatement_openBraces_success() {
        Character character = STATEMENTS_KEYWORDS.get(SECOND_INDEX);
        IsStatementVA isStatementVA = new IsStatementVA();

        assertTrue(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertTrue(isStatementVA.getStack().empty());
    }

    @Test
    public void isStatement_openBracesAfterSingleQuote_success() {
        Character character = STATEMENTS_KEYWORDS.get(SECOND_INDEX);
        IsStatementVA isStatementVA = singleQuotedStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('\''));
    }

    @Test
    public void isStatement_openBracesAfterDoubleQuote_success() {
        Character character = STATEMENTS_KEYWORDS.get(SECOND_INDEX);
        IsStatementVA isStatementVA = doubleQuotedStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('\"'));
    }

    @Test
    public void isStatement_openBracesAfterOpenParentheses_success() {
        Character character = STATEMENTS_KEYWORDS.get(SECOND_INDEX);
        IsStatementVA isStatementVA = openParenthesesStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('('));
    }

    @Test
    public void isStatement_singleQuote_success() {
        Character character = '\'';
        IsStatementVA isStatementVA = new IsStatementVA();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), character);
    }

    @Test
    public void isStatement_singleQuoteAfterSingleQuote_success() {
        Character character = '\'';
        IsStatementVA isStatementVA = singleQuotedStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertTrue(isStatementVA.getStack().empty());
    }

    @Test
    public void isStatement_singleQuoteAfterDoubleQuote_success() {
        Character character = '\'';
        IsStatementVA isStatementVA = doubleQuotedStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('\"'));
    }

    @Test
    public void isStatement_singleQuoteAfterOpenParentheses_success() {
        Character character = '\'';
        IsStatementVA isStatementVA = openParenthesesStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('('));
    }

    @Test
    public void isStatement_singleQuoteAfterEscape_success() {
        Character character = '\'';
        IsStatementVA isStatementVA = escapedStatementVA();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertTrue(isStatementVA.getStack().empty());
    }

    @Test
    public void isStatement_doubleQuote_success() {
        Character character = '\"';
        IsStatementVA isStatementVA = new IsStatementVA();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), character);
    }

    @Test
    public void isStatement_doubleQuoteAfterSingleQuote_success() {
        Character character = '\"';
        IsStatementVA isStatementVA = singleQuotedStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('\''));
    }

    @Test
    public void isStatement_doubleQuoteAfterDoubleQuote_success() {
        Character character = '\"';
        IsStatementVA isStatementVA = doubleQuotedStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertTrue(isStatementVA.getStack().empty());
    }

    @Test
    public void isStatement_doubleQuoteAfterOpenParentheses_success() {
        Character character = '\"';
        IsStatementVA isStatementVA = openParenthesesStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('('));
    }

    @Test
    public void isStatement_doubleQuoteAfterEscape_success() {
        Character character = '\"';
        IsStatementVA isStatementVA = escapedStatementVA();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertTrue(isStatementVA.getStack().empty());
    }

    @Test
    public void isStatement_openParentheses_success() {
        Character character = '(';
        IsStatementVA isStatementVA = new IsStatementVA();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), character);
    }

    @Test
    public void isStatement_openParenthesesAfterSingleQuote_success() {
        Character character = '(';
        IsStatementVA isStatementVA = singleQuotedStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('\''));
    }

    @Test
    public void isStatement_openParenthesesAfterDoubleQuote_success() {
        Character character = '(';
        IsStatementVA isStatementVA = doubleQuotedStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('\"'));
    }

    @Test
    public void isStatement_openParenthesesAfterOpenParentheses_success() {
        Character character = '(';
        IsStatementVA isStatementVA = openParenthesesStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('('));
        assertEquals(isStatementVA.getStack().pop(), new Character('('));
    }

    @Test
    public void isStatement_closeParentheses_success() {
        Character character = ')';
        IsStatementVA isStatementVA = new IsStatementVA();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertTrue(isStatementVA.getStack().empty());
    }

    @Test
    public void isStatement_closeParenthesesAfterSingleQuote_success() {
        Character character = ')';
        IsStatementVA isStatementVA = singleQuotedStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('\''));
    }

    @Test
    public void isStatement_closeParenthesesAfterDoubleQuote_success() {
        Character character = ')';
        IsStatementVA isStatementVA = doubleQuotedStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('\"'));
    }

    @Test
    public void isStatement_closeParenthesesAfterOpenParentheses_success() {
        Character character = ')';
        IsStatementVA isStatementVA = openParenthesesStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertTrue(isStatementVA.getStack().empty());
    }

    @Test
    public void isStatement_escape_success() {
        Character character = '\\';
        IsStatementVA isStatementVA = new IsStatementVA();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertTrue(isStatementVA.getEscape().get());
        assertTrue(isStatementVA.getStack().empty());
    }

    @Test
    public void isStatement_escapeAfterSingleQuote_success() {
        Character character = '\\';
        IsStatementVA isStatementVA = singleQuotedStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertTrue(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('\''));
    }

    @Test
    public void isStatement_escapeAfterDoubleQuote_success() {
        Character character = '\\';
        IsStatementVA isStatementVA = doubleQuotedStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertTrue(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('\"'));
    }

    @Test
    public void isStatement_escapeAfterOpenParentheses_success() {
        Character character = '\\';
        IsStatementVA isStatementVA = openParenthesesStack();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertTrue(isStatementVA.getEscape().get());
        assertEquals(isStatementVA.getStack().pop(), new Character('('));
    }

    @Test
    public void isStatement_escapeAfterEscape_success() {
        Character character = '\\';
        IsStatementVA isStatementVA = escapedStatementVA();

        assertFalse(statementHelper.isStatement(character, isStatementVA));
        assertFalse(isStatementVA.getEscape().get());
        assertTrue(isStatementVA.getStack().empty());
    }

    private IsStatementVA singleQuotedStack() {
        IsStatementVA isStatementVA = new IsStatementVA();
        isStatementVA.getStack().push('\'');

        return isStatementVA;
    }

    private IsStatementVA doubleQuotedStack() {
        IsStatementVA isStatementVA = new IsStatementVA();
        isStatementVA.getStack().push('\"');

        return isStatementVA;
    }

    private IsStatementVA openParenthesesStack() {
        IsStatementVA isStatementVA = new IsStatementVA();
        isStatementVA.getStack().push('(');

        return isStatementVA;
    }

    private IsStatementVA escapedStatementVA() {
        IsStatementVA isStatementVA = new IsStatementVA();
        isStatementVA.getEscape().set(Boolean.TRUE);

        return isStatementVA;
    }
}