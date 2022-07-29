package ru.nbki.task4.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Account {
    private String fId;
    private String serialNumber;
    private String memberCode;
    private String acctType;
    private String openedDt;
    private String acctRteCde;
    private String reportingDt;
    private String creditLimit;

    @Override
    public String toString() {
        return  fId + ";" +
                serialNumber + ";" +
                memberCode + ";" +
                acctType + ";" +
                openedDt + ";" +
                acctRteCde + ";" +
                reportingDt + ";" +
                creditLimit;
    }
}
