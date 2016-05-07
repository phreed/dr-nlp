package dr.fpe;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.StringReader;
import java.net.URL;

/**
 * Unit test for simple Nlp.
 */
public class NlpTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public NlpTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( NlpTest.class );
    }


    /**
     * Main Tests
     */
    public void testMain_missing_args()
    {
        Nlp.main(new String[] {});
        assertTrue( Nlp.status == Nlp.Status.FAIL );
    }

    /**
     * This is the test provided with the specification.
     */
    public void testMain_specified()
    {
        final String fpath = getClass().getResource("/nlp_data.txt").getFile();
        Nlp.main(new String[] {"-l", fpath});
        assertTrue( Nlp.status == Nlp.Status.OK );

        assertEquals("foo",
                Nlp.result);
    }


}
