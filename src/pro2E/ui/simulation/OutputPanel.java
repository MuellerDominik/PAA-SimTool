package pro2E.ui.simulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pro2E.DPIFixV3;
import pro2E.controller.Controller;
import pro2E.model.Model;
import pro2E.ui.CustomBorder;
import pro2E.ui.simulation.output.DiagramPanel;
import pro2E.ui.simulation.output.RadiatorTypePanel;
import pro2E.ui.simulation.output.TablePanel;

/**
 * <pre>
 * The <b><code>OutputPanel</code></b> class builds the output panel.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class OutputPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Color background = Color.WHITE;

	private JTabbedPane tabbedPane = new JTabbedPane();

	public DiagramPanel diagramPanel = new DiagramPanel();
	public RadiatorTypePanel radiatorTypePanel = new RadiatorTypePanel();
	private TablePanel tablePanel = new TablePanel();

	/**
	 * <pre>
	 * Builds the output panel.
	 * </pre>
	 */
	public OutputPanel() {
		setBorder(CustomBorder.createBorder("Output"));
		setPreferredSize(new Dimension(2 * DPIFixV3.screenSize.width / 5, 5 * DPIFixV3.screenSize.height / 6));
		setLayout(new BorderLayout());
		setSize(new Dimension(800, 1000));
		setBackground(background);

		this.tabbedPane.setBackground(background);
		this.tabbedPane.addTab("Diagram", this.diagramPanel);
		this.tabbedPane.addTab("Radiator Type", this.radiatorTypePanel);
		this.tabbedPane.addTab("Table", this.tablePanel);
		add(this.tabbedPane, BorderLayout.CENTER);
	}

	/**
	 * <pre>
	 * Sets the controller.
	 * </pre>
	 * 
	 * @param controller
	 */
	public void setController(Controller controller) {
		this.diagramPanel.setController(controller);
	}

	/**
	 * <pre>
	 * Calls the corresponding update methods of all the panels on top of the output panel.
	 * </pre>
	 * 
	 * @param model
	 * @param simIndex
	 */
	public void update(Model model, int simIndex) {
		this.diagramPanel.update(model, simIndex);
		this.radiatorTypePanel.update(model, simIndex);
		this.tablePanel.update(model, simIndex);
	}

}
