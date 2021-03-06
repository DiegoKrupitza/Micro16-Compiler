package com.diegokrupitza.microcompiler.messages;

import com.diegokrupitza.microcompiler.generator.ValueGenerator;

/**
 * This enum contains all the mesages that the compiler displays during the process of generating the micro-16 code.
 *
 * @author Diego Krupitza
 * @version 1.1
 * @date 12.12.18
 */
public enum ErrorMessages {
    /**
     * The Error message in case the given file does not get found
     */
    FILE_NOTEXIST("The file you want to accessCount does not exists!"),
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
    VALUE_OUT_OF_RANGE("The number you want to store into a variable is not possible! The value has to be in a range from " + ValueGenerator.MAX_NEGATIV_NUMBER + " to " + ValueGenerator.MAX_POSITIV_NUMBER),
    /**
     * The Error message in case someone creates a variable that already exists
     */
    VARIABLE_ALREADY_EXISTS("Variable %s on line %d already exists!"),
    /**
     * The Error message in case the file extension is not correct
     */
    INVALID_FILE_EXTENSION("The file extensions is not correct!"),
    /**
     *
     */
    VARIABLE_DOES_NOT_EXISTS("The Variable %s on line %d does not exists"),
    /**
     * The Error message in case the multiplication is not possible
     */
    CANNOT_GENERATE_MULTIPLICATION("The multiplication instruction you want to generate is not possible"),
    /**
     * The Error message in case the addition of the values is not possible
     */
    CANNOT_GENERATE_ADDITION("The addition instruction you want to generate is not possible"),
    /**
     * The Error message in case there is no free Memory address
     */
    NO_FREE_MEMORYADDRESS("There is no free Memory Address in the current program state! You can change the maximum memory range, but its not adviced to use more than the default value! "),
    /**
     * The Error message in case there is a problem with generating a number
     */
    CANNOT_GENERATE_MEMORY_NUMBER("The memory address that has to be generated is not possible!");

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
