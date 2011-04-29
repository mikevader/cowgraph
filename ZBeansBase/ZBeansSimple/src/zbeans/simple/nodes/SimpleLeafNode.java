package zbeans.simple.nodes;

import java.beans.IntrospectionException;

/**
 * Simple node base class for nodes having no children at all.
 * 
 * @param <T>
 *            generic bean type that this node contains
 */
public abstract class SimpleLeafNode<T> extends SimpleNode<T, Void> {

    /**
     * Create a leaf node with a bean and no children.
     * 
     * @param bean
     *            the bean
     * @throws IntrospectionException
     *             if there was a introspection exception
     */
    public SimpleLeafNode(final T bean) throws IntrospectionException {
        super(bean);
    }

}
