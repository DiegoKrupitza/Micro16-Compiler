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

    /**
     * Calculates the nearest power of two number to the given number
     *
     * @param number the number to find the nearest power of two
     * @return the nearest power of two
     */
    public static double getNearestPowerOfTwo(int number) {
        // according to http://graphics.stanford.edu/~seander/bithacks.html#RoundUpPowerOf2Float this is the fastest way to find the nearest power of two
        // this is just till 16 because the micro-16 is only capable of 16 bit
        int v = number;

        v--;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        v++; // next power of 2

        // previous power of 2
        int x = v >> 1;

        return (v - number) > (number - x) ? x : v;

    }

}
