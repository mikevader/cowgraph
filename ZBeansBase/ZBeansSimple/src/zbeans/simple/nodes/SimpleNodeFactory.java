package zbeans.simple.nodes;

import java.beans.IntrospectionException;
import java.util.Collection;

import org.openide.nodes.Node;

/**
 * Interface for simple factories for creating children in a tree based on the
 * {@link SimpleNode} base classes.
 * 
 * @param <C>
 *            generic type of the child beans
 */
public interface SimpleNodeFactory<C> {

    /**
     * Return the (probably cached) child beans that represent the nodes. It is okay to
     * start a long running operation inside this method for loading the children, when
     * needed (for clean refresh). But usually the method should return the cached client
     * side model objects.
     * 
     * @param toPopulate
     *            the list to add the beans into
     * @return true if all beans are returned, false if the method has to be called again
     *         (useful for paged loading)
     * @throws Exception
     *             if loading failed somehow
     */
    boolean getChildBeans(final Collection<? super C> toPopulate) throws Exception;

    /**
     * Create a node for a bean.
     * 
     * @param bean
     *            the bean to create the node for.
     * @return the node
     * @throws IntrospectionException
     *             if creating the bean node failed (unexpected)
     */
    Node createNodeForChildBean(final C bean) throws IntrospectionException;

}
