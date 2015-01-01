package com.mscharhag.et;

public interface TargetExceptionResolver {

    RuntimeException getTargetException(Exception source);

}
