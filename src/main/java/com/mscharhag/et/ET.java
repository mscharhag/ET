package com.mscharhag.et;

import com.mscharhag.et.impl.DefaultExceptionTranslatorConfigurer;

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
