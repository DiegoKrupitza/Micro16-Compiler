package com.diegokrupitza.microcompiler.exceptions;

import com.diegokrupitza.microcompiler.messages.ErrorMessages;

/**
 * Project: micro16-compiler
 * Document: MemoryException.java
 * Author: Diego Krupitza
 * Created: 3.1.19
 */
public class MemoryException extends Micro16Exception {

    public MemoryException(String message) {
        super(message);
    }

    public MemoryException(ErrorMessages messages) {
        super(messages.toString());
    }

}
