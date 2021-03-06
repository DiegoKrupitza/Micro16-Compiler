package com.diegokrupitza.microcompiler.exceptions;

import com.diegokrupitza.microcompiler.messages.ErrorMessages;

/**
 * @author Diego Krupitza
 * @version 1.1
 * @date 21.12.18
 */
public class FileException extends Micro16Exception {

    public FileException(String message) {
        super(message);
    }

    public FileException(ErrorMessages messages) {
        super(messages.toString());
    }

}
