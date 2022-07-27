package ru.nbki.task4.model;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Account {
    private int fId;
    private int serialNumber;
    private String memberCode;
    private int acctType;
    private Date openedDt;
    private int acctRteCde;
    private Date reportingDt;
    private int creditLimit;
}
