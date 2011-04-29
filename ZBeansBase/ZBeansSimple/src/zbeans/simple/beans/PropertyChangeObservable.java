package zbeans.simple.beans;

import java.beans.PropertyChangeListener;

/**
 * Interface for beans that support listening to property changes.
 */
public interface PropertyChangeObservable {

    /**
     * Register a listener for all property change events on this object.
     * 
     * @param listener
     *            the listener
     */
    void addPropertyChangeListener(final PropertyChangeListener listener);

    /**
     * Register a listener for change events on a specific property of this object.
     * 
     * @param propertyName
     *            the name of the property
     * @param listener
     *            the listener
     */
    void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener);

    /**
     * Remove a listener for all property change events on this object.
     * 
     * @param listener
     *            the listener
     */
    void removePropertyChangeListener(final PropertyChangeListener listener);

    /**
     * Remove a listener for change events on a specific property of this object.
     * 
     * @param propertyName
     *            the name of the property
     * @param listener
     *            the listener
     */
    void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener);

}
