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

/**
 * Cash cow buble: a circle with a specified color, size, position, text and
 * arrow.
 *
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
public class CashCowProduct extends GraphElementImpl implements CompositeGraphElement {
    private List<GraphElement> elements;

    public CashCowProduct() {
        super(GraphElementType.CIRCLE);
        elements = new LinkedList<GraphElement>();

        // TODO: Create here a complete CashCow buble with circles arrows and text graph elements.
    }

    @Override
    public List<GraphElement> getElements() {
        return Collections.unmodifiableList(elements);
    }
    
}
