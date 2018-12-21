package com.diegokrupitza.microcompiler.generator;

import com.diegokrupitza.microcompiler.exceptions.Micro16Exception;
import lombok.Getter;
import lombok.Setter;

/**
 * Project: micro16-compiler
 * Document: Micro16Instruction.java
 * Author: Diego Krupitza
 * Created: 20.12.18
 */
@Setter
@Getter
public abstract class Micro16Instruction {

    protected String microInstruction = "";

    Micro16Instruction(String instruction) throws Micro16Exception {
        parseInstruction(instruction);
        generateInstruction();
    }

    /**
     * Generates a matching Micro16Instruction object based on a instruction set
     *
     * @param instruction the parsed code instruction
     * @throws Micro16Exception
     */
    abstract void parseInstruction(String instruction) throws Micro16Exception;

    /**
     * Generates the Micro16 instructions for that given code
     *
     * @throws Micro16Exception
     */
    abstract void generateInstruction() throws Micro16Exception;
}
