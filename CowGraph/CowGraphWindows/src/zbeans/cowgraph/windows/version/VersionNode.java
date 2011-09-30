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

import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;
import zbeans.cowgraph.model.CowGraphVersion;

/**
 *
 * @author Michael Muehlebach <michael@anduin.ch>
/** Wrapping the children in a FilterNode */
public class VersionNode extends AbstractNode {

    public VersionNode(CowGraphVersion version) {
        super(Children.LEAF, /*new ProxyLookup(*/Lookups.singleton(version)/*, Lookups.fixed(new EntryOpenCookie(version)))*/);
    }

    /** Using HtmlDisplayName ensures any HTML in RSS entry titles are properly handled, escaped, entities resolved, etc. */
    @Override
    public String getDisplayName() {
        CowGraphVersion version = getLookup().lookup(CowGraphVersion.class);
        return version.getName();
    }

    /** Making a tooltip out of the entry's description */
    @Override
    public String getShortDescription() {
        CowGraphVersion version = getLookup().lookup(CowGraphVersion.class);
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(version.getName()).append("; ");
        if (version.getDate() != null) {
            sb.append("Published: ").append(version.getDate().toString());
        }
        return sb.toString();
    }

    /** Providing the Open action on a feed entry */
    @Override
    public Action[] getActions(boolean popup) {
        return new Action[]{/*SystemAction.get(OpenAction.class)*/};
    }

//    @Override
//    public Action getPreferredAction() {
//        return getActions(false)[0];
//    }

    /** Specifying what should happen when the user invokes the Open action */
//    private static class EntryOpenCookie implements OpenCookie {
//
//        private final CowGraphVersion entry;
//
//        EntryOpenCookie(CowGraphVersion entry) {
//            this.entry = entry;
//        }
//
//        @Override
//        public void open() {
//            TopComponent tc = WindowManager.getDefault().findTopComponent("CowGraphVisualEditorTopComponent");
//
//            if (tc != null) {
//                tc.open();
//                tc.requestActive();
//            }
//        }
//    }
}
