package zbeans.simple.nodes;


import java.beans.IntrospectionException;
import org.apache.log4j.Logger;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

/**
 * Special bean node that provides special lazy loading behaviour and functionality.
 * 
 * Actually this is currently quite tricky with the Nodes API (in Netbeans 6.9, may be
 * getting better in future releases ...), as not foreseen so far in this API.
 * 
 * Should be used with {@link SimpleOutlineView} which provides some special view
 * functionality for this kind of nodes (e.g. automatically reloading children for
 * expanded nodes of this type etc.).
 * 
 * @param <T>
 *            the bean type of this node
 */
public class LazyBeanNode<T> extends BeanNode<T> {

    private static final Logger LOGGER = Logger.getLogger(LazyBeanNode.class);

    private ChildFactoryCreator<?> childFactoryCreator;

    private LazyChildFactory<?> childFactory;

    private volatile boolean cleanRefreshNeeded = false;

    /**
     * The constructor for internal use. Only necessary to pass the first created factory
     * too for setting it as member variable.
     * 
     * @param bean
     *            the bean
     * @param childFactory
     *            the child factory
     * @param childFactoryCreator
     *            the child factory creator (for later recrating of factories when
     *            refreshing)
     * @param lookup
     *            the lookup
     * @throws IntrospectionException
     *             if something went wrong with Introspection in BeanNode constructor
     */
    private LazyBeanNode(final T bean, final LazyChildFactory<?> childFactory,
        final ChildFactoryCreator<?> childFactoryCreator, final Lookup lookup) throws IntrospectionException {
        super(bean, Children.create(childFactory, true), lookup);
        this.childFactory = childFactory;
        this.childFactoryCreator = childFactoryCreator;
    }

    /**
     * Create with an additional lookup to use in lookup.
     * 
     * @param bean
     *            the bean
     * @param childFactoryCreator
     *            the child factory creator (for later recreating of factories when
     *            refreshing)
     * @param lookup
     *            the lookup
     * @throws IntrospectionException
     *             if something went wrong with Introspection in BeanNode constructor
     */
    protected LazyBeanNode(final T bean, final ChildFactoryCreator<?> childFactoryCreator, final Lookup lookup)
        throws IntrospectionException {
        this(bean, childFactoryCreator.create(), childFactoryCreator, lookup);
    }

    /**
     * Create a leaf node with no children at all.
     * 
     * @param bean
     *            the bean
     * @param lookup
     *            the lookup
     * @throws IntrospectionException
     *             if something went wrong with Introspection in BeanNode constructor
     */
    protected LazyBeanNode(final T bean, final Lookup lookup) throws IntrospectionException {
        super(bean, Children.LEAF, lookup);
    }

    /**
     * Causes to refresh this nodes children asynchronously.
     * 
     * If {@link #setCleanRefreshNeeded(boolean)} has been set to true before, this will
     * also cause to recreate all child nodes and display a "Please Wait ..."-Node during
     * the loading. Otherwise no wait node will be displayed during loading.
     */
    public synchronized void refresh() {
        if (!isLeaf()) { // do nothing for leaf nodes having no children at all.
            if (cleanRefreshNeeded) {
                LOGGER.info("Doing a clean refresh on node " + getDisplayName());
                synchronized (childFactory) {
                    childFactory = childFactoryCreator.create();
                    setChildren(Children.create(childFactory, true));
                    cleanRefreshNeeded = false;
                }
            } else {
                childFactory.refresh();
            }
        }
    }

    /**
     * Check whether there is currently some asynch loading of children still going on.
     * 
     * @return true if background thread is still loading any children.
     */
    public synchronized boolean isLoading() {
        return childFactory.isLoading();
    }

    /**
     * Set that this node needs a clean refresh, recreating all child nodes on next
     * refresh, which will cause to display a "Please Wait..." node during the next
     * refresh of this node's children.
     * 
     * @param cleanRefreshNeeded
     *            true or false
     */
    public synchronized void setCleanRefreshNeeded(final boolean cleanRefreshNeeded) {
        this.cleanRefreshNeeded = cleanRefreshNeeded;
        // REMARK rolf: may be in this case we should immediately cause to throw away all
        // children and hang some kind of dummy factory into the children just to free
        // memory ... but currently this is no issue, just an idea for future improvement.
    }

    /**
     * Does the node need clean refresh on next access (recreating all child nodes with
     * displaying a wait node when the node gets expanded?).
     * 
     * @return true if this is the case.
     */
    public synchronized boolean isCleanRefreshNeeded() {
        return cleanRefreshNeeded;
    }

    /**
     * Interface for factory creators for this bean node class.
     */
    public interface ChildFactoryCreator<T> {

        /**
         * Create the factory for a bean node's children.
         * 
         * @return the factory to use.
         */
        LazyChildFactory<T> create();

    }

}
