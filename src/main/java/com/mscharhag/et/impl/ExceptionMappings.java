package com.mscharhag.et.impl;

import com.mscharhag.et.TargetExceptionResolver;
import com.mscharhag.et.TranslationException;

import java.util.HashMap;
import java.util.Map;

class ExceptionMappings {

    protected ExceptionMappings parentExceptionMappings;
    protected Map<Class<? extends Exception>, TargetExceptionResolver> exceptionMappings = new HashMap<>();


    private ExceptionMappings() {
        // private constructor for internal default parent mapping creation
    }


    ExceptionMappings(ExceptionMappings parentExceptionMappings) {
        this.parentExceptionMappings = parentExceptionMappings;
        if (this.parentExceptionMappings == null) {
            this.parentExceptionMappings = this.createDefaultParentExceptionMappings();
        }
    }


    protected ExceptionMappings createDefaultParentExceptionMappings() {
        ExceptionMappings parentMappings = new ExceptionMappings();

        // add mapping to translate checked exception to runtime exceptions
        parentMappings.addExceptionMapping(Exception.class, (ex) -> {
            if (ex instanceof RuntimeException) {
                return (RuntimeException) ex;
            }
            return new RuntimeException(ex.getMessage(), ex);
        });

        return parentMappings;
    }


    void addExceptionMapping(Class<? extends Exception> sourceClass, TargetExceptionResolver targetExceptionResolver) {
        Arguments.ensureNotNull(sourceClass, "source class cannot be null");
        Arguments.ensureNotNull(targetExceptionResolver, "targetExceptionResolver cannot be null");

        if (this.exceptionMappings.containsKey(sourceClass)) {
            throw new TranslationException("Duplicate exception mapping for source class " + sourceClass.getCanonicalName());
        }
        this.exceptionMappings.put(sourceClass, targetExceptionResolver);
    }


    TargetExceptionResolver getExceptionResolver(Class<? extends Exception> sourceExceptionClass) {
        Class exceptionClass = sourceExceptionClass;

        while (!exceptionClass.equals(Throwable.class)) {
            TargetExceptionResolver resolver = this.exceptionMappings.get(exceptionClass);
            if (resolver != null) {
                return resolver;
            }
            exceptionClass = exceptionClass.getSuperclass();
        }

        if (this.parentExceptionMappings != null) {
            return this.parentExceptionMappings.getExceptionResolver(sourceExceptionClass);
        }

        throw new TranslationException("No resolver for exception found, exception: " + sourceExceptionClass.getCanonicalName());
    }
}
