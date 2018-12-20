package com.diegokrupitza.microcompiler.generator;

import com.diegokrupitza.microcompiler.helper.MathematicalHelper;

import java.util.Optional;

/**
 * Project: micro16-compiler
 * Document: ValueGenerator.java
 * Author: Diego Krupitza
 * Created: 20.12.18
 */
public class ValueGenerator {

    /**
     * Generates the Micro16 instructions to make a number. The result of that calculation is after that saved into the register AC
     *
     * @param value the number i want to have in my AC register
     * @return the code for generating that given number
     */
    public static Optional<String> generateValue(int value) {
        String returnString = "\nAC <- 0";

        if (MathematicalHelper.isPowerOfTwo(value)) {
            // you can generate a number by shifting if its a number made of power 2
            // starting by writing the number 1 into AC
            returnString += "\nAC <- 1";

            // shifting the number 1 that often to reach the power I want to get
            double powerOfTwo = MathematicalHelper.getPowerOfTwo(value);
            for (int i = 0; i < powerOfTwo; i++) {
                returnString += "\nAC <- lsh(AC)";
            }
        } else {
            // finding the nearest power of two
            double nearestPowerOfTwo = MathematicalHelper.getNearestPowerOfTwo(value);

            // generating that nearest number
            // starting by writing the number 1 into AC
            returnString += "\nAC <- 1";

            // shifting the number 1 that often to reach the power I want to get
            int powerOfTwo = (int) MathematicalHelper.getPowerOfTwo((int) nearestPowerOfTwo);
            for (int i = 0; i < powerOfTwo; i++) {
                returnString += "\nAC <- lsh(AC)";
            }

            // fixing the overhead
            int difference = (int) (nearestPowerOfTwo - value);

            // deciding of i have to subtrac 1 or add 1 ->
            // Example: 8 - 7 = 1
            // Example: 16 - 20 = -4
            boolean subtract = difference > 0;

            // iterating to the wished number
            for (int i = 0; i < Math.abs(difference); i++) {
                returnString += (subtract) ? "\nAC <- AC + -1" : "\nAC <- AC + 1";
            }
        }

        return Optional.of(returnString);
    }

}
