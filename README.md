# dr-nlp

None of this has been tested on MS/Windows.

Download in the normal way.

```bash
git clone https://github.com/phreed/dr-nlp.git
```

Build and test with Maven.

```bash
mvn test
```

Individual tests suites are run like this:

```bash
mvn test -Dtest=dr.fpe.NamedEntityTreeTest
```

Individual test are requested like this:

```bash
`mvn test -Dtest=dr.fpe.NamedEntityTreeTest#testSingle
```
Results are in the 'results' directory.

In order to run the built application for problem #1.

```bash
mvn exec:java -Dexec.args="-l ./src/test/resources/nlp_data.txt -x results/p1.xml"
```

Run application for problem #2.

```bash
mvn exec:java -Dexec.args="-l ./src/test/resources/nlp_data.txt -n ./src/test/resources/NER.txt -r results/p2.txt"
```




