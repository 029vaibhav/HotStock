package com.niyo.hotstock.exceptions;

public class UserUpdateFailed extends RuntimeException{

    public UserUpdateFailed(String message) {
        super(message);
    }

    public UserUpdateFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
