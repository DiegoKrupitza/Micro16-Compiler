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
 * Document: VariableICopyMicro16Instruction.java
 * Author: Diego Krupitza
 * Created: 21.12.18
 */
@Slf4j
@Getter
@Setter
public class AdvancedVariableICopyMicro16Instruction extends Micro16Instruction {

    private String variableName = "undefined";
    private String referencedVariable = "undefined";
    private String operation = "undefined";
    private String addedValue = "undefined";
    private boolean isVariableLeft = true;

    public AdvancedVariableICopyMicro16Instruction(String instructionString) throws Micro16Exception {
        super(instructionString);
    }

    @Override
    public void parseInstruction(String instruction) throws Micro16Exception {

        String[] splitedInstruction = instruction.split(" ");
        this.variableName = splitedInstruction[1];
        this.operation = splitedInstruction[4];

        if (instruction.matches("(var)( )((?:[a-z][a-z0-9_]*))( )(=)( )((?:[a-z][a-z0-9_]*))( )([+-/*])( )(\\d+)")) {
            // var [variableName1] = [variableName2] [operation] [value]
            this.referencedVariable = splitedInstruction[3];
            this.addedValue = splitedInstruction[5];
            this.isVariableLeft = true;
        } else {
            // (var)( )((?:[a-z][a-z0-9_]*))( )(=)( )(\d+)( )([+-/*])( )((?:[a-z][a-z0-9_]*))
            // var [variableName1] = [value] [operation] [variableName2]
            this.referencedVariable = splitedInstruction[5];
            this.addedValue = splitedInstruction[3];
            this.isVariableLeft = false;
        }
    }

    @Override
    public void generateInstruction() throws Micro16Exception {
        StringBuilder instructions = new StringBuilder();

        // check if there enought space in my registers
        Optional<String> optionalRegister = Main.STORAGE_HANDLER.reserveRegister();
        if (!optionalRegister.isPresent()) {
            //TODO: try to free up some space by moving some values into the memory, so there is no exception to throw
            throw new GeneratorException(ErrorMessages.NO_REGISTER_AVAILABLE);
        }

        // the register of the other varaible im using to deklare my other var
        String referecedVariableLocation = StorageHandler.getVariableLocation(this.referencedVariable);

        // generate the add value
        // the value ist store in the AC Register
        Optional<String> optionalValueGeneratorCode = ValueGenerator.generateValue(Integer.parseInt(getAddedValue()));
        if (!optionalValueGeneratorCode.isPresent()) {
            throw new GeneratorException(ErrorMessages.CANNOT_GENERATE_NUMBER);
        }

        // adding the generated variable so its accessable in register AC
        instructions.append(optionalValueGeneratorCode.get());
        instructions.append("\n");

        //TODO: implement cleaner solution
        if (isVariableLeft()) {
            if (getOperation().equals("+")) {
                instructions.append(optionalRegister.get());
                instructions.append(" <- ");
                instructions.append(referecedVariableLocation);
                instructions.append(" ");
                instructions.append(this.getOperation());
                instructions.append(" AC");
            } else if (getOperation().equals("-")) {
                instructions.append(ValueGenerator.convertToNegativeNumber("AC"));
                instructions.append(optionalRegister.get());
                instructions.append(" <- ");
                instructions.append(referecedVariableLocation);
                instructions.append(" +");
                instructions.append(" AC");
            } else if (getOperation().equals("*")) {
                //TODO
            } else if (getOperation().equals("/")) {
                //TODO
            }
        } else {
            if (getOperation().equals("+")) {
                instructions.append(optionalRegister.get());
                instructions.append(" <- ");
                instructions.append("AC ");
                instructions.append(this.getOperation());
                instructions.append(" ");
                instructions.append(referecedVariableLocation);
            } else if (getOperation().equals("-")) {
                instructions.append("PC <- " + referecedVariableLocation);
                instructions.append(ValueGenerator.convertToNegativeNumber("PC"));

                instructions.append(optionalRegister.get());
                instructions.append(" <- AC + PC");
            } else if (getOperation().equals("*")) {
                //TODO
            } else if (getOperation().equals("/")) {
                //TODO
            }
        }

        Variable variable = Variable.builder()
                .registerName(optionalRegister.get())
                .name(this.getVariableName())
                .isInRegister(true)
                .value("Read from other register and calculated with a other value")
                .build();

        StorageHandler.addVariable(variable);

        setMicroInstruction(instructions.toString());
    }
}
