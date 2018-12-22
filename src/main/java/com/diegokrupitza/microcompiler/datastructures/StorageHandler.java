package com.diegokrupitza.microcompiler.datastructures;

import com.diegokrupitza.microcompiler.Main;
import com.diegokrupitza.microcompiler.exceptions.VariableException;
import com.diegokrupitza.microcompiler.messages.ErrorMessages;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Project: micro16-compiler
 * Document: StorageHandler.java
 * Author: Diego Krupitza
 * Created: 12.12.18
 */
@Getter
public class StorageHandler {

    public static final int MEMORY_START_ADDR = 0x0000;
    protected static final boolean[] REGISTER_USE = new boolean[10];
    private static List<Variable> variableList = new ArrayList<>();

    /**
     * Adds a new variable to the Storagehandler
     *
     * @param variable the variable object to add
     * @throws VariableException
     */
    public static void addVariable(Variable variable) throws VariableException {
        if (!variableExist(variable.getName())) {
            variableList.add(variable);
        } else {
            String message = String.format(ErrorMessages.VARIABLE_ALREADY_EXISTS.toString(), variable.getName(), Main.CODE_LINE);
            throw new VariableException(message);
        }
    }

    /**
     * Gets the locations in a register or in memory of a given variable
     *
     * @param variableName the name of the variable
     * @return the location of that variable
     * @throws VariableException
     */
    public static String getVariableLocation(String variableName) throws VariableException {
        if (!variableExist(variableName)) {
            //TODO: check if variable is in memory
            //  till this is implemented an exception is thrown
            throw new VariableException(String.format(ErrorMessages.VARIABLE_DOES_NOT_EXISTS.toString(), variableName, Main.CODE_LINE));
        }

        // variable names a case sensitive
        Variable variable = getVariableList().stream().filter(item -> item.getName().equals(variableName)).findFirst().get();
        return variable.getRegisterName();
    }

    /**
     * checks if an variable does already exists
     *
     * @param variableName the variable name to check on
     * @return true - when the varaible does exist
     */
    public static boolean variableExist(String variableName) {
        return variableList.stream().map(Variable::getName).anyMatch(variableName::equals);
    }

    public static boolean[] getRegisterUse() {
        return REGISTER_USE;
    }

    public static List<Variable> getVariableList() {
        return variableList;
    }

    /**
     * Reserves a slot for a register.
     * If there is no register available there is a null object in the Optional
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
    public String getRegisterName(int registerId) {
        String returnString = null;
        if (registerId >= 0) {
            // every other found register
            registerId++;
            returnString = "R" + registerId;
        }
        return returnString;
    }

    /**
     * Searches for the first free register.
     * If there is no than you get the min value of Integer
     *
     * @return the index of the register
     */
    private int getFirstFreeRegister() {
        int registerId = Integer.MIN_VALUE;
        for (int i = 0; i < REGISTER_USE.length; i++) {
            if (!REGISTER_USE[i]) {
                REGISTER_USE[i] = true;
                registerId = i;
                break;
            }
        }
        return registerId;
    }
}
