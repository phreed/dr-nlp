# dr-nlp

Download in the normal way.

```bash
git clone https://github.com/phreed/dr-nlp.git
```

Build and test with Maven.

```bash
mvn test
```

In order to run the built application.

```bash
mvn exec:java -Dexec.args="-l ./src/test/resources/nlp_data.txt -x target/foo.xml"
```

