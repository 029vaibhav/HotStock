package com.niyo.hotstock.exceptions;

public class StockNotFound extends RuntimeException {

    public StockNotFound(String message) {
        super(message);
    }

    public StockNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
