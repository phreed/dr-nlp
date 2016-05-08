/**
 * Created by fred on 5/7/16.
 */
package dr.fpe;

/**
 * Recognize names in sentences.
 */
public interface IRecognizer {


    /**
     * * The recognize method looks in the sentence for names from the named-entity-tree.
     *
     * @param net
     * @param fx The index for the source file.
     * @param st
     * @param psr
     * @return
     */
    Boolean recognize(final IReporter reporter, final INamedEntityDictionary net, int fx, final SymbolTable st, final Parser psr);
}
