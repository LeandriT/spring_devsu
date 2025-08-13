package com.devsu.hackerearth.backend.account.exception;

public class InactiveAccountException  extends RuntimeException {
    public InactiveAccountException(String message) {
        super(message);
    }

}