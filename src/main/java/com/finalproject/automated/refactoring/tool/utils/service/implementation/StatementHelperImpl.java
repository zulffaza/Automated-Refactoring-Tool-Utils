package com.finalproject.automated.refactoring.tool.utils.service.implementation;

import com.finalproject.automated.refactoring.tool.utils.model.request.IsStatementVA;
import com.finalproject.automated.refactoring.tool.utils.service.StatementHelper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 5 March 2019
 */

@Service
public class StatementHelperImpl implements StatementHelper {

    private static final Character DOUBLE_QUOTE_CHARACTER = '\"';
    private static final Character SINGLE_QUOTE_CHARACTER = '\'';
    private static final Character OPEN_PARENTHESES_CHARACTER = '(';
    private static final Character CLOSED_PARENTHESES_CHARACTER = ')';
    private static final Character ESCAPE_CHARACTER = '\\';

    private static final List<Character> QUOTES_KEYWORDS = Arrays.asList(SINGLE_QUOTE_CHARACTER, DOUBLE_QUOTE_CHARACTER);
    private static final List<Character> STATEMENTS_KEYWORDS = Arrays.asList(';', '{');

    @Override
    public Boolean isStatement(Character character, IsStatementVA isStatementVA) {
        searchWantedExpression(character, DOUBLE_QUOTE_CHARACTER, isStatementVA);
        searchWantedExpression(character, SINGLE_QUOTE_CHARACTER, isStatementVA);
        searchParenthesesExpression(character, isStatementVA.getStack());
        searchEscapeCharacter(character, isStatementVA.getEscape());

        return isStatement(character, isStatementVA.getStack());
    }

    private void searchWantedExpression(Character character, Character wantedCharacter,
                                        IsStatementVA isStatementVA) {
        if (isWantedExpression(character, wantedCharacter, isStatementVA.getEscape())) {
            analyzeWantedExpression(character, wantedCharacter, isStatementVA.getStack());
        }
    }

    private Boolean isWantedExpression(Character character, Character wantedCharacter,
                                       AtomicBoolean escape) {
        return character.equals(wantedCharacter) &&
                !escape.get();
    }

    private void analyzeWantedExpression(Character character, Character wantedCharacter,
                                         Stack<Character> stack) {
        if (stack.empty()) {
            stack.push(character);
        } else if (stack.peek().equals(wantedCharacter)) {
            stack.pop();
        }
    }

    private void searchParenthesesExpression(Character character, Stack<Character> stack) {
        if (character.equals(OPEN_PARENTHESES_CHARACTER)) {
            analyzeOpenParenthesesExpression(character, stack);
        }

        if (isClosedParenthesesExpression(character, stack)) {
            stack.pop();
        }
    }

    private void analyzeOpenParenthesesExpression(Character character, Stack<Character> stack) {
        Boolean isQuotes = isQuotes(stack);
        stack.push(character);

        redoPushIfInsideQuotes(isQuotes, stack);
    }

    private Boolean isQuotes(Stack<Character> stack) {
        return !stack.empty() &&
                QUOTES_KEYWORDS.contains(stack.peek());
    }

    private void redoPushIfInsideQuotes(Boolean isQuotes, Stack stack) {
        if (isQuotes) {
            stack.pop();
        }
    }

    private Boolean isClosedParenthesesExpression(Character character, Stack<Character> stack) {
        return character.equals(CLOSED_PARENTHESES_CHARACTER) &&
                !stack.empty() &&
                stack.peek().equals(OPEN_PARENTHESES_CHARACTER);
    }

    private void searchEscapeCharacter(Character character, AtomicBoolean escape) {
        escape.set(character.equals(ESCAPE_CHARACTER) && !escape.get());
    }

    private Boolean isStatement(Character character, Stack stack) {
        return stack.empty() &&
                STATEMENTS_KEYWORDS.contains(character);
    }
}
