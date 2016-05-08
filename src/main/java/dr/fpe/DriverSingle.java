/**
 * Created by fred on 5/8/16.
 */
package dr.fpe;

import org.apache.commons.cli.CommandLine;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the single-threaded driver.
 * It instantiated when the '-l' option is present.
 */
public class DriverSingle {
    private static final Logger log = Logger.getLogger(Cli.class.getName());

    final private Nlp.Context ctx;
    final private CommandLine cmd;
    final private NamedEntityTree net;

    public DriverSingle(Nlp.Context ctx, final CommandLine cmd, final NamedEntityTree net) {
        this.ctx = ctx;
        this.cmd = cmd;
        this.net = net;
    }

    /**
     *
     */
    public Boolean main() {
        Lexer lex = new Lexer();
        {
            log.log(Level.INFO, "reading language file");
            Reader rdr = null;
            try {
                InputStream is = new FileInputStream(cmd.getOptionValue("l"));
                rdr = new InputStreamReader(is);
                lex.load(rdr);
            } catch (FileNotFoundException ex) {
                log.log(Level.SEVERE, "could not open the named-entity file.");
                ctx.status = Nlp.Status.FAIL;
                return Boolean.FALSE;
            } finally {
                try {
                    if (rdr != null)
                        rdr.close();
                } catch (IOException ex) {
                    log.log(Level.SEVERE, "could not close the file.");
                    ctx.status = Nlp.Status.FAIL;
                    return Boolean.FALSE;
                }
            }
            lex.analyze();
        }

        final Parser parser = new Parser();
        parser.parse(lex);

        String xmlString = parser.asXmlString();
        if (cmd.hasOption("x")) {
            log.log(Level.INFO, "writing AST xml");
            Writer wtr = null;
            try {
                OutputStream os = new FileOutputStream(cmd.getOptionValue("x"));
                wtr = new OutputStreamWriter(os);
                wtr.write(xmlString);
            } catch (IOException ex) {
                log.log(Level.SEVERE, "could not write to the output file.");
                ctx.status = Nlp.Status.FAIL;
                return Boolean.FALSE;
            } finally {
                try {
                    if (wtr != null)
                        wtr.close();
                } catch (IOException ex) {
                    log.log(Level.SEVERE, "could not close the output file.");
                    ctx.status = Nlp.Status.FAIL;
                    return Boolean.FALSE;
                }
            }
        } else {
            ctx.result = xmlString;
        }

        if (cmd.hasOption("n")) {
            if (cmd.hasOption("r")) {
                log.log(Level.INFO, "writing named-entity recognition file");
                Writer wtr = null;
                try {
                    final OutputStream os = new FileOutputStream(cmd.getOptionValue("r"));
                    wtr = new OutputStreamWriter(os);
                    final ReportWriter rptr = new ReportWriter(wtr);
                    rptr.report("This is a report of the named-entities detected");
                    rptr.report(new java.util.Date().toString());
                    net.recognize(rptr, net, 1, lex.getSymbolTable(), parser);

                } catch (IOException ex) {
                    log.log(Level.SEVERE, "could not write to the output file.");
                    ctx.status = Nlp.Status.FAIL;
                    return Boolean.FALSE;
                } finally {
                    try {
                        if (wtr != null)
                            wtr.close();
                    } catch (IOException ex) {
                        log.log(Level.SEVERE, "could not close the output file.");
                        ctx.status = Nlp.Status.FAIL;
                        return Boolean.FALSE;
                    }
                }
            }
        }

        ctx.status = Nlp.Status.OK;
        return Boolean.TRUE;
    }

}
