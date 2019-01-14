package com.diegokrupitza.microcompiler;

import com.diegokrupitza.microcompiler.exceptions.FileException;
import com.diegokrupitza.microcompiler.exceptions.Micro16Exception;
import com.diegokrupitza.microcompiler.instructions.AdvancedVariableICopyMicro16Instruction;
import com.diegokrupitza.microcompiler.instructions.Micro16Instruction;
import com.diegokrupitza.microcompiler.instructions.SimpleVariableMicro16Instruction;
import com.diegokrupitza.microcompiler.instructions.VariableICopyMicro16Instruction;
import com.diegokrupitza.microcompiler.messages.ErrorMessages;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class has the job to parse the code for given keywords and to call the correct instruction class.
 * Beside that it also reads the file content of the program
 * </p>
 *
 * @author Diego Krupitza
 * @version 1.1
 * @date 26.12.18
 */
@Slf4j
@Setter
@Getter
public class CodeInspector {

    public static final String FILE_EXTENSION = "mcr";
    private final StringBuilder OUTPUT_BUILDER = new StringBuilder();

    public void parseCode(String inputCode) throws Micro16Exception {

        String[] inputCodeRowed = inputCode.split("\n");

        for (int i = 0; i < inputCodeRowed.length; i++, Main.CODE_LINE++) {
            String instructionString = inputCodeRowed[i];
            log.debug("{}", instructionString);


            if (instructionString.matches("((var)( )((?:[a-z][a-z0-9_]*))( )(=)( )([+-]?)(\\d+))|((var)( )((?:[a-z][a-z0-9_]*))( )(=)( )(\\d+)( )([+-/*])( )(\\d+))") || instructionString.matches("")) {
                // in case the line is -> var [variableName] = [value]
                // or var [variableName] = [value1] [operation] [value2]
                Micro16Instruction micro16Instruction = new SimpleVariableMicro16Instruction(instructionString);
                OUTPUT_BUILDER.append(micro16Instruction.getMicroInstruction());
            } else if (instructionString.matches("((var)( )((?:[a-z][a-z0-9_]*))( )(=)( )(?:[a-z][a-z0-9_]*))")) {
                // normal copying var
                // example var [variableName1] = [variableName2]
                Micro16Instruction micro16Instruction = new VariableICopyMicro16Instruction(instructionString);
                OUTPUT_BUILDER.append(micro16Instruction.getMicroInstruction());
            } else if (instructionString.matches("(var)( )((?:[a-z][a-z0-9_]*))( )(=)( )((?:[a-z][a-z0-9_]*))( )([+-/*])( )([+-]?)(\\d+)|(var)( )((?:[a-z][a-z0-9_]*))( )(=)( )([+-]?)(\\d+)( )([+-/*])( )((?:[a-z][a-z0-9_]*))")) {
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
    protected String readCode(String inputCodeLocation) throws Micro16Exception {
        String inputCode = "";

        if (!FilenameUtils.getExtension(inputCodeLocation).equalsIgnoreCase(FILE_EXTENSION)) {
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
