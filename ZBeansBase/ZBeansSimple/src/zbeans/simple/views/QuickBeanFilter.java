package zbeans.simple.views;

import org.openide.nodes.Node;

/**
 * Simple filter for filtering beans of a specific class type inside of a
 * {@link SimpleOutlineView} .
 * 
 * @param <T>
 *            the bean type (model object bean that is wrapped in a {@link Node} of the
 *            view).
 */
public interface QuickBeanFilter<T> {

    /**
     * Decide if this bean (the lookup model object contained in a node of a
     * {@link EToolsOutlineView}'s row) should be accepted and therefore the row should be
     * visible in the view?
     * 
     * @param bean
     *            the bean to filter
     * @return true if the row should be displayed.
     */
    boolean accept(final T bean);

}
