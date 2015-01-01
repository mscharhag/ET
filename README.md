ET
==

A small Java utility library for exception translation

Instead of
```
try {
    // code that throws IOException
} catch (IOException e) {
    throw new YourRuntimeException(e);
}
```

do this with ET:
```
// configure this once in your application
ExceptionTranslator translator = ET.newConfiguration()
        .translate(IOException.class).to(YourRuntimeException.class)
        .done();

// wrap the section that can throw the specified exceptions in a `withTranslation` lambda expression
translator.withTranslation(() -> {
    // code that throws IOException
});
```

