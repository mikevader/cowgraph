package zbeans.simple.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base class for beans that can be observed using {@link PropertyChangeListener}s.
 * 
 * Use {@link #firePropertyChange(String, Object, Object)} and other fire-methods in
 * subclasses to fire change events on properties changed.
 */
public class ObservableBean implements PropertyChangeObservable {

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {        
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * Fire property change event.
     * 
     * @param propertyName
     *            -
     * @param index
     *            -
     * @param oldValue
     *            -
     * @param newValue
     *            -
     */
    protected final void fireIndexedPropertyChange(final String propertyName, final int index,
        final boolean oldValue, final boolean newValue) {
        propertyChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    /**
     * Fire indexed property change event.
     * 
     * @param propertyName
     *            -
     * @param index
     *            -
     * @param oldValue
     *            -
     * @param newValue
     *            -
     */
    protected final void fireIndexedPropertyChange(final String propertyName, final int index,
        final int oldValue, final int newValue) {
        propertyChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    /**
     * Fire indexed property change event.
     * 
     * @param propertyName
     *            -
     * @param index
     *            -
     * @param oldValue
     *            -
     * @param newValue
     *            -
     */
    protected final void fireIndexedPropertyChange(final String propertyName, final int index,
        final Object oldValue, final Object newValue) {
        propertyChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    /**
     * Fire property change event.
     * 
     * @param evt
     *            the event
     */
    protected final void firePropertyChange(final PropertyChangeEvent evt) {
        propertyChangeSupport.firePropertyChange(evt);
    }

    /**
     * Fire property change event.
     * 
     * @param propertyName
     *            -
     * @param oldValue
     *            what -
     * @param newValue
     *            -
     */
    protected final void firePropertyChange(final String propertyName, final boolean oldValue,
        final boolean newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Fire property change event.
     * 
     * @param propertyName
     *            -
     * @param oldValue
     *            -
     * @param newValue
     *            -
     */
    protected final void firePropertyChange(final String propertyName, final int oldValue, final int newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Fire property change event.
     * 
     * @param propertyName
     *            -
     * @param oldValue
     *            -
     * @param newValue
     *            -
     */
    protected final void firePropertyChange(final String propertyName, final Object oldValue,
        final Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    /**
     * Fire change event for an element added to a collection
     */
    protected final void firePropertyElementAdded(final String collectionPropertyName, final Object elementAdded) {
        firePropertyChange(getCollectionElementAddedPropertyName(collectionPropertyName), null, elementAdded);
    }

    /**
     * Fire change event for an element removed to a collection
     */
    protected final void firePropertyElementRemoved(final String collectionPropertyName, final Object elementRemoved) {
        firePropertyChange(getCollectionElementRemovedPropertyName(collectionPropertyName), elementRemoved, null);
    }
    
    /**
     * Name of the property for which a change event is fired, when an element is added to the collection property of the given name.
     */
    public static String getCollectionElementAddedPropertyName(String collectionPropertyName) {
        return collectionPropertyName + "_ADDED";
    }
    
    /**
     * Name of the property for which a change event is fired, when an element is removed from the collection property of the given name.
     */
    public static String getCollectionElementRemovedPropertyName(String collectionPropertyName) {
        return collectionPropertyName + "_REMOVED";
    }



}
