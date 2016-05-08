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
     * Get a symbol name by index.
     */
    String get(int ix);

    /**
     * Get a symbol index by name.
     * @param name
     * @return
     */
    Integer get(String name);

    /**
     * Does the symbol table know this name?
     * @param name
     * @return
     */
    Boolean hasName(String name);
}
