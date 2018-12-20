package com.diegokrupitza.microcompiler.generator;

import com.diegokrupitza.microcompiler.exceptions.GeneratorException;

/**
 * Project: micro16-compiler
 * Document: Micro16Instruction.java
 * Author: Diego Krupitza
 * Created: 20.12.18
 */
public abstract class Micro16Instruction {

    public String microInstruction = "";

    Micro16Instruction(String instruction) throws GeneratorException {
        parseInstruction(instruction);
    }

    /**
     * Generates a matching Micro16Instruction object based on a instruction set
     *
     * @param instruction the parsed code instruction
     */
    abstract void parseInstruction(String instruction) throws GeneratorException;

    /**
     * Generates the Micro16 instructions for that given code
     */
    abstract void generateInstruction() throws GeneratorException;
}
