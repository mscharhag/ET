package com.mscharhag.et.test.exceptions;

public class FooException extends Exception {

    public FooException(String message) {
        super(message);
    }

    public FooException(String message, Throwable cause) {
        super(message, cause);
    }
}
