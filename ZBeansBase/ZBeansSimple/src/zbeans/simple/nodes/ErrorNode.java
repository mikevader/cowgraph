package zbeans.simple.nodes;

import java.beans.IntrospectionException;

import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;

/**
 * A special node for displaying an error, when children could not be loaded.
 * 
 * @author rbr
 */
public class ErrorNode extends BeanNode<Exception> {

    private final Exception exception;

    /**
     * Initialize an error node with the exception that caused this special error node.
     * 
     * @param exception
     *            the exception that occured when lazily loading the children (in the
     *            concrete {@link LazyChildFactory} implementation.
     * @throws IntrospectionException
     */
    public ErrorNode(final Exception exception) throws IntrospectionException {
        super(exception, Children.LEAF);
        this.exception = exception;
        setDisplayName("ERROR");
        setShortDescription("Error on loading, please try again to refresh later!");
    }

    /**
     * Get the exception that occured when trying to load or refresh this children.
     * 
     * @return the exception that occured in the children factory
     */
    public Exception getException() {
        return exception;
    }

}
