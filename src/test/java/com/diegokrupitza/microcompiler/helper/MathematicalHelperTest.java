package com.diegokrupitza.microcompiler.helper;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MathematicalHelperTest {

    @Test
    public void getNearestPowerOfTwo() {
        double nearestPowerOfTwo = MathematicalHelper.getNearestPowerOfTwo(6);
        assertThat(nearestPowerOfTwo).isEqualTo(8);

        nearestPowerOfTwo = MathematicalHelper.getNearestPowerOfTwo(244);
        assertThat(nearestPowerOfTwo).isEqualTo(256);

        nearestPowerOfTwo = MathematicalHelper.getNearestPowerOfTwo(2);
        assertThat(nearestPowerOfTwo).isEqualTo(2);

        nearestPowerOfTwo = MathematicalHelper.getNearestPowerOfTwo(64);
        assertThat(nearestPowerOfTwo).isEqualTo(64);
    }

    @Test
    public void isPowerOfTwoTest() {
        boolean powerOfTwo = MathematicalHelper.isPowerOfTwo(64);
        assertThat(powerOfTwo).isEqualTo(true);

        powerOfTwo = MathematicalHelper.isPowerOfTwo(295);
        assertThat(powerOfTwo).isEqualTo(false);
    }

}