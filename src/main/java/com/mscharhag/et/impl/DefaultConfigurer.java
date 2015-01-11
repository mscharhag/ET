package com.mscharhag.et.impl;

import com.mscharhag.et.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultConfigurer extends ExceptionTranslatorConfigurer {

    protected ExceptionMappings mappings;

    public DefaultConfigurer() {
        this(null);
    }

    DefaultConfigurer(ExceptionMappings mappings) {
        this.mappings = mappings;
        if (mappings == null) {
            this.mappings = this.createDefaultExceptionMappings();
        }
    }

    private ExceptionMappings createDefaultExceptionMappings() {
        List<ExceptionMapping> list = new ArrayList<>();
        list.add(new ExceptionMapping(Exception.class, (ex) -> {
            if (ex instanceof RuntimeException) {
                return (RuntimeException) ex;
            }
            return new RuntimeException(ex.getMessage(), ex);
        }));

        ExceptionMappings parent = new ExceptionMappings(list);
        return new ExceptionMappings(parent);
    }

    @Override // TODO rename?
    protected ExceptionMappingConfigurer getExceptionMappingConfigurer(List<Class<? extends Exception>> sources) {
        return new DefaultExceptionMappingConfigurer(this.mappings, sources);
    }

    public ExceptionTranslator done() {
        return new DefaultExceptionTranslator(mappings);
    }


}
