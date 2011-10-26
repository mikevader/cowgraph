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
import java.util.LinkedList;
import java.util.List;
import zbeans.simple.beans.ObservableBean;

/**
 * CowGraph document which acts as root element for all versions of a cow graphic.
 *
 * 
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
public class CowGraphDocument extends ObservableBean {

    public static final String PROP_NAME = "name";
    public static final String PROP_VERSIONS = "versions";
    
    private String name;
    private List<CowGraphVersion> versions;

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        firePropertyChange(PROP_NAME, oldName, name);
    }


    public CowGraphDocument() {
        versions = new LinkedList<CowGraphVersion>();
    }

    public List<CowGraphVersion> getVersions() {
        return versions;
    }

    public boolean remove(CowGraphVersion version) {
        firePropertyChange(PROP_VERSIONS, version, null);
        return versions.remove(version);
    }

    public boolean add(CowGraphVersion version) {
        firePropertyChange(PROP_VERSIONS, null, version);
        return versions.add(version);
    }
}
