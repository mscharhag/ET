ET
==
ET is a small Java 8+ library for exception conversion/translation.

### Motivation

From time to time every Java developer needs to convert exceptions of type `X` to type `Y`.

This sort of code typically looks like that:
```java
try {
    // code that can throw SomeException
    throw new SomeException();
} catch (SomeException e) {
    // convert SomeException to the exception you need
    throw new MyRuntimeException(e);
}
```
In addition to that, many Java developers prefer to work with `RuntimeExceptions` only
(there are [solid reasons](http://stackoverflow.com/questions/613954/the-case-against-checked-exceptions/614330#614330) for that).
In such situations, you often end up catching checked exceptions from standard or third party
components in order to convert them to into your own `RuntimeExceptions`.

ET is a (very) small Java 8 library that simplifies your exception conversion code using
Lambda expressions.

With ET it looks like this:
```Java
// Configure your exception mappings once, share this instance
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
Here `et.withTranslation(() -> ...)` will execute the passed lambda expression. If the Lambda
expression throws `SomeException`, `ExceptionTranslator` will catch it and throw a
`MyRuntimeException` instead. The `cause` of `MyRuntimeException` will be the caught
instance of `SomeException`.

`ExceptionTranslator` is immutable and thread safe, so it is safe to make `ExceptionTranslators`
globally available.

### Setup

To use ET you only need to add the following maven dependency to your project:

```xml
<dependency>
    <groupId>com.mscharhag</groupId>
    <artifactId>et</artifactId>
    <version>0.1.0</version>
</dependency>
```

When using ET you typically use Java 8 Lambda expression. So make sure to set your compiler level
to Java 8 or higher. With Maven you can do this with the `maven-compiler-plugin`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.1</version>
    <configuration>
        <source>1.8</source>
        <target>1.8</target>
    </configuration>
</plugin>
```

### Getting started

Exceptions are translated using an `ExceptionTranslator`, which is created from an
exception mapping configuration. New configurations are created with `ET.newConfiguration()`

For example:
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

        // create an ExceptionTranslator from this configuration
        .done();
```

To translate exceptions using the configured mappings simply use `ExceptionTranslator.withTranslation()`
and pass a Java 8 Lambda expression.

For example:
```java
ExceptionTranslator et = ET.newConfiguration()
        // convert all reflection exceptions to SomeRuntimeException
        .translate(ReflectiveOperationException.class).to(SomeRuntimeException.class)
        .done();

et.withTranslation(() -> {

    // this piece of code can throw NoSuchMethodException,
    // InvocationTargetException and IllegalAccessException

    // call String.toLowerCase() using reflection
    Method method = String.class.getMethod("toLowerCase");
    String result = (String) method.invoke("FOO");
    System.out.println(result); // prints "foo"
});
```
Alternatively you can use `ExceptionTranslator.withReturningTranslation()` if you
want to return a value from the Lambda expression.

For example:
```java
String result = et.withReturningTranslation(() -> {
    Method method = String.class.getMethod("toLowerCase");
    return (String) method.invoke("FOO");
});

System.out.println(result); // "foo"
```
Do not get confused by the `String` cast from the example above (this is required because
`method.invoke()` returns `Object`). Thanks to Java 8 type inference you can return
any type from `et.withReturningTranslation()`.

For Example:
```java
Person p = et.withReturningTranslation(() -> {
    ...
    return new Person("john");
});
```

Please note that `ExceptionTranslator` is thread safe and immutable. It is safe to
configure it once and make it globally available.

If no exception mappings are configured, `ExceptionTranslator` will translate all
checked exceptions to `RuntimeExceptions`.

For example:
```java
ExceptionTranslator et = ET.newConfiguration().done();

et.withTranslation(() -> {
    // this exception will be converted to RuntimeException
    throw new IOException("error");
});
```


### Configuration inheritance

You can create new `ExceptionTranslator` instances based on the configuration of an
existing `ExceptionTranslator`.

For example:
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

Note that `ExceptionTranslator` checks its own mapping configuration before using
the inherited configuration. So you can define more specific sub mappings when needed.

For example:
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

### Exception constructors

Whenever an exception should be converted to another exception, ET uses reflection to instantiate the
target exception. In order to do this, at least one of the following public constructors is required
in the target exception class. This list will be checked in the shown order. The first matching
constructor will be used to create the target exception:

* `(String, Throwable)`, exception message and cause will be passed
* `(String, Exception)`, exception message and cause will be passed
* `(String, RuntimeException)`, exception message and cause will be passed. Is only used if the source
    exception is a sub class of `RuntimeException`
* `(Throwable)`, exception cause will be passed
* `(Exception)`, exception cause will be passed
* `(RuntimeException)`, exception cause will be passed. Is only used if the source exception is sub
    class of `RuntimeException`
* `(String)`, exception message will be passed
* `()`, default constructor, no arguments will be passed

If no matching constructor can be found a `TranslationException` will be thrown. Cause of this
`TranslationException` will be the source exception.


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

