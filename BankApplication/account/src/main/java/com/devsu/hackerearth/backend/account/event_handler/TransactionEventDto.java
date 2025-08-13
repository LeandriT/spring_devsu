package com.devsu.hackerearth.backend.account.event_handler;

import java.util.Date;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class TransactionEventDto extends ApplicationEvent {
    private final Date date;
    private final String type;
    private final double amount;
    private final Long accountId;
    private final double balance;

    public TransactionEventDto(Object source, Date date, String type,
            double amount, Long accountId, double balance) {
        super(source);
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.accountId = accountId;
        this.balance = balance;
    }
}
