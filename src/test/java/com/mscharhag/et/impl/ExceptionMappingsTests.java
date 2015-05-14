package com.mscharhag.et.impl;

import com.mscharhag.et.TargetExceptionResolver;
import com.mscharhag.et.TranslationException;
import com.mscharhag.et.test.TestUtil;
import com.mscharhag.et.test.exceptions.BarRuntimeException;
import static org.junit.Assert.*;

import com.mscharhag.et.test.exceptions.FooRuntimeException;
import org.junit.Test;

public class ExceptionMappingsTests {

    ExceptionMappings exceptionMappings = new ExceptionMappings(null);


    @Test
    public void parentMappingsConvertExceptionToRuntimeException() {
        Exception ex = new Exception("foo");
        TestUtil.expectException(getTargetException(this.exceptionMappings, ex), RuntimeException.class, "foo", ex);
    }


    @Test
    public void parentMappingsDoNotConvertRuntimeException() {
        RuntimeException ex = new RuntimeException("foo");
        assertEquals(ex, getTargetException(this.exceptionMappings, ex));
    }


    @Test
    public void itIsPossibleToOverrideDefaultMappings() {
        Exception ex = new Exception("foo");
        this.exceptionMappings.addExceptionMapping(Exception.class, new ReflectiveExceptionResolver(FooRuntimeException.class));
        TestUtil.expectException(getTargetException(this.exceptionMappings, ex), FooRuntimeException.class, "foo", ex);
    }


    @Test(expected = TranslationException.class)
    public void itIsNotPossibleToAddDuplicateSourceExceptionMappings() {
        this.exceptionMappings.addExceptionMapping(Exception.class, new ReflectiveExceptionResolver(FooRuntimeException.class));
        this.exceptionMappings.addExceptionMapping(Exception.class, new ReflectiveExceptionResolver(BarRuntimeException.class));
    }


    private RuntimeException getTargetException(ExceptionMappings mappings, Exception source) {
        TargetExceptionResolver exceptionResolver = mappings.getExceptionResolver(source.getClass());
        return exceptionResolver.getTargetException(source);
    }
}
