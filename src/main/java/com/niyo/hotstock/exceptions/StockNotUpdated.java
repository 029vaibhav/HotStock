package com.niyo.hotstock.exceptions;

public class StockNotUpdated extends RuntimeException{

    public StockNotUpdated(String message) {
        super(message);
    }

    public StockNotUpdated(String message, Throwable cause) {
        super(message, cause);
    }
}
