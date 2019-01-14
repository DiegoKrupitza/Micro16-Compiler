package com.diegokrupitza.microcompiler.instructions;

import com.diegokrupitza.microcompiler.Main;
import com.diegokrupitza.microcompiler.datastructures.OutsourcedRegister;
import com.diegokrupitza.microcompiler.datastructures.StorageHandler;
import com.diegokrupitza.microcompiler.datastructures.Variable;
import com.diegokrupitza.microcompiler.exceptions.GeneratorException;
import com.diegokrupitza.microcompiler.exceptions.Micro16Exception;
import com.diegokrupitza.microcompiler.generator.ValueGenerator;
import com.diegokrupitza.microcompiler.helper.MathematicalHelper;
import com.diegokrupitza.microcompiler.messages.ErrorMessages;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Project: micro16-compiler
 * Document: SimpleVariableMicro16Instruction.java
 *
 * @author Diego Krupitza
 * @version 1.1
 * @date 20.12.18
 */
@Slf4j
@Getter
@Setter
public class SimpleVariableMicro16Instruction extends Micro16Instruction {

    private String variableName = "undefined";
    private String value = "undefined";

    public SimpleVariableMicro16Instruction(String instructionString) throws Micro16Exception {
        super(instructionString);
    }

    @Override
    public void parseInstruction(String instruction) throws Micro16Exception {
        // Starting with a simple instructions -> var a = 4;
        String[] splitedInstruction = instruction.split(" ");

        this.variableName = splitedInstruction[1];
        this.value = splitedInstruction[3];

        if (instruction.matches("(var)( )((?:[a-z][a-z0-9_]*))( )(=)( )(\\d+)( )([+-/*])( )(\\d+)")) {
            int leftValue = Integer.parseInt(splitedInstruction[3]);
            String operation = splitedInstruction[4];
            int rightValue = Integer.parseInt(splitedInstruction[5]);

            // deciding what operation you should use
            // based on the result there is a chance to optimize the calculation already before compiling
            int tempVal = MathematicalHelper.getBinaryOperationResult(leftValue, operation, rightValue);

            this.value = tempVal + "";
        }

        log.debug("Varname: {} \t Value: {}", variableName, value);
    }

    @Override
    void generateInstruction() throws Micro16Exception {
        StringBuilder instruction = new StringBuilder();

        String registerName = null;

        // check if there enought space in my registers
        // getting the name of the to use register
        Optional<String> currentWorkRegister = Main.STORAGE_HANDLER.reserveRegister();
        if (!currentWorkRegister.isPresent()) {
            OutsourcedRegister outsourcedRegister = Main.STORAGE_HANDLER.freeUpRegister();

            registerName = outsourcedRegister.getRegisterName();
            String outsourceInstructions = outsourcedRegister.getInstruction();

            // adding the outsource instruction to the instruction set
            instruction.append(outsourceInstructions)
                    .append("\n");
        } else {
            registerName = currentWorkRegister.get();
        }

        // try to generate the number i want to save in my variable
        int calculatedValue = Integer.parseInt(getValue());
        Optional<String> optionalValueGeneratorCode = ValueGenerator.generateValue(calculatedValue);
        if (!optionalValueGeneratorCode.isPresent()) {
            throw new GeneratorException(ErrorMessages.CANNOT_GENERATE_NUMBER);
        }

        String valueGeneratorCode = optionalValueGeneratorCode.get();

        // generating the micro instruction based on the register name and generated value
        instruction.append(valueGeneratorCode)
                .append("\n")
                .append(registerName)
                .append(" <- AC\n");

        // saving the generated information into the list of all defined variables
        Variable variable = Variable.builder()
                .isInRegister(true)
                .name(this.variableName)
                .value(String.valueOf(this.value))
                .registerName(registerName)
                .build();

        StorageHandler.addVariable(variable);

        setMicroInstruction(instruction.toString());
    }
}
