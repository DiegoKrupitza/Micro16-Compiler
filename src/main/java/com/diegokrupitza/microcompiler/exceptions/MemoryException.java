package com.diegokrupitza.microcompiler.exceptions;

import com.diegokrupitza.microcompiler.messages.ErrorMessages;

/**
 * @author Diego Krupitza
 * @version 1.1
 * @date 3.1.19
 */
public class MemoryException extends Micro16Exception {

    public MemoryException(String message) {
        super(message);
    }

    public MemoryException(ErrorMessages messages) {
        super(messages.toString());
    }

}
