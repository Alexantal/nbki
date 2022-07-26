package ru.nbki;

public class StringUtil {

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
}
