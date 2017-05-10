package com.niyo.hotstock.exceptions;

public class UserRegistrationFailed extends RuntimeException{

    public UserRegistrationFailed(String message) {
        super(message);
    }

    public UserRegistrationFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
