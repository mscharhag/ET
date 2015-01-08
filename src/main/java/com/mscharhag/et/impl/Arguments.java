package com.mscharhag.et.impl;

public class Arguments {

    public static void ensureNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

}
