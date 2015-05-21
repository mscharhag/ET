package com.mscharhag.et;

import com.mscharhag.et.impl.DefaultExceptionTranslatorConfigurer;

/**
 * Global factory that can be used to create new {@code ExceptionTranslator} configurations using
 * {@code ET.newConfiguration()}.
 * <p>Usage:
 * <pre>
 *     ExceptionTranslator et = ET.newConfiguration()
 *                                  // Exception translator configuration
 *                                  .translate(FooException.class).to(MyRuntimeException.class)
 *                                  .translate(BazException.class).using(OtherException::new)
 *                                  .translate(BarException.class).using((message, exception) -> { .. })
 *                                  .done(); // Create ExceptionTranslator from configuration
 * </pre>
 */
public final class ET {

    private ET() {

    }

    /**
     * Returns a new {@link com.mscharhag.et.ExceptionTranslatorConfigurer} that can be used to create a new
     * {@link com.mscharhag.et.ExceptionTranslator} configuration.
     * @return an {@link com.mscharhag.et.ExceptionTranslatorConfigurer}, never {@code null}
     */
    public static ExceptionTranslatorConfigurer newConfiguration() {
        return new DefaultExceptionTranslatorConfigurer();
    }

}
