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

/**
 * Cash cow buble: a circle with a specified color, size, position, text and
 * arrow.
 *
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
public class CowGraphProduct extends CompositeGraphElementImpl {
    
    private Circle circle = new Circle();
    private Arrow arrow = new Arrow();
    private LabelText labelText = new LabelText();
    
    private static int count = 1;
    
    public CowGraphProduct() {
        super(GraphElementType.PRODUCT);
        circle.setColor(Color.BLUE);        
        arrow.setColor(Color.BLUE);
        labelText.setText("Product " + count++);
        labelText.setX(0);
        labelText.setY(45);        
        addGraphElement(arrow);
        addGraphElement(circle);
        addGraphElement(labelText);
    }

    public Circle getCircle() {
        return circle;
    }

    public Arrow getArrow() {
        return arrow;
    }

    public LabelText getLabelText() {
        return labelText;
    }
    
    
    
}
