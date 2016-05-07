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

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<nlp>\n" +
                "<s><w>The</w><w>term</w><w>First</w><w>World</w><w>War</w><w>was</w><w>first</w><w>used</w><w>in</w><w>September</w><w>by</w><w>the</w><w>German</w><w>philosopher</w><w>Ernst</w><w>Haeckel</w><w>who</w><w>claimed</w><w>that</w><w>there</w><w>is</w><w>no</w><w>doubt</w><w>that</w><w>the</w><w>course</w><w>and</w><w>character</w><w>of</w><w>the</w><w>feared</w><w>European</w><w>War</w><w>will</w><w>become</w><w>the</w><w>first</w><w>world</w><w>war</w><w>in</w><w>the</w><w>full</w><w>sense</w><w>of</w><w>the</w><w>word</w><w>Why</w><w>did</w><w>the</w><w>war</w><w>begin</w><w>The</w><w>immediate</w><w>trigger</w><w>for</w><w>war</w><w>was</w><w>the</w><w>June</w><w>assassination</w><w>of</w><w>Archduke</w><w>Franz</w><w>Ferdinand</w><w>of</w><w>Austria</w><w>heir</w><w>to</w><w>the</w><w>throne</w><w>of</w><w>Austria</w><w>Hungary</w><w>by</w><w>Yugoslav</w><w>nationalist</w><w>Gavrilo</w><w>Princip</w><w>years</w><w>old</w><w>at</w><w>the</w><w>time</w><w>in</w><w>Sarajevo</w></s>\n" +
                "<s><w>This</w><w>set</w><w>off</w><w>a</w><w>diplomatic</w><w>crisis</w><w>when</w><w>Austria</w><w>Hungary</w><w>delivered</w><w>an</w><w>ultimatum</w><w>to</w><w>the</w><w>Kingdom</w><w>of</w><w>Serbia</w><w>and</w><w>entangled</w><w>international</w><w>alliances</w><w>formed</w><w>over</w><w>the</w><w>previous</w><w>decades</w><w>were</w><w>invoked</w></s>\n" +
                "<s><w>Within</w><w>weeks</w><w>the</w><w>major</w><w>powers</w><w>were</w><w>at</w><w>war</w><w>and</w><w>the</w><w>conflict</w><w>soon</w><w>spread</w><w>around</w><w>the</w><w>world</w></s>\n" +
                "<s></s>\n" +
                "</nlp>\n",
                Nlp.result);
    }


}
