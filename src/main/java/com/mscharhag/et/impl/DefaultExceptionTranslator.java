package com.mscharhag.et.impl;

import com.mscharhag.et.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DefaultExceptionTranslator implements ExceptionTranslator {

    private List<ExceptionMapping> mappings;

    DefaultExceptionTranslator(List<ExceptionMapping> mapping) {
        this.mappings = Collections.unmodifiableList(new ArrayList<>(mapping));
    }

    public void withTranslation(TryBlock tryBlock) {
        Objects.requireNonNull(tryBlock, "null is not a valid argument for ET.withTranslation()");
        this.withReturningTranslation(() -> {
            tryBlock.run();
            return null;
        });
    }

    public <T> T withReturningTranslation(ReturningTryBlock<T> invokable) {
        Objects.requireNonNull(invokable, "null is not a valid argument for ET.withReturningTranslation()"); // TODO: replace with Arguments
        try {
            return invokable.run();
        } catch (Exception e) {
            throw this.getTargetException(e);
        }
    }

    public ExceptionTranslatorConfigurer configure() {
        return new DefaultConfigurer(this.mappings);
    }


    public RuntimeException getTargetException(Exception e) {
        for (ExceptionMapping mapping : this.mappings) {
            if (mapping.getSource().isAssignableFrom(e.getClass())) {
                return mapping.getProvider().getTargetException(e);
            }
        }
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        }
        return new RuntimeException(e.getMessage(), e);
    }
}
