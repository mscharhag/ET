package com.mscharhag.et.impl;

import com.mscharhag.et.ExceptionMappingConfigurer;
import com.mscharhag.et.TargetExceptionResolver;

import java.util.ArrayList;
import java.util.List;

public class DefaultExceptionMappingConfigurer implements ExceptionMappingConfigurer {

    private DefaultConfigurer configurer;
    private List<Class<? extends Exception>> ex;

    public DefaultExceptionMappingConfigurer(DefaultConfigurer configurer, List<Class<? extends Exception>> ex) {
        this.configurer = configurer;
        this.ex = ex;
    }

    public DefaultConfigurer to(Class<? extends RuntimeException> targetException) {
        return this.to(new ReflectiveExceptionResolver(targetException));
    }

    public DefaultConfigurer using(TargetExceptionResolver resolver) {
        return this.to(new DelegatingExceptionResolver(resolver));
    }


    private DefaultConfigurer to(TargetExceptionResolver targetExceptionResolver) {
        List<ExceptionMapping> t = new ArrayList<>(configurer.targets);

        for (Class<? extends Exception> aClass : this.ex) {
            t.add(new ExceptionMapping(aClass, targetExceptionResolver));
        }

        return new DefaultConfigurer(t);
    }

}
