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

import java.awt.Image;

/**
 *
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
public enum GraphElementType {
    CIRCLE("Circle", GraphElementGroup.BASIC, "zbeans/cowgraph/visual/editor/palette/image1.png");
    
    
    
    public final String name;
    public final GraphElementGroup group;
    public final String image;

    private GraphElementType(String name, GraphElementGroup group, String image) {
        this.name = name;
        this.group = group;
        this.image = image;
    }
}
