package com.mscharhag.et;

import com.mscharhag.et.test.exceptions.BarException;
import com.mscharhag.et.test.exceptions.BarRuntimeException;
import com.mscharhag.et.test.exceptions.FooException;
import com.mscharhag.et.test.exceptions.FooRuntimeException;
import static com.mscharhag.et.test.TestUtil.*;
import org.junit.Test;

public class EtInheritedConfigurationTests {

    private ExceptionTranslator baseEt = ET.newConfiguration()
            .translate(FooException.class).to(FooRuntimeException.class)
            .done();

    @Test
    public void baseConfigurationIsInherited() {
        ExceptionTranslator et = baseEt.configure()
                .translate(BarException.class).to(BarRuntimeException.class)
                .done();
        RuntimeException result = translateException(et, FOO_EXCEPTION);
        expectException(result, FooRuntimeException.class, FOO_EXCEPTION_MESSAGE, FOO_EXCEPTION);
    }

    @Test
    public void configurationCanBeAdded() {
        ExceptionTranslator et = baseEt.configure()
                .translate(BarException.class).to(BarRuntimeException.class)
                .done();
        RuntimeException result = translateException(et, BAR_EXCEPTION);
        expectException(result, BarRuntimeException.class, BAR_EXCEPTION_MESSAGE, BAR_EXCEPTION);
    }
}
