package zbeans.simple.views;

import zbeans.simple.nodes.LazyBeanNode;
import zbeans.simple.nodes.SimpleNode;
import zbeans.simple.tasks.SimpleTask;

import java.beans.PropertyVetoException;
import java.text.Collator;
import java.util.Comparator;

import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.JTableHeader;

import org.apache.log4j.Logger;
import org.netbeans.swing.etable.ETableColumn;
import org.netbeans.swing.etable.ETableColumnModel;
import org.netbeans.swing.etable.QuickFilter;
import org.netbeans.swing.outline.Outline;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.OutlineView;
import org.openide.explorer.view.Visualizer;
import org.openide.nodes.Node;

/**
 * The etools specific outline view providing some additional special functionality and
 * tree behaviour and some convenience API.
 * 
 * Currently the following is different compared to a usual {@link OutlineView}:
 * <ul>
 * <li>
 * <b>Reload Children whenever a node is expanded:</b><br/>
 * When a node is expanded and the children have already been loaded before (and are
 * therefore probably outdated), this triggers a clean reload of the children. This means
 * that the cached childrens won't be shown to the user. Instead the user will see a
 * "Please Wait ..." node and the children are reloaded again (same behaviour as on first
 * opening). But this only works for nodes that are based on {@link LazyBeanNode}. Other
 * nodes are only loaded once into the nodes model.</li>
 * <li><b>Better Sorting setup for first column:</b><br/>
 * The first column is setup to be sorted using a Collator (instead of simple Stirng
 * comparison of display names) and the nodes have the ability to override this behaviour
 * by implementing a Comparator. To setup the first column sorted programmatically just
 * call the method {@link #setColumnSorted(0, true}</li>
 * </ul>
 * 
 * For providing the model for the outline view, one of its parent panels has to implement
 * the interface {@link ExplorerManager.Provider}
 */
public final class SimpleOutlineView extends OutlineView {
    
    private static final Logger LOGGER = Logger.getLogger(SimpleOutlineView.class);
    private static final long serialVersionUID = 1L;

    /**
     * Create an outline view for displaying a simple tree, without any columns. This
     * disables the column headers and displaying the root node by default.
     * 
     * @return an outline view for trees.
     */
    public static SimpleOutlineView createTree() {
        SimpleOutlineView treeView = new SimpleOutlineView();
        treeView.setRootVisible(false);
        treeView.setTableHeader(null);
        return treeView;
    }

    /**
     * Create an outline view for displaying a tree with some columns behind it (also
     * displaying column headers). This disables displaying of the root node too, because
     * usually this is just the container for all table row nodes.
     * 
     * @return an outline view for tree tables with columns.
     */
    public static SimpleOutlineView createTreeTable() {
        SimpleOutlineView tableView = new SimpleOutlineView();
        tableView.setRootVisible(false);
        return tableView;
    }

    /**
     * Initialize with some default customizations. See class comment for documentation of
     * special customization settings/behaviour of {@link EToolsOutlineView}.
     * 
     * This constructor is private, since the {@link EToolsOutlineView} provides factory
     * methods for convenience to setup often used configurations of a
     * {@link EToolsOutlineView}.
     */
    private SimpleOutlineView() {
        super();

        // Set tree expansion for refreshing children when node is collapsed and expanded
        // again.
        addTreeExpansionListener(new LazyLoadChildNodesOnTreeExpansion());

        // Let the first column be sorted by a Collator instead of using ugly string
        // comparison ordering.
        // Default outline view is using String comparison for the display texts on the
        // first column in any case, which is not very nice at all!! :-(
        // This outline view default behaviour has two disadvantages:
        // 1. First column is ordered like this: A, B, C, ..., a, b, c, .... instead of A,
        // a, B, b, C, c,... :-(
        // 2. Nodes have no possibility to overwrite this behaviour by implementing the
        // Comparable interface.
        // The following code ensures the sorting of the first column using a Collator and
        // gives the Nodes the possibility to define a different Comparison by
        // implementing Comparable.
        // Only has an effect as soon as sorting on the first column is turned on using
        // setColumnSorted(0, true)
        getColumn(0).setNestedComparator(new Comparator<Node>()  {

            private Collator collator = Collator.getInstance();
            
            @Override
            public int compare(final Node n1, final Node n2) {
                // Compare comparable nodes if possible.
                if ((n1 instanceof Comparable) && (n1.getClass().isAssignableFrom(n2.getClass()))) {
                    @SuppressWarnings("unchecked")
                    Comparable<Node> cn1 = (Comparable<Node>) n1;
                    return cn1.compareTo(n2);
                } else if ((n2 instanceof Comparable) && (n2.getClass().isAssignableFrom(n1.getClass()))) {
                    @SuppressWarnings("unchecked")
                    Comparable<Node> cn2 = (Comparable<Node>) n2;
                    return -cn2.compareTo(n1);
                } else {
                    // Otherwise compare the display names using the Collator
                    return collator.compare(n1.getDisplayName(), n2.getDisplayName());
                }
            }
        });
    }

