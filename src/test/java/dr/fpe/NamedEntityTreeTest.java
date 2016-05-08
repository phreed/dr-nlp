package dr.fpe;

/**
 * Created by fred on 5/7/16.
 */
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.*;


/**
 * Unit test for simple Nlp.
 */
public class NamedEntityTreeTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public NamedEntityTreeTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(NamedEntityTreeTest.class);
    }

    /**
     * Stand-alone test
     */
    public void testSingle()
    {
        final NamedEntityTree net = new NamedEntityTree();
        net.add(" foo bar-baz   ");
        assertEquals( "named = ROOT:foo:bar:baz" + '\n',
                net.toString() );
    }

    public void testHypenated()
    {
        final NamedEntityTree net = new NamedEntityTree();
        net.add("Broyden–Fletcher–Goldfarb–Shannon");
        assertEquals( "named = " +
                "ROOT:Broyden:Fletcher:Goldfarb:Shannon" + '\n',
                net.toString() );
    }

    public void testString()
    {
        final StringBuffer sb = new StringBuffer()
                .append("Carl Benjamin Boyer").append('\n')
                .append("Bible").append('\n')
                .append("Broyden–Fletcher–Goldfarb–Shannon").append('\n')
                .append(" fig Newton").append('\n');
        final Reader rdr = new StringReader(sb.toString());
        final NamedEntityTree net = new NamedEntityTree();
        net.load(rdr);

        assertEquals( "named = " +
                "ROOT:fig:Newton" + '\n' +
                "ROOT:Carl:Benjamin:Boyer" + '\n' +
                "ROOT:Bible" + '\n' +
                "ROOT:Broyden:Fletcher:Goldfarb:Shannon" + '\n',
                net.toString() );
    }

    public void testMain_specified()
    {
        final String fpath = getClass().getResource("/NER.txt").getFile();
        Reader rdr = null;
        String result = "none";
        try {
            InputStream is = new FileInputStream(fpath);
            rdr = new InputStreamReader(is);
            final NamedEntityTree net = new NamedEntityTree();
            net.load(rdr);
            result = net.toString();
        } catch (FileNotFoundException ex) {
            fail("could not find/open the named-entity file");
        } finally {
            try {
                if (rdr != null)
                    rdr.close();
            } catch (IOException ex) {
                fail("could not close the named-entity file");
            }
        }

        assertEquals("named = " +
                "ROOT:Isaac:Newton" + '\n' +
                "ROOT:Moon" + '\n' +
                "ROOT:Bible" + '\n' +
                "ROOT:Wales" + '\n' +
                "ROOT:England" + '\n' +
                "ROOT:Euclid" + '\n' +
                "ROOT:Powys" + '\n' +
                "ROOT:Broyden:Fletcher:Goldfarb:Shanno" + '\n' +
                "ROOT:Llywelyn:ap:Gruffudd" + '\n' +
                "ROOT:Elements" + '\n' +
                "ROOT:Franz:Ferdinand" + '\n' +
                "ROOT:Earth" + '\n' +
                "ROOT:Austria:Hungary" + '\n' +
                "ROOT:Yugoslavia" + '\n' +
                "ROOT:Prince:of:Wales" + '\n' +
                "ROOT:King:Henry:III" + '\n' +
                "ROOT:Robert:of:Belleme" + '\n' +
                "ROOT:Shrewsbury" + '\n' +
                "ROOT:Michael:Collins" + '\n' +
                "ROOT:North:America" + '\n' +
                "ROOT:Albert:Einstein" + '\n' +
                "ROOT:Serbia" + '\n' +
                "ROOT:Oracle:Corporation" + '\n' +
                "ROOT:Sarajevo" + '\n' +
                "ROOT:Neil:Armstrong" + '\n' +
                "ROOT:Roger:de:Montgomery" + '\n' +
                "ROOT:Gavrilo:Princip" + '\n' +
                "ROOT:Ernst:Haeckel" + '\n' +
                "ROOT:James:Clerk:Maxwell" + '\n' +
                "ROOT:Europe" + '\n' +
                "ROOT:Montgomery:Castle" + '\n' +
                "ROOT:Japan" + '\n' +
                "ROOT:Apollo:11" + '\n' +
                "ROOT:Carl:Benjamin:Boyer" + '\n' +
                "ROOT:Buzz:Aldrin" + '\n' +
                "ROOT:Olympic:Games" + '\n' +
                "ROOT:Sun" + '\n' +
                "ROOT:Sun:Microsystems" + '\n' +
                "ROOT:Sea:of:Tranquility" + '\n' +
                "ROOT:Venice" + '\n' +
                "ROOT:BFGS" + '\n' +
                "ROOT:Newton" + '\n' +
                "ROOT:Pacific:Ocean" + '\n' +
                "ROOT:Antikythera" + '\n' +
                "ROOT:Germany" + '\n' ,
                result);
    }

}


