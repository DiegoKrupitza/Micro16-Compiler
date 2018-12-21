package com.diegokrupitza.microcompiler.generator;

import com.diegokrupitza.microcompiler.exceptions.GeneratorException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ValueGeneratorTest {

    @Test
    void generateValuePowerOfTwo1() throws GeneratorException {
        int number = 1;
        Optional<String> optionalGeneratedCode = ValueGenerator.generateValue(number);
        assertThat(optionalGeneratedCode).isNotEmpty();
        assertThat(optionalGeneratedCode.get().trim()).isEqualTo("AC <- 0\n" +
                "AC <- 1".trim());
    }

    @Test
    void generateValuePowerOfTwo32() throws GeneratorException {
        int number = 32;
        Optional<String> optionalGeneratedCode = ValueGenerator.generateValue(number);
        assertThat(optionalGeneratedCode).isNotEmpty();
        assertThat(optionalGeneratedCode.get().trim()).isEqualTo("AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)".trim());
    }

    @Test
    void generateValuePowerOfTwo2() throws GeneratorException {
        int number = 2;
        Optional<String> optionalGeneratedCode = ValueGenerator.generateValue(number);
        assertThat(optionalGeneratedCode).isNotEmpty();
        assertThat(optionalGeneratedCode.get().trim()).isEqualTo("AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)".trim());
    }

    @Test
    void generateValuePowerNumber3() throws GeneratorException {
        int number = 3;
        Optional<String> optionalGeneratedCode = ValueGenerator.generateValue(number);
        assertThat(optionalGeneratedCode).isNotEmpty();
        assertThat(optionalGeneratedCode.get().trim()).isEqualTo("AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- AC + -1".trim());
    }

    @Test
    void generateNegativeNumer4() throws GeneratorException {
        int number = -4;
        Optional<String> optionalGeneratedCode = ValueGenerator.generateValue(number);
        assertThat(optionalGeneratedCode).isNotEmpty();
        assertThat(optionalGeneratedCode.get().trim()).isEqualTo("AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- ~AC\n" +
                "AC <- AC + 1".trim());
    }

    @Test
    void generateNegativeNumer() throws GeneratorException {
        int number = -65;
        Optional<String> optionalGeneratedCode = ValueGenerator.generateValue(number);
        assertThat(optionalGeneratedCode).isNotEmpty();
        assertThat(optionalGeneratedCode.get().trim()).isEqualTo("AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- AC + 1\n" +
                "AC <- ~AC\n" +
                "AC <- AC + 1".trim());
    }

    @Test
    void validNumberTest() {
        int number = 3;
        boolean validNumber = ValueGenerator.isValidNumber(number);
        assertThat(validNumber).isEqualTo(true);
    }

    @Test
    void validNumberBigTest() {
        int number = -543;
        boolean validNumber = ValueGenerator.isValidNumber(number);
        assertThat(validNumber).isEqualTo(true);
    }

    @Test
    void validNumberCloseTest() {
        int number = -32768;
        boolean validNumber = ValueGenerator.isValidNumber(number);
        assertThat(validNumber).isEqualTo(true);
    }

    @Test
    void invalidNegNumberCloseTest() {
        int number = -32769;
        boolean validNumber = ValueGenerator.isValidNumber(number);
        assertThat(validNumber).isEqualTo(false);
    }

    @Test
    void invalidPosNumberCloseTest() {
        int number = 32769;
        boolean validNumber = ValueGenerator.isValidNumber(number);
        assertThat(validNumber).isEqualTo(false);
    }


}