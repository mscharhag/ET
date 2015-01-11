package com.mscharhag.et.impl;

import com.mscharhag.et.ExceptionMappingConfigurer;
import com.mscharhag.et.TargetExceptionResolver;

import java.util.List;
import java.util.stream.Collectors;

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
        List<ExceptionMapping> t = this.sourceExceptionClasses.stream()
                .map(aClass -> new ExceptionMapping(aClass, targetExceptionResolver))
                .collect(Collectors.toList());

        return new DefaultExceptionTranslatorConfigurer(this.exceptionMappings.withMappings(t));
    }
}
