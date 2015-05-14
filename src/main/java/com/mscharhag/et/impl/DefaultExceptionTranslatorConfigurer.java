package com.mscharhag.et.impl;

import com.mscharhag.et.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultExceptionTranslatorConfigurer extends ExceptionTranslatorConfigurer {

    protected ExceptionMappings mappings;

    public DefaultExceptionTranslatorConfigurer() {
        this(null);
    }

    DefaultExceptionTranslatorConfigurer(ExceptionMappings mappings) {
        this.mappings = mappings;
        if (mappings == null) {
            this.mappings = this.createDefaultExceptionMappings();
        }
    }

    protected ExceptionMappings createDefaultExceptionMappings() {
        Map<Class<? extends Exception>, TargetExceptionResolver> mappings = new HashMap<>();

        // add mapping to translate checked exception to runtime exceptions
        mappings.put(Exception.class, (ex) -> {
            if (ex instanceof RuntimeException) {
                return (RuntimeException) ex;
            }
            return new RuntimeException(ex.getMessage(), ex);
        });

        ExceptionMappings parent = new ExceptionMappings(mappings);
        return new ExceptionMappings(parent);
    }

    @Override
    protected ExceptionMappingConfigurer translate(List<Class<? extends Exception>> sources) {
        return new DefaultExceptionMappingConfigurer(this.mappings, sources);
    }

    @Override
    public ExceptionTranslator done() {
        return new DefaultExceptionTranslator(mappings);
    }

}
