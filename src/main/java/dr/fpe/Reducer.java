

/**
 * Created by fred on 5/8/16.
 */
package dr.fpe;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class Reducer implements  IReducer {
    private static final Logger log = Logger.getLogger(Reducer.class.getName());

    final NamedEntityTree net;
    final BlockingQueue queue = new LinkedBlockingQueue();

    final private Writer wtr;

    public Reducer(final NamedEntityTree net, final Writer wtr) {
        this.wtr = wtr;
        this.net = net;
    }

    /** see IReducer */
    public Boolean reduce() {
        return Boolean.TRUE;
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
