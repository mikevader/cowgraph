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

import java.util.HashMap;
import java.util.Map;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import zbeans.cowgraph.model.Circle;
import zbeans.cowgraph.model.GraphElement;

public class GraphElementWidgetFactory {

    private static Map<Class<? extends GraphElement>, Class<? extends Widget>> widgetClasses = new HashMap<Class<? extends GraphElement>, Class<? extends Widget>>();

    /**
     * Configure all supported GraphElement types with corresponding Widget classes here.
     */
    static {
        registerGraphElementWidget(Circle.class, CircleWidget.class);
    }

    public static void registerGraphElementWidget(Class<? extends GraphElement> graphElementClass, Class<? extends Widget> widgetClass) {
        widgetClasses.put(graphElementClass, widgetClass);
    }

    public static Widget createWidget(Scene scene, GraphElement element) {
        Class<? extends Widget> widgetClass = widgetClasses.get(element.getClass());        
        if (widgetClass == null) {
            throw new UnsupportedOperationException("Unsupported graph element type: " + element.getClass().getSimpleName());
        }
        try {
            Widget widget = widgetClass.getConstructor(Scene.class, element.getClass()).newInstance(scene, element);
            return widget;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Could not create widet for element of type " + element.getClass().getSimpleName(), e);
        }

    }
}
