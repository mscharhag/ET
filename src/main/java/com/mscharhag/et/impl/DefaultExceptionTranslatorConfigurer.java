package com.mscharhag.et.impl;

import com.mscharhag.et.*;

import java.util.ArrayList;
import java.util.List;

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
        List<ExceptionMapping> list = new ArrayList<>();

        // add mapping to translate checked exception to runtime exceptions
        list.add(new ExceptionMapping(Exception.class, (ex) -> {
            if (ex instanceof RuntimeException) {
                return (RuntimeException) ex;
            }
            return new RuntimeException(ex.getMessage(), ex);
        }));

        ExceptionMappings parent = new ExceptionMappings(list);
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
