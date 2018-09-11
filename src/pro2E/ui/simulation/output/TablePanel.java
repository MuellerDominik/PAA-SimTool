package pro2E.ui.simulation.output;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import pro2E.controller.Controller;
import pro2E.model.Model;
import pro2E.ui.SpecializedTextFields.EngineeringUtil;

/**
 * <pre>
 * The <b><code>TablePanel</code></b> class builds the table panel.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class TablePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Color background = Color.WHITE;

	Controller controller;

	public DefaultTableModel tableModel = new DefaultTableModel();
	public JTable table = new JTable(tableModel);
	public JScrollPane scrollpane = new JScrollPane(table);

	/**
	 * <pre>
	 * Builds the table panel.
	 * </pre>
	 */
	public TablePanel() {
		setLayout(new GridLayout());
		add(this.scrollpane);
		setBackground(background);
		this.scrollpane.getViewport().setBackground(background);
		this.tableModel.addColumn("Radiator");
		this.tableModel.addColumn("x-Position");
		this.tableModel.addColumn("y-Position");
		this.tableModel.addColumn("Amplitude");
		this.tableModel.addColumn("Phase");
		this.table.setDefaultEditor(Object.class, null);
		this.table.setFillsViewportHeight(true);
	}

	/**
	 * <pre>
	 * - Fetches all the radiator parameters from the model
	 * - Clears the whole table
	 * - Fills the table with the fetched radiator parameters
	 * </pre>
	 * 
	 * @param model
	 * @param simIndex
	 */
	public void update(Model model, int simIndex) {
		int dp = 3;
		double[][] radiatorParameters = model.getRadiators(simIndex);
		this.tableModel.setRowCount(0);
		for (int i = 0; i < radiatorParameters.length; i++) {
			String x = EngineeringUtil.convert(radiatorParameters[i][0], dp);
			String y = EngineeringUtil.convert(radiatorParameters[i][1], dp);
			String a = EngineeringUtil.convert(radiatorParameters[i][2], dp);
			String p = EngineeringUtil.convert(Math.toDegrees(radiatorParameters[i][3]), dp);
			this.tableModel.addRow(new Object[] { i + 1, x, y, a, p });
		}
	}

}
