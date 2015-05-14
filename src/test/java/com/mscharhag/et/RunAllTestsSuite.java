package com.mscharhag.et;

import com.mscharhag.et.impl.ExceptionMappingsTests;
import com.mscharhag.et.impl.ReflectiveExceptionResolverTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ DefaultConfigurationTests.class, CustomConfigurationTests.class,
        InheritedConfigurationTests.class, ReflectiveExceptionResolverTests.class, ExceptionMappingsTests.class })
public class RunAllTestsSuite {
}
