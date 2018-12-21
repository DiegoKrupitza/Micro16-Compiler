package com.diegokrupitza.microcompiler.exceptions;

import com.diegokrupitza.microcompiler.messages.ErrorMessages;

/**
 * Project: micro16-compiler
 * Document: FileException.java
 * Author: Diego Krupitza
 * Created: 21.12.18
 */
public class FileException extends Micro16Exception {

    public FileException(String message) {
        super(message);
    }

    public FileException(ErrorMessages messages) {
        super(messages.toString());
    }

}
