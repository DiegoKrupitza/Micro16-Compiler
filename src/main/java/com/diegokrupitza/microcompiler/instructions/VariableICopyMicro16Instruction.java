package com.diegokrupitza.microcompiler.instructions;

import com.diegokrupitza.microcompiler.Main;
import com.diegokrupitza.microcompiler.datastructures.StorageHandler;
import com.diegokrupitza.microcompiler.datastructures.Variable;
import com.diegokrupitza.microcompiler.exceptions.GeneratorException;
import com.diegokrupitza.microcompiler.exceptions.Micro16Exception;
import com.diegokrupitza.microcompiler.messages.ErrorMessages;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Project: micro16-compiler
 * Document: VariableICopyMicro16Instruction.java
 *
 * @author Diego Krupitza
 * @version 1.1
 * @date 21.12.18
 */
@Slf4j
@Getter
@Setter
public class VariableICopyMicro16Instruction extends Micro16Instruction {

    private String variableName = "undefined";
    private String referencedVariable = "undefined";

    public VariableICopyMicro16Instruction(String instructionString) throws Micro16Exception {
        super(instructionString);
    }

    @Override
    public void parseInstruction(String instruction) throws Micro16Exception {
        // var a = b
        String[] splitedInstruction = instruction.split(" ");

        this.variableName = splitedInstruction[1];
        this.referencedVariable = splitedInstruction[3];
    }

    @Override
    public void generateInstruction() throws Micro16Exception {

        // check if there enought space in my registers
        Optional<String> currentWorkRegister = Main.STORAGE_HANDLER.reserveRegister();

        if (!currentWorkRegister.isPresent()) {
            //TODO: try to free up some space by moving some values into the memory, so there is no exception to throw
            throw new GeneratorException(ErrorMessages.NO_REGISTER_AVAILABLE);
        }
        String variableLocation = StorageHandler.getVariableLocation(this.referencedVariable);

        setMicroInstruction(currentWorkRegister.get() + " <- " + variableLocation + "\n");

        Variable variable = Variable.builder()
                .registerName(currentWorkRegister.get())
                .name(this.getVariableName())
                .isInRegister(true)
                .value("Read from other register")
                .build();

        StorageHandler.addVariable(variable);
    }
}
