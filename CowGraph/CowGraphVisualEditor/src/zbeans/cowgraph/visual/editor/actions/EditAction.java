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
package zbeans.cowgraph.visual.editor.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import zbeans.cowgraph.model.CowGraphVersion;
import zbeans.cowgraph.visual.editor.CowGraphVisualEditorTopComponent;

/**
 *
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
@ActionID(category = "CowGraph",
id = "zbeans.cowgraph.visual.editor.actions.EditAction")
@ActionRegistration(iconBase = "zbeans/cowgraph/visual/editor/new_icon.png",
displayName = "#CTL_EditAction")
@ActionReferences({
    @ActionReference(path = "Toolbars/File", position = 0)
})
@Messages("CTL_EditAction=Edit Version...")
public class EditAction extends AbstractAction implements LookupListener, ContextAwareAction {

    private Lookup.Result<CowGraphVersion> result;
    private JButton toolbarBtn;

    public EditAction() {
        this(Utilities.actionsGlobalContext());
    }

    public EditAction(Lookup lookup) {
        super(Bundle.CTL_EditAction(), new ImageIcon("zbeans/cowgraph/visual/editor/new_icon.png"));

        this.result = lookup.lookupResult(CowGraphVersion.class);
        this.result.addLookupListener(this);
        this.resultChanged(new LookupEvent(result));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.result != null && this.result.allInstances().size() > 0) {
            CowGraphVersion version = this.result.allInstances().iterator().next();
            EditAction.openInVersionEditor(version);
        }
    }

    public static void openInVersionEditor(CowGraphVersion version) {
        CowGraphVisualEditorTopComponent editor = CowGraphVisualEditorTopComponent.findInstance(version);
        editor.open();
        editor.requestActive();
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        boolean actionEnabled = false;
        if (this.result.allInstances().size() > 0) {
            actionEnabled = true;
        }
        this.setEnabled(actionEnabled);
        if (this.toolbarBtn != null) {
            this.toolbarBtn.setEnabled(actionEnabled);
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new EditAction(actionContext);
    }
}
