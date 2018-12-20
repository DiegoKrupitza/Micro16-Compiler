package com.diegokrupitza.microcompiler.helper;

/**
 * Project: micro16-compiler
 * Document: MathematicalHelper.java
 * Author: Diego Krupitza
 * Created: 20.12.18
 */
public class MathematicalHelper {

    /**
     * checks if a number is a result of power 2
     *
     * @param number the number to check
     * @return is it a number made of power 2
     */
    public static boolean isPowerOfTwo(int number) {
        return (int) (Math.ceil((getPowerOfTwo(number)))) ==
                (int) (Math.floor(getPowerOfTwo(number)));
    }

    /**
     * Gets the power of 2
     *
     * @param number the number to find the power of 2
     * @return the power of 2
     */
    public static double getPowerOfTwo(int number) {
        return Math.log(number) / Math.log(2);
    }

}
