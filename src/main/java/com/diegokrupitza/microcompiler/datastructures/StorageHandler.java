package com.diegokrupitza.microcompiler.datastructures;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Project: micro16-compiler
 * Document: StorageHandler.java
 * Author: Diego Krupitza
 * Created: 12.12.18
 */
@Getter
public class StorageHandler {

    public static final boolean[] registerUse = new boolean[11];
    public static final int MEMORY_START_ADDR = 0x0000;

    /**
     * Reserves a slot for a register. If there is no register available there is a null object in the Optional
     *
     * @return the name of the reserver register
     */
    public Optional<String> reserveRegister() {
        int registerId = getFirstFreeRegister();
        String returnRegister = getRegisterName(registerId);
        return Optional.ofNullable(returnRegister);
    }

    /**
     * Generates the name for a given registerId
     *
     * @param registerId the id of the register
     * @return the name of that register as a String
     */
    private String getRegisterName(int registerId) {
        String returnString = null;
        if (registerId == 10) {
            returnString = "PC";
        } else if (registerId >= 0) {
            // every other found register
            registerId++;
            returnString = "R" + registerId;
        }
        return returnString;
    }


    /**
     * Searches for the first free register. If there is no than you get the min value of Integer
     *
     * @return the index of the register
     */
    private int getFirstFreeRegister() {
        int registerId = Integer.MIN_VALUE;
        for (int i = 0; i < registerUse.length; i++) {
            if (!registerUse[i]) {
                registerUse[i] = true;
                registerId = i;
                break;
            }
        }
        return registerId;
    }
}
