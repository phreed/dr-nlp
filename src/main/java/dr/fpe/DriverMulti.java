/**
 * Created by fred on 5/8/16.
 */
package dr.fpe;

import org.apache.commons.cli.CommandLine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
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
        final private File filePath;

        public Task(final File filePath) {
            this.filePath = filePath;
        }

        /**
         * There really should be a callback to announce that the task is complete.
         * I ran out of time.
         */
        public void run() {
            Lexer lex = new Lexer();
            {
                log.log(Level.INFO, "reading language file");
                Reader rdr = null;
                try {
                    InputStream is = new FileInputStream(filePath);
                    rdr = new InputStreamReader(is);
                    lex.load(rdr);
                } catch (FileNotFoundException ex) {
                    log.log(Level.SEVERE, "could not open the named-entity file.");
                    ctx.status = Nlp.Status.FAIL;
                    return;
                } finally {
                    try {
                        if (rdr != null)
                            rdr.close();
                    } catch (IOException ex) {
                        log.log(Level.SEVERE, "could not close the file.");
                        ctx.status = Nlp.Status.FAIL;
                        return;
                    }
                }
                lex.analyze();
            }
            final Parser parser = new Parser();
            parser.parse(lex);

            net.recognize(reducer, net, 1, lex.getSymbolTable(), parser);
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
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        if (cmd.hasOption("z")) {
            log.log(Level.INFO, "reading zipped input file");
            InputStream zippy = null;
            byte[] buffer = new byte[2048];
            try {
                final File tmpDirPath = (cmd.hasOption("t")) ?
                        new File(cmd.getOptionValue("t")) :
                        new File(System.getProperty("java.io.tmpdir"));
                // final File tmpDir = Files.createTempDirectory(tmpDirPath);
                tmpDirPath.mkdirs();
                tmpDirPath.deleteOnExit();

                zippy = new FileInputStream(cmd.getOptionValue("z"));
                final ZipInputStream stream = new ZipInputStream(zippy);
                ZipEntry entry;
                while((entry = stream.getNextEntry()) != null) {
                    final File langFile = new File(tmpDirPath, entry.getName());
                    langFile.deleteOnExit();
                    FileOutputStream tmp = null;
                    try {
                        tmp = new FileOutputStream(langFile);
                        int len = 0;
                        while ((len = stream.read(buffer)) > 0) {
                            tmp.write(buffer, 0, len);
                        }
                    } finally {
                        if (tmp != null) {
                            tmp.close();
                        }
                    }
                    final Task task = new Task(langFile);
                    log.log(Level.INFO, "task started");
                    executor.execute(task);
                }
                executor.shutdown();
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
                final Task task = new Task(langFile);
                log.log(Level.INFO, "task started");
                executor.execute(task);
            }
            executor.shutdown();
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
