package com.diegokrupitza.microcompiler.exceptions;

import com.diegokrupitza.microcompiler.messages.ErrorMessages;

/**
 * Project: micro16-compiler
 * Document: GeneratorException.java
 *
 * @author Diego Krupitza
 * @version 1.1
 * @date 20.12.18
 */
public class GeneratorException extends Micro16Exception {

    public GeneratorException(String message) {
        super(message);
    }

    public GeneratorException(ErrorMessages messages) {
        super(messages.toString());
    }

}
