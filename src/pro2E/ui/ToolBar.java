package pro2E.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import pro2E.PAASimTool;
import pro2E.controller.Controller;
import pro2E.controller.Settings;

/**
 * <pre>
 * The <b><code>ToolBar</code></b> class builds the toolbar of the ui.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class ToolBar extends JToolBar {

	private static final long serialVersionUID = 1L;

	private PAASimTool frame;
	private View view;

	private Controller controller;

	private Color bg = new Color(238, 238, 238);

	private JButton btNew = new JButton("New", Utility.loadResourceIcon("axialis-blue-round/Document New.png"));
	private JButton btDuplicate = new JButton("Duplicate",
			Utility.loadResourceIcon("axialis-blue-round/Applications.png"));
	private JButton btDelete = new JButton("Delete", Utility.loadResourceIcon("axialis-blue-round/Cancel.png"));
	private JButton btRename = new JButton("Rename", Utility.loadResourceIcon("axialis-blue-round/Write Message.png"));

	private JButton btOpen = new JButton("Open", Utility.loadResourceIcon("axialis-blue-round/Folder2.png"));
	private JButton btSave = new JButton("Save", Utility.loadResourceIcon("axialis-blue-round/Save.png"));

	private JButton btSimulate = new JButton("Simulate", Utility.loadResourceIcon("axialis-blue-round/Start.png"));
	private JButton btCopyDiagram = new JButton("Copy Diagram",
			Utility.loadResourceIcon("axialis-blue-round/Clipboard Copy.png"));

	private JButton btSettings = new JButton("Settings", Utility.loadResourceIcon("axialis-blue-round/Tool.png"));

	/**
	 * <pre>
	 * Builds the toolbar of the ui.
	 * </pre>
	 */
	public ToolBar() {
		setBorder(CustomBorder.createBorder("Toolbar"));
		setFloatable(false);
		setBackground(bg);

		add(btNew);
		btNew.setToolTipText("New Simulation");
		btNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.newSimulation(Controller.NEW, null, null, null);
			}
		});

		add(btDuplicate);
		btDuplicate.setToolTipText("Duplicate Simulation");
		btDuplicate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.newSimulation(Controller.DUPLICATE, null, null, null);
			}
		});

		add(btDelete);
		btDelete.setToolTipText("Delete Simulation");
		btDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.deleteSimulation();
			}
		});

		add(btRename);
		btRename.setToolTipText("Rename Simulation");
		btRename.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(frame, "Enter a new name:", "Rename Simulation",
						JOptionPane.PLAIN_MESSAGE);
				if (name != null) {
					controller.renameSimulation(name);
				}
			}
		});

		addSeparator();

		add(btOpen);
		btOpen.setToolTipText("Open Simulation");
		btOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				view.jFileChooser.setSelectedFile(new File(""));

				int returnValue = view.jFileChooser.showOpenDialog(frame);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File file = view.jFileChooser.getSelectedFile();
					String filePath = file.getAbsolutePath();
					try {
						BufferedReader inputFile = new BufferedReader(new FileReader(filePath));
						String line;
						String content = "";
						while ((line = inputFile.readLine()) != null) {
							content = content + line + "\n";
						}
						inputFile.close();
						controller.loadSimulation(content);
					} catch (IOException exc) {
						String error = "Error: Could not load the file '" + file.getName() + "'.";
						controller.displayError(error);
					}
				}
			}
		});

		add(btSave);
		btSave.setToolTipText("Save Simulation");
		btSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = controller.getName(view.simTabbedPane.getSelectedIndex());
				view.jFileChooser.setSelectedFile(new File(name + ".csv"));

				int returnValue = view.jFileChooser.showSaveDialog(frame);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					String content = controller.saveSimulation();
					File file = view.jFileChooser.getSelectedFile();
					String filePath = file.getAbsolutePath();
					if (!filePath.toLowerCase().endsWith(".csv")) {
						filePath += ".csv";
					}
					try {
						PrintWriter outputFile = new PrintWriter(new FileWriter(filePath, false));
						outputFile.print(content);
						outputFile.close();
					} catch (IOException exc) {
						String error = "Error: The simulation '" + name + "' could not be saved.";
						controller.displayError(error);
					}
					controller.displayMsg("Info: The simulation '" + name + "' has been saved successfully.");
				}
			}
		});

		addSeparator();

		add(btSimulate);
		btSimulate.setToolTipText("Run Simulation");
		btSimulate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.updateSimulation(Controller.FORCE_UPDATE);
			}
		});

		add(btCopyDiagram);
		btCopyDiagram.setToolTipText("Copy Diagram to Clipboard");
		btCopyDiagram.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.copyDiagram();
			}
		});

		addSeparator();

		add(btSettings);
		btSettings.setToolTipText("Open Settings");
		btSettings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int electromagneticRadiationUnit = controller.getElectromagneticRadiationUnit();

				Object[] physicalQuantities = { "Wavelength", "Frequency" };

				String physicalQuantity = (String) JOptionPane.showInputDialog(frame,
						"Select your preferred physical quantity:", "Settings", JOptionPane.PLAIN_MESSAGE, null,
						physicalQuantities, physicalQuantities[electromagneticRadiationUnit]);

				if (physicalQuantity != null) {
					if (physicalQuantity.equals(physicalQuantities[Settings.WAVELENGTH])) {
						controller.setElectromagneticRadiationUnit(Controller.TOOLBAR, Settings.WAVELENGTH);
					} else {
						controller.setElectromagneticRadiationUnit(Controller.TOOLBAR, Settings.FREQUENCY);
					}
				}
			}
		});
	}

	/**
	 * <pre>
	 * Sets the parent classes.
	 * </pre>
	 * 
	 * @param frame
	 * @param view
	 */
	public void setParents(PAASimTool frame, View view) {
		this.frame = frame;
		this.view = view;
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
