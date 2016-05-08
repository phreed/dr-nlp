/**
 * Created by fred on 5/7/16.
 */
package dr.fpe;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.*;
import java.util.logging.Level;


/**
 * Unit test for simple Nlp.
 */
public class ReporterTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ReporterTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ReporterTest.class);
    }

    /**
     * This test makes use of the actual Lexer, Parser and NamedEntityTree.
     * There should also be tests using mock versions of these classes.
     */
    public void testStrings()
    {
        final ReportStringWriter rptr = new ReportStringWriter();

        rptr.report("foo");
        rptr.report("bar");
        rptr.report("baz");

        assertEquals( "foo\n" +
                        "bar\n" +
                        "baz\n",
                rptr.toString());
    }

}

