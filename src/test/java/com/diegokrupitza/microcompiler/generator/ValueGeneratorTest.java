package com.diegokrupitza.microcompiler.generator;

import com.diegokrupitza.microcompiler.datastructures.StorageHandler;
import com.diegokrupitza.microcompiler.exceptions.GeneratorException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class ValueGeneratorTest {

    @Test
    public void generateValuePowerOfTwo1() throws GeneratorException {
        int number = 1;
        Optional<String> optionalGeneratedCode = ValueGenerator.generateValue(number);
        assertThat(optionalGeneratedCode).isNotEmpty();
        assertThat(optionalGeneratedCode.get().trim()).isEqualTo("AC <- 0\n" +
                "AC <- 1".trim());
    }

    @Test
    public void generateValuePowerOfTwo32() throws GeneratorException {
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
    public void generateValuePowerOfTwo2() throws GeneratorException {
        int number = 2;
        Optional<String> optionalGeneratedCode = ValueGenerator.generateValue(number);
        assertThat(optionalGeneratedCode).isNotEmpty();
        assertThat(optionalGeneratedCode.get().trim()).isEqualTo("AC <- 0\n" +
                "AC <- 1\n" +
                "AC <- lsh(AC)".trim());
    }

    @Test
    public void generateValuePowerNumber3() throws GeneratorException {
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
    public void generateNegativeNumer4() throws GeneratorException {
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
    public void generateNegativeNumer() throws GeneratorException {
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
    public void generateNumberException() {
        int number = -32769;
        assertThatExceptionOfType(GeneratorException.class)
                .isThrownBy(() -> ValueGenerator.generateValue(number));
    }

    @Test
    public void validNumberTest() {
        int number = 3;
        boolean validNumber = ValueGenerator.isValidNumber(number);
        assertThat(validNumber).isEqualTo(true);
    }

    @Test
    public void validNumberBigTest() {
        int number = -543;
        boolean validNumber = ValueGenerator.isValidNumber(number);
        assertThat(validNumber).isEqualTo(true);
    }

    @Test
    public void validNumberCloseTest() {
        int number = -32768;
        boolean validNumber = ValueGenerator.isValidNumber(number);
        assertThat(validNumber).isEqualTo(true);
    }

    @Test
    public void invalidNegNumberCloseTest() {
        int number = -32769;
        boolean validNumber = ValueGenerator.isValidNumber(number);
        assertThat(validNumber).isEqualTo(false);
    }

    @Test
    public void invalidPosNumberCloseTest() {
        int number = 32769;
        boolean validNumber = ValueGenerator.isValidNumber(number);
        assertThat(validNumber).isEqualTo(false);
    }

}