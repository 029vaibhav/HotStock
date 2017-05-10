package com.niyo.hotstock.exceptions;

public class StockNotDeleted extends RuntimeException{

    public StockNotDeleted(String message) {
        super(message);
    }

    public StockNotDeleted(String message, Throwable cause) {
        super(message, cause);
    }
}
