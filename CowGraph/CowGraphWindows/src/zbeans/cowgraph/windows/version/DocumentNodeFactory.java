/*
 * Copyright (C) 2011 Michael Muehlebach <michael@anduin.ch>
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
/*
 * $Id$
 * 
 * Copyright (c) 2010 Anduin Developments. http://www.anduin.ch
 */
package zbeans.cowgraph.windows.version;

import java.beans.IntrospectionException;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import zbeans.cowgraph.datasource.DocumentDataSource;
import zbeans.cowgraph.model.CowGraphDocument;

/**
 * Getting the children of the root node.
 *
 * @author Michael Muehlebach <michael@anduin.ch>
 */
public class DocumentNodeFactory extends ChildFactory<CowGraphDocument> {

    private DocumentDataSource dataSource;

    public DocumentNodeFactory(DocumentDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected boolean createKeys(List<CowGraphDocument> toPopulate) {
        toPopulate.addAll(dataSource.getDocuments());

        return true;
    }

    @Override
    protected Node createNodeForKey(CowGraphDocument key) {
        Node node = null;
        try {
            node = new DocumentNode(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return node;
    }
}