/**
 * Created by fred on 5/7/16.
 */
package dr.fpe;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.*;


/**
 * Unit test for simple Nlp.
 */
public class RecognitionTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RecognitionTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(RecognitionTest.class);
    }

    /**
     * This test makes use of the actual Lexer, Parser and NamedEntityTree.
     * There should also be tests using mock versions of these classes.
     */
    public void testStrings()
    {
        final ReportStringWriter rptr = new ReportStringWriter();

        final StringBuffer sb = new StringBuffer()
                .append("Cat food").append('\n');
        final Reader netRdr = new StringReader(sb.toString());
        final NamedEntityTree net = new NamedEntityTree();
        net.load(netRdr);

        StringReader txtRdr = new StringReader("Dog food. Eat more Cat food now.");
        final Lexer lex = new Lexer();
        lex.load(txtRdr);
        lex.analyze();

        final Parser parser = new Parser();
        parser.parse(lex);

        net.recognize(rptr, net, 1, lex.getSymbolTable(), parser);

        assertEquals( "ROOT:Cat:food\n",
                rptr.toString());
    }

}



