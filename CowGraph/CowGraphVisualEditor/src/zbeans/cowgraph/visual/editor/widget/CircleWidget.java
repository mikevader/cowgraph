/*
 * Copyright (C) 2011 rbr
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
package zbeans.cowgraph.visual.editor.widget;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import zbeans.cowgraph.model.Circle;

/**
 *
 * @author rbr
 */
public class CircleWidget extends Widget {

    private Circle element;

    public CircleWidget(Scene scene, Circle element) {
        super(scene);
        this.element = element;
    }

    @Override
    protected Rectangle calculateClientArea() {
        int radius = (int) element.getRadius();
        Rectangle rect = new Rectangle(0, 0, 2 * radius, 2 * radius);
        return rect;
    }

    @Override
    protected void paintWidget() {
        super.paintWidget();
        Graphics2D gr = getGraphics();
        gr.setColor(Color.BLACK);
        gr.drawOval(0, 0, 2 * (int) element.getRadius(), 2 * (int) element.getRadius());
    }
}
