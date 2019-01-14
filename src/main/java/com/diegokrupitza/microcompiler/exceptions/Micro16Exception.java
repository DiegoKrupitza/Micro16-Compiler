package com.diegokrupitza.microcompiler.exceptions;

import com.diegokrupitza.microcompiler.messages.ErrorMessages;

/**
 * @author Diego Krupitza
 * @version 1.1
 * @date 21.12.18
 */
public abstract class Micro16Exception extends Exception {

    public Micro16Exception(String message) {
        super(message);
    }

    public Micro16Exception(ErrorMessages messages) {
        super(messages.toString());
    }

}
