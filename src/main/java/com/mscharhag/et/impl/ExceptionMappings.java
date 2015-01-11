package com.mscharhag.et.impl;

import com.mscharhag.et.TargetExceptionResolver;
import com.mscharhag.et.TranslationException;

import java.util.ArrayList;
import java.util.List;

public class ExceptionMappings {

    protected ExceptionMappings parentMappings;
    protected List<ExceptionMapping> exceptionMappings;


    public ExceptionMappings(ExceptionMappings parentMappings) {
        this(parentMappings, null);
    }

    public ExceptionMappings(List<ExceptionMapping> mappings) {
        this(null, mappings);
    }

    public ExceptionMappings(ExceptionMappings parentMappings, List<ExceptionMapping> mappings) {
        this.parentMappings = parentMappings;
        this.exceptionMappings = mappings;

        if (this.exceptionMappings == null) {
            this.exceptionMappings = new ArrayList<>();
        }
    }


    public TargetExceptionResolver getExceptionResolver(Exception e) {
        for (ExceptionMapping exceptionMapping : this.exceptionMappings) {
            if (exceptionMapping.matches(e)) {
                return exceptionMapping.getExceptionResolver();
            }
        }
        if (parentMappings != null) {
            return parentMappings.getExceptionResolver(e);
        }
        throw new TranslationException(""); // TODO
    }


    public ExceptionMappings withMappings(List<ExceptionMapping> mappings) {
        List<ExceptionMapping> totalMappings = new ArrayList<>(this.exceptionMappings);
        totalMappings.addAll(mappings);
        return new ExceptionMappings(this.parentMappings, totalMappings);
    }
}
