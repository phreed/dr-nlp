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
    public static Status status = Status.OK;
    public static String result = "not set";

    /**
     * The arguments are all required but they may be default.
     * 1) The name of the file to be processed.
     * 2) The name of the
     * @param args
     */
    public static void main(String[] args) {
        final CommandLine cmd;
        cmd = new Cli().eval(args);

        if (!cmd.hasOption("l")) {
            final StringBuffer sb = new StringBuffer();
            sb.append("missing file name to process: ");
            sb.append(args.length);
            sb.append(" number of args ");
            log.log(Level.WARNING, sb.toString());
            status = Status.FAIL;
            return;
        }
        try {
            Reader rdr = null;
            Lexer lex = new Lexer();
            try {
                InputStream is = new FileInputStream(cmd.getOptionValue("l"));
                rdr = new InputStreamReader(is);
                lex.load(rdr);
            } finally {
                try {
                    if (rdr != null)
                        rdr.close();
                } catch (IOException ex) {
                    log.log(Level.SEVERE, "could not close the file.");
                    status = Status.FAIL;
                    return;
                }
            }

            lex.analyze();

            final Parser parser = new Parser();
            parser.parse(lex);

            String xmlString = parser.asXmlString();
            if (cmd.hasOption("x")) {
                Writer wtr = null;
                try {
                    OutputStream os = new FileOutputStream(cmd.getOptionValue("x"));
                    wtr = new OutputStreamWriter(os);
                    wtr.write(xmlString);
                } catch (IOException ex) {
                        log.log(Level.SEVERE, "could not write to the output file.");
                        status = Status.FAIL;
                        return;
                } finally {
                    try {
                        if (wtr != null)
                            wtr.close();
                    } catch (IOException ex) {
                        log.log(Level.SEVERE, "could not close the output file.");
                        status = Status.FAIL;
                        return;
                    }
                }
            } else {
                result = xmlString;
            }

            status = Status.OK;

        } catch (FileNotFoundException ex) {
            final StringBuffer sb = new StringBuffer();
            sb.append("missing file to process: ");
            sb.append(cmd.getOptionValue("l"));
            log.log(Level.WARNING, sb.toString());
            status = Status.FAIL;
            return;
        }



    }
}