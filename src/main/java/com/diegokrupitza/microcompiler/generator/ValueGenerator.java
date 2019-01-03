package com.diegokrupitza.microcompiler.generator;

import com.diegokrupitza.microcompiler.exceptions.GeneratorException;
import com.diegokrupitza.microcompiler.helper.MathematicalHelper;
import com.diegokrupitza.microcompiler.messages.ErrorMessages;
import org.apache.commons.lang.StringUtils;

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

    /**
     * Generates the Micro16 instructions to make a number.
     * The result of that calculation is after that saved into the register AC
     *
     * @param value the number i want to have in my AC register
     * @return the code for generating that given number.
     * The result of that generated value is located at the register AC
     * @throws GeneratorException
     */
    public static Optional<String> generateValue(int value) throws GeneratorException {
        //TODO: log more of the process
        // TODO: think if its intelligent to use Optional
        StringBuilder returnString = new StringBuilder();
        if (!isValidNumber(value)) {
            throw new GeneratorException(ErrorMessages.VALUE_OUT_OF_RANGE);
        }

        int workValue = Math.abs(value);

        returnString.append("\nAC <- 0");

        // exit because 0 does not need more calculation
        // this is just because I want to save some steps
        if (workValue == 0) {
            return Optional.of(returnString.toString());
        }

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
            // so to display negative values i have to invert the value and add 1
            returnString.append(convertToNegativeNumber("AC"));
        }

        String returnElement = StringUtils.isBlank(returnString.toString()) ? null : returnString.toString();
        return Optional.ofNullable(returnElement);
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
    public static String convertToNegativeNumber(String register) {
        return "\n" + register + " <- ~" + register + "\n" +
                register + " <- " + register + " + 1\n";
    }

    /**
     * Generates the micro 16 instructions to get to the nearest power of two number
     *
     * @param nearestPowerOfTwo the number to generate
     * @return the micro 16 instructions to get to that certain number.
     * The result of that is located at the register AC
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
