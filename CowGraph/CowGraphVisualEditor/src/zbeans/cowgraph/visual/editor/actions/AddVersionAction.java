/*
 * Copyright (C) 2011 mmu
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
package zbeans.cowgraph.visual.editor.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import zbeans.cowgraph.model.CowGraphDocument;
import zbeans.cowgraph.model.CowGraphVersion;

@ActionID(category = "CowGraph",
id = "zbeans.cowgraph.visual.editor.actions.AddVersionAction")
@ActionRegistration(iconBase = "zbeans/cowgraph/visual/editor/actions/addversion.png",
displayName = "#CTL_AddVersionAction")
@ActionReferences({
    @ActionReference(path = "CowGraph/Nodes/Version/Actions", position=0)
})
@Messages("CTL_AddVersionAction=Create New Version...")
public final class AddVersionAction extends AbstractAction implements ActionListener, ContextAwareAction, LookupListener {

    private Lookup.Result<CowGraphDocument> documentResult;
    private Lookup.Result<CowGraphVersion> versionResult;

    public AddVersionAction() {
        this(Bundle.CTL_AddVersionAction());
    }

    private AddVersionAction(Lookup lookup) {
        this(Bundle.CTL_AddVersionAction());

        this.documentResult = lookup.lookupResult(CowGraphDocument.class);
        this.documentResult.addLookupListener(this);
        this.resultChanged(new LookupEvent(documentResult));

        this.versionResult = lookup.lookupResult(CowGraphVersion.class);
        this.versionResult.addLookupListener(this);
        this.resultChanged(new LookupEvent(documentResult));
    }

    private AddVersionAction(String label) {
        super(label);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CowGraphVersion version = null;

        if (this.documentResult != null && !this.documentResult.allInstances().isEmpty()) {
            // if a document is selected, create a new initial version
            CowGraphDocument doc = this.documentResult.allInstances().iterator().next();
            version = new CowGraphVersion(doc);
            doc.add(version);
        } else if (this.versionResult != null && !this.versionResult.allInstances().isEmpty()) {
            // version is selected, create copy from this
            version = this.versionResult.allInstances().iterator().next();
            CowGraphVersion copy = CowGraphVersion.nextVersion(version);
            version.getDocument().add(copy);
            version = copy;
        }

        EditAction.openInVersionEditor(version);
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new AddVersionAction(actionContext);
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        if ((this.documentResult != null && !this.documentResult.allInstances().isEmpty()) || (this.versionResult != null && !this.versionResult.allInstances().isEmpty())) {
            this.setEnabled(true);
        } else {
            this.setEnabled(false);
        }
    }
}
