package com.diegokrupitza.microcompiler.messages;

/**
 * Project: micro16-compiler
 * Document: ErrorMessages.java
 * Author: Diego Krupitza
 * Created: 12.12.18
 */
public enum ErrorMessages {
    /**
     * The Error message in case the given file does not get found
     */
    FILE_NOTEXIST("The file you want to access does not exists!"),
    /**
     * The Error message in case there is no Register available
     */
    NO_REGISTER_AVAILABLE("There is no Register available at the moment."),
    /**
     * The Error message in case there is a problem with generating a number
     */
    CANNOT_GENERATE_NUMBER("The number you want to generate is not possible!");


    private String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.getMessage();
    }

    public String getMessage() {
        return message;
    }
}
