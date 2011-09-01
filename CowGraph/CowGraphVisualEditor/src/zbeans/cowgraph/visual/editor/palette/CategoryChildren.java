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
package zbeans.cowgraph.visual.editor.palette;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
public class CategoryChildren extends Children.Keys {

    public CategoryChildren() {
    }

    @Override
    protected Node[] createNodes(Object key) {
        GraphElementGroup obj = (GraphElementGroup) key;
        return new Node[]{new CategoryNode(obj)};
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        setKeys(GraphElementGroup.values());
    }
}