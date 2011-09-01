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
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.geom.AffineTransform;
import javax.swing.JComponent;

import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;

import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import zbeans.cowgraph.model.CowGraphVersion;
import zbeans.cowgraph.model.GraphElement;
import zbeans.cowgraph.model.GraphElementFactory;
import zbeans.cowgraph.model.GraphElementType;

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



        getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {

            @Override
            public ConnectorState isAcceptable(Widget widget, Point point, Transferable transferable) {
                Node node = NodeTransfer.node(transferable, NodeTransfer.DND_COPY_OR_MOVE);
                GraphElementType type = node.getLookup().lookup(GraphElementType.class);
                if (type!=null) {
                return ConnectorState.ACCEPT;
                }
                else {
                    return ConnectorState.REJECT;
                }
            }

            @Override
            public void accept(Widget widget, Point point, Transferable transferable) {
                addGraphElementsFromTransferable(transferable, point);
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

            Point pointInScene = convertLocalToScene(point);
            elem.setX(pointInScene.getX());
            elem.setY(pointInScene.getY());

            version.add(elem);
        }
    }

    @Override
    protected Widget attachNodeWidget(GraphElement node) {
        
        IconNodeWidget widget = new IconNodeWidget(this);
        //widget.setImage(node.getImage());
        widget.setLabel(Long.toString(node.hashCode()));
        // TODO: Reflect changes in model (GraphElement)
        widget.getActions().addAction(ActionFactory.createMoveAction());

        widget.addDependency(new GraphElementWidgetDependency(widget, node));
        widget.setPreferredLocation(new Point((int)node.getX(), (int)node.getY()));
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
            addNode((GraphElement) evt.getNewValue());            
        }
        if (evt.getPropertyName().equals(CowGraphVersion.Property.ELEMENTS_ADDED.name())) {
        }
    }
}
