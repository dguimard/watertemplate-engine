package org.watertemplate.interpreter.lexer.exception;

import org.watertemplate.interpreter.lexer.TokenClass;

public class IncompleteCommandException extends RuntimeException {
    public IncompleteCommandException(final int i, final int j) {
        super("Incomplete command at " + i +":" + j);
    }
}