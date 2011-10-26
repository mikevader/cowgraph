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

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;
import zbeans.cowgraph.datasource.DocumentDataSource;
import zbeans.cowgraph.model.CowGraphDocument;
import zbeans.cowgraph.model.CowGraphVersion;

@ActionID(category = "File",
id = "zbeans.cowgraph.visual.editor.actions.AddDocumentAction")
@ActionRegistration(iconBase = "zbeans/cowgraph/visual/editor/actions/newDocument.png",
displayName = "#CTL_AddDocumentAction")
@ActionReferences({
    @ActionReference(path = "Toolbars/File", position = 0),
    @ActionReference(path = "CowGraph/Nodes/Document/Actions")
})
@Messages("CTL_AddDocumentAction=Create New Document...")
public final class AddDocumentAction extends AbstractAction implements Presenter.Toolbar {

    public AddDocumentAction() {
        super(Bundle.CTL_AddDocumentAction(), new ImageIcon("zbeans/cowgraph/visual/editor/actions/newDocument.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DocumentDataSource dataSource = Lookup.getDefault().lookup(DocumentDataSource.class);

        CowGraphVersion version = null;

        // No document is selected and a new doc and version is created
        CowGraphDocument doc = dataSource.createNewDocument();
        version = new CowGraphVersion(doc);
        doc.add(version);

        EditAction.openInVersionEditor(version);
    }

    @Override
    public Component getToolbarPresenter() {
        return new JButton(this);
    }
}
