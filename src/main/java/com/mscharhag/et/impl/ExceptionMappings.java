package com.mscharhag.et.impl;

import com.mscharhag.et.TargetExceptionResolver;
import com.mscharhag.et.TranslationException;

import java.util.ArrayList;
import java.util.List;

class ExceptionMappings {

    protected ExceptionMappings parentMappings;
    protected List<ExceptionMapping> exceptionMappings;

    ExceptionMappings(ExceptionMappings parentMappings) {
        this(parentMappings, null);
    }

    ExceptionMappings(List<ExceptionMapping> mappings) {
        this(null, mappings);
    }

    ExceptionMappings(ExceptionMappings parentMappings, List<ExceptionMapping> mappings) {
        this.parentMappings = parentMappings;
        this.exceptionMappings = mappings;

        if (this.exceptionMappings == null) {
            this.exceptionMappings = new ArrayList<>();
        }
    }

    TargetExceptionResolver getExceptionResolver(Exception e) {
        for (ExceptionMapping exceptionMapping : this.exceptionMappings) {
            if (exceptionMapping.matches(e)) {
                return exceptionMapping.getExceptionResolver();
            }
        }
        if (parentMappings != null) {
            return parentMappings.getExceptionResolver(e);
        }
        throw new TranslationException("No resolver for exception found, exception: " + e.getClass().getCanonicalName());
    }

    ExceptionMappings withMappings(List<ExceptionMapping> mappings) {
        List<ExceptionMapping> totalMappings = new ArrayList<>(this.exceptionMappings);
        totalMappings.addAll(mappings);
        return new ExceptionMappings(this.parentMappings, totalMappings);
    }
}
