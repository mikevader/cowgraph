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

import java.awt.Color;
import zbeans.simple.beans.PropertyChangeObservable;

/**
 * Base interface for all graph elements.
 *
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
public interface GraphElement extends PropertyChangeObservable {

    public static final String PROP_X = "x";
    public static final String PROP_Y = "y";
    public static final String PROP_COLOR = "color";

    public static enum ChildChangeEvent {

        /**
         * On change events to this property we only pass the new element as the new value (old value is null)
         */
        ADD_ELEMENT,
        /**
         * On change events to this 
         */
        REMOVE_ELEMENT;
    }

    public GraphElementType getType();

    public double getX();

    public double getY();

    public Color getColor();

    public void setX(double x);

    public void setY(double y);
}
