package com.mscharhag.et;

import static com.mscharhag.oleaster.matcher.Matchers.*;

import com.mscharhag.et.test.TestUtil;
import org.junit.Test;

import java.io.IOException;

public class DefaultConfigurationTests {

    private ExceptionTranslator et = ET.newConfiguration().done();
    private Exception checkedException = new IOException("checked exception", null);
    private RuntimeException runtimeException = new NullPointerException("runtime exception");


    @Test
    public void withTranslationTranslatesCheckedExceptionsToRuntimeExceptions() {
        RuntimeException result = TestUtil.catchException(() -> {
            et.withTranslation(() -> {
                throw checkedException;
            });
        });
        expect(result.getMessage()).toEqual("checked exception");
        expect(result.getCause()).toEqual(checkedException);
    }

    @Test
    public void withTranslationDoesNothingIfNoExceptionIsThrown() {
        et.withTranslation(() -> {
            // empty
        });
    }

    @Test
    public void withTranslationDoesNotTranslateRuntimeExceptions() {
        RuntimeException result = TestUtil.catchException(() -> {
            et.withTranslation(() -> {
                throw runtimeException;
            });
        });
        expect(result).toEqual(runtimeException);
    }

    @Test
    public void withReturningTranslationTranslatesCheckedExceptionsToRuntimeExceptions() {
        RuntimeException result = TestUtil.catchException(() -> {
            et.withReturningTranslation(() -> {
                throw checkedException;
            });
        });
        expect(result.getMessage()).toEqual("checked exception");
        expect(result.getCause()).toEqual(checkedException);
    }

    @Test
    public void withReturningTranslationReturnsTheValueIfNoExceptionIsThrown() {
        String result = et.withReturningTranslation(() -> "foo");
        expect(result).toEqual("foo");
    }

    @Test
    public void withReturningTranslationDoesNotTranslateRuntimeExceptions() {
        RuntimeException result = TestUtil.catchException(() -> {
            et.withReturningTranslation(() -> {
                throw runtimeException;
            });
        });
        expect(result).toEqual(runtimeException);
    }

    @Test
    public void withTranslationFailsIfNullIsPassed() {
        RuntimeException result = TestUtil.catchException(() -> et.withTranslation(null));
        expect(result.getClass()).toEqual(IllegalArgumentException.class);
        expect(result.getMessage()).toEqual("null is not a valid argument for ET.withTranslation()");
    }

    @Test
    public void withReturningTranslationFailsIfNullIsPassed() {
        RuntimeException result = TestUtil.catchException(() -> et.withReturningTranslation(null));
        expect(result.getClass()).toEqual(IllegalArgumentException.class);
        expect(result.getMessage()).toEqual("null is not a valid argument for ET.withReturningTranslation()");
    }

}
