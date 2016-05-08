package dr.fpe;

import java.io.Reader;

/**
 * Created by fred on 5/8/16.
 */
public interface ILoadable {
    /**
    * This is the reader.
    * If the input is a string then it can be wrapped in a StringReader.
    *
    * @param rdr
    * @return did it work?
     */
    Boolean load(Reader rdr);
}
