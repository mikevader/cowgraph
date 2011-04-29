package zbeans.simple.views;

import javax.swing.ActionMap;

import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;

/**
 * Wrapper for an explorer manager to use with an SimpleOutlineView or other explorer views for
 * convenience. Makes it easier to register for selection and to find some nodes for beans.
 * 
 * Thsi class is especially very helpfull when the OutlineView is used without the Netbeans Platform, where you can not rely on Netbean's global context for getting selections. In thsi case the convenience methods for selections are usefull.
 * 
 * This class is needed, to encapsulate some functionality, that is special, because we
 * can not rely on Netbeans Platform global context.
 */
public class SimpleExplorerManager implements ExplorerManager.Provider {

    private ExplorerManager explorerManager = new ExplorerManager();
    private Lookup lookup;

    /**
     * Initialize with an empty action map to use.
     */
    public SimpleExplorerManager() {
        lookup = ExplorerUtils.createLookup(explorerManager, new ActionMap());
    }

    /**
     * Initialize with an action map of actions to use.
     * 
     * @param actionMap
     *            map of actions to associate with this explorer manager
     */
    public SimpleExplorerManager(final ActionMap actionMap) {
        lookup = ExplorerUtils.createLookup(explorerManager, actionMap);
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    /**
     * Get the lookup of this explorer manager. This is used to register for specific
     * selections on this explorer manager etc.
     * 
     * If we were running under real Netbeans Platform we would use
     * Utilities.actionsGlobalContext() instead. But this seems not to work without
     * running under a full Netbeans Platform.
     * 
     * @return the lookup for this manager
     */
    public Lookup getLookup() {
        return lookup;
    }

    /**
     * Set the root node for this explorer manager.
     * 
     * @param root
     *            the root to use
     */
    public void setRootContext(final Node root) {
        explorerManager.setRootContext(root);
    }

    /**
     * Get the root node.
     */
    public Node getRootContext() {
        return explorerManager.getRootContext();
    }

    /**
     * Get a lookup result for listening to selections of the passed type of selection
     * objects.
     * 
     * Example for listening to selections of the lookup objects of element bean type
     * ExampleBean:
     * 
     * <pre>
     * final Lookup.Result&lt;ExampleBean&gt; selection = etExplorerManager.lookupResult(ExampleBean.class);
     *  selection.addLookupListener(new LookupListener() {
     *       &#064;Override
     *       public void resultChanged(final LookupEvent ev) {
     *            Collection&lt;?ExampleBean&gt; selectedElements = selection.allInstances();
     *           ...
     *       }
     *  });
     * </pre>
     * 
     * If we were running under real Netbeans Platform we would use
     * Utilities.actionsGlobalContext() instead. But this seems not to work without
     * running under a full Netbeans Platform.
     * 
     * @param <T>
     *            the generic selection type to listen for
     * @param clazz
     *            the class of the lookup object to listen for
     * @return the lookup result for this kind of lookup class
     */
    public <T> Result<T> lookupResult(final Class<T> clazz) {
        return lookup.lookupResult(clazz);
    }

    /**
     * Find a node in the passed nodes with the appropriate lookup object.
     * 
     * @param nodes
     *            the nodes to search
     * @param lookupObj
     *            the lookup object to look for
     * @return the found node
     */
    public static Node findNode(final Node[] nodes, final Object lookupObj) {
        for (Node node : nodes) {
            Object nodeObj = node.getLookup().lookup(lookupObj.getClass());
            if (lookupObj.equals(nodeObj)) {
                return node;
            }
        }
        return null;
    }
}
