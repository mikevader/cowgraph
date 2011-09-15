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
package zbeans.cowgraph.visual.editor;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.awt.datatransfer.Transferable;

import javax.swing.SwingUtilities;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;

import org.openide.util.Lookup;
import zbeans.cowgraph.model.CowGraphVersion;
import zbeans.cowgraph.model.GraphElement;
import zbeans.cowgraph.model.GraphElementFactory;
import zbeans.cowgraph.model.GraphElementType;
import zbeans.cowgraph.visual.editor.widget.GraphElementWidgetFactory;

/**
 *
 * @author Rolf Bruderer
 */
public class CowGraphVisualEditorScene extends GraphScene<GraphElement, String> implements PropertyChangeListener {

    final private CowGraphVersion version;
    private LayerWidget mainLayer;

    CowGraphVisualEditorScene(CowGraphVersion version) {
        this.version = version;
        version.addPropertyChangeListener(this);

        mainLayer = new LayerWidget(this);
        addChild(mainLayer);

        //TODO: Scene should initialize itself according to the given version. Now it starts with an empty scene and reacts to add/remove event since its creation which omits all object create before.

        getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {

            @Override
            public ConnectorState isAcceptable(Widget widget, Point point, Transferable transferable) {
                Node node = NodeTransfer.node(transferable, NodeTransfer.DND_COPY_OR_MOVE);
                GraphElementType type = node.getLookup().lookup(GraphElementType.class);
                if (type != null) {
                    return ConnectorState.ACCEPT;
                } else {
                    return ConnectorState.REJECT;
                }
            }

            @Override
            public void accept(Widget widget, Point point, Transferable transferable) {
                addGraphElementsFromTransferable(transferable, widget.convertLocalToScene(point));
            }
        }));

        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(ActionFactory.createPanAction());
    }

    private void addGraphElementsFromTransferable(Transferable transferable, Point point) {
        Node[] nodes = NodeTransfer.nodes(transferable, NodeTransfer.DND_COPY_OR_MOVE);
        for (Node node : nodes) {
            GraphElementType type = node.getLookup().lookup(GraphElementType.class);
            GraphElementFactory factory = Lookup.getDefault().lookup(GraphElementFactory.class);
            GraphElement elem = factory.createGraphElement(type);

            elem.setX(point.getX());
            elem.setY(point.getY());

            version.add(elem);
        }
    }

    @Override
    protected Widget attachNodeWidget(GraphElement node) {
        Widget widget = GraphElementWidgetFactory.createWidget(this, node);
        //widget.setLabel(Long.toString(node.hashCode()));
        widget.getActions().addAction(ActionFactory.createMoveAction());
        widget.setPreferredLocation(new Point((int) node.getX(), (int) node.getY()));
        widget.addDependency(new GraphElementWidgetDependency(widget, node));
        mainLayer.addChild(widget);
        return widget;
    }

    @Override
    protected Widget attachEdgeWidget(String egde) {
        return null;
    }

    @Override
    protected void attachEdgeSourceAnchor(String edge, GraphElement oldSourceNode, GraphElement sourceNode) {
    }

    @Override
    protected void attachEdgeTargetAnchor(String edge, GraphElement oldTargetNode, GraphElement targetNode) {
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(CowGraphVersion.Property.ELEMENTS_ADDED.name())) {
            this.addNode((GraphElement) evt.getNewValue());

            if (SwingUtilities.isEventDispatchThread()) {
                repaint();
                getScene().validate();
            } else {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        repaint();
                        getScene().validate();
                    }
                });
            }

        } else if (evt.getPropertyName().equals(CowGraphVersion.Property.ELEMENTS_REMOVED.name())) {
            GraphElement element = (GraphElement) evt.getOldValue();
            //TODO: Search in the graph for the node (aka GraphElement) and remove it. Maybe it would be an good idea to keep pointers to the nodes in a hashtable for better performance.
        }
    }
}
