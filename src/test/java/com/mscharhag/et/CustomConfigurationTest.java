package com.mscharhag.et;

import com.mscharhag.et.test.exceptions.*;
import com.mscharhag.et.test.TestUtil;
import org.junit.Test;

import static com.mscharhag.et.test.TestUtil.*;
import static com.mscharhag.et.test.TestUtil.FOO_CHILD_EXCEPTION;
import static com.mscharhag.oleaster.matcher.Matchers.*;

public class CustomConfigurationTest {

    @Test
    public void customExceptionMappingIsUsed() {
        ExceptionTranslator et = ET.newConfiguration()
                .translate(FooException.class).to(FooRuntimeException.class)
                .done();

        RuntimeException result = TestUtil.translateException(et, TestUtil.FOO_EXCEPTION);
        TestUtil.expectException(result, FooRuntimeException.class, "fooException", TestUtil.FOO_EXCEPTION);
    }

    @Test
    public void runtimeExceptionsCanBeMapped() {
        ExceptionTranslator et = ET.newConfiguration()
                .translate(FooRuntimeException.class).to(BarRuntimeException.class)
                .done();

        RuntimeException result = TestUtil.translateException(et, TestUtil.FOO_RUNTIME_EXCEPTION);
        TestUtil.expectException(result, BarRuntimeException.class, "fooRuntimeException", TestUtil.FOO_RUNTIME_EXCEPTION);
    }

    @Test
    public void defaultMappingWillBeUsedIfNoCustomMappingCanBeFound() {
        ExceptionTranslator et = ET.newConfiguration()
                .translate(FooException.class).to(FooRuntimeException.class)
                .done();

        RuntimeException result = TestUtil.translateException(et, TestUtil.BAR_EXCEPTION);
        TestUtil.expectException(result, RuntimeException.class, "barException", TestUtil.BAR_EXCEPTION);
    }

    @Test
    public void baseClassCanBeUsedInMappings() {
        ExceptionTranslator et = ET.newConfiguration()
                .translate(FooException.class).to(FooRuntimeException.class)
                .done();

        RuntimeException result = TestUtil.translateException(et, TestUtil.FOO_CHILD_EXCEPTION);
        TestUtil.expectException(result, FooRuntimeException.class, "fooChildException", TestUtil.FOO_CHILD_EXCEPTION);
    }

    @Test
    public void mostSpecificMappingIsUsed() {
        ExceptionTranslator et = ET.newConfiguration()
                .translate(Exception.class).to(BarRuntimeException.class)
                .translate(FooChildException.class).to(FooChildRuntimeException.class)
                .translate(FooException.class).to(FooRuntimeException.class)
                .done();

        RuntimeException result = translateException(et, FOO_CHILD_EXCEPTION);
        expectException(result, FooChildRuntimeException.class, FOO_CHILD_EXCEPTION_MESSAGE, FOO_CHILD_EXCEPTION);
    }

    @Test
    public void lambdaMapping() {
        ExceptionTranslator et = ET.newConfiguration()
                .translate(FooException.class).using((m, ex) -> TestUtil.FOO_RUNTIME_EXCEPTION)
                .done();

        RuntimeException result = TestUtil.translateException(et, TestUtil.FOO_EXCEPTION);
        expect(result).toEqual(TestUtil.FOO_RUNTIME_EXCEPTION);
    }

    @Test(expected = TranslationException.class)
    public void lambdaMappingReturnsNull() {
        ExceptionTranslator et = ET.newConfiguration()
                .translate(FooException.class).using((m, ex) -> null)
                .done();

        et.withTranslation(() -> {
            throw new FooException("foo");
        });
    }

    @Test
    public void multipleMappings() {
        ExceptionTranslator et = ET.newConfiguration()
                .translate(FooException.class, BarException.class).to(FooRuntimeException.class)
                .done();

        RuntimeException first = TestUtil.translateException(et, TestUtil.FOO_EXCEPTION);
        RuntimeException second = TestUtil.translateException(et, TestUtil.BAR_EXCEPTION);

        TestUtil.expectException(first, FooRuntimeException.class, "fooException", TestUtil.FOO_EXCEPTION);
        TestUtil.expectException(second, FooRuntimeException.class, "barException", TestUtil.BAR_EXCEPTION);
    }


    @Test
    public void methodReference() {
        ExceptionTranslator et = ET.newConfiguration()
                .translate(FooException.class).using(FooRuntimeException::new)
                .done();

        RuntimeException result = TestUtil.translateException(et, TestUtil.FOO_EXCEPTION);
        expect(result.getMessage()).toEqual(TestUtil.FOO_EXCEPTION.getMessage());
        expect(result.getCause()).toEqual(TestUtil.FOO_EXCEPTION);
    }

}
