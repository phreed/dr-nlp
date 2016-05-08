/**
 * Created by fred on 5/8/16.
 */
package dr.fpe;

import org.apache.commons.cli.CommandLine;

import java.io.*;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * This is the single-threaded driver.
 * It instantiated when the '-l' option is present.
 */
public class DriverMulti {
    private static final Logger log = Logger.getLogger(DriverMulti.class.getName());

    final private Nlp.Context ctx;
    final private CommandLine cmd;
    final private NamedEntityTree net;
    final private Reducer reducer;

    public DriverMulti(Nlp.Context ctx, final CommandLine cmd, final NamedEntityTree net, Reducer reducer) {
        this.ctx = ctx;
        this.cmd = cmd;
        this.net = net;
        this.reducer = reducer;
    }

    public class Task implements Runnable {
        final private InputStream is;

        public Task(final InputStream is) {
            this.is = is;
        }

        /**
         * There really should be a callback to announce that the task is complete.
         * I ran out of time.
         */
        public void run() {
            Lexer lex = new Lexer();
            {
                // log.log(Level.INFO, "reading language file " + filePath.getPath().toString());
                final Reader rdr = new InputStreamReader(this.is);
                lex.load(rdr);
                lex.analyze();
            }
            final Parser parser = new Parser();
            parser.parse(lex);

            net.recognize(reducer, net, 1, lex.getSymbolTable(), parser);
            return;
        }
    }
    /**
     * This is only called with the '-z' option.
     * We unpack the zip file and send the work to the thread-pool.
     *
     * The unzipping could probably be done to input-streams but I
     * am not familiar with how to do that.
     */
    public Boolean main() {
        final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        if (cmd.hasOption("z")) {
            log.log(Level.INFO, "reading zipped input file");
            ZipFile zippy = null;
            try {
                zippy = new ZipFile(cmd.getOptionValue("z"));
                final Enumeration<? extends ZipEntry> entries = zippy.entries();
                while(entries.hasMoreElements()) {
                    final ZipEntry entry = entries.nextElement();
                    final Task task = new Task( zippy.getInputStream(entry) );
                    log.log(Level.INFO, "task started " + entry.toString());
                    executor.execute(task);
                }
                executor.shutdown();
                reducer.reduce();
                try {
                    executor.awaitTermination(30, TimeUnit.SECONDS);
                } catch (InterruptedException ex) {
                    log.log(Level.SEVERE, "threads interrupted");
                }

            } catch (IOException ex) {
                //
            } finally {
                try {
                    if (zippy != null) {
                        zippy.close();
                    }
                } catch (IOException ex1) {
                    log.log(Level.INFO, "give up");
                }
            }
        }
        // I had too many problems with the zip.
        if (cmd.hasOption("y")) {
            log.log(Level.INFO, "reading directory of input files");

            final File dirPath = new File(cmd.getOptionValue("y"));
            log.log(Level.INFO, dirPath.toString());
            for (final File langFile : dirPath.listFiles()) {
                Reader rdr = null;
                try {
                    InputStream is = new FileInputStream(langFile);
                    final Task task = new Task(is);
                    log.log(Level.INFO, "task started " + langFile);
                    executor.execute(task);
                } catch (FileNotFoundException ex) {
                    log.log(Level.SEVERE, "could not open the named-entity file.");
                    ctx.status = Nlp.Status.FAIL;
                    continue;
                } finally {
                    try {
                        if (rdr != null)
                            rdr.close();
                    } catch (IOException ex) {
                        log.log(Level.SEVERE, "could not close the file.");
                        ctx.status = Nlp.Status.FAIL;
                        continue;
                    }
                }
            }
            executor.shutdown();
            reducer.reduce();
            try {
                executor.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                log.log(Level.SEVERE, "threads interrupted");
            }
        }
        ctx.status = Nlp.Status.OK;
        return Boolean.TRUE;
    }

}
