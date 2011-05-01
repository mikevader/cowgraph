/*
 * Copyright (C) 2011 Rolf Bruderer
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

import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Rolf Bruderer
 */
public class CowGraphVisualEditorScene extends GraphScene<Object, Object> {

    @Override
    protected Widget attachNodeWidget(Object n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Widget attachEdgeWidget(Object e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void attachEdgeSourceAnchor(Object e, Object n, Object n1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void attachEdgeTargetAnchor(Object e, Object n, Object n1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
    
}
