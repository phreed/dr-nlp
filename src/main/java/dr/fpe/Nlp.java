/**
 * Created by fred on 5/7/16.
 */
package dr.fpe;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;

/**
 * The main driver.
 */
public class Nlp {
    private static final Logger log = Logger.getLogger(Cli.class.getName());

    public enum Status {
        OK,
        FAIL
    }

    public static class Context {
        public Status status = Status.OK;
        public String result = "not set";

        public Context() {}
    }

    public static Context gctx = null;

    /**
     * The arguments are all required but they may be default.
     * 1) The name of the file to be processed.
     * 2) The name of the
     * @param args
     */
    public static void main(String[] args) {
        final CommandLine cmd;
        cmd = new Cli().eval(args);
        final Context ctx = new Context();

        // the global context is made available so the main() can be tested.
        gctx = ctx;

        NamedEntityTree net = null;
        if (cmd.hasOption("n")) {
            log.log(Level.INFO, "reading named-entity file");
            Reader rdr = null;
            net = new NamedEntityTree();
            try {
                InputStream is = new FileInputStream(cmd.getOptionValue("n"));
                rdr = new InputStreamReader(is);
                net.load(rdr);
            } catch (FileNotFoundException ex) {
                log.log(Level.SEVERE, "could not open the named-entity file.");
                ctx.status = Status.FAIL;
                return;
            } finally {
                try {
                    if (rdr != null)
                        rdr.close();
                } catch (IOException ex) {
                    log.log(Level.SEVERE, "could not close the named-entity file.");
                    ctx.status = Status.FAIL;
                    return;
                }
            }
        }

        if (cmd.hasOption("l")) {
            final DriverSingle drvr = new DriverSingle(ctx, cmd, net);
            drvr.main();
            return;
        } else if (cmd.hasOption("z")) {

        }
    }

}