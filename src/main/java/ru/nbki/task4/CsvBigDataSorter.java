package ru.nbki.task4;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nbki.task4.model.Account;
import ru.nbki.task4.util.AccountParser;
import ru.nbki.task4.util.FileUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

@Slf4j
@Setter
@Getter
public class CsvBigDataSorter {
    private String fileName;
    private String tableHeader;

    public CsvBigDataSorter(String fileName) {
        this.fileName = fileName;
    }

    public void sortCsvDataByFirstColumn(int chunkSize) throws IOException {
        if (!FileUtil.checkCsvFile(fileName)) {
            log.info("The path \"{}\" hasn't passed the check.", fileName);
            return;
        }

        //Read all data, divide it to chunks, sort and write the chunks to temp files
        int tempFilesCount = readAllDataSortAndWriteToTempFiles(chunkSize);

        //Execute K-way merge sorting and write the result to the final file
        executeKWayMergeSorting(tempFilesCount);
    }

    private int readAllDataSortAndWriteToTempFiles(int chunkSize) throws IOException {
        List<Account> accounts = new ArrayList<>();
        int tempFilesCount = 0;

        try(BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            String line = fileReader.readLine();
            tableHeader = line.startsWith("FID") ? line : null;

            while ((line = fileReader.readLine()) != null) {
                Account newAccount = AccountParser.parseAccountString(line);
                if (newAccount != null) {
                    accounts.add(newAccount);
                }

                //Write the chunk to a temp file
                if (accounts.size() == chunkSize) {
                    accounts.sort(Comparator.comparing(Account::getFId));
                    tempFilesCount++;
                    writeChunkToTempFile(accounts, tempFilesCount);
                    accounts.clear();
                }
            }

            //Write the last chunk to a temp file if it exists
            if (accounts.size() > 0) {
                accounts.sort(Comparator.comparing(Account::getFId));
                tempFilesCount++;
                writeChunkToTempFile(accounts, tempFilesCount);
            }
        }
        return tempFilesCount;
    }

    private void writeChunkToTempFile(List<Account> accounts, int tempFilesCount) throws IOException {
        String tmpFileName = Paths.get(fileName).getParent().toString()
                + "\\temp\\sorted_" + tempFilesCount + ".tmp";
        Files.createDirectories(Paths.get(tmpFileName).getParent());

        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(tmpFileName))) {
            for (Account a : accounts) {
                String forWrite = a.toString();
                fileWriter.write(forWrite);
                fileWriter.newLine();
            }
        }

    }

    private void executeKWayMergeSorting(int tempFilesCount) {
        List<Path> tempPathList = new ArrayList<>();
        String tmpParentPath = Paths.get(fileName).getParent().toString();

        for (int i = 0; i < tempFilesCount; i++) {
            Path tempPath = Path.of(tmpParentPath + "\\temp\\sorted_" + tempFilesCount + ".tmp");
            if (!Files.exists(tempPath)) {
                log.info("Temp file:{} - don't exist or available. Merge sorting can't be executed.", tempPath);
                return;
            }
            tempPathList.add(tempPath);
        }

        ExecutorService mergeService = Executors.newFixedThreadPool(tempFilesCount + 1);

        Queue<String> mergeQueue = new LinkedBlockingQueue<>(10);

        AtomicInteger workChunkCount = new AtomicInteger(tempFilesCount);


        //Start chunk reading process
        for (Path p : tempPathList) {
            mergeService.execute(() -> {
                try(BufferedReader reader = new BufferedReader(new FileReader(p.toFile()))) {
                    String line;
                    int threadCount = 0;
                    while ((line = reader.readLine()) != null) {
                        mergeQueue.add(line);
                        threadCount++;

                        if (threadCount == 2) {
                            threadCount = 0;
                            LockSupport.park();
                        }
                    }
                    workChunkCount.decrementAndGet();
                } catch (FileNotFoundException e) {
                    log.info("FileNotFoundException in {} file reading process", p);
                    e.printStackTrace();
                } catch (IOException e) {
                    log.info("IOException in {} file reading process", p);
                    e.printStackTrace();
                }
            });
        }

        mergeService.execute(() -> {
            String fileNameWithEnding = FileUtil.addEndingToFileName(fileName, "_sorted");
            try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileNameWithEnding))) {
                if (tableHeader != null) {
                    fileWriter.write(tableHeader);
                    fileWriter.newLine();
                }
                while (workChunkCount.get() != 0 && mergeQueue.size() != 0) {
                    if (mergeQueue.size() == workChunkCount.get() * 2) {
                        List<String> strAccounts = mergeQueue.stream().toList();
                        List<Account> accounts = new ArrayList<>();
                        for (String s : strAccounts) {
                            Account newAccount = AccountParser.parseAccountString(s);
                            if (newAccount != null) {
                                accounts.add(newAccount);
                            }
                        }
                        accounts.sort(Comparator.comparing(Account::getFId));

                        for (Account a : accounts) {
                            String forWrite = a.toString();
                            fileWriter.write(forWrite);
                            fileWriter.newLine();
                        }
                        mergeQueue.clear();
                    }
                }
            } catch (IOException e) {
                log.info("IOException in {} file reading process", fileNameWithEnding);
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws IOException {
        CsvBigDataSorter dataSorter = new CsvBigDataSorter("c:\\Projects\\TestData\\CsvTest.csv");
        dataSorter.sortCsvDataByFirstColumn(10);
    }
}
