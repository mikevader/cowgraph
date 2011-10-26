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
package zbeans.cowgraph.visual.editor;

import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.Widget.Dependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zbeans.cowgraph.model.Circle;
import zbeans.cowgraph.model.GraphElement;
import zbeans.cowgraph.visual.editor.widget.CircleWidget;

/**
 * Synchronisiert Änderungen zwischen Widget und GraphElement
 */
public class GraphElementWidgetDependency implements Dependency, PropertyChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphElementWidgetDependency.class);
    private Widget widget;
    private GraphElement node;
    private boolean propagatingChangesToNode;
    private boolean propagatingChangesToWidget;

    public GraphElementWidgetDependency(Widget widget, GraphElement node) {
        this.widget = widget;
        this.node = node;
        this.propagatingChangesToNode = false;
        this.propagatingChangesToWidget = false;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (propagatingChangesToNode) {
            return;
        }
        propagatingChangesToWidget = true;

        if (GraphElement.PROP_X.equals(evt.getPropertyName()) || GraphElement.PROP_Y.equals(evt.getPropertyName()) || Circle.PROP_WIDTH.equals(evt.getPropertyName())) {
            updateWidget();

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    widget.getScene().validate();
                }
            });
        }

        propagatingChangesToWidget = false;

    }

    @Override
    public void revalidateDependency() {
        if (propagatingChangesToWidget) {
            return;
        }
        this.propagatingChangesToNode = true;
        updateGraphElement();
        this.propagatingChangesToNode = false;
    }

    public void updateGraphElement() {
        this.node.setX(widget.getPreferredLocation().x);
        this.node.setY(widget.getPreferredLocation().y);
        if (node instanceof Circle) {
            Circle c = (Circle) node;
            int width = widget.getPreferredBounds().width - 2 * CircleWidget.BOUNDS_INSET;
            c.setWidth(width);
            LOGGER.info("Circle width updated: " + width);
        }
    }

    public void updateWidget() {
        widget.setPreferredLocation(new Point((int) node.getX(), (int) node.getY()));
        if (node instanceof Circle) {
            Circle c = (Circle) node;
            int width = (int) c.getWidth() + 2 * CircleWidget.BOUNDS_INSET;
            widget.setPreferredBounds(new Rectangle(0, 0, width, width));
            LOGGER.info("Widget width updated: " + widget.getPreferredBounds().width);
        }
    }
}
