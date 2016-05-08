## Simple Natural Language Processing

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

### Problem #1

Implemented a lexical-analyzer (look-ahead not state-machine) and parser.

In order to run the built application for problem #1.

```bash
mvn exec:java -Dexec.args="-l ./src/test/resources/nlp_data.txt -x results/p1.xml"
```

### Problem #2

Added a named-entity-dictionary, implemented as a tree, so that searching for
names in the sentences could be performed quickly.

Run application for problem #2.

```bash
mvn exec:java -Dexec.args="-l ./src/test/resources/nlp_data.txt -n ./src/test/resources/NER.txt -r results/p2.txt"
```

### Problem #3

The main problem here is coordinating updates to the named-entity-tree.
Augment the reporter to perform the updates on the named-entity-tree.
The reporter will make use of a conncurrent-queue to post the updates
which will be performed in the main thread.

```bash
`mvn exec:java -Dexec.args="-z ./src/test/resources/nlp_data.zip -n ./src/test/resources/NER.txt -r results/p3.txt"
 ```

 ### Remaining items

  * The tests are not very robust and were written to verify basic functionality.
  * Lots of little bugs.
  * Decide what to do with hyphenation (currently hyphens are treated as whitespace).



