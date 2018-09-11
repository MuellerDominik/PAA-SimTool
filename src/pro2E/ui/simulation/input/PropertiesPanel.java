package pro2E.ui.simulation.input;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pro2E.controller.Controller;
import pro2E.controller.Settings;
import pro2E.model.AntennaArray;
import pro2E.model.AntennaArrayFunctions;
import pro2E.ui.SpecializedTextFields.JEngineeringTextField;

/**
 * <pre>
 * The <b><code>PropertiesPanel</code></b> class builds the properties panel.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class PropertiesPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Controller controller;

	private Color background = Color.WHITE;

	private int col = 10;

	// Wavelength / Frequency
	public JLabel lbWavelengthFrequency = new JLabel("Wavelength:");
	public JEngineeringTextField tfWavelengthFrequency = new JEngineeringTextField(col);
	public JLabel lbWavelengthFrequencyUnit = new JLabel("[m]");

	// Radiator Type
	private JLabel lbRadiatorType = new JLabel("Radiator Type:");
	private String[] cbRadiatorTypeList = { "Isotropic", "Half-Wave Dipole" };
	public JComboBox<String> cbRadiatorType = new JComboBox<>(cbRadiatorTypeList);;

	private JLabel lbRadiatorAngle = new JLabel("Dipole Angle:");
	public JEngineeringTextField tfRadiatorAngle = new JEngineeringTextField(col);
	private JLabel lbRadiatorAngleUnit = new JLabel("[°]");

	public JSlider sliderRadiatorAngle = new JSlider(-90, 90);

	// Reflector
	private JLabel lbReflectorState = new JLabel("Reflector:");
	public JRadioButton rbOn = new JRadioButton("On");
	public JRadioButton rbOff = new JRadioButton("Off");

	private ButtonGroup btGroup = new ButtonGroup();

	private JLabel lbVerticalIntercept = new JLabel("Vertical Intercept:");
	public JEngineeringTextField tfVerticalIntercept = new JEngineeringTextField(col);
	private JLabel lbVerticalInterceptUnit = new JLabel("[m]");

	// Listeners
	private ActionListener alUpdateSimulation;
	private ItemListener ilUpdateSimulation;
	private ChangeListener clRadiatorAngle;
	private MouseWheelListener mwlRadiatorAngle;
	private ItemListener ilReflectorState;

	/**
	 * <pre>
	 * Builds the properties panel.
	 * </pre>
	 */
	public PropertiesPanel() {
		setLayout(new GridBagLayout());
		setBackground(background);

		// Wavelength / Frequency
		add(this.lbWavelengthFrequency, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.tfWavelengthFrequency, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.lbWavelengthFrequencyUnit, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfWavelengthFrequency.setRange(1e-3, 300e9);

		add(new JLabel(), new GridBagConstraints(0, 1, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

		// Radiator Type
		add(this.lbRadiatorType, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.cbRadiatorType, new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.cbRadiatorType.setBackground(background);

		add(this.lbRadiatorAngle, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.tfRadiatorAngle, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.lbRadiatorAngleUnit, new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfRadiatorAngle.setRange(-90.0, 90.0);

		add(this.sliderRadiatorAngle, new GridBagConstraints(0, 4, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.sliderRadiatorAngle.setBackground(background);
		this.sliderRadiatorAngle.setMajorTickSpacing(30);
		this.sliderRadiatorAngle.setMinorTickSpacing(10);
		this.sliderRadiatorAngle.setPaintTicks(true);
		this.sliderRadiatorAngle.setPaintLabels(true);

		add(new JLabel(), new GridBagConstraints(0, 5, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

		// Reflector
		add(this.lbReflectorState, new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.rbOn, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.rbOff, new GridBagConstraints(2, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.rbOn.setBackground(background);
		this.rbOff.setBackground(background);

		this.btGroup.add(this.rbOn);
		this.btGroup.add(this.rbOff);

		add(this.lbVerticalIntercept, new GridBagConstraints(0, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.tfVerticalIntercept, new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.lbVerticalInterceptUnit, new GridBagConstraints(2, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfVerticalIntercept.setRange(-1e3, 1e3);

		add(new JLabel(), new GridBagConstraints(0, 8, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

		this.alUpdateSimulation = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sliderRadiatorAngle.setValue((int) tfRadiatorAngle.getValue());
				controller.updateSimulation(Controller.UPDATE);
			}
		};

		this.ilUpdateSimulation = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					hideFields();
					controller.updateSimulation(Controller.UPDATE);
				}
			}
		};

		this.clRadiatorAngle = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (sliderRadiatorAngle.getValueIsAdjusting()) {
					tfRadiatorAngle.setValue(sliderRadiatorAngle.getValue());
					controller.updateSimulation(Controller.UPDATE);
				}
			}
		};

		this.mwlRadiatorAngle = new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 1) {
					int newValue = sliderRadiatorAngle.getValue() + 1;
					if (newValue <= sliderRadiatorAngle.getMaximum()) {
						sliderRadiatorAngle.setValue(newValue);
						tfRadiatorAngle.setValue(sliderRadiatorAngle.getValue());
						controller.updateSimulation(Controller.UPDATE);
					}
				} else {
					int newValue = sliderRadiatorAngle.getValue() - 1;
					if (newValue >= sliderRadiatorAngle.getMinimum()) {
						sliderRadiatorAngle.setValue(newValue);
						tfRadiatorAngle.setValue(sliderRadiatorAngle.getValue());
						controller.updateSimulation(Controller.UPDATE);
					}
				}
			}
		};

		this.ilReflectorState = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					hideFields();
					controller.updateSimulation(Controller.UPDATE);
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
		this.lbRadiatorAngle.setVisible(false);
		this.tfRadiatorAngle.setVisible(false);
		this.lbRadiatorAngleUnit.setVisible(false);

		this.sliderRadiatorAngle.setVisible(false);

		if (this.cbRadiatorType.getSelectedIndex() == AntennaArray.DIPOLE) {
			this.lbRadiatorAngle.setVisible(true);
			this.tfRadiatorAngle.setVisible(true);
			this.lbRadiatorAngleUnit.setVisible(true);

			this.sliderRadiatorAngle.setVisible(true);
		}

		this.lbVerticalIntercept.setEnabled(false);
		this.tfVerticalIntercept.setEnabled(false);
		this.lbVerticalInterceptUnit.setEnabled(false);

		if (this.rbOn.isSelected()) {
			this.lbVerticalIntercept.setEnabled(true);
			this.tfVerticalIntercept.setEnabled(true);
			this.lbVerticalInterceptUnit.setEnabled(true);
		}
	}

	/**
	 * <pre>
	 * Registers all the corresponding listeners.
	 * </pre>
	 */
	public void registerListeners() {
		this.tfWavelengthFrequency.addActionListener(this.alUpdateSimulation);

		this.cbRadiatorType.addItemListener(this.ilUpdateSimulation);
		this.tfRadiatorAngle.addActionListener(this.alUpdateSimulation);
		this.sliderRadiatorAngle.addChangeListener(this.clRadiatorAngle);
		this.sliderRadiatorAngle.addMouseWheelListener(this.mwlRadiatorAngle);

		this.rbOn.addItemListener(this.ilReflectorState);
		this.rbOff.addItemListener(this.ilReflectorState);
		this.tfVerticalIntercept.addActionListener(this.alUpdateSimulation);
	}

	/**
	 * <pre>
	 * Change the electromagnetic radiation unit toe the given parameter.
	 * </pre>
	 * 
	 * @param electromagneticRadiationUnit
	 */
	public void setElectromagneticRadiationUnit(int electromagneticRadiationUnit) {
		if (electromagneticRadiationUnit == Settings.WAVELENGTH) {
			double frequency = this.tfWavelengthFrequency.getValue();
			this.tfWavelengthFrequency.setValue(AntennaArrayFunctions.wavelength(frequency));
			this.lbWavelengthFrequency.setText("Wavelength:");
			this.lbWavelengthFrequencyUnit.setText("[m]");
		} else {
			double wavelength = this.tfWavelengthFrequency.getValue();
			this.tfWavelengthFrequency.setValue(AntennaArrayFunctions.frequency(wavelength));
			this.lbWavelengthFrequency.setText("Frequency:");
			this.lbWavelengthFrequencyUnit.setText("[Hz]");
		}
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
		if (controller.getElectromagneticRadiationUnit() != Settings.WAVELENGTH) {
			this.setElectromagneticRadiationUnit(controller.getElectromagneticRadiationUnit());
		}
	}

}
