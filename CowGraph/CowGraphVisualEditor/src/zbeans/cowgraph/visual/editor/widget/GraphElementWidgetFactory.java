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

import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import zbeans.cowgraph.model.Circle;
import zbeans.cowgraph.model.GraphElement;

/**
 *
 * @author rbr
 */
public class GraphElementWidgetFactory {
    
    public static Widget createWidget(Scene scene, GraphElement element) {
        if (Circle.class.isAssignableFrom(element.getClass())) {
            return new CircleWidget(scene, (Circle)element);
        }
        throw new UnsupportedOperationException("Unsupported graph element type: " + element.getClass().getName());
    }
    
}
