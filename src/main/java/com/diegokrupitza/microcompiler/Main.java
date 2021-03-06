package com.diegokrupitza.microcompiler;

import com.diegokrupitza.microcompiler.datastructures.StorageHandler;
import com.diegokrupitza.microcompiler.exceptions.Micro16Exception;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * The main entry point of the Micro16-Compiler.
 *
 * @author Diego Krupitza
 * @date 12.12.18
 */
@Slf4j
@Getter
@Setter
public class Main {

    public static final String INPUT_CODE_LOCATION = "samplecode/variable-outsourcing-getback.mcr";


    public static final StorageHandler STORAGE_HANDLER = new StorageHandler();

    public static final CodeInspector CODE_INSPECTOR = new CodeInspector();

    public static int CODE_LINE = 1;


    public static void main(String[] args) {
        setup();

        try {
            String inputCode = CODE_INSPECTOR.readCode(INPUT_CODE_LOCATION);
            CODE_INSPECTOR.parseCode(inputCode);
        } catch (Micro16Exception e) {
            log.error("", e);
            System.exit(-1);
        }
        String output = CODE_INSPECTOR.getOUTPUT_BUILDER().toString();
        System.out.println(output);
    }

    /**
     * Prepares the compiler for parsing and generating the Micro-16 code
     */
    private static void setup() {

        //TODO: memory advanced settings by using an argument flag
        //setting the use of all memory addresses to false
        Arrays.fill(StorageHandler.getMemoryUse(), false);

        //setting use of all register to false
        Arrays.fill(StorageHandler.getRegisterUse(), false);
    }


}