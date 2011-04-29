package zbeans.simple.nodes;

import zbeans.simple.beans.PropertyChangeObservable;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.Action;
import org.apache.log4j.Logger;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;

import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * A simple node for lazy loading of children.
 * 
 * If the beans implement the interface PropertyChangeObservable they will be observed
 * automatically for changes to update the node.
 * 
 * Comes with additional default convenience functionality:
 * <ol>
 * <li>Update node state whenever bean changes (through property change listener)</li>
 * <li>When the node is selected it provides its internal bean as lookup</li>
 * <li>Improved lazy loading functionality with possibility to clear the cached children
 * nodes for reloading them again on next access (with showing the "Please Wait"-node again in outline views).</li>
 * </ol>
 * 
 * @param <T>
 *            generic internal bean type
 * @param <C>
 *            generic type of child beans
 */
public abstract class SimpleNode<T, C> extends BeanNode<T> {

    private static final Logger LOGGER = Logger.getLogger(SimpleNode.class);
    
    /**
     * SIMPLE NODE FEATURE 1: CHILD REFRESH (probably long running again)
     * 1. Possibility to call refresh on the node for refreshing its children through the LazyChildFactory.
     * 2. Special clean refresh: Recreating the Lazy-Child-Factory again instead of calling refresh on it,
     * which is the only way to ensure that the "Please Wait..." node is displayed again if the refresh lasts longer, 
     * which is what you probably might want to have at least in some cases. 
     * Simple node leaves the decission up to you ... either you call refresh() or cleanRefresh(). 
     * On cleanRefresh the LazyChildFactory is recreated using the same passed SimpleChildFactory internaly.
     */
    private SimpleNodeFactory<C> childFactory;
    private LazyChildFactory<C> lazyChildFactory;    
    private volatile boolean cleanRefreshNeeded = false;
    
    /**
     * SIMPLE NODE FEATURE NR 2: AUTOMATIC UPDATE OF NODE STATE
     * If the contained bean just implements the PropertyChangeObservable interface 
     * for PropertyChangeListener support, then the bean will automatically be observed
     * and the node is updated on any changes.
     */
    private PropertyChangeListener beanPropertyChangeListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            updateNodeState(getBean());
        }
    };
    
    /**
     * The constructor for internal use. Only necessary to pass the first created lazy factory
     * too for setting it as member variable (needed for refreshes on nodes).
     * 
     * @param bean
     *            the bean
     * @param lazyChildFactory
     *            the initial lazy child factory (must be passed in super constructor)
     * @param childFactory
     *            the simple node factory (wrapped inside the lazy child factory, can be used to recreate the lazy child factory later on clean refreshes)
     * @param lookup
     *            the lookup
     * @throws IntrospectionException
     *             if something went wrong with Introspection in BeanNode constructor
     */
    private SimpleNode(final T bean, final LazyChildFactory<C> lazyChildFactory,
            final SimpleNodeFactory<C> childFactory, final Lookup lookup) throws IntrospectionException {
        super(bean, Children.create(lazyChildFactory, true), lookup);
        this.childFactory = childFactory;
        this.lazyChildFactory = lazyChildFactory;       
    }
    
    /**
     * Create a simple node with lazy loaded children.
     * 
     * @param bean
     *            the bean
     * @param childFactory
     *            the child factory to create the child nodes lazily when needed
     * @throws IntrospectionException
     */
    protected SimpleNode(final T bean, final SimpleNodeFactory<C> childFactory) throws IntrospectionException {
        this(bean, createLazyChildFactory(childFactory, bean), childFactory, Lookups.singleton(bean));
        observeBean(bean);
        updateNodeState(bean);
    }
    
        /**
     * Create a simple bean node as leaf node having no children at all.
     * 
     * @param bean
     *            the bean
     */
    protected SimpleNode(final T bean) throws IntrospectionException {
        super(bean, Children.LEAF, Lookups.singleton(bean));
        observeBean(bean);
        updateNodeState(bean);
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
                synchronized (lazyChildFactory) {
                    lazyChildFactory = createLazyChildFactory(childFactory, getBean());
                    setChildren(Children.create(lazyChildFactory, true));
                    cleanRefreshNeeded = false;
                }
            } else {
                lazyChildFactory.refresh();
            }
        }
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
     * Check whether there is currently some asynch loading of children still going on.
     * 
     * @return true if background thread is still loading any children.
     */
    public synchronized boolean isLoading() {
        return lazyChildFactory.isLoading();
    }
 
//    /**
//     * FIXME: Proably this does not make sense if running under Netbeans Platform ...
//     * But important for standalone apps.
//     * ===> Solution: make this default behaviour configurable (thorugh some kind of static switch)
//     */
//    @Override
//    public Action getPreferredAction() {
//        // Default is nothing
//        return null;
//    }

//    /**
//     * FIXME: Proably this does not make sense if running under Netbeans Platform ...
//     * But important for standalone apps.
//     */
//    @Override
//    public Action[] getActions(final boolean context) {
//        // turn actions off by default (super implementation tries to get netbaens
//        // platform actions otherwise)
//        return new Action[]{};
//    }

    /**
     * SIMPLE NODE FEATURE NR 2: AUTOMATIC UPDATE OF NODE STATE
     * Simply update this nodes state to any changes in the passed bean.
     * Subclasses only overwrite this method and define the state of the node depending on the state of the passed bean. 
     * The method is called in the node's constructor, as well as on all following changes to the bean.
     * 
     * @param bean
     *            the bean
     */
    protected abstract void updateNodeState(final T bean);
    
    /**
     * Start to observe the bean for property changes to update the bean state when
     * needed. Only registers the property change listener on the bean if it implements
     * {@link PropertyChangeObservable}.
     * 
     * @param bean
     *            the bean that has to be observed for changes
     */
    private void observeBean(final T bean) {
        if (bean instanceof PropertyChangeObservable) {
            PropertyChangeObservable obs = (PropertyChangeObservable) bean;
            obs.addPropertyChangeListener(beanPropertyChangeListener);
        }
    }    
    
    /**
     * Factory method to recreate the lazy child factory that delegates to the simple node factory, if needed.
     * The lazy factory has to be recreated if you want to do a clean refresh with displaying the "Please Wait" node again during refresh.
     */
    private static <T, C> LazyChildFactory<C> createLazyChildFactory(final SimpleNodeFactory<C> childFactory, final T bean) {  
         return new LazyChildFactory<C>() {
                    @Override
                    protected boolean createKeyBeans(final List<Object> toPopulate) throws Exception {
                        return childFactory.getChildBeans(toPopulate);
                    }

                    @Override
                    protected Node createNodeForKeyBean(final C bean) throws IntrospectionException {
                        return childFactory.createNodeForChildBean(bean);
                    }
                };
    }
    
}
