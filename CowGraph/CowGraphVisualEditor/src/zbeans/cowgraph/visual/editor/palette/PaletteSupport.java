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
package zbeans.cowgraph.visual.editor.palette;

import javax.swing.Action;
import org.netbeans.spi.palette.DragAndDropHandler;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.nodes.AbstractNode;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExTransferable;

/**
 *
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
public class PaletteSupport {

    public static PaletteController createPalette() {
        AbstractNode paletteRoot = new AbstractNode(new CategoryNodeFactory());
        paletteRoot.setName("Palette Root");
        return PaletteFactory.createPalette(paletteRoot, new MyActions(), null, new MyDnDHandler());
    }

    private static class MyActions extends PaletteActions {

        public Action[] getImportActions() {
            return null;
        }

        public Action[] getCustomPaletteActions() {
            return null;
        }

        public Action[] getCustomCategoryActions(Lookup lookup) {
            return null;
        }

        public Action[] getCustomItemActions(Lookup lookup) {
            return null;
        }

        public Action getPreferredAction(Lookup lookup) {
            return null;
        }
    }

    private static class MyDnDHandler extends DragAndDropHandler {

        public void customize(ExTransferable exTransferable, Lookup lookup) {
//            Node node = lookup.lookup(Node.class);
//            final Image image = (Image) node.getIcon(BeanInfo.ICON_COLOR_16x16);
//            exTransferable.put(new ExTransferable.Single(DataFlavor.imageFlavor) {
//
//                protected Object getData() throws IOException, UnsupportedFlavorException {
//                    return image;
//                }
//            });
        }
    }
}
