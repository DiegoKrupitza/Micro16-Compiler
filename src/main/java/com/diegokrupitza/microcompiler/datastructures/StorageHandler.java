package com.diegokrupitza.microcompiler.datastructures;

import com.diegokrupitza.microcompiler.Main;
import com.diegokrupitza.microcompiler.exceptions.VariableException;
import com.diegokrupitza.microcompiler.messages.ErrorMessages;
import lombok.Getter;

import java.util.LinkedList;
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
    private static LinkedList<Variable> variableList = new LinkedList<>();

    /**
     * Adds a new variable to the Storagehandler
     *
     * @param variable the variable object to add
     * @throws VariableException when the variable already exists
     */
    public static void addVariable(Variable variable) throws VariableException {
        if (!variableExist(variable.getName())) {
            variableList.addFirst(variable);
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
     * @throws VariableException in case the variable is not existing
     */
    public static String getVariableLocation(String variableName) throws VariableException {
        if (!variableExist(variableName)) {
            throw new VariableException(String.format(ErrorMessages.VARIABLE_DOES_NOT_EXISTS.toString(), variableName, Main.CODE_LINE));
        }

        // variable names are case sensitive
        Variable variable = getVariableList().stream().filter(item -> item.getName().equals(variableName)).findFirst().get();

        if (!variable.isInRegister()) {
            // the var is in the memory that means i have to search for that in the memory
            //TODO: implement memory search
        }

        // for further variable analysis
        variable.setAccessCount(variable.getAccessCount() + 1);
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

    public static LinkedList<Variable> getVariableList() {
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
     * Searches for the first free register and registers it
     * If there is no than you get the min value of Integer
     *
     * @return the index of the register. If there is no free register
     * the return value is <code>Integer.MIN_VALUE</code>
     */
    private int getFirstFreeRegister() {
        int registerId = Integer.MIN_VALUE;
        for (int i = 0; i < REGISTER_USE.length; i++) {
            if (!REGISTER_USE[i]) {
                // reserving the first found
                REGISTER_USE[i] = true;
                registerId = i;
                break;
            }
        }
        return registerId;
    }

    public String freeUpRegister() {
        StringBuilder instructions = new StringBuilder();
        Variable lastVariable = getVariableList().getLast();
        //TODO outsource into memory

        return instructions.toString();
    }
}
