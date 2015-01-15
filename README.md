ET
==
ET is a small Java 8+ library for exception conversion/translation.

### Motivation

From time to time every Java developer needs to convert exceptions of type `X` to exceptions of type `Y`.
This type of code typically looks like that:
```java
try {
    // code that can throw SomeException
    throw new SomeException();
} catch (SomeException e) {
    // convert SomeException to the type you need
    throw new MyRuntimeException(e);
}
```
In addition to that, many Java developers prefer to work with `RuntimeExceptions` only
(there are [solid reasons](http://stackoverflow.com/questions/613954/the-case-against-checked-exceptions/614330#614330) for that).
When doing this, you often end up catching checked exceptions from standard or third party components to convert them to into `RuntimeExceptions`.

ET is a (very) small Java 8 library that simplifies your exception conversion code:
```Java
// Configure your exception mappings once in your application/module
ExceptionTranslator et = ET.newConfiguration()
        .translate(SomeException.class).to(MyRuntimeException.class)
        .done();

// Wrap the code that can throw SomeException in a Java 8 Lambda
// expression, no try/catch block required
et.withTranslation(() -> {
    // code that can throw SomeException
    throw new SomeException();
});
```
Here `et.withTranslation(() -> ...)` will execute the passed lambda expression. If the Lambda expression throws `SomeException`,
 `ExceptionTranslator` will catch it and throw a `MyRuntimeException` instead. The `cause` of `MyRuntimeException` will be the
 caught `SomeException`.

Note that `ExceptionTranslator` is immutable and thread safe, so it is safe to make `ExceptionTranslators` globally available.

### Setup

TODO:

* Small
* Java 8
* Maven dependency
* Gradle dependency

### Getting started

Exceptions are translated using an `ExceptionTranslator`. An `ExceptionTranslator` can be obtained
 from an exception mapping configuration. New mapping configurations can be created with `ET.newConfiguration()`:

```java
ExceptionTranslator et = ET.newConfiguration()

        // SomeException should be translated to SomeRuntimeException
        .translate(SomeException.class).to(SomeRuntimeException.class)

        // FooException and BarException should be translated to BazException
        .translate(FooException.class, BarException.class).to(BazException.class)

        // Exception base classes can be used.
        // For example: FileNotFoundException (which extends IOException)
        //              will be translated to SomeRuntimeException
        .translate(IOException.class).to(SomeRuntimeException.class)

        // The following statement will not work, because UnknownHostException is an
        // IOException which is already translated to SomeRuntimeException (previous statement)
        // To make this work, this mapping should be defined before the IOException mapping
        // More specific mappings should be defined first! (like the order of catch statements)
        .translate(UnknownHostException.class).to(HostNotFoundException.class)

        // create an ExceptionTranslator from this configuration
        .done();
```

To translate exceptions using the configured mappings simply use `ExceptionTranslator.withTranslation()` and pass a lambda expression:
```java
// The above configuration defines a mapping from SomeException to SomeRuntimeException
// withTranslation() will throw SomeRuntimeException in case the lambda expression throws SomeException
et.withTranslation(() -> {
    // code that can throw SomeException
});
```

If you want to return a value from the lambda expression, use `ExceptionTranslator.withReturningTranslation()` instead:
```java
Person p = et.withReturningTranslation(() -> {
    // code that can throw SomeException
    return new Person("john");
});
```

Please note that `ExceptionTranslator` is thread safe and immutable. It is safe to configure it once and make it globally
 available.

You can create new `ExceptionTranslator` instances based on the configuration of an existing `ExceptionTranslator`:

```java
ExceptionTranslator base = ET.newConfiguration()
        .translate(FooException.class).to(BaseRuntimeException.class)
        .translate(BarException.class).to(BaseRuntimeException.class)
        .done();

ExceptionTranslator et = base.newConfiguration()

        // override mapping for BarException
        .translate(BarException.class).to(SomeRuntimeException.class)

        // add new mapping for BazException
        .translate(BazException.class).to(SomeRuntimeException.class)
        .done();

et.withTranslation(() -> {
    // FooException will be translated to BaseRuntimeException
    // BarException will be translated to SomeRuntimeException
    // BazException will be translated to SomeRuntimeException
});
```

Note that ExceptionTranslator checks its own mapping configuration before using the inherited configuration.
So you can define more specific sub mappings when needed:

```java
ExceptionTranslator base = ET.newConfiguration()
        .translate(IOException.class).to(MyRuntimeException.class)
        .done();

ExceptionTranslator et = base.newConfiguration()
        .translate(FileNotFoundException.class).to(MyFileNotFoundRuntimeException.class)
        .done();

et.withTranslation(() -> {
    // FileNotFoundException will be translated to MyFileNotFoundRuntimeException
    // Other sub classes of IOException will be translated to MyRuntimeException
});
```

If no exception mappings are configured, ExceptionTranslator will translate all checked exceptions to `RuntimeExceptions`:
```java
ExceptionTranslator et = ET.newConfiguration().done();
// will throw RuntimeException
et.withTranslation(() -> {
    throw new IOException("error");
});
```

TODO:

* Exception constructors
* message/cause

### Licence

Copyright 2015 Michael Scharhag

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


