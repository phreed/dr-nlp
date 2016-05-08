/**
* Created by fred on 5/8/16.
*/
package dr.fpe;

import java.io.*;
import java.util.logging.Logger;

/**
 * This records the events to a writer.
 */
public class ReportStringWriter implements IReducer {
    private static final Logger log = Logger.getLogger(Cli.class.getName());

    final private StringWriter wtr;

    public ReportStringWriter() {
        this.wtr = new StringWriter();
    }

    public String toString() {
        return wtr.toString();
    }

    /** see IReducer */
    public Boolean report(final NamedEntityTree.Update update) {
        update.tn.recognized(update.ixs);

        if (update.tn.occurenceCount() < 2) {
            // log.log(Level.INFO, msg);
            wtr.write(update.tn.toString());
            wtr.write('\n');
        }
        return Boolean.TRUE;
    }
}