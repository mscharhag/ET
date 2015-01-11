package com.mscharhag.et;

import java.util.ArrayList;
import java.util.List;

public abstract class ExceptionTranslatorConfigurer {

    @SafeVarargs
    public final ExceptionMappingConfigurer translate(Class<? extends Exception>... sources) {
        List<Class<? extends Exception>> sourceList = new ArrayList<>(sources.length);
        for (Class<? extends Exception> source : sources) {
            sourceList.add(source);
        }
        return this.translate(sourceList);
    }

    protected abstract ExceptionMappingConfigurer translate(List<Class<? extends Exception>> sources);

    public abstract ExceptionTranslator done();

}
