package com.mscharhag.et.test.exceptions;

public class FooRuntimeException extends RuntimeException {

    public FooRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FooRuntimeException(String message) {
        super(message);
    }
}
