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
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.ResizeStrategy;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import zbeans.cowgraph.model.Circle;

/**
 * Example widget to visualize a Circle, that can move and resize the circle and updates to changes in the Circle model element.
 * 
 * See following tutorials for information about resizing functionality:
 * http://netbeans.org/community/magazine/html/04/visuallibrary.html
 * http://java.dzone.com/news/how-add-resize-functionality-v
 * http://blogs.oracle.com/geertjan/entry/small_netbeans_visual_library_resize
 */
public class CircleWidget extends Widget {
    
    public static final int BOUNDS_INSET = 10;

    private Circle element;
    private CircleDependency dependency;

    public CircleWidget(Scene scene, Circle element) {
        super(scene);
        this.element = element;


        setBorder(BorderFactory.createEmptyBorder());

        // order of actions added is important: resize action won't get any events, when after move action
        getActions().addAction(ActionFactory.createResizeAction(new CircleResizeStrategy(), null));
        getActions().addAction(ActionFactory.createMoveAction());

        getActions().addAction(ActionFactory.createSelectAction(new SelectProvider() {

            @Override
            public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
                return true;
            }

            @Override
            public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
                return true;
            }

            @Override
            public void select(Widget widget, Point localLocation, boolean invertSelection) {
                if (invertSelection) {
                    setBorder(BorderFactory.createEmptyBorder());
                } else {
                    setBorder(BorderFactory.createLineBorder(2));
                }
            }
        }));

        WidgetAction hoverAction = ActionFactory.createHoverAction(new TwoStateHoverProvider() {

            @Override
            public void unsetHovering(Widget widget) {
                setBorder(BorderFactory.createEmptyBorder());
            }

            @Override
            public void setHovering(Widget widget) {
                setBorder(BorderFactory.createResizeBorder(BOUNDS_INSET-1, Color.BLACK, false));
            }
        });
        
        getActions().addAction(hoverAction);
        getScene().getActions().addAction(hoverAction);

        dependency = new CircleDependency(this, element);
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
        int width = (int) element.getWidth() + 2 * CircleWidget.BOUNDS_INSET;
        int x = - width / 2;
        int y = - width / 2;
        Rectangle rect = new Rectangle(x, y, width, width);
        return rect;
    }

    @Override
    protected void paintWidget() {
        super.paintWidget();
        Graphics2D gr = getGraphics();
        gr.setColor(element.getColor());
        int x = - element.getWidth() / 2;
        int y = - element.getWidth() / 2;
        gr.drawOval(x, y, element.getWidth(), element.getWidth());
    }

    /**
     * A simple strategy that puts the bounds according to current choosen width (height same as width).
     */
    private static class CircleResizeStrategy implements ResizeStrategy {

        @Override
        public Rectangle boundsSuggested(final Widget widget,
                final Rectangle originalBounds,
                final Rectangle suggestedBounds,
                final ResizeProvider.ControlPoint controlPoint) {
            final Rectangle result = new Rectangle(suggestedBounds);

            final double deltaW = Math.abs(suggestedBounds.getWidth() - originalBounds.getWidth());
            final double deltaH = Math.abs(suggestedBounds.getHeight() - originalBounds.getHeight());
            
            if (deltaW >= deltaH) { // moving mostly horizontally
                result.height = result.width;
            } else { // moving mostly vertically
                result.width = result.height;
            }
            
            // keep position
            result.x = - result.width / 2;
            result.y = - result.width / 2;
            return result;
        }
    }
    
}
