package pro2E.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import pro2E.PAASimTool;
import pro2E.controller.Controller;
import pro2E.controller.Settings;

/**
 * <pre>
 * The <b><code>MenuBar</code></b> class builds the menu bar of the ui.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class MenuBar extends JMenuBar {

	private static final long serialVersionUID = 1L;

	private PAASimTool frame;
	private View view;

	private Controller controller;

	private JMenu menuFile = new JMenu("File");
	private JMenu menuFileSettings = new JMenu("Settings");
	private JMenu menuDiagram = new JMenu("Diagram");
	private JMenu menuAbout = new JMenu("About");

	private JMenuItem itemNew = new JMenuItem("New", Utility.loadResourceIcon("axialis-blue-round/Document New.png"));
	private JMenuItem itemDuplicate = new JMenuItem("Duplicate",
			Utility.loadResourceIcon("axialis-blue-round/Applications.png"));
	private JMenuItem itemDelete = new JMenuItem("Delete", Utility.loadResourceIcon("axialis-blue-round/Cancel.png"));
	private JMenuItem itemRename = new JMenuItem("Rename",
			Utility.loadResourceIcon("axialis-blue-round/Write Message.png"));

	private JMenuItem itemOpen = new JMenuItem("Open", Utility.loadResourceIcon("axialis-blue-round/Folder2.png"));
	private JMenuItem itemSave = new JMenuItem("Save", Utility.loadResourceIcon("axialis-blue-round/Save.png"));

	public JRadioButtonMenuItem checkBoxItemWavelength = new JRadioButtonMenuItem("Wavelength");
	public JRadioButtonMenuItem checkBoxItemFrequency = new JRadioButtonMenuItem("Frequency");

	private ButtonGroup btGroup = new ButtonGroup();

	private JMenuItem itemExit = new JMenuItem("Exit");

	private JMenuItem itemSimulate = new JMenuItem("Simulate",
			Utility.loadResourceIcon("axialis-blue-round/Start.png"));
	private JMenuItem itemCopyDiagram = new JMenuItem("Copy Diagram",
			Utility.loadResourceIcon("axialis-blue-round/Clipboard Copy.png"));

	private JMenuItem itemAbout = new JMenuItem("About");

	public ItemListener ilWavelength;
	public ItemListener ilFrequency;

	/**
	 * <pre>
	 * Builds the menu bar of the ui.
	 * </pre>
	 */
	public MenuBar() {
		// File
		add(this.menuFile);
		this.menuFile.setMnemonic(KeyEvent.VK_F);

		this.menuFile.add(this.itemNew);
		this.itemNew.setMnemonic(KeyEvent.VK_N);
		this.itemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		this.itemNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.newSimulation(Controller.NEW, null, null, null);
			}
		});

		this.menuFile.add(this.itemDuplicate);
		this.itemDuplicate.setMnemonic(KeyEvent.VK_D);
		this.itemDuplicate
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		this.itemDuplicate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.newSimulation(Controller.DUPLICATE, null, null, null);
			}
		});

		this.menuFile.add(this.itemDelete);
		this.itemDelete.setMnemonic(KeyEvent.VK_E);
		this.itemDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		this.itemDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.deleteSimulation();
			}
		});

		this.menuFile.add(this.itemRename);
		this.itemRename.setMnemonic(KeyEvent.VK_R);
		this.itemRename.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(frame, "Enter a new name:", "Rename Simulation",
						JOptionPane.PLAIN_MESSAGE);
				if (name != null) {
					controller.renameSimulation(name);
				}
			}
		});

		this.menuFile.addSeparator();

		this.menuFile.add(this.itemOpen);
		this.itemOpen.setMnemonic(KeyEvent.VK_O);
		this.itemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		this.itemOpen.addActionListener(new ActionListener() {

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

		this.menuFile.add(this.itemSave);
		this.itemSave.setMnemonic(KeyEvent.VK_S);
		this.itemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		this.itemSave.addActionListener(new ActionListener() {

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

		this.menuFile.addSeparator();

		// Settings
		this.menuFile.add(this.menuFileSettings);
		this.menuFileSettings.setMnemonic(KeyEvent.VK_T);
		this.menuFileSettings.setIcon(Utility.loadResourceIcon("axialis-blue-round/Tool.png"));

		this.menuFileSettings.add(this.checkBoxItemWavelength);
		this.checkBoxItemWavelength.setMnemonic(KeyEvent.VK_W);
		this.checkBoxItemWavelength.setSelected(true);
		this.ilWavelength = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					controller.setElectromagneticRadiationUnit(Controller.MENUBAR, Settings.WAVELENGTH);
				}
			}
		};
		this.checkBoxItemWavelength.addItemListener(this.ilWavelength);

		this.menuFileSettings.add(this.checkBoxItemFrequency);
		this.checkBoxItemFrequency.setMnemonic(KeyEvent.VK_F);
		this.ilFrequency = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					controller.setElectromagneticRadiationUnit(Controller.MENUBAR, Settings.FREQUENCY);
				}
			}
		};
		this.checkBoxItemFrequency.addItemListener(this.ilFrequency);

		this.btGroup.add(this.checkBoxItemWavelength);
		this.btGroup.add(this.checkBoxItemFrequency);

		this.menuFile.add(this.itemExit);
		this.itemExit.setMnemonic(KeyEvent.VK_X);
		this.itemExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// Diagram
		add(this.menuDiagram);
		this.menuDiagram.setMnemonic(KeyEvent.VK_D);

		this.menuDiagram.add(this.itemSimulate);
		this.itemSimulate.setMnemonic(KeyEvent.VK_S);
		this.itemSimulate.setAccelerator(KeyStroke.getKeyStroke("F5"));
		this.itemSimulate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.updateSimulation(Controller.FORCE_UPDATE);
			}
		});

		this.menuDiagram.add(this.itemCopyDiagram);
		this.itemCopyDiagram.setMnemonic(KeyEvent.VK_C);
		this.itemCopyDiagram.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.copyDiagram();
			}
		});

		// About
		add(this.menuAbout);
		this.menuAbout.setMnemonic(KeyEvent.VK_A);

		this.menuAbout.add(this.itemAbout);
		this.itemAbout.setMnemonic(KeyEvent.VK_A);
		this.itemAbout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String title = "Phased Array Antenna - SimTool\nVersion: 1.0.0\n\n";
				String names = "Pascal Fankhauser, Project Manager\nAndrin Döbeli, Asst. Project Manager\nDennis Aeschbacher\nDominik Müller\nNico Canzani\nAndreas Frei\n\n";
				String copyright = "Copyright \u00A9 2018 pro2E - Team3";
				String spacer = "                          ";
				JOptionPane.showMessageDialog(frame, title + names + copyright + spacer, "About PAA - SimTool",
						JOptionPane.INFORMATION_MESSAGE, Utility.loadResourceIcon("icon.png"));
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
