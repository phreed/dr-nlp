/**
 * Created by fred on 5/8/16.
 */
package dr.fpe;

import java.io.*;
import java.util.logging.Logger;

/**
 * This records the events to a writer.
 */
public class ReportWriter implements IReducer {
    private static final Logger log = Logger.getLogger(Cli.class.getName());

    final private Writer wtr;

    public ReportWriter(final Writer wtr) {
        this.wtr = wtr;
    }

    /** see IReducer */
    public Boolean report(final NamedEntityTree.Update update) {

        update.tn.recognized(update.ixs);

        if (update.tn.occurenceCount() < 2) {
            try {
                // log.log(Level.INFO, msg);
                wtr.write(update.tn.toString());
                wtr.write('\n');
            } catch (IOException ex) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
}
