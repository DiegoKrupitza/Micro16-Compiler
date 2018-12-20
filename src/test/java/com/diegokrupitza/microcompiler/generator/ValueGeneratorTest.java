package com.diegokrupitza.microcompiler.generator;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ValueGeneratorTest {

    @Test
    void generateValuePowerOfTwo1() {
        int number = 1;
        Optional<String> optionalGeneratedCode = ValueGenerator.generateValue(number);
        assertThat(optionalGeneratedCode).isNotEmpty();
        assertThat(optionalGeneratedCode.get()).isEqualTo("AC <- 1");
    }

    @Test
    void generateValuePowerOfTwo32() {
        int number = 32;
        Optional<String> optionalGeneratedCode = ValueGenerator.generateValue(number);
        assertThat(optionalGeneratedCode).isNotEmpty();
        assertThat(optionalGeneratedCode.get()).isEqualTo("AC <- 1\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)\n" +
                "AC <- lsh(AC)");
    }

    @Test
    void generateValuePowerOfTwo2() {
        int number = 2;
        Optional<String> optionalGeneratedCode = ValueGenerator.generateValue(number);
        assertThat(optionalGeneratedCode).isNotEmpty();
        assertThat(optionalGeneratedCode.get()).isEqualTo("AC <- 1\n" +
                "AC <- lsh(AC)");
    }
}