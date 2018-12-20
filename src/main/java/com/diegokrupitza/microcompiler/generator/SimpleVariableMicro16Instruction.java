package com.diegokrupitza.microcompiler.generator;

import com.diegokrupitza.microcompiler.Main;
import com.diegokrupitza.microcompiler.exceptions.GeneratorException;
import com.diegokrupitza.microcompiler.messages.ErrorMessages;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Project: micro16-compiler
 * Document: SimpleVariableMicro16Instruction.java
 * Author: Diego Krupitza
 * Created: 20.12.18
 */
@Slf4j
@Getter
@Setter
public class SimpleVariableMicro16Instruction extends Micro16Instruction {

    private String variableName = "undefined";
    private String value = "undefined";

    public SimpleVariableMicro16Instruction(String instructionString) throws GeneratorException {
        super(instructionString);
    }

    @Override
    public void parseInstruction(String instruction) throws GeneratorException {
        // Starting with a simple instructions -> var a = 4;
        String[] splitedInstruction = instruction.split(" ");

        this.variableName = splitedInstruction[1];
        this.value = splitedInstruction[3];

        log.debug("Varname: {} \t Value: {}", variableName, value);

        generateInstruction();
    }

    @Override
    void generateInstruction() throws GeneratorException {
        // check if there enought space in my registers
        Optional<String> optionalRegister = Main.STORAGE_HANDLER.reserveRegister();

        if (!optionalRegister.isPresent()) {
            //TODO: try to free up some space by moving some values into the memory, so there is no exception to throw
            throw new GeneratorException(ErrorMessages.NO_REGISTER_AVAILABLE);
        }

        // try to generate the number i want to save in my variable
        int value = Integer.parseInt(getValue());
        Optional<String> optionalValueGeneratorCode = ValueGenerator.generateValue(value);
        if (!optionalValueGeneratorCode.isPresent()) {
            throw new GeneratorException(ErrorMessages.CANNOT_GENERATE_NUMBER);
        }

        String registerName = optionalRegister.get();
        String valueGeneratorCode = optionalValueGeneratorCode.get();

        this.microInstruction = valueGeneratorCode + "\n" + registerName + " <- AC\n";
        System.out.println(microInstruction);
    }
}
