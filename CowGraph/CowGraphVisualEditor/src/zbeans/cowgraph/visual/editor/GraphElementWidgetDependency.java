/*
 * Copyright (C) 2011 Michael M&uuml;hlebach <michael at anduin.ch>
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

import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.Widget.Dependency;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import zbeans.cowgraph.model.GraphElement;

/**
 * Verantwortlich für updates im GraphElement bei Änderungen im Widget.
 * 
 * 
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
public class GraphElementWidgetDependency implements Dependency {

    Widget widget;
    GraphElement node;

    public GraphElementWidgetDependency(Widget widget, GraphElement node) {
        this.widget = widget;
        this.node = node;
    }

    @Override
    public void revalidateDependency() {
        this.node.setX(widget.getLocation().x);
        this.node.setY(widget.getLocation().y);
    }
    
}
