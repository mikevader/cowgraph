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
import org.openide.actions.OpenAction;
import org.openide.cookies.OpenCookie;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import zbeans.cowgraph.model.CowGraphVersion;
import zbeans.cowgraph.visual.editor.CowGraphVisualEditorTopComponent;

/**
 *
 * @author Michael Muehlebach <michael@anduin.ch>
/** Wrapping the children in a FilterNode */
public class VersionNode extends BeanNode {

    public VersionNode(CowGraphVersion version) throws IntrospectionException {
        super(version, Children.LEAF, new ProxyLookup(Lookups.singleton(version), Lookups.fixed(new EntryOpenCookie(version))));
    }

    /** Using HtmlDisplayName ensures any HTML in RSS entry titles are properly handled, escaped, entities resolved, etc. */
    @Override
    public String getDisplayName() {
        CowGraphVersion version = getVersion();
        return version.getName();
    }

    private CowGraphVersion getVersion() {
        return getLookup().lookup(CowGraphVersion.class);
    }

    /** Making a tooltip out of the entry's description */
    @Override
    public String getShortDescription() {
        CowGraphVersion version = getVersion();
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
        return new Action[]{SystemAction.get(OpenAction.class)};
    }

    @Override
    public Action getPreferredAction() {
        return getActions(false)[0];
    }

    /** Specifying what should happen when the user invokes the Open action */
    private static class EntryOpenCookie implements OpenCookie {

        private final CowGraphVersion version;

        EntryOpenCookie(CowGraphVersion version) {
            this.version = version;
        }

        @Override
        public void open() {
            CowGraphVisualEditorTopComponent.openOrActivateForSelectedVersion();
        }
    }
}
