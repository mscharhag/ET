package com.mscharhag.et;

public interface TargetExceptionResolver {

    /**
     * Translates the passed exception to a {@code RuntimeException}.
     *
     * If, for some reason, no target exception can be returned, a {@link com.mscharhag.et.TranslationException} should be thrown.
     * @param source
     * @return a {@code RuntimeException}, never {@code null}
     */
    RuntimeException getTargetException(Exception source);

}
