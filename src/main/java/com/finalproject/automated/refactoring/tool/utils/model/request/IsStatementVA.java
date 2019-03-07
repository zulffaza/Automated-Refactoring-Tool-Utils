package com.finalproject.automated.refactoring.tool.utils.model.request;

import lombok.Data;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 5 March 2019
 */

@Data
public class IsStatementVA {

    private Stack<Character> stack = new Stack<>();

    private AtomicBoolean escape = new AtomicBoolean();
}
