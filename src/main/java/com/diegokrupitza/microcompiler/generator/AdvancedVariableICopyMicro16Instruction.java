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
    private int addedValue = Integer.MIN_VALUE;
    private boolean isVariableLeft = true;

    public AdvancedVariableICopyMicro16Instruction(String instructionString) throws Micro16Exception {
        super(instructionString);
    }

    @Override
    public void parseInstruction(String instruction) throws Micro16Exception {

        String[] splitedInstruction = instruction.split(" ");
        this.variableName = splitedInstruction[1];
        this.operation = splitedInstruction[4];

        if (instruction.matches("(var)( )((?:[a-z][a-z0-9_]*))( )(=)( )((?:[a-z][a-z0-9_]*))( )([+-/*])( )([+-]?)(\\d+)")) {
            // var [variableName1] = [variableName2] [operation] [value]
            this.referencedVariable = splitedInstruction[3];
            this.addedValue = Integer.parseInt(splitedInstruction[5]);
            this.isVariableLeft = true;
        } else {
            // (var)( )((?:[a-z][a-z0-9_]*))( )(=)( )([+-]?)(\d+)( )([+-/*])( )((?:[a-z][a-z0-9_]*))
            // var [variableName1] = [value] [operation] [variableName2]
            this.referencedVariable = splitedInstruction[5];
            this.addedValue = Integer.parseInt(splitedInstruction[3]);
            this.isVariableLeft = false;
        }
    }

    @Override
    public void generateInstruction() throws Micro16Exception {
        StringBuilder instructions = new StringBuilder();

        // check if there enought space in my registers and reservers a place for that instructions
        Optional<String> currentWorkRegister = Main.STORAGE_HANDLER.reserveRegister();
        if (!currentWorkRegister.isPresent()) {
            //TODO: try to free up some space by moving some values into the memory, so there is no exception to throw
            throw new GeneratorException(ErrorMessages.NO_REGISTER_AVAILABLE);
        }

        // the register of the other varaible im using to deklare my other var
        String referecedVariableLocation = StorageHandler.getVariableLocation(this.referencedVariable);

        // generate the add value
        // the value ist store in the AC Register
        Optional<String> optionalValueGeneratorCode = ValueGenerator.generateValue(getAddedValue());
        if (!optionalValueGeneratorCode.isPresent()) {
            throw new GeneratorException(ErrorMessages.CANNOT_GENERATE_NUMBER);
        }

        // adding the generated variable so its accessable in register AC
        instructions.append(optionalValueGeneratorCode.get());
        instructions.append("\n");

        //TODO: implement cleaner solution
        if (isVariableLeft()) {
            if ("+".equals(getOperation())) {
                Optional<String> optionalAdditionInstruction = OperationHandler.generateAddition(referecedVariableLocation);
                if (!optionalAdditionInstruction.isPresent()) {
                    throw new GeneratorException(ErrorMessages.CANNOT_GENERATE_ADDITION);
                }
                instructions.append(optionalAdditionInstruction.get());
                instructions.append("\n").append(currentWorkRegister.get()).append(" <- AC");
            } else if ("-".equals(getOperation())) {
                instructions.append(ValueGenerator.convertToNegativeNumber("AC"));
                instructions.append(currentWorkRegister.get());
                instructions.append(" <- ");
                instructions.append(referecedVariableLocation);
                instructions.append(" +");
                instructions.append(" AC");
            } else if ("*".equals(getOperation())) {

                // mulitplication with a negative number is easier calculation without the negative number
                // and then negate the result
                // PART 1
                if (addedValue < 0) {
                    // deleting the last two rows because that the place where the number is negated
                    int last = instructions.lastIndexOf("AC <- ~AC");
                    instructions.delete(last, instructions.length());

                }

                Optional<String> optionalMultiplyInstructions = OperationHandler.generateMultiplication(referecedVariableLocation);
                if (!optionalMultiplyInstructions.isPresent()) {
                    throw new GeneratorException(ErrorMessages.CANNOT_GENERATE_MULTIPLICATION);
                }
                instructions.append(optionalMultiplyInstructions.get());

                // mulitplication with a negative number is easier calculation without the negative number
                // and then negate the result
                // PART 2
                if (addedValue < 0) {
                    // negating the number so the result is still correct
                    instructions.append("AC <- ~AC\n");
                    instructions.append("AC <- AC + 1");
                }

                instructions.append("\n").append(currentWorkRegister.get()).append(" <- AC");
            } else if ("/".equals(getOperation())) {
                //TODO

            }
        } else {
            if ("+".equals(getOperation())) {
                Optional<String> optionalAdditionInstruction = OperationHandler.generateAddition(referecedVariableLocation);
                if (!optionalAdditionInstruction.isPresent()) {
                    throw new GeneratorException(ErrorMessages.CANNOT_GENERATE_ADDITION);
                }
                instructions.append(optionalAdditionInstruction.get());
                instructions.append("\n").append(currentWorkRegister.get()).append(" <- AC");
            } else if ("-".equals(getOperation())) {
                instructions.append("PC <- ").append(referecedVariableLocation);
                instructions.append(ValueGenerator.convertToNegativeNumber("PC"));

                instructions.append(currentWorkRegister.get());
                instructions.append(" <- AC + PC");
            } else if ("*".equals(getOperation())) {
                //TODO
            } else if ("/".equals(getOperation())) {
                //TODO
            }
        }

        Variable variable = Variable.builder()
                .registerName(currentWorkRegister.get())
                .name(this.getVariableName())
                .isInRegister(true)
                .value("Read from other register and calculated with a other value")
                .build();

        StorageHandler.addVariable(variable);

        setMicroInstruction(instructions.toString());
    }
}
