package com.diegokrupitza.microcompiler.datastructures;

import com.diegokrupitza.microcompiler.Main;
import com.diegokrupitza.microcompiler.exceptions.GeneratorException;
import com.diegokrupitza.microcompiler.exceptions.MemoryException;
import com.diegokrupitza.microcompiler.exceptions.VariableException;
import com.diegokrupitza.microcompiler.generator.ValueGenerator;
import com.diegokrupitza.microcompiler.messages.ErrorMessages;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Optional;

/**
 * Project: micro16-compiler
 * Document: StorageHandler.java
 *
 * @author Diego Krupitza
 * @version 1.1
 * @date 12.12.18
 */
@Getter
@Slf4j
public class StorageHandler {

    public static final int MEMORY_START_ADDR = 0x0000;
    public static final boolean[] MEMORY_USE = new boolean[254];
    protected static final boolean[] REGISTER_USE = new boolean[10];
    private static LinkedList<Variable> variableRegisterStack = new LinkedList<>();

    /**
     * Adds a new variable to the Storagehandler
     *
     * @param variable the variable object to add
     * @throws VariableException when the variable already exists
     */
    public static void addVariable(Variable variable) throws VariableException {
        if (!variableExist(variable.getName())) {
            log.debug("Adding a new variable with the name {}", variable.getName());
            variableRegisterStack.addFirst(variable);
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
        log.debug("Trying to get the location of the variable {}", variableName);
        if (!variableExist(variableName)) {
            throw new VariableException(String.format(ErrorMessages.VARIABLE_DOES_NOT_EXISTS.toString(), variableName, Main.CODE_LINE));
        }

        // variable names are case sensitive
        Variable variable = getVariableRegisterStack().stream().filter(item -> item.getName().equals(variableName)).findFirst().get();

        if (!variable.isInRegister()) {
            log.debug("Checking if the variable is in memory");
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
        return variableRegisterStack.stream().map(Variable::getName).anyMatch(variableName::equals);
    }

    public static boolean[] getRegisterUse() {
        return REGISTER_USE;
    }

    public static int getMemoryStartAddr() {
        return MEMORY_START_ADDR;
    }

    public static boolean[] getMemoryUse() {
        return MEMORY_USE;
    }

    public static LinkedList<Variable> getVariableRegisterStack() {
        return variableRegisterStack;
    }

    /**
     * Searches for the first free memory and registers it
     * If there is no than you get the min value of Integer
     *
     * @return the index of the memory address. If there is no free memory address
     * the return value is <code>Integer.MIN_VALUE</code>
     */
    private static int getFirstFreeMemoryAddress() {
        int addressId = Integer.MIN_VALUE;
        for (int i = 0; i < MEMORY_USE.length; i++) {
            if (!MEMORY_USE[i]) {
                // reserving the first found
                MEMORY_USE[i] = true;
                addressId = i;
                break;
            }
        }
        return addressId;
    }

    /**
     * Reserves a slot for a register.
     * If there is no register available there is a null object in the Optional
     *
     * @return the name of the reserved register
     */
    public Optional<String> reserveRegister() {
        log.debug("Reserving a new register");
        int registerId = getFirstFreeRegister();
        String returnRegister = getRegisterName(registerId);

        log.info("Reserved the register {} with the ID {}", returnRegister, registerId);
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
        log.debug("Converting the id {}", registerId);
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

    /**
     * Generates the instructions to free up a register.
     * It outsources the oldest not used register into the memory
     *
     * @return an <code>OutsourcedRegister</code> object that contains the name of the free Register
     * and the instructions to outsource the old one
     * @throws MemoryException in case there are no more memory addreses free
     * @note AC register has to be free
     */
    public OutsourcedRegister freeUpRegister() throws MemoryException, GeneratorException {
        OutsourcedRegister returnOutsourcedRegister = null;
        StringBuilder instructions = new StringBuilder();

        // just getting the most important information about the variabel
        Variable lastVariable = getOldestNotUsedVariable();
        String variableName = lastVariable.getName();
        String registerName = lastVariable.getRegisterName();

        log.info("The variable {} in register {} gets outsourced", variableName, registerName);

        // getting the right place to store the value of that variable.
        int addressId = getFirstFreeMemoryAddress();
        if (addressId == Integer.MIN_VALUE) {
            throw new MemoryException(ErrorMessages.NO_FREE_MEMORYADDRESS);
        }
        int memAddress = convertToMemoryAddress(addressId);
        log.info("Outsource location of {} is {} in memory", registerName, memAddress);

        // the instruction to move the value from reg into memory

        String moveIntoMemoryInstruction = moveIntoMemory(registerName, memAddress);
        instructions.append(moveIntoMemoryInstruction);

        // setting the correct information of that variable
        lastVariable.setInRegister(false);
        lastVariable.setRegisterName("MEMORY");
        lastVariable.setMemoryAddress(memAddress);
        lastVariable.setValue("MEMORY");

        // relocating the outsourced variable in the order of variable list
        getVariableRegisterStack().addFirst(lastVariable);

        returnOutsourcedRegister = OutsourcedRegister.builder()
                .registerName(registerName)
                .instruction(instructions.toString())
                .build();

        return returnOutsourcedRegister;
    }

    /**
     * Moves the value from one register into the memory at a certain address
     *
     * @param registerName the register to move in
     * @param memAddress   the address where to store the value from the register
     * @return the instructions to move the value from a register into the <code>memAddress</code> memory address
     * @throws GeneratorException thrown in case the <code>memAddress</code> cannot be generated
     */
    public String moveIntoMemory(String registerName, int memAddress) throws GeneratorException {
        StringBuilder instructions = new StringBuilder();

        // we have to generate the value of the address into the AC register
        // because the micro16 is not able to direct address
        Optional<String> optionalValue = ValueGenerator.generateValue(memAddress);
        if (!optionalValue.isPresent()) {
            throw new GeneratorException(ErrorMessages.CANNOT_GENERATE_MEMORY_NUMBER);
        }

        instructions.append("\n")
                .append(optionalValue.get())
                .append("\n")
                .append("MAR <- AC\n")
                .append("MBR <- ").append(registerName).append("; wr\n")
                .append("wr\n");

        return instructions.toString();
    }


    /**
     * Gets the oldest not used variable that is not the the memory.
     *
     * @return the oldest not used variable
     */
    private Variable getOldestNotUsedVariable() {
        Variable returnVariable = null;
        do {
            returnVariable = getVariableRegisterStack().getLast();
        } while (!returnVariable.isInRegister());

        // removing it from the list so
        // its simpler to relocate later
        getVariableRegisterStack().remove(returnVariable);

        return returnVariable;
    }

    /**
     * Converts the id of the <code>MEMORY_USE</code> array into a memory address
     * that can be use in the Micro16
     *
     * @param addressId the id of that memory address in the <code>MEMORY_USE</code> array
     * @return the calculated Memory address to use in a Micro16, in case that there is an
     * invalid memory address generated the return value is <code>Integer.MIN_VALUE</code>
     */
    private int convertToMemoryAddress(int addressId) {
        int returnAddress = MEMORY_START_ADDR + addressId;
        return (returnAddress < 0 || returnAddress > MEMORY_USE.length - 1) ? Integer.MIN_VALUE : returnAddress;
    }
}
