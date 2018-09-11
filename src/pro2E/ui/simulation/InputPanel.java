package pro2E.ui.simulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pro2E.DPIFixV3;
import pro2E.controller.Controller;
import pro2E.model.AntennaArray;
import pro2E.model.Model;
import pro2E.ui.CustomBorder;
import pro2E.ui.simulation.input.GeometryPanel;
import pro2E.ui.simulation.input.MainLobePanel;
import pro2E.ui.simulation.input.PropertiesPanel;
import pro2E.ui.simulation.input.SideLobePanel;

/**
 * <pre>
 * The <b><code>InputPanel</code></b> class builds the input panel.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class InputPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Controller controller;

	private Color background = Color.WHITE;

	public JTabbedPane tabbedPane = new JTabbedPane();

	public GeometryPanel geometryPanel = new GeometryPanel();
	public PropertiesPanel propertiesPanel = new PropertiesPanel();
	public MainLobePanel mainLobePanel = new MainLobePanel();
	public SideLobePanel sideLobePanel = new SideLobePanel();

	/**
	 * <pre>
	 * Builds the input panel.
	 * </pre>
	 */
	public InputPanel() {
		setBorder(CustomBorder.createBorder("Input"));
		setPreferredSize(new Dimension(DPIFixV3.screenSize.width / 5, 5 * DPIFixV3.screenSize.height / 14));
		setMinimumSize(new Dimension((int) (DPIFixV3.screenSize.width / 4.8), 5 * DPIFixV3.screenSize.height / 14));
		setLayout(new BorderLayout());
		setBackground(background);

		this.tabbedPane.setBackground(background);
		this.tabbedPane.addTab("Geometry", geometryPanel);
		this.tabbedPane.addTab("Properties", propertiesPanel);
		this.tabbedPane.addTab("Main Lobe", mainLobePanel);
		this.tabbedPane.addTab("Side Lobe", sideLobePanel);

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
		this.controller = controller;

		this.geometryPanel.setController(controller);
		this.propertiesPanel.setController(controller);
		this.mainLobePanel.setController(controller);
		this.sideLobePanel.setController(controller);
	}

	/**
	 * <pre>
	 * Checks after an update if the geometry is 'Linear':
	 * - If not, disable the main lobe and side lobe panels.
	 * </pre>
	 *
	 * @param model
	 * @param simIndex
	 */
	public void update(Model model, int simIndex) {
		double[] config = this.controller.getConfig(simIndex);
		if (((int) config[3]) == AntennaArray.LINEAR) {
			this.tabbedPane.setEnabledAt(2, true);
			this.tabbedPane.setEnabledAt(3, true);
		} else {
			this.tabbedPane.setEnabledAt(2, false);
			this.tabbedPane.setEnabledAt(3, false);
		}
	}

}
