package com.mscharhag.et.doc;

import com.mscharhag.et.ET;
import com.mscharhag.et.ExceptionTranslator;
import com.mscharhag.et.test.TestUtil;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLConnection;

public class DocumentationExamplesTest {

    @Test
    public void motivation() {
        RuntimeException ex = TestUtil.catchException(() -> {
            /*
                Instead of:
                try {
                    // code that can throw SomeException
                    throw new SomeException();
                } catch (SomeException e) {
                    throw new SomeRuntimeException(e);
                }
            */

            // configure your mappings once
            ExceptionTranslator et = ET.newConfiguration()
                    .translate(SomeException.class).to(SomeRuntimeException.class)
                    .done();

            // will throw SomeRuntimeException
            et.withTranslation(() -> {
                // code that can throw SomeException
                throw new SomeException();
            });
        });

        assertEquals(SomeRuntimeException.class, ex.getClass());
        assertEquals(SomeException.class, ex.getCause().getClass());
    }


    @Test
    public void gettingStartedWithTranslation()  {
        ExceptionTranslator et = ET.newConfiguration()
                .translate(ReflectiveOperationException.class).to(SomeRuntimeException.class)
                .done();

        et.withTranslation(() -> {

            // this piece of code can throw NoSuchMethodException,
            // InvocationTargetException and IllegalAccessException

            // call String.toLowerCase() using reflection
            Method method = String.class.getMethod("toLowerCase");
            String result = (String) method.invoke("FOO");
        });

    }

    @Test
    public void gettingStartedWithReturningTranslation()  {
        ExceptionTranslator et = ET.newConfiguration()
                .translate(ReflectiveOperationException.class).to(SomeRuntimeException.class)
                .done();

        String result = et.withReturningTranslation(() -> {
            Method method = String.class.getMethod("toLowerCase");
            return (String) method.invoke("FOO");
        });

        assertEquals("foo", result);
    }



    public static class SomeException extends Exception {

    }

    public static class SomeRuntimeException extends RuntimeException {
        public SomeRuntimeException(Throwable cause) {
            super(cause);
        }
    }

}
