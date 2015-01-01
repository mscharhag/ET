package com.mscharhag.et.impl;

import com.mscharhag.et.TargetExceptionResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectiveExceptionResolver implements TargetExceptionResolver {

    private Class<? extends RuntimeException> targetException;

    public ReflectiveExceptionResolver(Class<? extends RuntimeException> targetException) {
        this.targetException = targetException;
    }

    @Override
    public RuntimeException getTargetException(Exception sourceException) {
        try {
            Constructor<? extends RuntimeException> constructor = targetException.getConstructor(String.class, Throwable.class);
            RuntimeException runtimeException = constructor.newInstance(sourceException.getMessage(), sourceException);
            return runtimeException;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
