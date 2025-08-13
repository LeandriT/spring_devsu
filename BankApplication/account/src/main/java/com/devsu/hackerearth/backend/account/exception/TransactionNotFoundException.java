package com.devsu.hackerearth.backend.account.exception;

public class TransactionNotFoundException  extends RuntimeException {
    public TransactionNotFoundException(String message) {
        super(message);
    }

}
