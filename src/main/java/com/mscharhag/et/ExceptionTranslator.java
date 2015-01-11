package com.mscharhag.et;

public interface ExceptionTranslator {

    void withTranslation(TryBlock tryBlock);

    <T> T withReturningTranslation(ReturningTryBlock<T> invokable);

    ExceptionTranslatorConfigurer newConfiguration();

}
