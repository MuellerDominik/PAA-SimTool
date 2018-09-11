package pro2E.ui.simulation.input;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import pro2E.controller.Controller;
import pro2E.model.AntennaArray;
import pro2E.ui.SpecializedTextFields.JEngineeringTextField;
import pro2E.ui.SpecializedTextFields.JIntegerTextField;

/**
 * <pre>
 * The <b><code>GeometryPanel</code></b> class builds the geometry panel.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class GeometryPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Controller controller;

	private Color background = Color.WHITE;

	private int col = 10;

	private JLabel lbGeometry = new JLabel("Geometry:");
	String cbGeometryList[] = { "Linear", "Grid", "Circle", "Square", "Custom" };
	public JComboBox<String> cbGeometry = new JComboBox<>(cbGeometryList);

	private JLabel lbNumberOfRadiators = new JLabel("Radiators:");
	public JIntegerTextField tfNumberOfRadiators = new JIntegerTextField(col);
	private JLabel lbNumberOfRadiatorsUnit = new JLabel("[ ]");

	private JLabel lbNumberOfHorizontalRadiators = new JLabel("Hor. Radiators:");
	public JIntegerTextField tfNumberOfHorizontalRadiators = new JIntegerTextField(col);
	private JLabel lbNumberOfHorizontalRadiatorsUnit = new JLabel("[ ]");

	private JLabel lbNumberOfVerticalRadiators = new JLabel("Vert. Radiators:");
	public JIntegerTextField tfNumberOfVerticalRadiators = new JIntegerTextField(col);
	private JLabel lbNumberOfVerticalRadiatorsUnit = new JLabel("[ ]");

	private JLabel lbHorizontalSpacing = new JLabel("Hor. Spacing:");
	public JEngineeringTextField tfHorizontalSpacing = new JEngineeringTextField(col);
	private JLabel lbHorizontalSpacingUnit = new JLabel("[m]");

	private JLabel lbVerticalSpacing = new JLabel("Vert. Spacing:");
	public JEngineeringTextField tfVerticalSpacing = new JEngineeringTextField(col);
	private JLabel lbVerticalSpacingUnit = new JLabel("[m]");

	private JLabel lbRadius = new JLabel("Radius:");
	public JEngineeringTextField tfRadius = new JEngineeringTextField(col);
	private JLabel lbRadiusUnit = new JLabel("[m]");

	private JLabel lbSideLength = new JLabel("Side Length:");
	public JEngineeringTextField tfSideLength = new JEngineeringTextField(col);
	private JLabel lbSideLengthUnit = new JLabel("[m]");

	private String warning = "Warning, changes will reset the radiators in the array!";
	private JTextArea lbWarning = new JTextArea(warning);

	public ActionListener alResetSimulation;
	public ItemListener ilResetSimulation;

	/**
	 * <pre>
	 * Build the geometry panel.
	 * </pre>
	 */
	public GeometryPanel() {
		setLayout(new GridBagLayout());
		setBackground(background);

		add(this.lbGeometry, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.cbGeometry, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.cbGeometry.setBackground(background);

		add(this.lbNumberOfRadiators, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.tfNumberOfRadiators, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.lbNumberOfRadiatorsUnit, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfNumberOfRadiators.setRange(1, 100);

		add(this.lbNumberOfHorizontalRadiators, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.tfNumberOfHorizontalRadiators, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.lbNumberOfHorizontalRadiatorsUnit, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfNumberOfHorizontalRadiators.setRange(1, 100);

		add(this.lbNumberOfVerticalRadiators, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.tfNumberOfVerticalRadiators, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.lbNumberOfVerticalRadiatorsUnit, new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfNumberOfVerticalRadiators.setRange(1, 100);

		add(this.lbRadius, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.tfRadius, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.lbRadiusUnit, new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfRadius.setRange(1e-3, 1e3);

		add(this.lbSideLength, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.tfSideLength, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.lbSideLengthUnit, new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfSideLength.setRange(1e-3, 1e3);

		add(this.lbHorizontalSpacing, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.tfHorizontalSpacing, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.lbHorizontalSpacingUnit, new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfHorizontalSpacing.setRange(1e-3, 1e3);

		add(this.lbVerticalSpacing, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.tfVerticalSpacing, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.lbVerticalSpacingUnit, new GridBagConstraints(2, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfVerticalSpacing.setRange(1e-3, 1e3);

		add(new JLabel(), new GridBagConstraints(0, 5, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

		add(this.lbWarning, new GridBagConstraints(0, 6, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.lbWarning.setForeground(new Color(139, 0, 0));
		this.lbWarning.setLineWrap(true);
		this.lbWarning.setWrapStyleWord(true);
		this.lbWarning.setEditable(false);

		add(new JLabel(), new GridBagConstraints(0, 7, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

		this.alResetSimulation = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (cbGeometry.getSelectedIndex() != AntennaArray.LINEAR) {
					controller.disableAntennaLobe();
				}
				controller.updateSimulation(Controller.RESET);
			}
		};

		this.ilResetSimulation = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					hideFields();
					controller.disableAntennaLobe();
					controller.updateSimulation(Controller.RESET);
				}
			}
		};
	}

	/**
	 * <pre>
	 * Hides/Shows input fields.
	 * </pre>
	 */
	public void hideFields() {
		this.lbNumberOfRadiators.setVisible(false);
		this.tfNumberOfRadiators.setVisible(false);
		this.lbNumberOfRadiatorsUnit.setVisible(false);

		this.lbNumberOfHorizontalRadiators.setVisible(false);
		this.tfNumberOfHorizontalRadiators.setVisible(false);
		this.lbNumberOfHorizontalRadiatorsUnit.setVisible(false);

		this.lbNumberOfVerticalRadiators.setVisible(false);
		this.tfNumberOfVerticalRadiators.setVisible(false);
		this.lbNumberOfVerticalRadiatorsUnit.setVisible(false);

		this.lbRadius.setVisible(false);
		this.tfRadius.setVisible(false);
		this.lbRadiusUnit.setVisible(false);

		this.lbSideLength.setVisible(false);
		this.tfSideLength.setVisible(false);
		this.lbSideLengthUnit.setVisible(false);

		this.lbHorizontalSpacing.setVisible(false);
		this.tfHorizontalSpacing.setVisible(false);
		this.lbHorizontalSpacingUnit.setVisible(false);

		this.lbVerticalSpacing.setVisible(false);
		this.tfVerticalSpacing.setVisible(false);
		this.lbVerticalSpacingUnit.setVisible(false);

		if (this.cbGeometry.getSelectedIndex() == AntennaArray.LINEAR) {
			this.lbNumberOfHorizontalRadiators.setVisible(true);
			this.tfNumberOfHorizontalRadiators.setVisible(true);
			this.lbNumberOfHorizontalRadiatorsUnit.setVisible(true);

			this.lbHorizontalSpacing.setVisible(true);
			this.tfHorizontalSpacing.setVisible(true);
			this.lbHorizontalSpacingUnit.setVisible(true);
		} else if (this.cbGeometry.getSelectedIndex() == AntennaArray.GRID) {
			this.lbNumberOfHorizontalRadiators.setVisible(true);
			this.tfNumberOfHorizontalRadiators.setVisible(true);
			this.lbNumberOfHorizontalRadiatorsUnit.setVisible(true);

			this.lbNumberOfVerticalRadiators.setVisible(true);
			this.tfNumberOfVerticalRadiators.setVisible(true);
			this.lbNumberOfVerticalRadiatorsUnit.setVisible(true);

			this.lbHorizontalSpacing.setVisible(true);
			this.tfHorizontalSpacing.setVisible(true);
			this.lbHorizontalSpacingUnit.setVisible(true);

			this.lbVerticalSpacing.setVisible(true);
			this.tfVerticalSpacing.setVisible(true);
			this.lbVerticalSpacingUnit.setVisible(true);
		} else if (this.cbGeometry.getSelectedIndex() == AntennaArray.CIRCLE) {
			this.lbNumberOfRadiators.setVisible(true);
			this.tfNumberOfRadiators.setVisible(true);
			this.lbNumberOfRadiatorsUnit.setVisible(true);

			this.lbRadius.setVisible(true);
			this.tfRadius.setVisible(true);
			this.lbRadiusUnit.setVisible(true);
		} else if (this.cbGeometry.getSelectedIndex() == AntennaArray.SQUARE) {
			this.lbNumberOfRadiators.setVisible(true);
			this.tfNumberOfRadiators.setVisible(true);
			this.lbNumberOfRadiatorsUnit.setVisible(true);

			this.lbSideLength.setVisible(true);
			this.tfSideLength.setVisible(true);
			this.lbSideLengthUnit.setVisible(true);
		}
	}

	/**
	 * <pre>
	 * Registers all the corresponding listeners.
	 * </pre>
	 */
	public void registerListeners() {
		this.cbGeometry.addItemListener(this.ilResetSimulation);
		this.tfNumberOfRadiators.addActionListener(this.alResetSimulation);
		this.tfNumberOfHorizontalRadiators.addActionListener(this.alResetSimulation);
		this.tfNumberOfVerticalRadiators.addActionListener(this.alResetSimulation);
		this.tfHorizontalSpacing.addActionListener(this.alResetSimulation);
		this.tfVerticalSpacing.addActionListener(this.alResetSimulation);
		this.tfRadius.addActionListener(this.alResetSimulation);
		this.tfSideLength.addActionListener(this.alResetSimulation);
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
