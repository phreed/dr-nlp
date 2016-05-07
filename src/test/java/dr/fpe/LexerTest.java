package dr.fpe;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.StringReader;


/**
 * Created by fred on 5/7/16.
 */
public class LexerTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public LexerTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(LexerTest.class);
    }


    /**
     * Lexer Testing
     */

    /**
     * This test uses a string reader to test the lexer.
     * <p>
     * https://docs.oracle.com/javase/7/docs/api/java/io/StringReader.html
     */

    public void testString() {
        final Lexer lex = new Lexer();
        lex.load("");
        lex.analyze();
        assertTrue(true);
    }

    /**
     * Comparing strings is not robust but it is quicker
     * for the initial version of the text.
     */
    public void testReader() {
        StringReader rdr = new StringReader("Dog food. Cat food.");
        final Lexer lex = new Lexer();
        lex.load(rdr);
        lex.analyze();

        assertEquals(
                "[word<Dog>, WHITE, word<food>, SBND, word<Cat>, WHITE, word<food>, SBND]",
                lex.getTokenList().toString());
    }

    /**
     * This is a brittle test (but quick).
     */
    public void testAnalysis() {
        StringReader rdr = new StringReader("Dog food. Cat food.");
        final Lexer lex = new Lexer();
        lex.load(rdr);
        lex.analyze();

        assertEquals("{Cat=2, Dog=0, food=1}",
                lex.getSymbolTable().toString());

        assertTrue(true);
    }

}
