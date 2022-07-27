package ru.nbki.task1_2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nbki.task1_2.StringUtil;

class StringUtilTest {

    @Test
    void replaceChartWithNormalArguments() {
        Assertions.assertEquals("Test string", StringUtil.replaceChar("Teso soring", 'o', 't'));
    }

    @Test
    void replaceChartNullReplaceString() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtil.replaceChar(null, 'a', 'b'));
    }

    @Test
    void convertStringToInt() {
        Assertions.assertEquals(6, StringUtil.convertStringToInt("abcdef"));
    }

    @Test
    void convertStringToDouble() {
        Assertions.assertEquals(101.0, StringUtil.convertStringToDouble("Test string"));
    }
}