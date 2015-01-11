package com.mscharhag.et;

import com.mscharhag.et.impl.DefaultExceptionTranslatorConfigurer;

public final class ET {

    private ET() {

    }

    public static ExceptionTranslatorConfigurer newConfiguration() {
        return new DefaultExceptionTranslatorConfigurer();
    }

}
