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
        String returnString = null;

        if (MathematicalHelper.isPowerOfTwo(value)) {
            // you can generate a number by shifting if its a number made of power 2
            // starting by writing the number 1 into AC
            returnString = "AC <- 1";

            // shifting the number 1 that often to reach the power I want to get
            double powerOfTwo = MathematicalHelper.getPowerOfTwo(value);
            for (int i = 0; i < powerOfTwo; i++) {
                returnString += "\nAC <- lsh(AC)";
            }

        } else {
            //TODO: implement generation of numbers that arent made of power 2
        }

        return Optional.ofNullable(returnString);
    }

}
