import datastructures.StorageHandler;
import datastructures.Variable;
import generator.Instruction;
import generator.VariableInstruction;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import messages.ErrorMessages;

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
    public static List<Variable> variableList = new ArrayList<>();


    public static void main(String[] args) {
        String inputCode = "";
        //TODO this is quite ugly! should be read from command line

        try {
            inputCode = new String(Files.readAllBytes(Paths.get(INPUT_CODE_LOCATION)));
        } catch (IOException e) {
            log.error("{}", ErrorMessages.FILE_NOTEXIST);
            e.printStackTrace();
        }

        // removing all the comments and empty lines from the code
        inputCode = inputCode.replaceAll("(?m)^#.*", "").replaceAll("(?m)^[ \t]*\r?\n", "");

        parseCode(inputCode);

    }

    private static void parseCode(String inputCode) {

        String[] inputCodeRowed = inputCode.split("\n");

        for (int i = 0; i < inputCodeRowed.length; i++) {
            String instructionString = inputCodeRowed[i];
            log.info("{}", instructionString);
            if (instructionString.startsWith("var")) {
                Instruction instruction = new VariableInstruction(instructionString);
            }
        }


    }

}