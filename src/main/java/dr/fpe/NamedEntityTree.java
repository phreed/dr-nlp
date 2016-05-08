/**
 * Created by fred on 5/7/16.
 */
package dr.fpe;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class to contain the named-entities.
 * The parsing here is presumed to be simple.
 * The hyphens are removed.
 * This is due to an ambiguity in the meaning of
 * hyphens in the input text.
 * See doc/plan.md for more discussion.
 *
 *
 * This is a simple implementation of a PreTrie.
 * I have written a token based pretrie in the past.
 * It is here...
 * https://github.com/vuisis-ammo/ammo-core/tree/master/AmmoCore/src/main/java/edu/vu/isis/ammo/pretrie
 * But that is to much work for this simple problem.
 *
 */
public class NamedEntityTree implements ILoadable, INamedEntityDictionary, IRecognizer {
    private static final Logger log = Logger.getLogger(Cli.class.getName());

    final public Node tree = new Node();

    public NamedEntityTree() {}

    /** see INamedEntityDictionary */
    public Node getTop() {
        return tree;
    }

    public class Indices {
        // file source of occurrence
        final public int fx;
        // sentence index of occurrence
        final public int sx;
        // first matching word in sentence
        final public int ix;
        // last matching word in sentence
        final public int jx;

        public Indices(int fx, int sx, int ix, int jx) {
            this.fx = fx;
            this.sx = sx;
            this.ix = ix;
            this.jx = jx;
        }
    }
    /**
     * It is possible for an internal node to
     * designate a named-entity, not just leaf nodes.
     * Plus it is just more obvious.
     */
    public class Node {
        final public String name;
        final Node parent;
        final public Map<String, Node> children;
        Boolean terminal;
        final public List<Indices> recognized = new ArrayList<Indices>();

        public Node() {
            this.name = "ROOT";
            this.parent = null;
            this.children = new HashMap<String, Node>();
            this.terminal = Boolean.FALSE;
        }

        public Node(final Node parent, int ix, final String[] fullName) {
            this.name = fullName[ix];
            this.parent = parent;
            this.children = new HashMap<String, Node>();
            this.terminal = (ix >= (fullName.length - 1));
        }

        public Boolean hasNamed(final String name) {
            return this.children.containsKey(name);
        }

        public Node getNamed(final String name) {
            return this.children.get(name);
        }

        public Boolean recognized(final Indices ixs) {
            recognized.add(ixs);
            return Boolean.TRUE;
        }

        public int occurenceCount() {
            return recognized.size();
        }

        public Boolean isRoot() {
            return this.parent == null;
        }

        /**
         * Add a child node to the current node.
         * The index indicates the current nodes name.
         * @param ix the index of the item in the fullname being added.
         * @param fullName the full name for the entity.
         * @return success?
         */
        public Boolean addChild(int ix, final String[] fullName) {
            /*
            log.log(Level.INFO, new StringBuffer("addChild ")
                    .append(ix).append(" name " )
                    .append(Arrays.toString(fullName))
                    .toString());
            */

            final Node child = new Node(this, ix, fullName);
            children.put(fullName[ix], child);
            if (child.terminal) {
                return Boolean.TRUE;
            }
            child.addChild(ix+1, fullName);
            return Boolean.TRUE;
        }

        public String toString() {
            if (this.parent == null) {
                // log.log(Level.INFO, "top");
                return name;
            }
            return new StringBuffer()
                    .append(this.parent.toString())
                    .append(':').append(this.name)
                    .toString();
        }

        public Boolean walk(Accumulator ac) {
            ac.assimilate(this);
            for (Node child : this.children.values()) {
                child.walk(ac);
            }
            return Boolean.TRUE;
        }
    }

    /**
     * An accumulator to build a list of terminal entity names.
     */
    private class Accumulator {
        final public StringBuffer sb = new StringBuffer();

        public Accumulator() {}
        public Boolean assimilate(final Node node) {
            if (! node.terminal) {
                return Boolean.TRUE;
            }
            sb.append(node).append('\n');
            return Boolean.TRUE;
        }
    }


    /** see INamedEntityDictionary */
    public Boolean add(final String rawName) {
        // note these are different hyphen-like objects
        // the Intellij error is apparently for JavaScript ???
        final String[] splitName = rawName.trim().split("[\\s-–]");

        Node workingNode = tree;
        for (int ix=0; ix < splitName.length; ix++) {
            final String name = splitName[ix];
            if (workingNode.children.containsKey(name)) {
                workingNode = workingNode.children.get(name);
                if (ix == (splitName.length - 1)) {
                    workingNode.terminal = Boolean.TRUE;
                }
            } else {
                workingNode.addChild(ix, splitName);
                break;
            }
        }
        return Boolean.TRUE;
    }

    /** see IRecognizer */
    /**
     * Find the longest matching sequence in the named-entity-tree.
     * Walk up from there looking for a terminal node.
     */
    public Boolean recognize(final IReporter reporter, final INamedEntityDictionary net, int fx, final SymbolTable st, final Parser psr) {
        for (int sx=0; sx < psr.sentenceList.size(); sx++) {
            Parser.Sentence sentence = psr.sentenceList.get(sx);

            MAIN:
            for (int ix = 0; ix < sentence.wordList.size(); ix++) {
                Parser.Word word = sentence.wordList.get(ix);
                String tokName = st.get(word.word);
                Node node = net.getTop();
                int jx;
                LOOK_AHEAD:
                for (jx = ix+1; node.hasNamed(tokName) && (jx < sentence.wordList.size()); jx++) {
                    node = node.getNamed(tokName);
                    word = sentence.wordList.get(jx);
                    tokName = st.get(word.word);
                }
                // log.log(Level.INFO, new StringBuffer("recognize ").append(node)
                //        .append( '<').append(node.terminal).append('>').toString());

                Node termNode = node;
                if (termNode == null) {
                    continue MAIN;
                }
                while (!termNode.terminal && !termNode.isRoot() ) {
                    termNode = termNode.parent;
                }
                if (termNode.terminal) {

                    termNode.recognized(new Indices(fx, sx, ix, jx));
                    if (termNode.occurenceCount() < 2) {
                        reporter.report(termNode.toString());
                    }
                }
            }
        }
        return Boolean.TRUE;
    }


    /** see ILoadable */
    public Boolean load(Reader rdr) {
        try {
            String thisLine = null;
            final BufferedReader brdr = new BufferedReader( rdr );
            while ((thisLine = brdr.readLine()) != null) {
                if (thisLine.trim().isEmpty()) continue;

                this.add(thisLine);
            }
            // log.log(Level.INFO, "loaded names via reader");
            return Boolean.TRUE;
        } catch (java.io.IOException ex) {
            log.log(Level.SEVERE, "Failed to read named entity file", ex);
            System.exit(2);
        }
        return Boolean.FALSE;
    }


    public String toString() {
        final Accumulator ac = new Accumulator();
        this.tree.walk(ac);
        return new StringBuffer("named = ")
                .append(ac.sb)
                .toString();
    }
}
