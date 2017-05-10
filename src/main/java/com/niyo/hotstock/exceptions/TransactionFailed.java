package com.niyo.hotstock.exceptions;

public class TransactionFailed extends RuntimeException {

    public TransactionFailed(String message) {
        super(message);
    }

    public TransactionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
