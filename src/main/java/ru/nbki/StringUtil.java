package ru.nbki;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class StringUtil {

    /**Replaces the pointed char of the string by the new char
     *
     * @param replaceString - regular String
     * @param oldChar - regular char
     * @param newChar - regular char
     * @return string with replaced chars
     * @throws IllegalArgumentException if replaceString is null
     */
    public static String replaceChar(String replaceString, char oldChar, char newChar) {
        if (replaceString == null) {
            throw new IllegalArgumentException("argument \"replaceString\" of the replaceChar() method can't be null!");
        }

        StringBuilder result = new StringBuilder();

        replaceString.chars()
                .mapToObj(c -> (char) c)
                .map(c -> c.equals(oldChar) ? newChar : c)
                .forEach(result::append);

        return result.toString();
    }

    /**Converts String value to int value.
     * Return the count of the string elements.
     * @param input - regular String value
     * @return int value
     * @throws IllegalArgumentException if input is null
     */
    public static int convertStringToInt(String input) {
        if (input == null) {
            throw new IllegalArgumentException("argument \"input\" of the convertStringToInt() method can't be null!");
        }
        return (int) input.chars().count();
    }

    /**Converts String value to double value.
     * Return the count of the string elements.
     * @param input - regular String value
     * @return int value
     * @throws IllegalArgumentException if input is null
     */
    public static double convertStringToDouble(String input) {
        if (input == null) {
            throw new IllegalArgumentException("argument \"input\" of the convertStringToInt() method can't be null!");
        }

        IntStream intStream = input.chars();
        AtomicLong sum = new AtomicLong();
        input.chars().forEach(sum::addAndGet);

        return sum.doubleValue()/intStream.count();
    }
}
