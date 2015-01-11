package com.mscharhag.et.doc;

import com.mscharhag.et.ET;
import com.mscharhag.et.ExceptionTranslator;
import com.mscharhag.et.test.TestUtil;
import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentationExamples {

    @Test
    public void basicSetup() {
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





    public static class SomeException extends Exception {

    }

    public static class SomeRuntimeException extends RuntimeException {
        public SomeRuntimeException(Throwable cause) {
            super(cause);
        }
    }

}
