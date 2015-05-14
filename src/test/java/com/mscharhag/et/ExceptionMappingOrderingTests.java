package com.mscharhag.et;

import static com.mscharhag.et.test.TestUtil.*;

import com.mscharhag.et.test.exceptions.*;
import org.junit.Test;

public class ExceptionMappingOrderingTests {

    private ExceptionTranslator et = ET.newConfiguration()
            .translate(FooException.class).to(FooRuntimeException.class)
            .translate(Exception.class).to(BarRuntimeException.class)
            .translate(FooChildException.class).to(FooChildRuntimeException.class)
            .done();

    @Test
    public void test() { // TODO rename tests
        RuntimeException result = translateException(et, FOO_CHILD_EXCEPTION);
        expectException(result, FooChildRuntimeException.class, FOO_CHILD_EXCEPTION_MESSAGE, FOO_CHILD_EXCEPTION);
    }


    @Test
    public void test2() {
        ExceptionTranslator et = ET.newConfiguration()
                .translate(Exception.class).to(FooRuntimeException.class)
                .done();
        Exception e = new Exception("e");
        RuntimeException result = translateException(et, e);
        expectException(result, FooRuntimeException.class, "e", e);
    }

    // TODO: fail on duplicate mappings?
}
