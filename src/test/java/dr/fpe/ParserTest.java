/**
 * Created by fred on 5/7/16.
 */
package dr.fpe;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.StringReader;


/**
 * Tests the parser.
 * An injection ILexer should be created.
 */
public class ParserTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ParserTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ParserTest.class);
    }


    /**
     * Comparing strings is not robust but it is quicker
     * for the initial version of the text.
     */

    /**
     * This text illustrates the retaining of an empty sentence at the end.
     * I guess this is ok?
     */
    public void testReader() {
        StringReader rdr = new StringReader("Dog food. Cat food.");
        final Lexer lex = new Lexer();
        lex.load(rdr);
        lex.analyze();

        final Parser parser = new Parser();
        parser.parse(lex);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<nlp>\n" +
                        "<s><w>Dog</w><w>food</w></s>\n" +
                        "<s><w>Cat</w><w>food</w></s>\n" +
                        "<s></s>\n" +
                        "</nlp>\n",
                parser.asXmlString());
    }


}
