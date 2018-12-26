package com.diegokrupitza.microcompiler.generator;

import com.diegokrupitza.microcompiler.CodeInspector;
import com.diegokrupitza.microcompiler.datastructures.StorageHandler;
import com.diegokrupitza.microcompiler.exceptions.Micro16Exception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class AdvancedVariableICopyMicro16InstructionTest {


    private CodeInspector codeInspector = null;

    @BeforeEach
    public void setUp() {
        this.codeInspector = new CodeInspector();

        // cleaning all the mess that happens after generating a code
        // removing all the variables
        StorageHandler.getVariableList().clear();

        // opening all the registers
        for (int i = 0; i < StorageHandler.getRegisterUse().length; i++) {
            StorageHandler.getRegisterUse()[i] = false;
        }

    }

    @Test
    public void additionTest() throws Micro16Exception {
        String inputInstruction = "var a = 5\n";
        inputInstruction += "var b = a + 2";

        String expectingInstructions = "AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- AC + 1\n" +
                "R1 <- AC\n" +
                "\n" +
                "AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)\n" +
                "AC <- R1 + AC\n" +
                "R2 <- AC";

        codeInspector.parseCode(inputInstruction);
        String result = codeInspector.getOUTPUT_BUILDER().toString().trim();

        assertThat(result).isEqualToIgnoringCase(expectingInstructions.trim());
    }

    @Test
    public void subtractionTest() throws Micro16Exception {
        String inputInstruction = "var a = 5\n";
        inputInstruction += "var b = a - 2";

        String expectingInstructions = "AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- AC + 1\n" +
                "R1 <- AC\n" +
                "\n" +
                "AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)\n" +
                "\n" +
                "AC <- ~AC\n" +
                "AC <- AC + 1\n" +
                "R2 <- R1 + AC";

        codeInspector.parseCode(inputInstruction);
        String result = codeInspector.getOUTPUT_BUILDER().toString().trim();

        assertThat(result).isEqualToIgnoringCase(expectingInstructions.trim());
    }

    @Test
    public void multiplicationTest() throws Micro16Exception {
        String inputInstruction = "var a = -5\n";
        inputInstruction += "var b = a * 2";

        String randomExpectation = "AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- AC + 1\n" +
                "AC <- ~AC\n" +
                "AC <- AC + 1\n" +
                "\n" +
                "R1 <- AC\n" +
                "\n" +
                "AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)\n" +
                "\n" +
                ":multiplyQ\n" +
                "AC; if Z goto .endmultiplyQ\n" +
                "PC <- PC + R1\n" +
                "AC <- AC + -1\n" +
                "goto .multiplyQ\n" +
                ":endmultiply\n" +
                "AC <- PC\n" +
                "\n" +
                "R2 <- AC";

        codeInspector.parseCode(inputInstruction);
        String result = codeInspector.getOUTPUT_BUILDER().toString().trim();

        assertThat(result.split("\n").length).isEqualTo(randomExpectation.split("\n").length);
    }

    @Test
    public void multiplicationPositivTest() throws Micro16Exception {
        String inputInstruction = "var a = 5\n";
        inputInstruction += "var b = a * 2";

        String randomExpectation = "AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- AC + 1\n" +
                "\n" +
                "R1 <- AC\n" +
                "AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)\n" +
                "\n" +
                ":multiplyQ\n" +
                "AC; if Z goto .endmultiplyQ\n" +
                "PC <- PC + R1\n" +
                "AC <- AC + -1\n" +
                "goto .multiplyQ\n" +
                ":endmultiply\n" +
                "AC <- PC\n" +
                "\n" +
                "R2 <- AC";

        codeInspector.parseCode(inputInstruction);
        String result = codeInspector.getOUTPUT_BUILDER().toString().trim();

        assertThat(result.trim().split("\n").length).isEqualTo(randomExpectation.split("\n").length);
    }

    @Test
    public void multiplicationNegativNumberTest() throws Micro16Exception {
        String inputInstruction = "var a = 5\n";
        inputInstruction += "var b = a * -2";

        String randomExpectation = "AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- AC + 1\n" +
                "R1 <- AC\n" +
                "\n" +
                "AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)\n" +
                "\n" +
                ":multiplyTMIRQW4FKT3TUDP5GYKG7LT6JRMJRFGSDS7WOBCX3QYZHII6QBUA\n" +
                "AC; if Z goto .endmultiplyTMIRQW4FKT3TUDP5GYKG7LT6JRMJRFGSDS7WOBCX3QYZHII6QBUA\n" +
                "PC <- PC + R1\n" +
                "AC <- AC + -1\n" +
                "goto .multiplyTMIRQW4FKT3TUDP5GYKG7LT6JRMJRFGSDS7WOBCX3QYZHII6QBUA\n" +
                ":endmultiplyTMIRQW4FKT3TUDP5GYKG7LT6JRMJRFGSDS7WOBCX3QYZHII6QBUA\n" +
                "AC <- PC\n" +
                "AC <- ~AC\n" +
                "AC <- AC + 1\n" +
                "R2 <- AC";

        codeInspector.parseCode(inputInstruction);
        String result = codeInspector.getOUTPUT_BUILDER().toString().trim();

        assertThat(result.trim().split("\n").length).isEqualTo(randomExpectation.split("\n").length);
    }

}