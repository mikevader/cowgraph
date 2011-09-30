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
import javax.swing.Action;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;
import zbeans.cowgraph.model.CowGraphDocument;

/**
 *
 * @author Michael Muehlebach <michael@anduin.ch>
 */
/** Getting the feed node and wrapping it in a FilterNode */
public class DocumentNode extends BeanNode {

    DocumentNode(CowGraphDocument document) throws IntrospectionException {
        super(document, Children.create(new VersionNodeFactory(document), true), Lookups.singleton(document));
    }

    @Override
    public String getDisplayName() {
        CowGraphDocument document = getDocument();
        return document.getName();
    }

    private CowGraphDocument getDocument() {
        return getLookup().lookup(CowGraphDocument.class);
    }

//    @Override
//    public Image getIcon(int type) {
//        return ImageUtilities.loadImage("org/netbeans/feedreader/rss16.gif");
//    }
//
//    @Override
//    public Image getOpenedIcon(int type) {
//        return getIcon(type);
//    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{/*SystemAction.get(DeleteAction.class)*/};
    }
}