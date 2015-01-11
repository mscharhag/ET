package com.mscharhag.et.impl;

import com.mscharhag.et.TargetExceptionResolver;

class ExceptionMapping {

    private Class<? extends Exception> source;
    private TargetExceptionResolver exceptionResolver;

    ExceptionMapping(Class<? extends Exception> source, TargetExceptionResolver resolver) {
        this.source = source;
        this.exceptionResolver = resolver;
    }

    boolean matches(Exception e) {
        return this.source.isAssignableFrom(e.getClass());
    }

    TargetExceptionResolver getExceptionResolver() {
        return exceptionResolver;
    }
}
