package com.diegokrupitza.microcompiler.exceptions;

import com.diegokrupitza.microcompiler.messages.ErrorMessages;

/**
 * Project: micro16-compiler
 * Document: GeneratorException.java
 * Author: Diego Krupitza
 * Created: 20.12.18
 */
public class GeneratorException extends Exception {

    public GeneratorException(String message) {
        super(message);
    }

    public GeneratorException(ErrorMessages messages) {
        super(messages.toString());
    }

}
