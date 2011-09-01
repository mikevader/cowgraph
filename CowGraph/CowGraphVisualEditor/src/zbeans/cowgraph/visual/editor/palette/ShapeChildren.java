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

import java.util.ArrayList;
import org.openide.nodes.Index;

/**
 *
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
public class ShapeChildren extends Index.ArrayChildren {

    private Category category;
    private String[][] items = new String[][]{
        {"0", "Shapes", "zbeans/cowgraph/visual/editor/palette/image1.png"},
        {"1", "Shapes", "zbeans/cowgraph/visual/editor/palette/image2.png"},
        {"2", "Shapes", "zbeans/cowgraph/visual/editor/palette/image3.png"},};

    public ShapeChildren(Category Category) {
        this.category = Category;
    }

    protected java.util.List initCollection() {
        ArrayList childrenNodes = new ArrayList(items.length);
        for (int i = 0; i < items.length; i++) {
            if (category.getName().equals(items[i][1])) {
                Shape item = new Shape();
                item.setNumber(new Integer(items[i][0]));
                item.setCategory(items[i][1]);
                item.setImage(items[i][2]);
                childrenNodes.add(new ShapeNode(item));
            }
        }
        return childrenNodes;
    }
}
