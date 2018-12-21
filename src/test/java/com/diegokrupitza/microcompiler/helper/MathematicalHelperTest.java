package com.diegokrupitza.microcompiler.helper;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MathematicalHelperTest {

    @Test
    void getNearestPowerOfTwo() {
        double nearestPowerOfTwo = MathematicalHelper.getNearestPowerOfTwo(6);
        assertThat(nearestPowerOfTwo).isEqualTo(8);

        nearestPowerOfTwo = MathematicalHelper.getNearestPowerOfTwo(244);
        assertThat(nearestPowerOfTwo).isEqualTo(256);

        nearestPowerOfTwo = MathematicalHelper.getNearestPowerOfTwo(2);
        assertThat(nearestPowerOfTwo).isEqualTo(2);

        nearestPowerOfTwo = MathematicalHelper.getNearestPowerOfTwo(64);
        assertThat(nearestPowerOfTwo).isEqualTo(64);
    }

}