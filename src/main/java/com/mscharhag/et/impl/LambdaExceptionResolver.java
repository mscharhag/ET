package com.mscharhag.et.impl;

import com.mscharhag.et.TargetExceptionResolver;

public class LambdaExceptionResolver implements TargetExceptionResolver {

    private TargetExceptionResolver lambaTarget;

    public LambdaExceptionResolver(TargetExceptionResolver lambaTarget) {
        this.lambaTarget = lambaTarget;
    }

    @Override
    public RuntimeException getTargetException(Exception sourceException) {
        return lambaTarget.getTargetException(sourceException);
    }
}
