package ru.nbki;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilTest {

    @Test
    void replaceChartWithNormalArguments() {
        Assertions.assertEquals("Test string", StringUtil.replaceChar("Teso soring", 'o', 't'));
    }

    @Test
    void replaceChartNullReplaceString() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtil.replaceChar(null, 'a', 'b'));
    }
}