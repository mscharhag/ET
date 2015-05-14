package com.mscharhag.et.impl;

import com.mscharhag.et.ExceptionMappingConfigurer;
import com.mscharhag.et.TargetExceptionResolver;

import java.util.List;

public class DefaultExceptionMappingConfigurer implements ExceptionMappingConfigurer {

    private final ExceptionMappings exceptionMappings;
    private final List<Class<? extends Exception>> sourceExceptionClasses;

    public DefaultExceptionMappingConfigurer(ExceptionMappings exceptionMappings, List<Class<? extends Exception>> sourceExceptionClasses) {
        this.exceptionMappings = exceptionMappings;
        this.sourceExceptionClasses = sourceExceptionClasses;
    }

    public DefaultExceptionTranslatorConfigurer to(Class<? extends RuntimeException> targetException) {
        return this.to(new ReflectiveExceptionResolver(targetException));
    }

    public DefaultExceptionTranslatorConfigurer using(TargetExceptionResolver resolver) {
        return this.to(resolver);
    }

    private DefaultExceptionTranslatorConfigurer to(TargetExceptionResolver targetExceptionResolver) {
        for (Class<? extends Exception> sourceExceptionClass : this.sourceExceptionClasses) {
            this.exceptionMappings.addExceptionMapping(sourceExceptionClass, targetExceptionResolver);
        }
        return new DefaultExceptionTranslatorConfigurer(this.exceptionMappings);
    }
}
