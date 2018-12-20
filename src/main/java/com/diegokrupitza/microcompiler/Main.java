package com.diegokrupitza.microcompiler;

import com.diegokrupitza.microcompiler.datastructures.StorageHandler;
import com.diegokrupitza.microcompiler.exceptions.GeneratorException;
import com.diegokrupitza.microcompiler.generator.Micro16Instruction;
import com.diegokrupitza.microcompiler.generator.SimpleVariableMicro16Instruction;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import com.diegokrupitza.microcompiler.messages.ErrorMessages;

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

    public static final String INPUT_CODE_LOCATION = "samplecode/test.mcr";

    // For later improvements like storage management, for example when all the 13 registers are already filled and you want to save a another variable.
    // In that case you habe to write one of the older variables into the memory
    public static final StorageHandler STORAGE_HANDLER = new StorageHandler();
    //public static List<Variable> variableList = new ArrayList<>();


    public static void main(String[] args) {
        setup();

        String inputCode = readCode(INPUT_CODE_LOCATION);
        try {
            parseCode(inputCode);
        } catch (GeneratorException e) {
            log.error("{}", e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Read the code from a file into a String variable for further parsing
     *
     * @param inputCodeLocation the location of the code as String
     * @return the read code
     */
    private static String readCode(String inputCodeLocation) {
        String inputCode = "";
        //TODO this is quite ugly! should be read from command line

        try {
            inputCode = new String(Files.readAllBytes(Paths.get(inputCodeLocation)));
        } catch (IOException e) {
            log.error("{}", ErrorMessages.FILE_NOTEXIST);
            e.printStackTrace();
        }

        // removing all the comments and empty lines from the code
        inputCode = inputCode.replaceAll("(?m)^#.*", "").replaceAll("(?m)^[ \t]*\r?\n", "");
        return inputCode;
    }


    /**
     * Prepares the compiler for parsing and generating the Micro-16 code
     */
    private static void setup() {
        //setting use of all register to false
        Arrays.fill(StorageHandler.registerUse, false);
    }

    private static void parseCode(String inputCode) throws GeneratorException {

        String[] inputCodeRowed = inputCode.split("\n");

        for (int i = 0; i < inputCodeRowed.length; i++) {
            String instructionString = inputCodeRowed[i];
            log.debug("{}", instructionString);

            if (instructionString.startsWith("var")) {
                Micro16Instruction micro16Instruction = new SimpleVariableMicro16Instruction(instructionString);
            }

        }


    }

}