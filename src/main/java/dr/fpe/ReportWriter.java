/**
 * Created by fred on 5/8/16.
 */
package dr.fpe;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This records the events to a writer.
 */
public class ReportWriter implements IReporter {
    private static final Logger log = Logger.getLogger(Cli.class.getName());

    final private Writer wtr;

    public ReportWriter(final Writer wtr) {
        this.wtr = wtr;
    }

    /** see IReporter */
    public Boolean report(final String msg) {
        try {
            // log.log(Level.INFO, msg);
            wtr.write(msg);
            wtr.write('\n');
        } catch (IOException ex) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