    /**
     * Set up a quick filter for doing simple filtering on View to filter out Rows/Nodes
     * according to their lookup element (for {@link SimpleNode}'s this is the contained
     * model bean of the node). Only if this lookup value conforms to the passed bean
     * class type, then the filter is applied on this lookup value (usually the node's
     * model bean). All other nodes (not having such a bean as lookup) will not be
     * filtered at all and stay visible in the view in any case.
     * 
     * @param filteredBeanClass
     *            only nodes having a lookup element that conforms to this class are
     *            filtered.
     * @param <T>
     *            generic bean type that is expected as a lookup on the node
     */
    public <T> void setQuickBeanFilter(final Class<T> filteredBeanClass, final QuickBeanFilter<T> filter) {
        getOutline().setQuickFilter(0, new QuickFilter()  {
            @Override
            public boolean accept(final Object value) {
                Node node = (Node) value;
                T bean = node.getLookup().lookup(filteredBeanClass);
                if (bean != null) {
                    return filter.accept(bean);
                } else {
                    return true;
                }
            }
        });
    }

    /**
     * Convenience method for setting alphabetical sorting on first column. This method
     * sets prority one ordering on a column.
     */
    public void setColumnSorted(final int colIndex, final boolean ascending) {
        ETableColumnModel etcm = (ETableColumnModel) getOutline().getColumnModel();
        etcm.setColumnSorted(getColumn(colIndex), ascending, 1);
    }

    /**
     * Access the column model of a column. This is needed to setup special per column
     * properties, e.g. like sorting etc.
     */
    public ETableColumn getColumn(final int index) {
        ETableColumnModel etcm = (ETableColumnModel) getOutline().getColumnModel();
        return (ETableColumn) etcm.getColumn(index);
    }

    /**
     * Set the table header to use for the {@link Outline}. Setting this to null would
     * disable any column headers.
     * 
     * @param tableHeader
     *            the table header
     */
    public void setTableHeader(final JTableHeader tableHeader) {
        
        getOutline().setTableHeader(tableHeader);
    }

    /**
     * Specify whether to display the root node or not.
     * 
     * @param visible
     *            the visibility to set
     */
    public void setRootVisible(final boolean visible) {
        getOutline().setRootVisible(visible);
    }
    
    /**
     * Try to select asynchronously (as soon as possible, but not blocking) the node
     * representing the passed lookup bean object from the passed children's nodes. The
     * selection is done asynch using invokeLater as soon as possible. The implementation
     * is trying to wait until the node (which might be asynchronous created) is realy
     * there inside the passed {@link ExplorerManager}.
     * 
     * Will cause to expand all nodes on the way.
     * 
     * @param explorerManager
     *            the explorer manager on which to do the selection
     * @param lookupObjPath
     *            the model bean for which to select the node (domain object set as bean
     *            on the {@link SimpleNode}) or multiple nodes which are a path to the
     *            node to select.
     */
    public void selectNode(final SimpleExplorerManager explorerManager, final Object... lookupObjPath) {
        selectNode(explorerManager.getExplorerManager(), lookupObjPath);
    }    

