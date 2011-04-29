package zbeans.simple.tasks;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.RequestProcessor;

/**
 * Base class for simple background tasks with simple progress reporting.
 */
public abstract class SimpleTask implements Runnable {

    private String taskDescription;
    private ProgressHandle progress;

    /**
     * Create background task.
     * 
     * @param taskDescription
     *            a description of the task for progress reporting
     */
    public SimpleTask(final String taskDescription) {
        this.taskDescription = taskDescription;
    }

    @Override
    public void run() {
        try {
            progress = ProgressHandleFactory.createHandle(taskDescription);
            progress.start();
            progress.progress(taskDescription + "...");
            doInBackground();
        } catch (Throwable t) {
            failed(t);
        } finally {
            progress.finish();
        }
    }

    /**
     * Do the background work. Fail with an exception if not succeeded.
     */
    public abstract void doInBackground() throws Exception;

    /**
     * Handle exception during execution of background task.
     * Default implementation throws a runtime exception.
     * @param cause zhe occured exception
     */
    protected void failed(final Throwable cause) {
        throw new IllegalStateException("Background thread (SimpleTask) threw an exception.", cause);
    }

    public void execute() {
        RequestProcessor.getDefault().post(this);
    }
}