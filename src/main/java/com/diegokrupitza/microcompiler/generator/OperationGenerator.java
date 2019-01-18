package com.diegokrupitza.microcompiler.generator;

import com.diegokrupitza.microcompiler.datastructures.LabelHandler;
import com.diegokrupitza.microcompiler.exceptions.GeneratorException;

import java.util.Optional;

/**
 * The Micro16 only has one operation available (+).
 * Because of that you have to implement the other three operations fully be hand!
 * This class aims to handle that lack of operations by generating those operations by using the addition.
 *
 * @author Diego Krupitza
 * @version 1.1
 * @date 25.12.18
 */
public class OperationGenerator {

    /**
     * Generates the instruction to have a multiplication with a variable and a already existing value in the AC register
     * The result is then written into the AC register
     *
     * @param variableLocation the register of one of the multiplicators
     * @return the instruction with the result in the AC register
     * @note !!!IMPORTANT!!! The mulitply number has to be already in the AC register.
     * Otherwise this would not work as wished
     */
    public static Optional<String> generateMultiplication(String variableLocation) throws GeneratorException {
        StringBuilder instruction = new StringBuilder();

        String multiplyLoopLabel = LabelHandler.generateNewLabel("multiply");
        String endMultiplyLoopLabel = "end" + multiplyLoopLabel;

        instruction.append("\n:").append(multiplyLoopLabel);
        instruction.append("\nAC; if Z goto .").append(endMultiplyLoopLabel);
        instruction.append("\nPC <- PC + ").append(variableLocation);
        instruction.append("\nAC <- AC + -1");
        instruction.append("\ngoto .").append(multiplyLoopLabel);
        instruction.append("\n:").append(endMultiplyLoopLabel);
        instruction.append("\nAC <- PC\n");

        return Optional.of(instruction.toString());
    }

    /**
     * Generates the instruction to have a addition with a variable and a already existing value int the AC register
     * The result is then written into the AC register
     *
     * @param variableLocation the register i want to use for the addition
     * @return the instruction with the result in the AC register
     * @note !!!IMPORTANT!!! The other number has to be already in the AC register.
     * Otherwise this would not work as wished
     */
    public static Optional<String> generateAddition(String variableLocation) {
        StringBuilder instruction = new StringBuilder();
        instruction.append("AC <- ");
        instruction.append(variableLocation);
        instruction.append(" + AC");

        return Optional.of(instruction.toString());
    }

}
