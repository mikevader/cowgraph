package zbeans.simple.nodes;

import zbeans.simple.beans.ObservableBean;

import java.beans.IntrospectionException;

/**
 * A simple base implementation for a root node that lazily updates its children according
 * to a simple node factory.
 * 
 * @param <C>
 *            generic type of children beans this root will contain
 */
public final class RootNode<C> extends SimpleNode<ObservableBean, C> {

    /**
     * Create a simple root node with the factory to create the root children.
     * 
     * @param childFactory
     *            the child factory for the nodes.
     * @throws IntrospectionException
     *             if there was a problem creating the root node
     */
    private RootNode(final SimpleNodeFactory<C> childFactory) throws IntrospectionException {
        // Simply use an internal dummy bean as object for the root node
        super(new ObservableBean(), childFactory);
    }

    protected void updateNodeState(final ObservableBean bean) {
        // Nothing to update here. The root node has no dynamic state, only children.
    }

    /**
     * Utility factory method to create a root node from the first level child factory for
     * a simple tree based on the {@link SimpleNode} API.
     * 
     * @param childFactory
     *            the factory that crates the nodes and accesses the beans
     * @param <C>
     *            generic type of child beans to display as child of the root node
     * @return the root node (an empty node with usual SimpleNode
     */
    public static <C> RootNode<C> create(final SimpleNodeFactory<C> childFactory) {
        try {
            return new RootNode<C>(childFactory);
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException("Failed to create root node for a nodes model,"
                + " because of an unexpected introspection exception.",
                e);
        }
    }

}
