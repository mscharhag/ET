package com.mscharhag.et.test.exceptions;

public class BarException extends Exception {

    public BarException(String message) {
        super(message);
    }

    public BarException(String message, Throwable cause) {
        super(message, cause);
    }
}
