package ru.nbki.task4.util;

import ru.nbki.task4.model.Account;

public class AccountParser {

    public static Account parseAccountString(String accountData) {
        String[] accountDataParts = accountData.split(";");

        return accountDataParts.length >= 8 ?
                new Account(accountDataParts[0], accountDataParts[1], accountDataParts[2], accountDataParts[3],
                        accountDataParts[4], accountDataParts[5], accountDataParts[6], accountDataParts[7]) :
                null;
    }
}
