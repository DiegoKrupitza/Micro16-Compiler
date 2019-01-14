package com.diegokrupitza.microcompiler.exceptions;

import com.diegokrupitza.microcompiler.messages.ErrorMessages;

/**
 * @author Diego Krupitza
 * @version 1.1
 * @date 21.12.18
 */
public class VariableException extends Micro16Exception {

    public VariableException(String message) {
        super(message);
    }

    public VariableException(ErrorMessages messages) {
        super(messages.toString());
    }
}
