package com.mscharhag.et.impl;

import com.mscharhag.et.TargetExceptionResolver;
import com.mscharhag.et.TranslationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ExceptionMappings {

    protected ExceptionMappings parentMappings;

    // TODO: rename mappings -> exceptionMappings??
    protected Map<Class<? extends Exception>, TargetExceptionResolver> mappings = new HashMap<>();

    ExceptionMappings(ExceptionMappings parentMappings) {
        this(parentMappings, null);
    }

    ExceptionMappings(Map<Class<? extends Exception>, TargetExceptionResolver> mappings) {
        this(null, mappings);
    }

    ExceptionMappings(ExceptionMappings parentMappings, Map<Class<? extends Exception>, TargetExceptionResolver> mappings) {
        this.parentMappings = parentMappings;
        if (mappings != null) {
            this.mappings.putAll(mappings);
        }
    }

    TargetExceptionResolver getExceptionResolver(Exception e) {
        Class exceptionClass = e.getClass();

        while (!exceptionClass.equals(Throwable.class)) {
            TargetExceptionResolver resolver = mappings.get(exceptionClass);
            if (resolver != null) {
                return resolver;
            }
            exceptionClass = exceptionClass.getSuperclass();
        }

        if (parentMappings != null) {
            return parentMappings.getExceptionResolver(e);
        }

        throw new TranslationException("No resolver for exception found, exception: " + e.getClass().getCanonicalName());
    }

    ExceptionMappings withMappings( Map<Class<? extends Exception>, TargetExceptionResolver> mappings) {
        Map<Class<? extends Exception>, TargetExceptionResolver> totalMappings = new HashMap<>(this.mappings);
        for (Map.Entry<Class<? extends Exception>, TargetExceptionResolver> entry : mappings.entrySet()) {
            if (totalMappings.containsKey(entry.getKey())) {
                throw new TranslationException("duplicate source exception " + entry.getKey().getCanonicalName());
            }
            totalMappings.put(entry.getKey(), entry.getValue());
        }

        return new ExceptionMappings(this.parentMappings, totalMappings);
    }
}
