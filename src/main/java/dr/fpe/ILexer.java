package dr.fpe;

/**
 * Created by fred on 5/7/16.
 */

import java.io.Reader;
import java.util.List;

/**
 * The interface for depencency-injection.
 */
public interface ILexer {
    Boolean load(Reader is);
    Boolean load(String in);
    Boolean analyze();
    List<Lexer.Token> getTokenList();
    SymbolTable getSymbolTable();
}
