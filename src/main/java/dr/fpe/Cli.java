/**
 * Created by fred on 5/7/16.

 */
package dr.fpe;

import java.util.logging.Logger;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.util.logging.Level;

/**
 * This does some text processing but it is not core to the problem.
 * It is useful for process an array of strings containing command line options.
 */
public class Cli {
    private static final Logger log = Logger.getLogger(Cli.class.getName());

    private final Options options;

    public Cli() {
        this.options = new Options();

        options.addOption("l", "langFile", true, "the language-file to process.");
        options.addOption("n", "nameFile", true, "the named-entities file.");
        options.addOption("x", "parseFile", true, "the output XML file.");
        options.addOption("r", "nameResultFile", true, "the output named-entity result file.");
        options.addOption("z", "zipLangFile", true, "the input language files are zipped.");
        options.addOption("t", "tempdir", true, "the temporary directory for unzipped files.");
        options.addOption("y", "langDir", true, "the directory holding many language files.");
    }

    public CommandLine eval(final String[] args) {
        final CommandLineParser parser = new DefaultParser();
        try {
            return parser.parse(options, args);
        } catch (ParseException ex) {
            log.log(Level.SEVERE, "Failed to parse command line properties", ex);
            System.exit(2);
        }
        return null;
    }

}
