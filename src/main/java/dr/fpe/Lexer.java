package dr.fpe;

/**
 * Created by fred on 5/7/16.
 *
 * The lexer identifies tokens in the input stream.
 */
import java.io.Reader;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Lexer implements ILexer, ILoadable {
    private static final Logger log = Logger.getLogger(Cli.class.getName());

    private final List<Character> raw = new ArrayList<Character>();
    private final List<Token> data = new ArrayList<Token>();

    // The returned list could be converted into an array to save space and process faster.
    // At this point that would be a premature optimization.
    public List<Character> getRaw() { return raw; }
    public List<Token> getTokenList() { return data; }


    private final SymbolTable st = new SymbolTable();
    public SymbolTable getSymbolTable() {
        return st;
    }

    public Lexer() {}

    /**
     * The types of tokens.
     */
    public enum TokType {
        SBND, // sentence-boundary
        WORD, // a set of latin characters
        PUNC, // a punctuation character
        COLL, // a collapsing character
        WHITE // a whitespace token
    }

    public class Token implements IToken {
        public final TokType tt;
        public final Integer offset;
        public final Integer len;
        public final Integer ix;

        /**
         * Construction of a new token adds it to the symbol-table.
         * @param tt The discovered token type
         * @param offset The index of the token in the raw
         * @param len The length of the token
         *
         */
        public Token(final TokType tt, final int offset, final int len, final String ch) {
            // log.log(Level.INFO, new StringBuffer("token: ").append(tt).append(" : ").append(ch).toString());

            this.tt = tt;
            this.offset = offset;
            this.len = len;
            switch (tt) {
                case WORD:
                    this.ix = st.add(ch);
                    break;
                default:
                    this.ix = -1;
            }
        }

        public Integer getIndex() { return this.ix; }

        public String toString() {
            if (tt == TokType.WORD) {
                return "word<" + st.get(this.ix) + ">";
            }
            return tt.toString();
        }
    }


    /**
     * A helper function to extract the name of the token.
     *
     * @param ix
     * @param jx
     * @return
     */
    private StringBuilder extract(final int ix, final int jx) {
        final StringBuilder sb = new StringBuilder();
        for (int kx = ix; kx < jx && kx < raw.size(); kx++) {
            sb.append(raw.get(kx));
        }
        if (jx > raw.size()) {
            log.log(Level.INFO, new StringBuilder().append(jx).append(':').append(raw.size())
                    .append(" ").append(sb).toString());
        }
        return sb;
    }
    /**
     * Lexical analysis could use a state-machine to keep
     * track of the object being created but I am using look-ahead.
     * I find that look-ahead makes more sense to my brain.
     * The problem with this approach is that the end-of-data
     * logic needs to be repeated in each look-ahead.
     *
     * @return
     */
    public Boolean analyze() {
        // TokType state = TokType.SBND;
        int ix=0;
        MAIN:
        while (ix < raw.size()) {
            final Character ci = raw.get(ix);
            // log.log(Level.INFO, new StringBuffer("analysis: ").append(ix).append(" : ").append(ci).toString());

            if (ci == '.') {
                // log.log(Level.OFF, "a sentence-boundry?");

                // look-ahead for white-space followed by upper-case (or raw end).
                // this indicates one type of sentence-boundary.
                // If not found then this is just a punctuation.
                int jx = ix+1;
                SENTENCE_BND_LA:
                while (jx < raw.size()) {
                    final Character cj = raw.get(jx);
                    if (Character.isWhitespace(cj)) {
                        jx++;
                        continue SENTENCE_BND_LA;
                    }
                    if (! Character.isUpperCase(cj)) {
                        ix = jx;
                        continue MAIN;
                    }
                    // found a sentence boundary token.
                    // squash the '.' but not the upper-case-char
                    data.add(new Token(TokType.SBND, ix, jx, extract(ix, jx).toString()));
                    ix = jx;
                    continue MAIN;
                }
                data.add(new Token(TokType.SBND, ix, jx, extract(ix, jx).toString()));
                return Boolean.TRUE;
            }
            if (Character.isWhitespace(ci)) {
                // log.log(Level.OFF, "a whitespace");

                int jx = ix+1;
                WHITE_SPACE_LA:
                while (jx < raw.size()) {
                    final Character cj = raw.get(jx);
                    if (Character.isWhitespace(cj)) {
                        jx++;
                        continue WHITE_SPACE_LA;
                    }
                    // found a non-whitespace
                    data.add(new Token(TokType.WHITE, ix, jx, extract(ix, jx).toString()));
                    ix = jx;
                    continue MAIN;
                }
                data.add(new Token(TokType.WHITE, ix, jx, extract(ix, jx+1).toString()));
                return Boolean.TRUE;
            }
            if (Character.isAlphabetic(ci)) {
                // log.log(Level.OFF, "a word");

                int jx = ix+1;
                WORD_LA:
                while (jx < raw.size()) {
                    final Character cj = raw.get(jx);
                    if (Character.isAlphabetic(cj))  {
                        jx++;
                        continue WORD_LA;
                    }
                    // found a non-alphabetic
                    data.add(new Token(TokType.WORD, ix, jx, extract(ix, jx).toString()));
                    ix = jx;
                    continue MAIN;
                }
                data.add(new Token(TokType.WORD, ix, jx, extract(ix, jx+1).toString()));
                return Boolean.TRUE;
            }
            // everything else is punctuation.
            // log.log(Level.OFF, "a punctuation");

            data.add(new Token(TokType.PUNC, ix, ix, extract(ix, ix).toString()));
            ix++;
            continue MAIN;
        }
        data.add(new Token(TokType.SBND, ix, ix, ""));
        return Boolean.TRUE;
    }

    /**
     * This version of load could be made faster by using reflection
     * to get at the backing store of the String.
     * However, as this is only used for testing use of charAt() should be fine.
     *
     * @param in
     * @return
     */
    public Boolean load(String in) {
        for (int ix=0; ix < in.length(); ix++) {
            raw.add(in.charAt(ix));
        }
        // log.log(Level.INFO, "loaded via string");
        return Boolean.TRUE;
    }

    /** see ILoadable */
    public Boolean load(Reader is) {
        try {
            int ch = is.read();
            while (ch != -1) {
                raw.add(new Character((char) ch));
                ch = is.read();
            }
            // log.log(Level.INFO, "loaded via reader");
            return Boolean.TRUE;
        } catch (java.io.IOException ex) {
            log.log(Level.SEVERE, "Failed to parse command line properties", ex);
            System.exit(2);
        }
        return Boolean.FALSE;
    }
}
