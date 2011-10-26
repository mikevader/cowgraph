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
import zbeans.cowgraph.visual.editor.widget.WidgetFactory;

/**
 *
 * @author Rolf Bruderer
 */
public class CowGraphVisualEditorScene extends GraphScene<GraphElement, String> implements PropertyChangeListener {

    private CowGraphVersion version;
    private LayerWidget mainLayer;

    CowGraphVisualEditorScene() {
        mainLayer = new LayerWidget(this);
        addChild(mainLayer);

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
                getView().requestFocusInWindow();
            }
        }));

        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(ActionFactory.createPanAction());
    }

    public CowGraphVersion getVersion() {
        return version;
    }

    /**
     * Has to be called before scene gets visible!!!
     */
    public void setVersion(CowGraphVersion version) {        
        unsubscribeFromVersionChanges();
        this.version = version;
        if (isVisible()) {
            subscribeToVersionChanges();
        }
    }

    private void subscribeToVersionChanges() {
        if (this.version != null) {
            this.version.addPropertyChangeListener(this);
        }
    }

    private void unsubscribeFromVersionChanges() {
        if (this.version != null) {
            this.version.removePropertyChangeListener(this);
        }
    }

    @Override
    protected void notifyAdded() {
        super.notifyAdded();
        // TODO: ensure to create all widgets first, according to current set version.
        subscribeToVersionChanges();        
    }

    @Override
    protected void notifyRemoved() {
        super.notifyRemoved();
        unsubscribeFromVersionChanges();
    }

    private void addGraphElementsFromTransferable(Transferable transferable, Point point) {
        Node[] nodes = NodeTransfer.nodes(transferable, NodeTransfer.DND_COPY_OR_MOVE);
        for (Node node : nodes) {
            GraphElementType type = node.getLookup().lookup(GraphElementType.class);
            GraphElementFactory factory = Lookup.getDefault().lookup(GraphElementFactory.class);
            GraphElement elem = factory.createGraphElement(type);

            elem.setX(point.x);
            elem.setY(point.y);

            version.add(elem);
        }
    }

    @Override
    protected Widget attachNodeWidget(GraphElement node) {
        Widget widget = WidgetFactory.createWidget(this, node);
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
