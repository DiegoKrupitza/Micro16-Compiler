package com.diegokrupitza.microcompiler.generator;

import com.diegokrupitza.microcompiler.exceptions.GeneratorException;
import com.diegokrupitza.microcompiler.helper.MathematicalHelper;
import com.diegokrupitza.microcompiler.messages.ErrorMessages;

import java.util.Optional;

/**
 * Project: micro16-compiler
 * Document: ValueGenerator.java
 * Author: Diego Krupitza
 * Created: 20.12.18
 */
public class ValueGenerator {

    public static final int MAX_POSITIV_NUMBER = 32767;
    public static final int MAX_NEGATIV_NUMBER = -32768;

    private ValueGenerator() {
    }

    /**
     * Generates the Micro16 instructions to make a number.
     * The result of that calculation is after that saved into the register AC
     *
     * @param value the number i want to have in my AC register
     * @return the code for generating that given number
     */
    public static Optional<String> generateValue(int value) throws GeneratorException {
        StringBuilder returnString = new StringBuilder();
        if (!isValidNumber(value)) {
            throw new GeneratorException(ErrorMessages.VALUE_OUT_OF_RANGE);
        }

        returnString.append("\nAC <- 0");

        int workValue = Math.abs(value);

        // finding the nearest power of two
        double nearestPowerOfTwo = MathematicalHelper.getNearestPowerOfTwo(workValue);
        returnString.append(generateNearestPowerOfTwo((int) nearestPowerOfTwo));

        if (!MathematicalHelper.isPowerOfTwo(workValue)) {
            // fixing the overhead, when the number is not a power of two
            int difference = (int) (nearestPowerOfTwo - workValue);

            // deciding of i have to subtract 1 or add 1 ->
            // Example: 8 - 7 = 1
            // Example: 16 - 20 = -4
            boolean subtract = difference > 0;

            // iterating to the wished number
            for (int i = 0; i < Math.abs(difference); i++) {
                returnString.append((subtract) ? "\nAC <- AC + -1" : "\nAC <- AC + 1");
            }
        }

        if (value < 0) {
            // the micro16 cpu works in twos complement
            // so to display negative values i have to  invert the value and add 1
            returnString.append(convertToNegativeNumber());
        }

        return Optional.of(returnString.toString());
    }

    /**
     * checks if a given number is in a valid range to work correctly in the micro16
     *
     * @param value the number to check
     * @return true - when the number is valid
     */
    public static boolean isValidNumber(int value) {
        return value >= MAX_NEGATIV_NUMBER && value <= MAX_POSITIV_NUMBER;
    }

    /**
     * converts the value from the register into a negative one
     *
     * @return the instructions to get a negative number
     */
    public static String convertToNegativeNumber() {
        return "\nAC <- ~AC\n" +
                "AC <- AC + 1\n";
    }

    /**
     * Generates the micro 16 instructions to get to the nearest power of two number
     *
     * @param nearestPowerOfTwo the number to generate
     * @return the micro 16 instructions to get to that certain number
     */
    public static String generateNearestPowerOfTwo(int nearestPowerOfTwo) {
        StringBuilder returnString = new StringBuilder();
        // generating that nearest number
        // starting by writing the number 1 into AC
        returnString.append("\nAC <- 1");

        // shifting the number 1 that often to reach the power I want to get
        int powerOfTwo = (int) MathematicalHelper.getPowerOfTwo(nearestPowerOfTwo);
        for (int i = 0; i < powerOfTwo; i++) {
            returnString.append("\nAC <- lsh(AC)");
        }
        return returnString.toString();
    }

}
