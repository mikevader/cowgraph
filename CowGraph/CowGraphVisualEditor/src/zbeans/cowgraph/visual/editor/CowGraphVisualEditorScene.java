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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
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
public class CowGraphVisualEditorScene extends GraphScene<GraphElement, String> {

    private CowGraphVersion version;
    private LayerWidget mainLayer;

    CowGraphVisualEditorScene(CowGraphVersion version) {
        this.version = version;

        mainLayer = new LayerWidget(this);
        addChild(mainLayer);



        getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {

            @Override
            public ConnectorState isAcceptable(Widget widget, Point point, Transferable transferable) {
                Node node = NodeTransfer.node(transferable, NodeTransfer.DND_COPY_OR_MOVE);
                GraphElementType type = node.getLookup().lookup(GraphElementType.class);

                Image dragImage = getImageFromTransferable(type);
                JComponent view = getView();
                Graphics2D g2 = (Graphics2D) view.getGraphics();
                Rectangle visRect = view.getVisibleRect();
                view.paintImmediately(visRect);
                g2.drawImage(dragImage, AffineTransform.getTranslateInstance(point.getX(), point.getY()), null);

                return ConnectorState.ACCEPT;
            }

            @Override
            public void accept(Widget widget, Point point, Transferable transferable) {
                Node node = NodeTransfer.node(transferable, NodeTransfer.DND_COPY_OR_MOVE);
                GraphElementType type = node.getLookup().lookup(GraphElementType.class);


                // TODO Add correct GraphElement to version.
                GraphElementFactory factory = Lookup.getDefault().lookup(GraphElementFactory.class);
                Widget w = CowGraphVisualEditorScene.this.addNode(factory.createGraphElement(type));
                w.setPreferredLocation(widget.convertLocalToScene(point));
            }
        }));

        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(ActionFactory.createPanAction());
    }

    @Override
    protected Widget attachNodeWidget(GraphElement node) {
        IconNodeWidget widget = new IconNodeWidget(this);
        //widget.setImage(node.getImage());
        widget.setLabel(Long.toString(node.hashCode()));
        // TODO: Reflect changes in model (GraphElement)
        widget.getActions().addAction(ActionFactory.createMoveAction());

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

    private Image getImageFromTransferable(GraphElementType type) {
        return ImageUtilities.loadImage(type.image);
    }
}
