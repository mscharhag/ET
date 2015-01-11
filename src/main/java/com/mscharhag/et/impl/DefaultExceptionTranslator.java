package com.mscharhag.et.impl;

import com.mscharhag.et.*;

import java.util.*;

class DefaultExceptionTranslator implements ExceptionTranslator {

    protected ExceptionMappings exceptionMappings;

//    protected Map<Class<? extends Exception>, TargetExceptionResolver> exceptionMappings;

    DefaultExceptionTranslator(ExceptionMappings exceptionMappings) {
        this.exceptionMappings = exceptionMappings;
//        this.exceptionMappings = Collections.unmodifiableList(new ArrayList<>(mapping));
//        exceptionMappings = new LinkedHashMap<>(mapping);
    }

    @Override
    public void withTranslation(TryBlock tryBlock) {
        Arguments.ensureNotNull(tryBlock, "null is not a valid argument for ET.withTranslation()");
        this.withReturningTranslation(() -> {
            tryBlock.run();
            return null;
        });
    }

    @Override
    public <T> T withReturningTranslation(ReturningTryBlock<T> invokable) {
        Arguments.ensureNotNull(invokable, "null is not a valid argument for ET.withReturningTranslation()");
        try {
            return invokable.run();
        } catch (Exception e) {
            throw this.getTargetException(e);
        }
    }

    @Override
    public ExceptionTranslatorConfigurer configure() {
        return new DefaultConfigurer(new ExceptionMappings(this.exceptionMappings));
    }


    protected RuntimeException getTargetException(Exception e) {
        TargetExceptionResolver resolver = this.exceptionMappings.getExceptionResolver(e);
        return resolver.getTargetException(e);
    }
}
