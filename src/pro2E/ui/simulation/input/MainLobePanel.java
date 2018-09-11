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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pro2E.controller.Controller;
import pro2E.ui.SpecializedTextFields.JEngineeringTextField;

/**
 * <pre>
 * The <b><code>MainLobePanel</code></b> class builds the main lobe panel.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class MainLobePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Controller controller;

	private Color background = Color.WHITE;

	private int col = 10;

	private JLabel lbMainLobe = new JLabel("Main Lobe Angle:");
	public JRadioButton rbOn = new JRadioButton("On");
	public JRadioButton rbOff = new JRadioButton("Off");

	private ButtonGroup btGroup = new ButtonGroup();

	private JLabel lbMainLobeAngle = new JLabel("Main Lobe Angle:");
	public JEngineeringTextField tfMainLobeAngle = new JEngineeringTextField(col);
	private JLabel lbMainLobeAngleUnit = new JLabel("[°]");

	public JSlider sliderMainLobeAngle = new JSlider(0, 180);

	public ItemListener ilMainLobeState;
	private ActionListener alUpdateSimulation;
	private ChangeListener clMainLobeAngle;
	private MouseWheelListener mwlMainLobeAngle;

	/**
	 * <pre>
	 * Builds the main lobe panel.
	 * </pre>
	 */
	public MainLobePanel() {
		setLayout(new GridBagLayout());
		setBackground(background);

		add(this.lbMainLobe, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.rbOn, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.rbOff, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.rbOn.setBackground(background);
		this.rbOff.setBackground(background);
		this.btGroup.add(this.rbOn);
		this.btGroup.add(this.rbOff);

		add(this.lbMainLobeAngle, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.tfMainLobeAngle, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.lbMainLobeAngleUnit, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfMainLobeAngle.setRange(0.0, 180.0);

		add(this.sliderMainLobeAngle, new GridBagConstraints(0, 2, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.sliderMainLobeAngle.setBackground(background);
		this.sliderMainLobeAngle.setMajorTickSpacing(30);
		this.sliderMainLobeAngle.setMinorTickSpacing(10);
		this.sliderMainLobeAngle.setPaintTicks(true);
		this.sliderMainLobeAngle.setPaintLabels(true);

		add(new JLabel(), new GridBagConstraints(0, 3, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

		this.ilMainLobeState = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					hideFields();
					controller.updateSimulation(Controller.UPDATE);
				}
			}
		};

		this.alUpdateSimulation = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sliderMainLobeAngle.setValue((int) tfMainLobeAngle.getValue());
				controller.updateSimulation(Controller.UPDATE);
			}
		};

		this.clMainLobeAngle = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (sliderMainLobeAngle.getValueIsAdjusting()) {
					tfMainLobeAngle.setValue(sliderMainLobeAngle.getValue());
					controller.updateSimulation(Controller.UPDATE);
				}
			}
		};

		this.mwlMainLobeAngle = new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 1) {
					int newValue = sliderMainLobeAngle.getValue() + 1;
					if (newValue <= sliderMainLobeAngle.getMaximum()) {
						sliderMainLobeAngle.setValue(newValue);
						tfMainLobeAngle.setValue(sliderMainLobeAngle.getValue());
						controller.updateSimulation(Controller.UPDATE);
					}
				} else {
					int newValue = sliderMainLobeAngle.getValue() - 1;
					if (newValue >= sliderMainLobeAngle.getMinimum()) {
						sliderMainLobeAngle.setValue(newValue);
						tfMainLobeAngle.setValue(sliderMainLobeAngle.getValue());
						controller.updateSimulation(Controller.UPDATE);
					}
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
		this.lbMainLobeAngle.setEnabled(false);
		this.tfMainLobeAngle.setEnabled(false);
		this.lbMainLobeAngleUnit.setEnabled(false);

		this.sliderMainLobeAngle.setEnabled(false);

		if (this.rbOn.isSelected()) {
			this.lbMainLobeAngle.setEnabled(true);
			this.tfMainLobeAngle.setEnabled(true);
			this.lbMainLobeAngleUnit.setEnabled(true);

			this.sliderMainLobeAngle.setEnabled(true);
		}
	}

	/**
	 * <pre>
	 * Registers all the corresponding listeners.
	 * </pre>
	 */
	public void registerListeners() {
		this.rbOn.addItemListener(this.ilMainLobeState);
		this.rbOff.addItemListener(this.ilMainLobeState);

		this.tfMainLobeAngle.addActionListener(this.alUpdateSimulation);

		this.sliderMainLobeAngle.addChangeListener(this.clMainLobeAngle);
		this.sliderMainLobeAngle.addMouseWheelListener(this.mwlMainLobeAngle);
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
