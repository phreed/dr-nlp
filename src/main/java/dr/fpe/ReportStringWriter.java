/**
* Created by fred on 5/8/16.
*/
package dr.fpe;

import java.io.*;
import java.util.logging.Logger;

/**
 * This records the events to a writer.
 */
public class ReportStringWriter implements IReporter {
    private static final Logger log = Logger.getLogger(Cli.class.getName());

    final private StringWriter wtr;

    public ReportStringWriter() {
        this.wtr = new StringWriter();
    }

    public String toString() {
        return wtr.toString();
    }

    /** see IReporter */
    public Boolean report(final String msg) {
        wtr.write(msg);
        wtr.write('\n');
        return Boolean.TRUE;
    }
}