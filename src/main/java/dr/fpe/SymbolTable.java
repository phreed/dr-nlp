package dr.fpe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by fred on 5/7/16.
 */

/**
 * The symbol table consisting of a mutually indexing pair, a map and a list.
 *
 */
public class SymbolTable implements ISymbolTable {
    private static final Logger log = Logger.getLogger(Cli.class.getName());

    private final Map<String, Integer> wordmap = new HashMap<String, Integer>();
    private final List<String> wordlist = new ArrayList<String>();

    public SymbolTable() {}

    /** see interface for doc */
    public int add(final String ch) {
        // log.log(Level.INFO, "adding symbol " + ch);

        if (wordmap.containsKey(ch)) {
            return wordmap.get(ch);
        }
        int ix = wordlist.size();
        wordlist.add(ch);
        wordmap.put(ch, ix);
        return ix;
    }

    /** see ISymbolTable for doc */
    public String get(int ix) {
        return wordlist.get(ix);
    }

    /** see ISymbolTable for doc */
    public Boolean hasName(final String name) {
        return wordmap.containsKey(name);
    }

    public Integer get(String name) {
        return wordmap.get(name);
    }

    public String toString() {
        return wordmap.toString();
    }
}
