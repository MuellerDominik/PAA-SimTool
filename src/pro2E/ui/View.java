package pro2E.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import pro2E.PAASimTool;
import pro2E.controller.Controller;
import pro2E.model.Model;
import pro2E.ui.simulation.SimPanel;

/**
 * <pre>
 * The <b><code>View</code></b> class builds the ui.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class View extends JPanel implements Observer, ChangeListener {

	private static final long serialVersionUID = 1L;

	private Controller controller;

	public List<SimPanel> simPanel = new ArrayList<>();

	public MenuBar menuBar = new MenuBar();
	public ToolBar toolBar = new ToolBar();
	public JTabbedPane simTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT);
	public StatusBar statusBar = new StatusBar();

	public JFileChooser jFileChooser = new JFileChooser();
	private FileFilter fileFilter = new FileNameExtensionFilter("Comma-Separated Values (*.csv)", "csv");

	/**
	 * <pre>
	 * Builds the ui.
	 * </pre>
	 * 
	 * @param frame
	 */
	public View(PAASimTool frame) {
		super(new BorderLayout());

		this.menuBar.setParents(frame, this);
		this.toolBar.setParents(frame, this);

		this.jFileChooser.addChoosableFileFilter(this.fileFilter);
		this.jFileChooser.setAcceptAllFileFilterUsed(false);

		add(this.toolBar, BorderLayout.PAGE_START);
		add(this.simTabbedPane, BorderLayout.CENTER);
		add(this.statusBar, BorderLayout.PAGE_END);
	}

	/**
	 * <pre>
	 * - Sets the controller for all the necessary ui classes
	 * - Adds a ChangeListener to the simTabbedPane object
	 * </pre>
	 * 
	 * @param controller
	 */
	public void setController(Controller controller) {
		this.controller = controller;
		this.simTabbedPane.addChangeListener(this);
		this.menuBar.setController(controller);
		this.toolBar.setController(controller);
	}

	/**
	 * <pre>
	 * - Object provides the simulation index as an integer
	 * - Updates the corresponding SimPanel object
	 * </pre>
	 */
	@Override
	public void update(Observable obs, Object obj) {
		Model model = (Model) obs;
		int simIndex = (int) obj;
		this.simPanel.get(simIndex).update(model, simIndex);
	}

	/**
	 * <pre>
	 * Notifies the controller about the currently selected tab, whenever tabs are switched.
	 * </pre>
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		this.controller.setSimIndex(this.simTabbedPane.getSelectedIndex());
	}

}
