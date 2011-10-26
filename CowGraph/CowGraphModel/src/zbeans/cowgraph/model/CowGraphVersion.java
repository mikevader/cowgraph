/*
 * Copyright (C) 2011 Michael M&uuml;hlebach <michael at anduin.ch>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package zbeans.cowgraph.model;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import zbeans.simple.beans.ObservableBean;

/**
 * A version of a cow graphic.
 *
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
public class CowGraphVersion extends ObservableBean implements Cloneable {

    public static final String PROP_NAME = "name";
    public static final String PROP_DATE = "date";

    public static enum Property {

        /**
         * We send a property change event to this property when an element is added, passing the added element as new value.
         */
        ELEMENTS_ADDED,
        /**
         * We send a property change event to this property when an element is removed, passing the removed element as old value.
         */
        ELEMENTS_REMOVED;
    }
    private CowGraphDocument document;
    private String name;
    private Date date;
    private List<GraphElement> elements;

    public CowGraphVersion() {
        this(null);
    }

    public CowGraphVersion(CowGraphDocument document) {
        this.document = document;
        name = "";
        date = new Date();

        elements = new LinkedList<GraphElement>();
    }

    public CowGraphDocument getDocument() {
        return this.document;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        Date oldDate = this.date;
        this.date = date;
        this.firePropertyChange(PROP_DATE, oldDate, this.date);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        this.firePropertyChange(PROP_NAME, oldName, this.name);
    }

    public List<GraphElement> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public GraphElement remove(int index) {
        GraphElement elem = elements.remove(index);
        firePropertyElementRemoved(Property.ELEMENTS_REMOVED.name(), elem);
        return elem;
    }

    public boolean add(GraphElement elem) {
        boolean added = elements.add(elem);
        if (added) {
            firePropertyElementAdded(Property.ELEMENTS_ADDED.name(), elem);
        }
        return added;
    }

    public String getLongDisplayName() {
        return getDocument().getName() + " - " + getName();
    }

    /**
     * FIXME: move to ObservableBean
     * Fire change event for element added.
     */
    protected final void firePropertyElementAdded(String propertyName, final Object elementAdded) {
        firePropertyChange(propertyName, null, elementAdded);
    }

    /**
     * FIXME: move to ObservableBean
     * Fire change event for element removed.
     */
    protected final void firePropertyElementRemoved(String propertyName, final Object elementRemoved) {
        firePropertyChange(propertyName, elementRemoved, null);
    }

    /**
     * Creates a deep copy of a version object within the same document. This can be used for creating a continuation of a version.
     * @param version1
     * @return 
     */
    public static CowGraphVersion nextVersion(CowGraphVersion version1) {
        CowGraphVersion version2 = new CowGraphVersion(version1.document);

        version2.name = version1.name + ".1";
        version2.date = new Date();
        
        for (GraphElement element1 : version1.elements) {
            version2.add(element1.clone());
        }
        
        
        return version2;
    }
}
