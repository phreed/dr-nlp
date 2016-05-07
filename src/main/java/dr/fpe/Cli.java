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
        options.addOption("n", "nameFile", false, "the named-entities file.");
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
