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
            this.mappings = new ExceptionMappings(null);
        }
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
