package com.mscharhag.et.impl;

import com.mscharhag.et.TargetExceptionResolver;
import com.mscharhag.et.TranslationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Returns {@link java.lang.RuntimeException} instances based on a passed {@link java.lang.Class}.
 */
public class ReflectiveExceptionResolver implements TargetExceptionResolver {

    private Class<? extends RuntimeException> targetExceptionClass;

    public ReflectiveExceptionResolver(Class<? extends RuntimeException> targetExceptionClass) {
        Arguments.ensureNotNull(targetExceptionClass, "targetExceptionClass cannot be null");
        this.targetExceptionClass = targetExceptionClass;
    }

    @Override
    public RuntimeException getTargetException(Exception sourceException) {
        List<ConstructorArgumentMapping> mappings = this.getConstructorMappings(sourceException);

        for (ConstructorArgumentMapping mapping : mappings) {
            for (Constructor<?> constructor : targetExceptionClass.getConstructors()) {
                if (mapping.matches(constructor)) {
                    try {
                        return (RuntimeException) constructor.newInstance(mapping.getArguments());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new TranslationException("Unable to instantiate target exception of type "
                                + this.targetExceptionClass.getCanonicalName(), e);
                    }
                }
            }
        }

        throw new TranslationException("No valid constructor found for target exception of type "
                + this.targetExceptionClass.getCanonicalName(), sourceException);
    }


    private List<ConstructorArgumentMapping> getConstructorMappings(Exception source) {
        List<ConstructorArgumentMapping> mappings = new ArrayList<>();

        mappings.add(new ConstructorArgumentMapping(String.class, Throwable.class).arguments(source.getMessage(), source)); // (String, Throwable)
        mappings.add(new ConstructorArgumentMapping(String.class, Exception.class).arguments(source.getMessage(), source)); // (String, Exception)

        if (source instanceof RuntimeException) {
            mappings.add(new ConstructorArgumentMapping(String.class, RuntimeException.class).arguments(source.getMessage(), source)); // (String, RuntimeException)
        }

        mappings.add(new ConstructorArgumentMapping(Throwable.class).arguments(source)); // (Throwable)
        mappings.add(new ConstructorArgumentMapping(Exception.class).arguments(source)); // (Throwable)

        if (source instanceof RuntimeException) {
            mappings.add(new ConstructorArgumentMapping(RuntimeException.class).arguments(source)); // (RuntimeException)
        }

        mappings.add(new ConstructorArgumentMapping(String.class).arguments(source.getMessage())); // (String)
        mappings.add(new ConstructorArgumentMapping()); // default constructor

        return mappings;
    }


    private static class ConstructorArgumentMapping {

        private Class<?>[] types;
        private Object[] arguments;

        ConstructorArgumentMapping(Class<?>... types) {
            this.types = types;
        }


        public ConstructorArgumentMapping arguments(Object... arguments) {
            Arguments.ensureTrue(this.types.length == arguments.length, "length of arguments has to be " + this.types.length);
            this.arguments = arguments;
            return this;
        }

        public boolean matches(Constructor<?> constructor) {
            return Arrays.equals(constructor.getParameterTypes(), this.types);
        }

        public Object[] getArguments() {
            return arguments;
        }
    }
}
