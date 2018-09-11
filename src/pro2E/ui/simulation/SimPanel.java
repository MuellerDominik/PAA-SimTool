package pro2E.ui.simulation;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import pro2E.controller.Controller;
import pro2E.model.Model;

/**
 * <pre>
 * The <b><code>SimPanel</code></b> class builds the simulation panel.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class SimPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Color background = Color.WHITE;

	public InputPanel inputPanel = new InputPanel();
	public RadiatorPanel radiatorPanel = new RadiatorPanel();
	public ArrayPanel arrayPanel = new ArrayPanel();
	public OutputPanel outputPanel = new OutputPanel();

	/**
	 * <pre>
	 * Builds the simulation panel.
	 * </pre>
	 */
	public SimPanel() {
		setLayout(new GridBagLayout());
		setBackground(background);

		add(this.inputPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.VERTICAL, new Insets(10, 10, 5, 5), 0, 0));

		add(this.radiatorPanel, new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 5, 5, 5), 0, 0));

		add(this.arrayPanel, new GridBagConstraints(0, 1, 2, 1, 0.1, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 10, 10, 5), 0, 0));

		add(this.outputPanel, new GridBagConstraints(2, 0, 1, 2, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 5, 10, 10), 0, 0));
	}

	/**
	 * <pre>
	 * Sets the controller for all the necessary panels on top of the simulation panel.
	 * </pre>
	 * 
	 * @param controller
	 */
	public void setController(Controller controller) {
		this.inputPanel.setController(controller);
		this.radiatorPanel.setController(controller);
		this.arrayPanel.setController(controller);
		this.outputPanel.setController(controller);
	}

	/**
	 * <pre>
	 * Calls the update method of all the necessary panels.
	 * </pre>
	 * 
	 * @param model
	 * @param simIndex
	 */
	public void update(Model model, int simIndex) {
		this.inputPanel.update(model, simIndex);
		this.radiatorPanel.update(model, simIndex);
		this.arrayPanel.update(model, simIndex);
		this.outputPanel.update(model, simIndex);
	}

}
