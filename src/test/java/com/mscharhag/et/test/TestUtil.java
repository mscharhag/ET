package com.mscharhag.et.test;

import com.mscharhag.et.ET;
import com.mscharhag.et.ExceptionTranslator;
import com.mscharhag.et.test.exceptions.BarException;
import com.mscharhag.et.test.exceptions.FooChildException;
import com.mscharhag.et.test.exceptions.FooException;
import com.mscharhag.et.test.exceptions.FooRuntimeException;

import static com.mscharhag.oleaster.matcher.Matchers.*;

public class TestUtil {

    public static final String FOO_EXCEPTION_MESSAGE = "fooException";
    public static final String FOO_CHILD_EXCEPTION_MESSAGE = "fooChildException";
    public static final String BAR_EXCEPTION_MESSAGE = "barException";

    public static final Exception FOO_EXCEPTION = new FooException(FOO_EXCEPTION_MESSAGE, null);
    public static final Exception FOO_CHILD_EXCEPTION = new FooChildException(FOO_CHILD_EXCEPTION_MESSAGE, null);
    public static final RuntimeException FOO_RUNTIME_EXCEPTION = new FooRuntimeException("fooRuntimeException", null);
    public static final Exception BAR_EXCEPTION = new BarException(BAR_EXCEPTION_MESSAGE, null);



    public static RuntimeException catchException(Runnable runnable) {
        try {
            runnable.run();
        } catch (RuntimeException e) {
            return e;
        }
        throw new AssertionError("Runnable did not throw an exception");
    }

    public static void expectException(Exception e, Class<? extends Exception> type, String message, Throwable cause) {
        expect(e.getClass()).toEqual(type);
        expect(e.getMessage()).toEqual(message);
        expect(e.getCause()).toEqual(cause);
    }

    public static RuntimeException translateException(ExceptionTranslator et, Exception exception) {
        return TestUtil.catchException(() -> {
            et.withTranslation(() -> {
                throw exception;
            });
        });
    }
}
