package com.mscharhag.et.test.exceptions;

public class BarRuntimeException extends RuntimeException {

    public BarRuntimeException(String message) {
        super(message);
    }

    public BarRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
