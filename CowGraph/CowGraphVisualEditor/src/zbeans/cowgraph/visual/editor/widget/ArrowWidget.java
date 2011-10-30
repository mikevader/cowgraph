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
import java.awt.Point;
import java.awt.Rectangle;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.ResizeStrategy;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import zbeans.cowgraph.model.Circle;
import zbeans.cowgraph.model.Arrow;

/**
 * Example widget to visualize a Circle, that can move and resize the circle and updates to changes in the Circle model element.
 * 
 * See following tutorials for information about resizing functionality:
 * http://netbeans.org/community/magazine/html/04/visuallibrary.html
 * http://java.dzone.com/news/how-add-resize-functionality-v
 * http://blogs.oracle.com/geertjan/entry/small_netbeans_visual_library_resize
 */
public class ArrowWidget extends Widget {
    
    public static final int BOUNDS_INSET = 10;

    private Arrow element;
    private GraphElementDependency<Arrow, ArrowWidget> dependency;
    
    public ArrowWidget(Scene scene, Arrow element) {
        super(scene);
        this.element = element;
        
        setBorder(BorderFactory.createEmptyBorder());                

        dependency = new GraphElementDependency<Arrow, ArrowWidget>(this, element);
        dependency.updateWidget();
        addDependency(dependency);
    }

    @Override
    protected void notifyAdded() {
        element.addPropertyChangeListener(dependency);
    }

    @Override
    protected void notifyRemoved() {
        element.removePropertyChangeListener(dependency);
    }
    
    @Override
    protected Rectangle calculateClientArea() {
        Rectangle rect = new Rectangle();
        rect.add(element.getX(), element.getY());
        rect.add(element.getToX(), element.getToY());        
        return rect;
    }

    @Override
    protected void paintWidget() {
        
        super.paintWidget();
        Graphics2D gr = getGraphics();
        gr.setColor(element.getColor());
        gr.drawLine(element.getX(), element.getY(), element.getToX(), element.getToY());
       
    }
    
}
