package dr.fpe;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by fred on 5/7/16.
 */
public class Parser implements IParser {
    private static final Logger log = Logger.getLogger(Cli.class.getName());

    final public List<Sentence> sentenceList = new ArrayList<Sentence>();
    private SymbolTable st;

    public Parser() {}


    /**
     * For this problem we only need to keep the words around.
     */
    public class Word {
        final public Integer word;

        public Word(final IToken tok) {
            this.word = tok.getIndex();
        }
    }

    /**
     * All words must belong to a sentence.
     */
    public class Sentence {
        public Sentence() {}
        public final List<Word> wordList = new ArrayList<Word>();

        /**
         * Include a word in the sentence.
         *
         * @param token
         * @return
         */
        public Boolean addWord(final Lexer.Token token) {
            wordList.add(new Word(token));
            return Boolean.TRUE;
        }
    }


    /**
     * Extract the tree.
     *
     * @param lex
     * @return
     */
    public Boolean parse(final ILexer lex) {
        this.st = lex.getSymbolTable();
        Sentence working_sentence = new Sentence();
        sentenceList.add(working_sentence);

        for (Lexer.Token tok : lex.getTokenList()) {
            switch (tok.tt) {
                case WORD:
                    working_sentence.addWord(tok);
                    break;
                case SBND:
                    final Sentence s = new Sentence();
                    sentenceList.add(s);
                    working_sentence = s;
                    break;
                default:

            }
        }
        return Boolean.TRUE;
    }

    /**
     * Produce a string that is well formed xml.
     * Some escaping may be required.
     * I could use the javax XML writer but this is good enough for this project.
     * @return
     */
    public String asXmlString() {

        final StringBuilder sb =
                new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<nlp>\n");
        for (Sentence s : sentenceList) {
            sb.append("<s>");
            for (Word w : s.wordList) {
                final String word = st.get(w.word);
                sb.append("<w>")
                  .append(StringEscapeUtils.escapeXml10(word))
                  .append("</w>");
            }
            sb.append("</s>");
            sb.append('\n');
        }
        sb.append("</nlp>\n");
        return sb.toString();
    }
}
