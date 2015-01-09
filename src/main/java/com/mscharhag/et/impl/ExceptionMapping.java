package com.mscharhag.et.impl;

import com.mscharhag.et.TargetExceptionResolver;

public class ExceptionMapping {

    private Class<? extends Exception> source;
    private TargetExceptionResolver provider;

    public ExceptionMapping(Class<? extends Exception> source, TargetExceptionResolver provider) {
        this.source = source;
        this.provider = provider;
    }

    public Class<? extends Exception> getSource() {
        return source;
    }

    // TODO: rename
    public TargetExceptionResolver getProvider() {
        return provider;
    }
}
