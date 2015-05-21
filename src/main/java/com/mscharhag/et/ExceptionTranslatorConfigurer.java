package com.mscharhag.et;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ExceptionTranslatorConfigurer {

    @SafeVarargs
    public final ExceptionMappingConfigurer translate(Class<? extends Exception>... sources) {
        List<Class<? extends Exception>> sourceList = new ArrayList<>(sources.length);
        Collections.addAll(sourceList, sources);
        return this.translate(sourceList);
    }

    protected abstract ExceptionMappingConfigurer translate(List<Class<? extends Exception>> sources);

    public abstract ExceptionTranslator done();

}
