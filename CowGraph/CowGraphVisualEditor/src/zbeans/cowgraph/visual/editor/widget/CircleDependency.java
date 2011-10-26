/*
 * Copyright (C) 2011 Rolf Bruderer
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

import java.awt.Rectangle;
import org.netbeans.api.visual.widget.Widget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zbeans.cowgraph.model.Circle;

/**
 *
 */
public class CircleDependency extends GraphElementDependency<Circle, CircleWidget> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CircleDependency.class);

    public CircleDependency(CircleWidget widget, Circle graphElement) {
        super(widget, graphElement);
    }   

    @Override
    public void updateGraphElement() {
        super.updateGraphElement();
        Circle circle = getGraphElement();
        int width = getWidget().getPreferredBounds().width - 2 * CircleWidget.BOUNDS_INSET;
        circle.setWidth(width);
        LOGGER.info("Circle width updated: " + width);        
    }

    @Override
    public void updateWidget() {
        super.updateWidget();
        Circle circle = getGraphElement();
        Widget widget = getWidget();
        int width = (int) circle.getWidth() + 2 * CircleWidget.BOUNDS_INSET;
        widget.setPreferredBounds(new Rectangle(0, 0, width, width));
        LOGGER.info("Circle Widget width updated: " + widget.getPreferredBounds().width);
    }   
    
}
