package pro2E.ui.simulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import pro2E.DPIFixV3;
import pro2E.controller.Controller;
import pro2E.model.AntennaArray;
import pro2E.model.Model;
import pro2E.ui.CustomBorder;
import pro2E.ui.SpecializedTextFields.JEngineeringTextField;

/**
 * <pre>
 * The <b><code>RadiatorPanel</code></b> class builds the radiator panel.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class RadiatorPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Controller controller;

	private Color background = Color.WHITE;

	private int col = 10;

	public JPanel tablePanel = new JPanel();
	public DefaultTableModel tableModel = new DefaultTableModel();
	public JTable table = new JTable(tableModel);
	public JScrollPane scrollpane = new JScrollPane(table);

	public JPanel paramPanel = new JPanel();

	private JLabel lbXPos = new JLabel("x-Position:");
	public JEngineeringTextField tfXPos = new JEngineeringTextField(col);
	private JLabel lbXPosUnit = new JLabel("[m]");

	private JLabel lbYPos = new JLabel("y-Position:");
	public JEngineeringTextField tfYPos = new JEngineeringTextField(col);
	private JLabel lbYPosUnit = new JLabel("[m]");

	private JLabel lbAmplitude = new JLabel("Amplitude:");
	public JEngineeringTextField tfAmplitude = new JEngineeringTextField(col);
	private JLabel lbAmplitudeUnit = new JLabel("[ ]");

	private JLabel lbPhase = new JLabel("Phase:");
	public JEngineeringTextField tfPhase = new JEngineeringTextField(col);
	private JLabel lbPhaseUnit = new JLabel("[°]");

	public JCheckBox cbHide = new JCheckBox("Hide");
	public JButton btDelete = new JButton("Delete");

	private ListSelectionModel listSelectionModel = table.getSelectionModel();

	private ListSelectionListener listSelectionListener;

	private ActionListener alUpdateRadiatorPosition;
	private ActionListener alUpdateRadiatorsAmplitude;
	private ActionListener alUpdateRadiatorsPhase;
	private ActionListener alSetRadiatorsState;
	private ActionListener alRemoveRadiators;

	private double[][] radiatorParameters;

	/**
	 * <pre>
	 * Builds the radiator panel.
	 * </pre>
	 */
	public RadiatorPanel() {
		setBorder(CustomBorder.createBorder("Radiators"));
		setLayout(new BorderLayout());
		setBackground(background);
		setPreferredSize(new Dimension(DPIFixV3.screenSize.width / 5, 5 * DPIFixV3.screenSize.height / 14));
		setMinimumSize(new Dimension(DPIFixV3.screenSize.width / 5, 0));

		add(this.tablePanel, BorderLayout.LINE_START);
		this.tablePanel.setLayout(new BorderLayout());
		this.tablePanel.add(scrollpane);
		this.scrollpane.setPreferredSize(new Dimension(DPIFixV3.screenSize.width / 20, 0));
		this.tableModel.addColumn("Radiator");
		this.table.setDefaultEditor(Object.class, null);
		this.table.setFillsViewportHeight(true);
		this.scrollpane.getViewport().setBackground(background);

		this.paramPanel.setLayout(new GridBagLayout());
		add(this.paramPanel, BorderLayout.CENTER);
		this.paramPanel.setBackground(background);

		this.paramPanel.add(this.lbXPos, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.tfXPos, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.lbXPosUnit, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

		this.paramPanel.add(this.lbYPos, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.tfYPos, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.lbYPosUnit, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

		this.paramPanel.add(this.lbAmplitude, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.tfAmplitude, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.lbAmplitudeUnit, new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.LINE_END, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

		this.paramPanel.add(this.lbPhase, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.tfPhase, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.lbPhaseUnit, new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

		this.paramPanel.add(this.cbHide, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START,
				GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.btDelete, new GridBagConstraints(1, 4, 2, 1, 1.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.cbHide.setBackground(background);

		this.paramPanel.add(new JLabel(), new GridBagConstraints(0, 5, 3, 1, 1.0, 1.0, GridBagConstraints.LINE_START,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

		this.alUpdateRadiatorPosition = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (radiatorParameters[selectedRow][0] != tfXPos.getValue()
						|| radiatorParameters[selectedRow][1] != tfYPos.getValue()) {
					controller.updateRadiatorPosition(table.getSelectedRow(), tfXPos.getValue(), tfYPos.getValue());
				}
			}
		};

		this.alUpdateRadiatorsAmplitude = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.updateRadiatorsAmplitude(table.getSelectedRows(), tfAmplitude.getValue());
			}
		};

		this.alUpdateRadiatorsPhase = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.updateRadiatorsPhase(table.getSelectedRows(), tfPhase.getValue());
			}
		};

		this.alSetRadiatorsState = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setRadiatorsState(table.getSelectedRows(), cbHide.isSelected());
			}
		};

		this.alRemoveRadiators = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.removeRadiators(table.getSelectedRows());
			}
		};

		this.listSelectionListener = new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					setParameters();
				}
			}
		};

		this.registerListeners();
	}

	/**
	 * <pre>
	 * Sets the values of the corresponding text fields when a selection on the table is made.
	 * </pre>
	 */
	public void setParameters() {
		int[] radiatorIndices = this.table.getSelectedRows();
		if (radiatorIndices.length == 0) {
			this.enableAllFields(false);

			this.tfXPos.setText("");
			this.tfYPos.setText("");
			this.tfAmplitude.setText("");
			this.tfPhase.setText("");
			this.cbHide.setSelected(false);
		} else {
			this.enableAllFields(true);
			if (radiatorIndices.length > 1) {
				this.lbXPos.setEnabled(false);
				this.tfXPos.setEnabled(false);
				this.lbXPosUnit.setEnabled(false);

				this.lbYPos.setEnabled(false);
				this.tfYPos.setEnabled(false);
				this.lbYPosUnit.setEnabled(false);
			}

			this.tfXPos.setValue(this.radiatorParameters[radiatorIndices[0]][0]);
			this.tfYPos.setValue(this.radiatorParameters[radiatorIndices[0]][1]);
			this.tfAmplitude.setValue(this.radiatorParameters[radiatorIndices[0]][2]);
			this.tfPhase.setValue(Math.toDegrees(this.radiatorParameters[radiatorIndices[0]][3]));
			if (((int) this.radiatorParameters[radiatorIndices[0]][5]) == AntennaArray.VISIBLE) {
				this.cbHide.setSelected(false);
			} else {
				this.cbHide.setSelected(true);
			}
		}
	}

	/**
	 * <pre>
	 * Enables/Disables the ui elements on the panel according to the given boolean enabled.
	 * </pre>
	 * 
	 * @param enabled
	 */
	public void enableAllFields(boolean enabled) {
		this.lbXPos.setEnabled(enabled);
		this.tfXPos.setEnabled(enabled);
		this.lbXPosUnit.setEnabled(enabled);

		this.lbYPos.setEnabled(enabled);
		this.tfYPos.setEnabled(enabled);
		this.lbYPosUnit.setEnabled(enabled);

		this.lbAmplitude.setEnabled(enabled);
		this.tfAmplitude.setEnabled(enabled);
		this.lbAmplitudeUnit.setEnabled(enabled);

		this.lbPhase.setEnabled(enabled);
		this.tfPhase.setEnabled(enabled);
		this.lbPhaseUnit.setEnabled(enabled);

		this.cbHide.setEnabled(enabled);
		this.btDelete.setEnabled(enabled);
	}

	/**
	 * <pre>
	 * Registers all the corresponding listeners.
	 * </pre>
	 */
	public void registerListeners() {
		this.tfXPos.addActionListener(this.alUpdateRadiatorPosition);
		this.tfYPos.addActionListener(this.alUpdateRadiatorPosition);
		this.tfAmplitude.addActionListener(this.alUpdateRadiatorsAmplitude);
		this.tfPhase.addActionListener(this.alUpdateRadiatorsPhase);
		this.cbHide.addActionListener(this.alSetRadiatorsState);
		this.btDelete.addActionListener(this.alRemoveRadiators);
		this.listSelectionModel.addListSelectionListener(this.listSelectionListener);
	}

	/**
	 * <pre>
	 * - Fetches the radiator parameters from the model and stores them in an attribute
	 * - Manipulates the selection in the table
	 * </pre>
	 * 
	 * @param model
	 * @param simIndex
	 */
	public void update(Model model, int simIndex) {
		double[][] radiatorParameters = model.getRadiators(simIndex);
		int radiatorParametersLength;
		if (radiatorParameters.length == 0) {
			radiatorParametersLength = 0;
		} else {
			radiatorParametersLength = radiatorParameters[0].length;
		}
		this.radiatorParameters = new double[radiatorParameters.length][radiatorParametersLength];
		this.radiatorParameters = radiatorParameters;

		int oldRowCount = this.table.getRowCount();
		int[] oldSelectedRows = this.table.getSelectedRows();
		int oldSelectedRow = this.table.getSelectedRow();

		// adding rows according to the amount of radiators
		this.tableModel.setRowCount(0);
		for (int i = 0; i < radiatorParameters.length; i++) {
			this.tableModel.addRow(new Object[] { i + 1 });
		}

		if ((oldRowCount - radiatorParameters.length) > 0 && radiatorParameters.length != 0) { // deleting
			if (oldSelectedRow == 0) {
				this.table.changeSelection(0, 0, false, false);
			} else {
				if (oldSelectedRow > radiatorParameters.length - 1) {
					this.table.changeSelection(radiatorParameters.length - 1, 0, false, false);
				} else {
					if ((oldRowCount - radiatorParameters.length) > 1) {
						// deleting more than one radiator
						this.table.changeSelection(oldSelectedRow, 0, false, false);
					} else {
						this.table.changeSelection(oldSelectedRow - 1, 0, false, false);
					}
				}
			}
		} else if (((oldRowCount - radiatorParameters.length) == 0)) {
			for (int i = 0; i < oldSelectedRows.length; i++) {
				this.table.changeSelection(oldSelectedRows[i], 0, true, false);
			}
		} else {
			// deleting all radiators
			this.table.changeSelection(radiatorParameters.length - 1, 0, false, false);
		}

		this.setParameters();
	}

	/**
	 * <pre>
	 * Sets the controller.
	 * </pre>
	 * 
	 * @param controller
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

}
