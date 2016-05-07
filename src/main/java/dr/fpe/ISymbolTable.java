package dr.fpe;

/**
 * Created by fred on 5/7/16.
 */
public interface ISymbolTable {
    /**
     * Add a symbol, represented as a String, to the table.
     *
     * @param ch
     * @return
     */
    int add(final String ch);

    /**
     * Get a symbol by index.
     */
    String get(int ix);
}
