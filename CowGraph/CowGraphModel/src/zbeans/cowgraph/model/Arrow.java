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

/**
 * A circle.
 *
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
public class Arrow extends GraphElementImpl {
    
    public static final String PROP_TOX = "toX";
    public static final String PROP_TOY = "toY";

    public static final String PROP_WIDTH = "width";
    private int toX = 20;
    private int toY = -20;

    public Arrow() {
        super(GraphElementType.ARROW);
    }

    public void setToX(int toX) {
        int oldToX = this.toX;        
        this.toX = toX;
        firePropertyChange(PROP_TOX, oldToX, toX);
    }

    public void setToY(int toY) {
        int oldToY = this.toY;        
        this.toY = toY;
        firePropertyChange(PROP_TOY, oldToY, toY);
    }

    public int getToX() {
        return toX;
    }

    public int getToY() {
        return toY;
    }
    
    public Arrow clone() {
        Arrow clone = (Arrow)super.clone();        
        clone.toX = toX;
        clone.toY = toY;        
        return clone;
    }
}
