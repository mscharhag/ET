package com.mscharhag.et;

import com.mscharhag.et.impl.DefaultConfigurer;

public final class ET {

    // TODO: exception has no correct constructor?
    // TODO: TranslationException bei "kein konstruktor" und "lambda liefert null" ?
    // TODO: parent ET?


    private ET() {

    }


    public static ExceptionTranslatorConfigurer newConfiguration() {
        return new DefaultConfigurer();
    }

}
