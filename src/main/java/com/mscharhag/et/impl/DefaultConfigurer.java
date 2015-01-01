package com.mscharhag.et.impl;

import com.mscharhag.et.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultConfigurer extends ExceptionTranslatorConfigurer {

    List<ExceptionMapping> targets; // TODO: fix scope


    public DefaultConfigurer() {
        this(null);
    }

    DefaultConfigurer(List<ExceptionMapping> targets) {
        this.targets = Collections.unmodifiableList(targets != null ? targets : new ArrayList<>());
    }

//    @SafeVarargs
//    public final DefaultExceptionMappingConfigurer translate(Class<? extends Exception>... sources) {
//        List<Class<? extends Exception>> ex = new ArrayList<>();
//        for (Class<? extends Exception> exception : sources) {
//            ex.add(exception);
//        }
//
//    }

    @Override
    protected ExceptionMappingConfigurer getExceptionMappingConfigurer(List<Class<? extends Exception>> sources) {
        return new DefaultExceptionMappingConfigurer(this, sources);
    }


    public ExceptionTranslator done() {
        return new DefaultExceptionTranslator(targets);
    }


}
