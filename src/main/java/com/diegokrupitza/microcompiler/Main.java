package com.diegokrupitza.microcompiler;

import com.diegokrupitza.microcompiler.datastructures.StorageHandler;
import com.diegokrupitza.microcompiler.exceptions.FileException;
import com.diegokrupitza.microcompiler.exceptions.Micro16Exception;
import com.diegokrupitza.microcompiler.generator.AdvancedVariableICopyMicro16Instruction;
import com.diegokrupitza.microcompiler.generator.Micro16Instruction;
import com.diegokrupitza.microcompiler.generator.SimpleVariableMicro16Instruction;
import com.diegokrupitza.microcompiler.generator.VariableICopyMicro16Instruction;
import com.diegokrupitza.microcompiler.messages.ErrorMessages;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Project: micro16-compiler
 * Document: Main.java
 * Author: Diego Krupitza
 * Created: 12.12.18
 */
@Slf4j
@Getter
@Setter
public class Main {

    public static final String INPUT_CODE_LOCATION = "samplecode/advanced-variables.mcr";

    // For later improvements like storage management.
    // For example when all the 10 registers are already filled and you want to save a another variable.
    // In that case you habe to write one of the older variables into the memory
    public static final StorageHandler STORAGE_HANDLER = new StorageHandler();

    public static final StringBuilder OUTPUT_BUILDER = new StringBuilder();

    public static int CODE_LINE = 1;


    public static void main(String[] args) {
        setup();

        try {
            String inputCode = readCode(INPUT_CODE_LOCATION);
            parseCode(inputCode);
        } catch (Micro16Exception e) {
            log.error("", e);
            System.exit(-1);
        }
        String output = OUTPUT_BUILDER.toString();
        System.out.println(output);
    }

    /**
     * Prepares the compiler for parsing and generating the Micro-16 code
     */
    private static void setup() {
        //setting use of all register to false
        Arrays.fill(StorageHandler.getRegisterUse(), false);
    }

    private static void parseCode(String inputCode) throws Micro16Exception {

        String[] inputCodeRowed = inputCode.split("\n");

        for (int i = 0; i < inputCodeRowed.length; i++, Main.CODE_LINE++) {
            String instructionString = inputCodeRowed[i];
            log.debug("{}", instructionString);


            if (instructionString.matches("((var)( )((?:[a-z][a-z0-9_]*))( )(=)( )(\\d+))|((var)( )((?:[a-z][a-z0-9_]*))( )(=)( )(\\d+)( )([+-/*])( )(\\d+))") || instructionString.matches("")) {
                // in case the line is -> var [variableName] = [value]
                // or var [variableName] = [value1] [operation] [value2]
                Micro16Instruction micro16Instruction = new SimpleVariableMicro16Instruction(instructionString);
                OUTPUT_BUILDER.append(micro16Instruction.getMicroInstruction());
            } else if (instructionString.matches("((var)( )((?:[a-z][a-z0-9_]*))( )(=)( )(?:[a-z][a-z0-9_]*))")) {
                // normal copying var
                // example var [variableName1] = [variableName2]
                Micro16Instruction micro16Instruction = new VariableICopyMicro16Instruction(instructionString);
                OUTPUT_BUILDER.append(micro16Instruction.getMicroInstruction());
            } else if (instructionString.matches("(var)( )((?:[a-z][a-z0-9_]*))( )(=)( )((?:[a-z][a-z0-9_]*))( )([+-/*])( )(\\d+)|(var)( )((?:[a-z][a-z0-9_]*))( )(=)( )(\\d+)( )([+-/*])( )((?:[a-z][a-z0-9_]*))")) {
                // copy the value of a var and combines that with a normal value
                // var [variableName1] = [variableName2] [operation] [value]
                // or
                // var [variableName1] = [value] [operation] [variableName2]
                Micro16Instruction micro16Instruction = new AdvancedVariableICopyMicro16Instruction(instructionString);
                OUTPUT_BUILDER.append(micro16Instruction.getMicroInstruction());
            }
        }
    }

    /**
     * Read the code from a file into a String variable for further parsing
     *
     * @param inputCodeLocation the location of the code as String
     * @return the read code
     */
    private static String readCode(String inputCodeLocation) throws Micro16Exception {
        String inputCode = "";

        if (!FilenameUtils.getExtension(inputCodeLocation).equalsIgnoreCase("mcr")) {
            throw new FileException(ErrorMessages.INVALID_FILE_EXTENSION);
        }

        try {
            inputCode = new String(Files.readAllBytes(Paths.get(inputCodeLocation)));
        } catch (IOException e) {
            log.error("{} \n {}", ErrorMessages.FILE_NOTEXIST, e);
        }

        // removing all the comments and empty lines from the code
        inputCode = inputCode.replaceAll("(?m)^#.*", "").replaceAll("(?m)^[ \t]*\r?\n", "");
        return inputCode;
    }
}