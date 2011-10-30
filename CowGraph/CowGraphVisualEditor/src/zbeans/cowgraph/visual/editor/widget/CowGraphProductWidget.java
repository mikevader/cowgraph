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
import java.awt.Point;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import zbeans.cowgraph.model.CowGraphProduct;
import zbeans.cowgraph.model.GraphElement;

/**
 * Widget to render and edit a cow graph product in the visual editor
 */
public class CowGraphProductWidget extends Widget {
    
    public static final int BOUNDS_INSET = 10;

    private CowGraphProduct element;
    private GraphElementDependency dependency;
    
    private CircleWidget circleWidget;
    private ArrowWidget arrowWidget;
    private LabelTextWidget textWidget;

    public CowGraphProductWidget(Scene scene, CowGraphProduct element) {
        super(scene);
        this.element = element;


        setBorder(BorderFactory.createEmptyBorder());

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

        dependency = new GraphElementDependency(this, element);
        dependency.updateWidget();
        addDependency(dependency);
        
        createCompositeChildWidgets();
    }

    @Override
    protected void notifyAdded() {
        element.addPropertyChangeListener(dependency);
    }

    @Override
    protected void notifyRemoved() {
        element.removePropertyChangeListener(dependency);
    }

    private void createCompositeChildWidgets() {
        circleWidget = new CircleWidget(getScene(), element.getCircle());
        arrowWidget = new ArrowWidget(getScene(), element.getArrow());
        textWidget = new LabelTextWidget(getScene(), element.getLabelText()); 
            
        circleWidget.addDependency(new Dependency() {
            @Override
            public void revalidateDependency() {
                element.getArrow().setToX(element.getCircle().getWidth() * 2);
                element.getArrow().setToY(-element.getCircle().getWidth() * 2);
                element.getLabelText().setX(-element.getCircle().getWidth() / 2);
                element.getLabelText().setY(element.getCircle().getWidth() / 2 + 20);
            }           
        });
        
        addChild(circleWidget);
        addChild(textWidget);
        // addChild(arrowWidget);
    }
    
}
