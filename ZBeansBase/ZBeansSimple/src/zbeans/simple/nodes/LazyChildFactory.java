package zbeans.simple.nodes;

import java.beans.IntrospectionException;
import java.util.List;

import org.apache.log4j.Logger;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 * A convenience base class for child factories, encapsulates some common behaviour for
 * all child factories, especially concerned with long running methods to fetch the childs
 * and with refreshing.
 * 
 * Ensures to show progress during the creation of children using Progress API.
 * 
 * Furthermore some common exception handling (no need to catch IntrospectionExceptions in
 * all factories and then failing anyway).
 * 
 * @param <BeanType>
 *            the type of model objects to use as keys for the child nodes
 */
public abstract class LazyChildFactory<BeanType> extends ChildFactory<Object> {

    private static final Logger LOGGER = Logger.getLogger(LazyChildFactory.class);

    private static volatile int countFactories = 0;

    private static volatile int loadingId = 0;

    private static volatile int countLoading = 0;

    private volatile int factoryId = countFactories++;

    private volatile ProgressHandle progress = null;

    private String progressTaskName = "Creating Tree Nodes.";

    private String progressTaskMessage = "Creating Tree Nodes.";

    /**
     * This method has to be implemented by concrete factories to create the keys.
     * 
     * @param toPopulate
     *            the list of childs to add created key beans into.
     * @return true if the list of keys has been completely populated, false if the list
     *         has only been partially populated and this method should be called again to
     *         batch more keys
     * @throws Exception
     *             if key beans could not be created for some reason, in this case a
     *             special {@link ErrorNode} will be created by this factory to
     *             display this error in the tree.
     */
    protected abstract boolean createKeyBeans(List<Object> toPopulate) throws Exception;

    /**
     * Convenience method to create a node for a key without having to catch any possible
     * {@link IntrospectionException}, to be implemented by subclasses. Is called by
     * {@link #createNodeForKey(Object)}.
     * 
     * @param key
     *            the key to create the node for
     * @return the created node for that key.
     * @throws IntrospectionException
     *             if the creation of the node causes some introspection exception.
     */
    protected abstract Node createNodeForKeyBean(BeanType key) throws IntrospectionException;

    /**
     * {@inheritDoc}.
     * 
     * Default implementation, calls the convenience method {@link #createNode(Object)}
     * which has to be implemented by subclasses.
     */
    @Override
    protected final Node createNodeForKey(final Object key) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating node for key " + key.toString());
        }
        try {
            if (key instanceof Exception) {
                // Create a special error node, there was an exception when creating this
                // key bean object (e.g. exception on loading from service or db).
                return new ErrorNode((Exception) key);
            } else {
                @SuppressWarnings("unchecked")
                BeanType keyBean = (BeanType) key;
                return createNodeForKeyBean(keyBean);
            }
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException("Failed to create node for " + key.getClass().getName()
                + ", because of unexpected introspection exception.", e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * This implementation ensures that the progress bar is displayed for long running
     * loading task (and not only the wait node) using the Progress API from Netbeans.
     */
    @Override
    protected final boolean createKeys(final List<Object> toPopulate) {

        startLoading();
        LOGGER.debug("Creating keys ...");

        boolean done;
        try {
            done = createKeyBeans(toPopulate);
        } catch (Throwable e) {
            // Use exception as the key bean object (causes to create a
            // LazyErrorNode).
            LOGGER.error(
                "Could not create children for a tree node, adding according error node to the tree.", e);
            toPopulate.add(e);
            done = true;
        }

        if (done) {
            stopLoading();
        }
        LOGGER.debug("Creating keys done.");
        return done;
    }

    /**
     * Set the display name to display in progress bar for the currently running loading
     * task. Also sets the progress message to the same value. Use {@link #progress}
     * during task running to specify a more detailed message.
     * 
     * @param name
     *            the name of the currently running loading task to set
     */
    public synchronized void setProgressTaskName(final String name) {
        progressTaskName = name;
        if (progress != null) {
            progress.setDisplayName(progressTaskName);
        }
        progress(name);
    }

    /**
     * Set a progress message to display progress of the task to the user.
     * 
     * @param message
     *            the message to set
     */
    protected synchronized void progress(final String message) {
        progressTaskMessage = message;
        if (progress != null) {
            progress.progress(message);
        }
    }

    /**
     * Triggers asynch refreshing of children.
     */
    public synchronized void refresh() {
        // Call the refresh in any case! The asynch child laoding mechanism seems to
        // already take care of only doing one refresh on multiple calls.
        refresh(false);
    }
    
        /**
     * Check whether the Factory is currently (asynchronously) loading its children (in a
     * background thread).
     */
    public synchronized boolean isLoading() {
        return progress != null;
    }

    /**
     * Thread safe going into loadig state and start monitoring progress. Only starts if
     * not beeing started before.
     * 
     * @return false if the component is already in loading state (there is probably
     *         another thread that is already loading), true otherwise.
     */
    private synchronized boolean startLoading() {
        if (!isLoading()) {
            countLoading++;
            loadingId++;
            LOGGER.debug("Loading started: running = " + countLoading + "loadingId = " + loadingId
                + ", factoryId = " + factoryId);
            progress = ProgressHandleFactory.createHandle(progressTaskName);
            progress.start();
            progress.progress(progressTaskMessage);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Thread safe stopping of monitoring the loading.
     * 
     * @return false if the component was not in loading state anyway.
     */
    private synchronized boolean stopLoading() {
        if (isLoading()) {
            countLoading--;
            progress.finish();
            progress = null;
            LOGGER.debug("Loading stopped: running = " + countLoading + "loadingId = " + loadingId
                + ", factoryId = " + factoryId);
            return true;
        } else {
            return false;
        }
    }



}