    /**
     * Try to select asynchronously (as soon as possible, but not blocking) the node
     * representing the passed lookup bean object from the passed children's nodes. The
     * selection is done asynch using invokeLater as soon as possible. The implementation
     * is trying to wait until the node (which might be asynchronous created) is realy
     * there inside the passed {@link ExplorerManager}.
     * 
     * Will cause to expand all nodes on the way.
     * 
     * @param explorerManager
     *            the explorer manager on which to do the selection
     * @param lookupObjPath
     *            the model bean for which to select the node (domain object set as bean
     *            on the {@link SimpleNode}) or multiple nodes which are a path to the
     *            node to select.
     */
    public void selectNode(final ExplorerManager explorerManager, final Object... lookupObjPath) {
        // Only do this after all other pending UI updates.
        SwingUtilities.invokeLater(new Runnable()  {
            @Override
            public void run() {
                // Find the node and select it:
                // This has to be called in background task for not letting UI freeze
                // while probably waiting on nodes (beeing loaded and/or expanded).
                SimpleTask nodeSelectionTask = new SimpleTask("Selecting node")  {
                    @Override
                    public void doInBackground() throws Exception {
                        selectNodeRecursively(0, explorerManager, explorerManager.getRootContext(),
                                lookupObjPath);
                    }
                };
                nodeSelectionTask.execute();
            }
        });
    }
    
    private void selectNodeRecursively(final int index, final ExplorerManager explorerManager,
            final Node parentNode, final Object[] lookupObjPath) throws Exception {
        
        final Object lookupObj = lookupObjPath[index];
        final int nextIndex = index + 1;

        // 1. Find the node for current object.
        // first try: without blocking (if not necessary)
        Node[] nodes = parentNode.getChildren().getNodes(false);
        Node node = null;
        if (nodes != null) {
            node = SimpleExplorerManager.findNode(nodes, lookupObj);
        }
        // second try: waiting on all subnodes beeing loaded ... (this might blcok if a
        // refresh is currently running)
        if (node == null) {
            
            nodes = parentNode.getChildren().getNodes(true);
            node = SimpleExplorerManager.findNode(nodes, lookupObj);
        }
        // node found now?
        if (node == null) {
            throw new IllegalArgumentException("SELECTION: " + lookupObj.getClass().getSimpleName()
                    + " object to select was not found in nodes.");
        }
        final Node finalNode = node;

        // 2. Either expand this node (recursively) or just select it.
        if (nextIndex < lookupObjPath.length) {
            // Not the last node on the path:
            // Expand this node first, and continue with recursive selection.
            SwingUtilities.invokeAndWait(new Runnable()  {

                @Override
                public void run() {
                    SimpleOutlineView.this.expandNode(finalNode);
                }
            });
            
            selectNodeRecursively(nextIndex, explorerManager, node, lookupObjPath);
        } else {
            // Last node: Select it (End of recursion):
            // The selection is called later, because the getNodes(true) call
            // could have triggered a update of the nodes in the EcplorerManager
            // for which we have to wait first (otherwise selection fails).
            // And furthermore we should call this in UI thread better anyway.
            SwingUtilities.invokeLater(new Runnable()  {

                @Override
                public void run() {
                    try {
                        explorerManager.setSelectedNodes(new Node[]{finalNode});
                    } catch (PropertyVetoException e) {
                        LOGGER.error("SELECTION: Object to select could not be selected in tree.", e);
                    }
                }
            });
        }
        
    }

    /**
     * A tree expansion listener that will trigger a recreation of childs through its
     * child factory on re-expansion of a node (causes to recreate the ChildFactory for
     * this purpose.).
     */
    private class LazyLoadChildNodesOnTreeExpansion implements TreeExpansionListener {

        /**
         * A flag for avoiding endless recursion inside the expansion listener that could
         * trigger collapsing and (re-)expanding nodes again.
         */
        private boolean inRecursion = false;
        
        @Override
        public synchronized void treeCollapsed(final TreeExpansionEvent event) {
            Node eventNode = Visualizer.findNode(event.getPath().getLastPathComponent());
            if (!inRecursion && eventNode instanceof LazyBeanNode) { // avoid endless
                // recursion
                final LazyBeanNode<?> node = (LazyBeanNode<?>) eventNode;
                node.setCleanRefreshNeeded(true);
            }
        }
        
        @Override
        public synchronized void treeExpanded(final TreeExpansionEvent event) {
            Node eventNode = Visualizer.findNode(event.getPath().getLastPathComponent());
            if (!inRecursion && eventNode instanceof LazyBeanNode) {
                final LazyBeanNode<?> node = (LazyBeanNode<?>) eventNode;
                node.refresh();
                if (!isExpanded(node)) {
                    // Seems that the refresh caused to collapse, re-expand again and
                    // avoid recursion in this listener!
                    inRecursion = true;
                    try {
                        expandNode(node);
                    } finally {
                        inRecursion = false;
                    }
                }
            }
            
        }
    }    

}
