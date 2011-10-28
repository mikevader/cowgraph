/*
 * Copyright (C) 2011 Michael M&uuml;hlebach and Rolf Bruderer
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

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.Widget.Dependency;
import zbeans.cowgraph.model.GraphElement;

/**
 * Base class for dependencies to synchronise changes between a widget and its graph element
 */
public class GraphElementDependency<G extends GraphElement, W extends Widget> implements Dependency, PropertyChangeListener {

    private W widget;
    private G graphElement;
    private boolean propagatingChangesToNode;
    private boolean propagatingChangesToWidget;

    public GraphElementDependency(W widget, G graphElement) {
        this.widget = widget;
        this.graphElement = graphElement;
        this.propagatingChangesToNode = false;
        this.propagatingChangesToWidget = false;
    }

    public G getGraphElement() {
        return graphElement;
    }

    public W getWidget() {
        return widget;
    }

    @Override
    public final void propertyChange(PropertyChangeEvent evt) {
        if (propagatingChangesToNode) {
            return;
        }
        propagatingChangesToWidget = true;

        if (isWidgetUpdateNeeded(evt)) {
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
    public final void revalidateDependency() {
        if (propagatingChangesToWidget) {
            return;
        }
        this.propagatingChangesToNode = true;
        updateGraphElement();
        this.propagatingChangesToNode = false;
    }

    public void updateGraphElement() {
        this.graphElement.setX(widget.getPreferredLocation().x);
        this.graphElement.setY(widget.getPreferredLocation().y);
    }

    public boolean isWidgetUpdateNeeded(PropertyChangeEvent event) {
        return GraphElement.PROP_X.equals(event.getPropertyName()) || GraphElement.PROP_Y.equals(event.getPropertyName()) || GraphElement.PROP_COLOR.equals(event.getPropertyName());
    }
    
    public void updateWidget() {
        widget.setPreferredLocation(new Point(graphElement.getX(), graphElement.getY()));
    }
}
