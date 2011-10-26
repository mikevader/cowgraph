/*
 * Copyright (C) 2011 rbr
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

import java.io.IOException;
import java.util.Map;
import org.openide.util.Utilities;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.cookies.SaveCookie;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import zbeans.cowgraph.model.CowGraphVersion;
import zbeans.cowgraph.visual.editor.palette.PaletteSupport;
import static zbeans.cowgraph.visual.editor.Bundle.*;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//zbeans.cowgraph.visual.editor//CowGraphVisualEditor//EN",
autostore = false)
@TopComponent.Description(preferredID = "CowGraphVisualEditorTopComponent",
iconBase = "/zbeans/cowgraph/visual/editor/new_icon.png",
persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "zbeans.cowgraph.visual.editor.CowGraphVisualEditorTopComponent")
@ActionReferences({})
@TopComponent.OpenActionRegistration(displayName = "#CTL_CowGraphVisualEditorAction")
@Messages({"EditorName=CowGraph: {0}/{1}"})
public final class CowGraphVisualEditorTopComponent extends TopComponent implements ActionListener, ChangeListener {

    private static Map<CowGraphVersion, CowGraphVisualEditorTopComponent> existingVersionEditors = new HashMap<CowGraphVersion, CowGraphVisualEditorTopComponent>();

    
    /**
     * Finds an existing editor for the given version or create a new editor for it and returns it.
     * 
     * @param version for which an editor is returned
     * @return an existing editor or a newly created if no editor for this version exists.
     */
    public static CowGraphVisualEditorTopComponent findInstance(CowGraphVersion version) {
        CowGraphVisualEditorTopComponent versionEditor = existingVersionEditors.get(version);
        if (versionEditor == null) {
            versionEditor = new CowGraphVisualEditorTopComponent(version);
        }
        
        return versionEditor;
    }
    
    /**
     * Opens the editor for currents selected version or activates it if already open.
     */
    public static void openOrActivateForSelectedVersion() {
        Lookup.Result<CowGraphVersion> result;
        result = Utilities.actionsGlobalContext().lookupResult(CowGraphVersion.class);
        CowGraphVersion version = result.allInstances().iterator().next();

        CowGraphVisualEditorTopComponent editor = existingVersionEditors.get(version);
        if (editor == null) {
            editor = new CowGraphVisualEditorTopComponent(version);
            existingVersionEditors.put(version, editor);
        }
        editor.open();
        editor.requestActive();
    }

    private final JComponent view;
    private final InstanceContent content;
    private final SaveCookie saveCookie;
    private CowGraphVisualEditorScene scene;
    private CowGraphVersion version;

    public CowGraphVisualEditorTopComponent() {
        initComponents();

        //Initial Display name
        setDisplayName("...");

        scene = new CowGraphVisualEditorScene();
        view = scene.createView();

        canvasScrollPane.setViewportView(view);
        add(scene.createSatelliteView(), BorderLayout.WEST);

        content = new InstanceContent();
        saveCookie = new SaveCookieImpl();

        content.add(PaletteSupport.createPalette());

        associateLookup(new AbstractLookup(content));
}
    
    

    private CowGraphVisualEditorTopComponent(CowGraphVersion version) {
        this();
        
        this.version = version;
        this.scene.setVersion(version);
        setDisplayName(EditorName(this.version.getDocument().getName(), this.version.getName()));

        //setToolTipText(NbBundle.getMessage(CowGraphVisualEditorTopComponent.class, "HINT_CowGraphVisualEditorTopComponent"));
        existingVersionEditors.put(version, this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        canvasScrollPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        add(canvasScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane canvasScrollPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
//        Lookup.Result<CowGraphVersion> result;
//        result = Utilities.actionsGlobalContext().lookupResult(CowGraphVersion.class);
//        version = result.allInstances().iterator().next();
//        version.addPropertyChangeListener(new PropertyChangeListener() {
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                
//                content.add(saveCookie);
//            }
//        });
//        scene.setVersion(version);
//
//        setDisplayName(version.getLongDisplayName());
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class SaveCookieImpl implements SaveCookie {

        @Override
        public void save() throws IOException {
            content.remove(saveCookie);
        }
    };
}
