package ru.nbki.task4.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@Slf4j
public class FileUtil {

    public static boolean checkCsvFile(String fileName) {
        if (fileName == null) {
            log.info("fileName must not be null.");
            return false;
        }

        int index = fileName.lastIndexOf('.');

        if (index > 0) {
            String extension = fileName.substring(index + 1);
            if (!extension.equalsIgnoreCase("csv")) {
                log.info("The file must have csv extension");
                return false;
            }
        } else {
            log.info("The fileName isn't a regular full path to file");
            return false;
        }

        try {
            return Files.exists(Path.of(fileName));
        } catch (InvalidPathException e) {
            log.info("InvalidPathException – the path string cannot be converted to a Path");
            return false;
        } catch (SecurityException e) {
            log.info("SecurityException - hasn't passed the check read access to the file.");
            return false;
        }
    }

    public static String addEndingToFileName(String fileName, String ending) {
        if (fileName == null || ending == null) {
            log.info("fileName and ending must not be null.");
            return fileName;
        }
        int index = fileName.lastIndexOf(".");
        return index > 0 ? fileName.substring(0, index) + ending + ".csv" : fileName;
    }
}
