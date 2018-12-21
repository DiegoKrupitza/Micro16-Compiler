package com.diegokrupitza.microcompiler.exceptions;

import com.diegokrupitza.microcompiler.messages.ErrorMessages;

/**
 * Project: micro16-compiler
 * Document: VariableException.java
 * Author: Diego Krupitza
 * Created: 21.12.18
 */
public class VariableException extends Micro16Exception {

    public VariableException(String message) {
        super(message);
    }

    public VariableException(ErrorMessages messages) {
        super(messages.toString());
    }
}
