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

/**
 * A version of a cow graphic.
 *
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
public class CowGraphVersion {
    private String name;
    private Date date;
    private List<GraphElement> elements;

    public CowGraphVersion() {
        name = "";
        date = new Date();
        
        elements = new LinkedList<GraphElement>();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GraphElement> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public GraphElement remove(int index) {
        return elements.remove(index);
    }

    public boolean add(GraphElement e) {
        return elements.add(e);
    }
}
