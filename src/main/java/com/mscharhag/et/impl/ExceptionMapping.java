package com.mscharhag.et.impl;

import com.mscharhag.et.TargetExceptionResolver;

public class ExceptionMapping {

    private Class<? extends Exception> source;
    private TargetExceptionResolver exceptionResolver;

    public ExceptionMapping(Class<? extends Exception> source, TargetExceptionResolver resolver) {
        this.source = source;
        this.exceptionResolver = resolver;
    }

    public Class<? extends Exception> getSource() {
        return source;
    }

    public boolean matches(Exception e) {
        return this.source.isAssignableFrom(e.getClass());
    }

    public TargetExceptionResolver getExceptionResolver() {
        return exceptionResolver;
    }
}
