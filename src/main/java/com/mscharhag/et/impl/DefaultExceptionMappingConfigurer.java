package com.mscharhag.et.impl;

import com.mscharhag.et.ExceptionMappingConfigurer;
import com.mscharhag.et.TargetExceptionResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultExceptionMappingConfigurer implements ExceptionMappingConfigurer {

    private ExceptionMappings mappings;
    private List<Class<? extends Exception>> ex;

    public DefaultExceptionMappingConfigurer(ExceptionMappings mappings, List<Class<? extends Exception>> ex) {
        this.mappings = mappings;
        this.ex = ex;
    }

    public DefaultConfigurer to(Class<? extends RuntimeException> targetException) {
        return this.to(new ReflectiveExceptionResolver(targetException));
    }

    public DefaultConfigurer using(TargetExceptionResolver resolver) {
        return this.to(new DelegatingExceptionResolver(resolver));
    }


    private DefaultConfigurer to(TargetExceptionResolver targetExceptionResolver) {
        List<ExceptionMapping> t = this.ex.stream()
                .map(aClass -> new ExceptionMapping(aClass, targetExceptionResolver))
                .collect(Collectors.toList());

        return new DefaultConfigurer(this.mappings.withMappings(t));
    }

}
