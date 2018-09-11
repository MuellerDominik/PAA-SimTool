package pro2E.ui.simulation.input;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pro2E.controller.Controller;
import pro2E.model.AntennaArray;
import pro2E.ui.SpecializedTextFields.JEngineeringTextField;

/**
 * <pre>
 * The <b><code>SideLobePanel</code></b> class builds the side lobe panel.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class SideLobePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Controller controller;

	private Color background = Color.WHITE;

	private int col = 10;

	private JLabel lbSideLobe = new JLabel("Side Lobe Attenution:");
	public JRadioButton rbOn = new JRadioButton("On");
	public JRadioButton rbOff = new JRadioButton("Off");

	private ButtonGroup btGroup = new ButtonGroup();

	private JLabel lbWindowFunction = new JLabel("Window Function:");
	private String[] cbWindowFunctionList = { "Rectangular", "Dolph-Chebyshev", "Sine (Offset)", "Hann (Sine Squared)",
			"Hamming", "Triangular", "Parzen", "Blackman", "Welch" };
	public JComboBox<String> cbWindowFunction = new JComboBox<>(cbWindowFunctionList);

	private JLabel lbAttenuation = new JLabel("Attenuation:");
	private JLabel lbOffset = new JLabel("Offset:");
	public JEngineeringTextField tfAttenuationOffset = new JEngineeringTextField(col);
	private JLabel lbAttenuationUnit = new JLabel("[dB]");
	private JLabel lbOffsetUnit = new JLabel("[ ]");

	public ItemListener ilSideLobeState;
	private ItemListener ilWindowFunction;
	private ActionListener alUpdateSimulation;

	/**
	 * <pre>
	 * Builds the side lobe panel.
	 * </pre>
	 */
	public SideLobePanel() {
		setLayout(new GridBagLayout());
		setBackground(background);

		add(this.lbSideLobe, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.rbOn, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.rbOff, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.rbOn.setBackground(background);
		this.rbOff.setBackground(background);

		this.btGroup.add(this.rbOn);
		this.btGroup.add(this.rbOff);

		add(this.lbWindowFunction, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.cbWindowFunction, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.cbWindowFunction.setBackground(background);

		add(this.lbAttenuation, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.lbOffset, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.tfAttenuationOffset, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.lbAttenuationUnit, new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		add(this.lbOffsetUnit, new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_END,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

		add(new JLabel(), new GridBagConstraints(0, 3, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

		this.ilSideLobeState = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					hideFields();
					controller.updateSimulation(Controller.UPDATE);
				}
			}
		};

		this.ilWindowFunction = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					hideFields();
					adjustInputFields();
					controller.updateSimulation(Controller.UPDATE);
				}
			}
		};

		this.alUpdateSimulation = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.updateSimulation(Controller.UPDATE);
			}
		};
	}

	/**
	 * <pre>
	 * Adjust the range of the window parameter input field according to the selected window function.
	 * </pre>
	 */
	public void adjustInputFields() {
		if (this.cbWindowFunction.getSelectedIndex() == AntennaArray.DOLPH_CHEBYSHEV) {
			this.tfAttenuationOffset.setRange(0, 200);
			this.tfAttenuationOffset.setValue(30);
		}

		if (this.cbWindowFunction.getSelectedIndex() == AntennaArray.SINE) {
			this.tfAttenuationOffset.setRange(0, 1);
			this.tfAttenuationOffset.setValue(0);
		}
	}

	/**
	 * <pre>
	 * Hides/Shows input fields.
	 * </pre>
	 */
	public void hideFields() {
		this.lbWindowFunction.setEnabled(false);
		this.cbWindowFunction.setEnabled(false);

		this.lbAttenuation.setVisible(false);
		this.lbAttenuation.setEnabled(false);
		this.lbOffset.setVisible(false);
		this.lbOffset.setEnabled(false);
		this.tfAttenuationOffset.setVisible(false);
		this.tfAttenuationOffset.setEnabled(false);
		this.lbAttenuationUnit.setVisible(false);
		this.lbAttenuationUnit.setEnabled(false);
		this.lbOffsetUnit.setVisible(false);
		this.lbOffsetUnit.setEnabled(false);

		if (this.rbOn.isSelected()) {
			this.lbWindowFunction.setEnabled(true);
			this.cbWindowFunction.setEnabled(true);

			this.lbAttenuation.setEnabled(true);
			this.lbOffset.setEnabled(true);
			this.tfAttenuationOffset.setEnabled(true);
			this.lbAttenuationUnit.setEnabled(true);
			this.lbOffsetUnit.setEnabled(true);
		}

		if (this.cbWindowFunction.getSelectedIndex() == AntennaArray.DOLPH_CHEBYSHEV) {
			this.lbAttenuation.setVisible(true);
			this.tfAttenuationOffset.setVisible(true);
			this.lbAttenuationUnit.setVisible(true);
		}

		if (this.cbWindowFunction.getSelectedIndex() == AntennaArray.SINE) {
			this.lbOffset.setVisible(true);
			this.tfAttenuationOffset.setVisible(true);
			this.lbOffsetUnit.setVisible(true);
		}
	}

	/**
	 * <pre>
	 * Registers all the corresponding listeners.
	 * </pre>
	 */
	public void registerListeners() {
		this.rbOn.addItemListener(this.ilSideLobeState);
		this.rbOff.addItemListener(this.ilSideLobeState);

		this.cbWindowFunction.addItemListener(this.ilWindowFunction);

		this.tfAttenuationOffset.addActionListener(this.alUpdateSimulation);
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
