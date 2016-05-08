package dr.fpe;

/**
 * Created by fred on 5/8/16.
 */
public interface INamedEntityDictionary {
    /**
     * Add a new named-entity to the tree.
     *
     * @param rawName
     * @return
     */
    Boolean add(final String rawName);

    NamedEntityTree.Node getTop();
}
