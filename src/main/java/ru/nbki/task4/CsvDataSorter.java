package ru.nbki.task4;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nbki.task4.model.Account;
import ru.nbki.task4.util.AccountParser;
import ru.nbki.task4.util.FileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Setter
@Getter
public class CsvDataSorter {
    private String fileName;

    public void sortCsvDataByFirstColumn() {
        if (!FileUtil.checkCsvFile(fileName)) {
            log.info("The path \"{}\" hasn't passed the check.", fileName);
            return;
        }

        List<Account> accounts = new ArrayList<>();
        String tableHeader = null;

        //Read data
        try(BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            String line = fileReader.readLine();

            tableHeader = line.startsWith("FID") ? line : null;

            while ((line = fileReader.readLine()) != null) {
                Account newAccount = AccountParser.parseAccountString(line);
                if (newAccount != null) {
                    accounts.add(newAccount);
                }
            }
        } catch (FileNotFoundException e) {
            log.info("Can't find the file:" + e.getMessage());
        } catch (IOException e) {
            log.info("IOException in the file reading process:" + e.getMessage());
        }

        //Data processing
        if (accounts.size() == 0) {
            log.info("The csv-table is empty.");
            return;
        }

        accounts.sort(Comparator.comparing(Account::getFId));

        //Write data
        String fileNameWithEnding = FileUtil.addEndingToFileName(fileName, "_sorted");
        writeAccountsToFileWithTableHeader(fileNameWithEnding, tableHeader, accounts);
    }

    private void writeAccountsToFileWithTableHeader(String fileName, String tableHeader, List<Account> accounts) {
        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName))) {
            if (tableHeader != null) {
                fileWriter.write(tableHeader);
                fileWriter.newLine();
            }

            for (Account a : accounts) {
                String forWrite = a.toString();
                fileWriter.write(forWrite);
                fileWriter.newLine();
            }
        } catch (IOException e) {
            log.info("IOException in the file writing process:" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        CsvDataSorter dataSorter = new CsvDataSorter("c:\\Projects\\TestData\\CsvTest.csv");
        dataSorter.sortCsvDataByFirstColumn();
    }
}
