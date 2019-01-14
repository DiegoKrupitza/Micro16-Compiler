package com.diegokrupitza.microcompiler.exceptions;

import com.diegokrupitza.microcompiler.messages.ErrorMessages;

/**
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
