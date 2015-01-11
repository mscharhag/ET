ET
==

### Introduction

TODO:

* Motivation

A small Java utility library for exception translation

With Java:
```
try {
    // code that can throw SomeException
    throw new SomeException();
} catch (SomeException e) {
    throw new SomeRuntimeException(e);
}
```

With ET:
```
// configure your mappings once
ExceptionTranslator et = ET.newConfiguration()
        .translate(SomeException.class).to(SomeRuntimeException.class)
        .done();

// will throw SomeRuntimeException
et.withTranslation(() -> {
    // code that can throw SomeException
    throw new SomeException();
});
```

### Getting started

TODO:

* Small
* Java 8
* Maven dependency
* Gradle dependency

### Exception mapping configuration

TODO:

* Thread safe
* basic configuration
* Exception inheritance check
* Inherit configuration

### Translation

TODO:

* withTranslation()
* withReturningTranslation

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


