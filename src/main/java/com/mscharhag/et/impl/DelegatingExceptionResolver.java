package com.mscharhag.et.impl;

import com.mscharhag.et.TargetExceptionResolver;
import com.mscharhag.et.TranslationException;

import java.util.Objects;

/**
 * A {@link com.mscharhag.et.TargetExceptionResolver} that delegates to another {@code TargetExceptionResolver}.
 * The only purpose of this implementation is to make sure that the contract of {@code TargetExceptionResolver}
 * (not returning {@code null}) is followed.
 *
 * If the delegate returns {code null} a {@link com.mscharhag.et.TranslationException} will be thrown.
 */
public class DelegatingExceptionResolver implements TargetExceptionResolver { // TODO: remove?

    private TargetExceptionResolver delegate;

    public DelegatingExceptionResolver(TargetExceptionResolver delegate) {
        Arguments.ensureNotNull(delegate, "delegate cannot be null");
        this.delegate = delegate;
    }

    @Override
    public RuntimeException getTargetException(Exception sourceException) {
        Arguments.ensureNotNull(sourceException, "sourceException cannot be null");
        RuntimeException exception = delegate.getTargetException(sourceException);
        if (exception == null) {
            throw new TranslationException("TargetExceptionResolver returned null as target exception, " +
                    "targetExceptionResolver: " + this.delegate.getClass().getCanonicalName());
        }
        return exception;
    }
}
