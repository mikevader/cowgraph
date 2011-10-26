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

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.awt.datatransfer.Transferable;

import javax.swing.SwingUtilities;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.BorderFactory;
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
            }
        }));

        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(ActionFactory.createPanAction());
    }

    public CowGraphVersion getVersion() {
        return version;
    }

    public void setVersion(CowGraphVersion version) {
        //TODO: Scene should initialize itself according to the given version. Now it starts with an empty scene and reacts to add/remove event since its creation which omits all object create before.
        this.version = version;
        this.version.addPropertyChangeListener(this);
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
        mainLayer.addChild(widget);
        return widget;
    }

// See for resize: http://netbeans.org/community/magazine/html/04/visuallibrary.html
// and :    http://java.dzone.com/news/how-add-resize-functionality-v
//    and : http://blogs.oracle.com/geertjan/entry/small_netbeans_visual_library_resize
//    private final static ResizeStrategy resizeStrategy = new ResizeStrategy() {
//public Rectangle boundsSuggested (final Widget widget,
//final Rectangle originalBounds,
//final Rectangle suggestedBounds,
//final ResizeProvider.ControlPoint controlPoint)
//{
//final Rectangle result = new Rectangle(suggestedBounds);
//final Thumbnail thumbnail = widget.getLookup().lookup(Thumbnail.class);
//
//// We could compute aspectRatio from originalBounds,
//// but rounding errors would accumulate.
//if (thumbnail != null) {
//// isImageAvailable() doesn’t guarantee the image is online
//final BufferedImage image = thumbnail.getImage();
//
//if (image != null) {
//final Insets insets = widget.getBorder().getInsets();
//final int mw = insets.left + insets.right;
//final int mh = insets.bottom + insets.top;
//final int contentWidth = result.width - mw;
//final int contentHeight = result.height - mh;
//final float aspectRatio = (float) image.getHeight()/image.getWidth();
//final double deltaW = Math.abs(suggestedBounds.getWidth() - originalBounds.getWidth());
//final double deltaH = Math.abs(suggestedBounds.getHeight() - originalBounds.getHeight());
//
//if (deltaW >= deltaH) { // moving mostly horizontally
//result.height = mh + Math.round(contentWidth * aspectRatio);
//}
//else { // moving mostly vertically
//result.width = mw + Math.round(contentHeight / aspectRatio);
//}
//}
//}
//return result;
//}
//};
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
