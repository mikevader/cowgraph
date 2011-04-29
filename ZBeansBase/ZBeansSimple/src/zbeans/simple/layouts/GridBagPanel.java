package zbeans.simple.layouts;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

/**
 * A simple convenience class for building panels with a {@link GridBagLayout} that makes
 * it easier to add components with {@link GridBagConstraints} to it using the Builder
 * Pattern.
 * 
 * Some examples for adding components into a cell with constraints on a GridBagPanel:
 * 
 * gridBagPanel.cell(0,0).left().add(component); <br/>
 * Adds a component on the top left cell (0,0) of the grid panel with left alignment
 * (vertical alignment is centered, and the component takes only as much place as needed
 * by default)
 * 
 * gridBagPanel.cell(0, 1).right().expandWidth().add(component); <br/>
 * Adds a component on the second cell on first row (0, 1) with right alignment and puts
 * as much space in front of the component as is available (by increasing the cell width).
 * (by default vertical alignment is centered, and the component itself takes only as much
 * place as needed)
 * 
 * gridBagPanel.cell(1,2).right().expandWidth().fillWidth().add(component); <br/>
 * Adds a component to the second row, third column (1,2) with right alignment and lets
 * the component and its cell fill all horizontal space that is available. (vertical
 * alignment is centered by default)
 * 
 * @author rbr
 */
public class GridBagPanel {

    private final JPanel panel;

    /**
     * Create a new panel with a {@link GridBagLayout}.
     */
    public GridBagPanel() {
        this(new JPanel());
    }

    /**
     * Create a grid bag panel using an exisiting panel. This will set a
     * {@link GridBagLayout} on the passed panel.
     * 
     * @param panel
     *            the panel to add components to using a {@link GridBagLayout}
     */
    public GridBagPanel(final JPanel panel) {
        this.panel = panel;
        panel.setLayout(new GridBagLayout());
    }

    /**
     * Create a cell builder for adding a component with a GridBagConstraint to this panel
     * at a specific cell.
     * 
     * Per default the components orientation (anchor) inside the cell is set to verticaly
     * and horizontaly centerd (anchor=CENTER).
     * 
     * @param row
     *            the row of the grid to add a component to
     * @param column
     *            the column of the grid to add a component to
     * 
     */
    public CellBuilder cell(final int row, final int column) {
        return new CellBuilder(row, column);
    }

    /**
     * Get the panel itself.
     * 
     * @return the panel that is built inside
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * A builder for building a grid cell with a component and a
     * {@link GridBagConstraints}.
     * 
     * Per default the cell is center aligned (horizontaly and verticaly by setting anchor
     * = CENTER) and the component and cell only take as much place (horizontaly and
     * verticaly) as they need.
     */
    public class CellBuilder {

        private final GridBagConstraints constraints;

        /**
         * Initialize a CellBuilder with a GridBagConstraints for the cell in the grid at
         * the given position.
         * 
         * 
         * 
         * Use {@link GridBags#constraint} to create an instance.
         * 
         * @param row
         *            the row in the grid to position the component in
         * @param column
         *            the column in the grid to position the component
         */
        CellBuilder(final int row, final int column) {
            this.constraints = new GridBagConstraints();
            constraints.gridx = column;
            constraints.gridy = row;
            constraints.anchor = GridBagConstraints.CENTER;
        }

        /**
         * Set the orientation of the component inside the cell to left and vertically
         * aligned centered. (anchor=LINE_START)
         * 
         * @return this builder itself to chain calls to other builder methods.
         */
        public CellBuilder left() {
            constraints.anchor = GridBagConstraints.LINE_START;
            return this;
        }

        /**
         * Set the orientation of the component inside the cell to right and vertically
         * aligned centered. (anchor=LINE_END)
         * 
         * @return this builder itself to chain calls to other builder methods.
         */
        public CellBuilder right() {
            constraints.anchor = GridBagConstraints.LINE_END;
            return this;
        }

        /**
         * Set the orientation of the component inside the cell to right bottom.
         * (anchor=LINE_END)
         * 
         * @return this builder itself to chain calls to other builder methods.
         */
        public CellBuilder rightBottom() {
            constraints.anchor = GridBagConstraints.LAST_LINE_END;
            return this;
        }

        /**
         * Set the cell to expand to the maximum of width that is available (weightx=1.0).
         * 
         * This will lead to increase the available width of the cell but not the width of
         * the component itself. For also increasing the components widht, you have to use
         * fillWidth().
         * 
         * @return this builder itself to chain calls to other builder methods.
         */
        public CellBuilder expandWidth() {
            constraints.weightx = 1.0; // this is kind of the maximum
            return this;
        }

        /**
         * Set the cell to expand to the maximum of height that is available
         * (weighty=1.0).
         * 
         * This will lead to increase the available height of the cell but not the height
         * of the component itself. For also increasing the components height, you have to
         * use fillHeight().
         * 
         * @return this builder itself to chain calls to other builder methods.
         */
        public CellBuilder expandHeight() {
            constraints.weighty = 1.0; // this is kind of the maximum
            return this;
        }

        /**
         * Set the component to horizintaly fill all available space of the grid cell.
         * (fill=HORIZONTAL)
         * 
         * @return this builder itself to chain calls to other builder methods.
         */
        public CellBuilder fillWidth() {
            constraints.fill = GridBagConstraints.HORIZONTAL;
            return this;
        }

        /**
         * Finally add (after all constraint settings have been set on the cell!) the
         * component to the grid cell.
         * 
         * @param component
         *            the component to add to the cell on the panel using the current set
         *            {@link GridBagConstraints}.
         */
        public void add(final Component component) {
            panel.add(component, constraints);
        }

    }

}
