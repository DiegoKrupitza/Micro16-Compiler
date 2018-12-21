package com.diegokrupitza.microcompiler.generator;

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
 * Document: AdvancedVariableMicro16Instruction.java
 * Author: Diego Krupitza
 * Created: 21.12.18
 */
@Slf4j
@Getter
@Setter
public class AdvancedVariableMicro16Instruction extends Micro16Instruction {

    private String variableName = "undefined";
    private String referencedVariable = "undefined";

    public AdvancedVariableMicro16Instruction(String instructionString) throws Micro16Exception {
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
        Optional<String> optionalRegister = Main.STORAGE_HANDLER.reserveRegister();

        if (!optionalRegister.isPresent()) {
            //TODO: try to free up some space by moving some values into the memory, so there is no exception to throw
            throw new GeneratorException(ErrorMessages.NO_REGISTER_AVAILABLE);
        }
        String variableLocation = StorageHandler.getVariableLocation(this.referencedVariable);

        setMicroInstruction(optionalRegister.get() + " <- " + variableLocation + "\n");

        Variable variable = Variable.builder()
                .registerName(optionalRegister.get())
                .name(this.getVariableName())
                .isInRegister(true)
                .value("Read from other register")
                .build();

        StorageHandler.addVariable(variable);
    }
}
