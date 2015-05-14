package com.mscharhag.et.impl;

import com.mscharhag.et.TranslationException;
import com.mscharhag.et.test.TestUtil;
import com.mscharhag.et.test.exceptions.FooException;
import org.junit.Test;

public class ReflectiveExceptionResolverTest {

    ReflectiveExceptionResolver resolver;

    @Test
    public void stringConstructor() {
        resolver = new ReflectiveExceptionResolver(StringConstructorException.class);
        RuntimeException result = resolver.getTargetException(new FooException("foo"));
        TestUtil.expectException(result, StringConstructorException.class, "foo", null);
    }


    @Test
    public void throwableConstructor() {
        resolver = new ReflectiveExceptionResolver(ThrowableConstructorException.class);
        FooException source = new FooException("foo");
        RuntimeException result = resolver.getTargetException(source);
        // Throwable uses cause.toString() in this case
        TestUtil.expectException(result, ThrowableConstructorException.class, "com.mscharhag.et.test.exceptions.FooException: foo", source);
    }


    @Test
    public void defaultConstructor() {
        resolver = new ReflectiveExceptionResolver(DefaultConstructorException.class);
        RuntimeException result = resolver.getTargetException(new FooException("foo"));
        TestUtil.expectException(result, DefaultConstructorException.class, null, null);
    }


    @Test(expected = TranslationException.class)
    public void noSuitableConstructor() {
        resolver = new ReflectiveExceptionResolver(NoSuitableConstructorException.class);
        resolver.getTargetException(new FooException("foo"));
    }



    private static class StringConstructorException extends RuntimeException {
        public StringConstructorException(String message) {
            super(message);
        }
    }


    public static class ThrowableConstructorException extends RuntimeException {
        public ThrowableConstructorException(Throwable cause) {
            super(cause);
        }
    }


    public static class DefaultConstructorException extends RuntimeException {

    }


    public static class NoSuitableConstructorException extends RuntimeException {
        public NoSuitableConstructorException(long l) {

        }
    }
}
