

/**
 * Created by fred on 5/8/16.
 */
package dr.fpe;

/**
 * The reporter is an accumulator of sorts that reduces
 * the results into a single object.
 */
public interface IReducer {

    /**
     * Handle a message.
     *
     * @param update
     * @return
     */
    Boolean report(final NamedEntityTree.Update update);
}
