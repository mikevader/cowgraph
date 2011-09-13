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
import zbeans.simple.beans.ObservableBean;

/**
 *
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
public class GraphElementImpl extends ObservableBean implements GraphElement {

    public static final String PROP_X = "x";
    public static final String PROP_Y = "y";
    public static final String PROP_COLOR = "color";
    private final GraphElementType type;
    private double x;
    private double y;
    private Color color;

    public GraphElementImpl(final GraphElementType type) {
        this.type = type;
    }

    /**
     * Get the value of color
     *
     * @return the value of color
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * Set the value of color
     *
     * @param color new value of color
     */
    public void setColor(Color color) {
        Color oldColor = this.color;
        this.color = color;
        this.firePropertyChange(PROP_COLOR, oldColor, color);
    }

    /**
     * Get the value of y
     *
     * @return the value of y
     */
    @Override
    public double getY() {
        return y;
    }

    /**
     * Set the value of y
     *
     * @param y new value of y
     */
    @Override
    public void setY(double y) {
        double oldY = this.y;
        this.y = y;
        this.firePropertyChange(PROP_Y, oldY, y);
    }

    /**
     * Get the value of x
     *
     * @return the value of x
     */
    @Override
    public double getX() {
        return x;
    }

    /**
     * Set the value of x
     *
     * 
     */
    @Override
    public void setX(double x) {
        double oldX = this.x;
        this.x = x;
        this.firePropertyChange(PROP_X, oldX, x);
    }

    @Override
    public GraphElementType getType() {
        return type;
    }
}
