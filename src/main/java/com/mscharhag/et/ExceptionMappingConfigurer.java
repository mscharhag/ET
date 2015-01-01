package com.mscharhag.et;

public interface ExceptionMappingConfigurer {

    ExceptionTranslatorConfigurer to(Class<? extends RuntimeException> targetException);

    ExceptionTranslatorConfigurer using(TargetExceptionResolver resolver);

}
