package dr.fpe;

/**
 * Created by fred on 5/7/16.
 */
public interface IParser {
    Boolean parse(final ILexer lex);
    String asXmlString();
}
