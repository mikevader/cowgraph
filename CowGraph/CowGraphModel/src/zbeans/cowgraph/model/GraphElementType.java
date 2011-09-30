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
package zbeans.cowgraph.model;

/**
 *
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
public enum GraphElementType {

    CIRCLE("Circle", Circle.class, GraphElementGroup.BASIC, "zbeans/cowgraph/visual/editor/palette/palette_circle.png");
    
    private final String name;
    private final GraphElementGroup group;
    private final Class<? extends GraphElement> elementClass;
    private final String imageResourcePath;

    private GraphElementType(String name, Class<? extends GraphElement> elementClass, GraphElementGroup group, String image) {
        this.name = name;
        this.group = group;
        this.imageResourcePath = image;  
        this.elementClass = elementClass;
    }

    public Class<? extends GraphElement> getElementClass() {
        return elementClass;
    }

    public String getName() {
        return name;
    }

    public GraphElementGroup getGroup() {
        return group;
    }

    public String getImageResourcePath() {
        return imageResourcePath;
    }
    
    
    
    
}
