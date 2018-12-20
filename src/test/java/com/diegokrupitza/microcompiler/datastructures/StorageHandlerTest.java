package com.diegokrupitza.microcompiler.datastructures;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class StorageHandlerTest {

    @Test
    void reserveRegisterPositivTest() {
        StorageHandler storageHandler = new StorageHandler();

        Optional<String> reserveRegister = storageHandler.reserveRegister();
        assertThat(reserveRegister).isNotEmpty();
        assertThat(reserveRegister.get()).isEqualTo("R1");
    }

    @Test
    void reserveRegisterNegativeTest() {
        //checking if when there is no register free the return is null
        Arrays.fill(StorageHandler.REGISTER_USE, true);
        StorageHandler storageHandler = new StorageHandler();

        Optional<String> reserveRegister = storageHandler.reserveRegister();
        assertThat(reserveRegister.isPresent()).isEqualTo(false);
    }
}