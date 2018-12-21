package com.diegokrupitza.microcompiler.messages;

import com.diegokrupitza.microcompiler.datastructures.StorageHandler;
import com.diegokrupitza.microcompiler.generator.ValueGenerator;

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
    CANNOT_GENERATE_NUMBER("The number you want to generate is not possible!"),
    /**
     * The Error message in case someone wants to define a number that is not possible to store in micro16
     */
    VALUE_OUT_OF_RANGE("The number you want to store into a variable is not possible! The value has to be in a range from " + ValueGenerator.MAX_NEGATIV_NUMBER + " to " + ValueGenerator.MAX_POSITIV_NUMBER);


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
