package generator;

/**
 * Project: micro16-compiler
 * Document: Instruction.java
 * Author: Diego Krupitza
 * Created: 20.12.18
 */
public abstract class Instruction {

    Instruction(String instruction) {
        parseInstruction(instruction);
    }

    abstract Instruction parseInstruction(String instruction);
}
