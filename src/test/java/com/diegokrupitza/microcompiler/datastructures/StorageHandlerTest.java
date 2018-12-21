package com.diegokrupitza.microcompiler.datastructures;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class StorageHandlerTest {

    @Test
    public void reserveRegisterPositivTest() {
        StorageHandler storageHandler = new StorageHandler();

        Optional<String> reserveRegister = storageHandler.reserveRegister();
        assertThat(reserveRegister).isNotEmpty();
        assertThat(reserveRegister.get()).isEqualTo("R1");
    }

    @Test
    public void reserveRegisterNegativeTest() {
        //checking if when there is no register free the return is null
        Arrays.fill(StorageHandler.REGISTER_USE, true);
        StorageHandler storageHandler = new StorageHandler();

        Optional<String> reserveRegister = storageHandler.reserveRegister();
        assertThat(reserveRegister.isPresent()).isEqualTo(false);
    }

    @Test
    public void getRegisterNameTest() {
        StorageHandler storageHandler = new StorageHandler();

        int[] registers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Arrays.stream(registers).forEach(
                item -> assertThat(storageHandler.getRegisterName(item)).isEqualToIgnoringCase("R" + ++item)
        );

        assertThat(storageHandler.getRegisterName(10)).isEqualToIgnoringCase("PC");
    }
}