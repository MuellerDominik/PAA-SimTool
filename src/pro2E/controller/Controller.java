package pro2E.controller;

import java.util.ArrayList;
import java.util.List;

import pro2E.model.AntennaArray;
import pro2E.model.AntennaArrayFunctions;
import pro2E.model.Model;
import pro2E.ui.View;
import pro2E.ui.simulation.SimPanel;

/**
 * <pre>
 * The <b><code>Controller</code></b> class updates and receives events from the view.
 * Furthermore it manipulates the model and handles the settings.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class Controller {

	private Model model;
	private View view;

	private List<Configuration> config = new ArrayList<>();

	private Settings settings = new Settings();

	private int simIndex; // the currently selected simulation index
	private int simNameIndex; // will be incremented each time a new simulation is created (used for the name)

	private String[] errorMsgs = { "The total radiator count must be equal or less than 100.",
			"The radiator count of a 'Square' array must be a multiple of 4.",
			"The 'Side Lobe Attenuation' must statisfy the following criteria: 0 \u2264 Input \u2264 200.",
			"The 'Sine-Offset' must statisfy the following criteria: 0.0 \u2264 Input \u2264 1.0.",
			"The 'Angle From' input value must be lower than the 'Angle To' input value.",
			"The 'Min Gain' input value must be lower than the 'Max Gain' input value." };

	// Condition for creating a Simulation
	public static final int NEW = 0;
	public static final int DUPLICATE = 1;
	public static final int LOAD = 2;

	// Condition for updating a Simulation
	public static final int UPDATE = 0;
	public static final int RESET = 1;
	public static final int RADIATOR_UPDATE = 2;
	public static final int FORCE_UPDATE = 3;

	// Source of the Settings changed Event
	public static final int MENUBAR = 0;
	public static final int TOOLBAR = 1;

	// Settings Enforcer - Direction
	public static final int CONFIGURATION_TO_MODEL = 0;
	public static final int CONFIGURATION_TO_VIEW = 1;
	public static final int VIEW_TO_CONFIGURATION = 2;

	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;

		// creates the first simulation upon startup
		this.newSimulation(Controller.NEW, null, null, null);
	}

	/**
	 * <pre>
	 * Initializes a new simulation by,
	 * - creating a new Configuration object with default parameters or
	 * - duplicating an existing Configuration object or
	 * - creating a new Configuration object by using the configuration parameters from a loaded file.
	 * Calls the corresponding method to create the simulation.
	 * </pre>
	 * 
	 * @param condition
	 * @param name
	 *            used when a simulation is loaded from a file
	 * @param radiatorParameters
	 *            used when a simulation is loaded from a file
	 * @param config
	 *            used when a simulation is loaded from a file
	 */
	public void newSimulation(int condition, String name, double[][] radiatorParameters, double[] config) {
		switch (condition) {
		case Controller.NEW:
			// pre-increment, because simNameIndex starts at 0
			this.config.add(new Configuration("Simulation " + ++this.simNameIndex));
			this.createSimulation(Controller.NEW, this.config.get(this.config.size() - 1).getName(), null);
			break;
		case Controller.DUPLICATE:
			double[] duplicatedConfig = this.config.get(this.simIndex).getConfig();
			this.config.add(new Configuration(this.config.get(this.simIndex).getName() + " - Copy", duplicatedConfig));
			this.createSimulation(Controller.DUPLICATE, this.config.get(this.config.size() - 1).getName(), null);
			break;
		case Controller.LOAD:
			this.config.add(new Configuration(name, config));
			this.createSimulation(Controller.LOAD, name, radiatorParameters);
			break;
		}
	}

	/**
	 * <pre>
	 * Creates a new SimPanel object, fills all the fields accordingly and adds the new object to the SimTabbedPane.
	 * Creates the diagram and notifies the observers.
	 * </pre>
	 * 
	 * @param condition
	 * @param name
	 * @param radiatorParameters
	 *            used when a simulation is loaded from a file
	 */
	public void createSimulation(int condition, String name, double[][] radiatorParameters) {
		this.view.simPanel.add(new SimPanel());
		// sets the controller for all the necessary view classes
		this.view.simPanel.get(this.config.size() - 1).setController(this);

		// enforce the settings on the config array for the model
		double[] modelConfig = this.settingsEnforcer(Controller.CONFIGURATION_TO_MODEL,
				this.config.get(this.config.size() - 1).getConfig());

		switch (condition) {
		case Controller.NEW:
			this.model.addAntennaArray(modelConfig);
			break;
		case Controller.DUPLICATE:
			this.model.duplicateAntennaArray(this.simIndex, modelConfig);
			break;
		case Controller.LOAD:
			this.model.loadAntennaArray(radiatorParameters, modelConfig);
			break;
		}

		// fill the input fields with the according values
		this.setInputValues(this.config.size() - 1);

		// create the diagram and notify the observers
		this.model.createDiagram(this.config.size() - 1, modelConfig);

		this.view.simTabbedPane.addTab(name, this.view.simPanel.get(this.view.simPanel.size() - 1));
		this.view.simTabbedPane.setSelectedIndex(this.config.size() - 1);
	}

	/**
	 * <pre>
	 * Deletes the currently selected simulation.
	 * Creates a new simulation, if it was the last simulation.
	 * </pre>
	 */
	public void deleteSimulation() {
		this.config.remove(this.simIndex);
		this.model.removeAntennaArray(this.simIndex);
		this.view.simPanel.remove(this.simIndex);
		// remove the tab from the pane (updates the simIndex)
		this.view.simTabbedPane.remove(this.simIndex);
		if (this.config.size() == 0) {
			this.newSimulation(Controller.NEW, null, null, null);
		}
	}

	/**
	 * <pre>
	 * Renames the selected simulation.
	 * </pre>
	 * 
	 * @param name
	 */
	public void renameSimulation(String name) {
		this.config.get(simIndex).setName(name);
		this.view.simTabbedPane.setTitleAt(simIndex, name);
		this.view.simPanel.get(this.simIndex).inputPanel.mainLobePanel.rbOff.setSelected(true);
	}

	/**
	 * <pre>
	 * Gets the values from all the input fields and checks if there were any changes.
	 * If the input values have changed and they are valid:
	 * - Resets error messages
	 * - Updates the diagram and notifies the observers
	 * - Warns if one ore more radiators were placed on top of the reflector
	 * </pre>
	 * 
	 * @param condition
	 */
	public void updateSimulation(int condition) {
		double[] config = this.getInputValues();

		int configAuditor = this.configAuditor(config);

		if (configAuditor != 0) {
			if (configAuditor > 0 && configAuditor < 7) {
				this.displayError(
						"[" + this.getName(this.simIndex) + "] " + "Error: " + this.errorMsgs[configAuditor - 1]);
			}
			return;
		}

		if (condition != Controller.RADIATOR_UPDATE && condition != Controller.FORCE_UPDATE) {
			if (this.checkForChanges(config) == 0) {
				return;
			}
		}

		this.resetStatusBar();

		this.config.get(this.simIndex).setConfig(config);

		if (condition == Controller.RESET) {
			this.model.resetAntennaArray(this.simIndex, config);
		}

		// Main Lobe / Side Lobe
		if (config[11] == 1.0 || config[13] == 1.0) {
			this.antennaLobeEnforcer(config);
		}

		// create the diagram and notify the observers
		this.model.createDiagram(this.simIndex, this.settingsEnforcer(Controller.CONFIGURATION_TO_MODEL, config));

		if ((int) config[16] == AntennaArray.ON) {
			if (this.reflectorAuditor(config) != 0) {
				this.displayMsg("[" + this.getName(this.simIndex) + "] "
						+ "Warning: Some radiators were ignored because they were placed on the reflector.");
			}
		}
	}

	/**
	 * <pre>
	 * Sets the field simIdex to the index of the currently selected tab.
	 * </pre>
	 * 
	 * @param simIndex
	 */
	public void setSimIndex(int simIndex) {
		this.simIndex = simIndex;
	}

	/**
	 * <pre>
	 * Compares the electromagnetic radiation unit from the Settings class to the one supplied to the method.
	 * If they are different:
	 * - Calls the corresponding method to set the electromagnetic radiation unit in the Settings class
	 * - Calls the method settingsChanged() to update the view
	 * - If the source is the toolbar:
	 *   - Change the selection in the menu bar
	 * </pre>
	 * 
	 * @param source
	 *            either the menu bar or the toolbar
	 * @param electromagneticRadiationUnit
	 */
	public void setElectromagneticRadiationUnit(int source, int electromagneticRadiationUnit) {
		int oldElectromagneticRadiationUnit = this.settings.getElectromagneticRadiationUnit();

		if (oldElectromagneticRadiationUnit != electromagneticRadiationUnit) {
			this.settings.setElectromagneticRadiationUnit(electromagneticRadiationUnit);
			this.settingsChanged(electromagneticRadiationUnit);
			if (source == Controller.TOOLBAR) {
				this.view.menuBar.checkBoxItemWavelength.removeItemListener(this.view.menuBar.ilWavelength);
				this.view.menuBar.checkBoxItemFrequency.removeItemListener(this.view.menuBar.ilFrequency);

				if (electromagneticRadiationUnit == Settings.WAVELENGTH) {
					this.view.menuBar.checkBoxItemWavelength.setSelected(true);
				} else {
					this.view.menuBar.checkBoxItemFrequency.setSelected(true);
				}

				this.view.menuBar.checkBoxItemWavelength.addItemListener(this.view.menuBar.ilWavelength);
				this.view.menuBar.checkBoxItemFrequency.addItemListener(this.view.menuBar.ilFrequency);
			}
		}
	}

	/**
	 * <pre>
	 * Changes the necessary input fields by calling the corresponding method in the PropertiesPanel class for each simulation.
	 * </pre>
	 * 
	 * @param electromagneticRadiationUnit
	 */
	public void settingsChanged(int electromagneticRadiationUnit) {
		int numberOfSimulations = this.config.size();
		for (int i = 0; i < numberOfSimulations; i++) {
			this.view.simPanel.get(i).inputPanel.propertiesPanel
					.setElectromagneticRadiationUnit(electromagneticRadiationUnit);
		}
	}

	/**
	 * <pre>
	 * Returns the angular unit.
	 * </pre>
	 * 
	 * @return the angular unit
	 */
	public int getAngularUnit() {
		return this.settings.getAngularUnit();
	}

	/**
	 * <pre>
	 * Returns the electromagnetic radiation unit.
	 * </pre>
	 * 
	 * @return the electromagnetic radiation unit
	 */
	public int getElectromagneticRadiationUnit() {
		return this.settings.getElectromagneticRadiationUnit();
	}

	/**
	 * <pre>
	 * Enforces the settings to a supplied configuration array.
	 * </pre>
	 * 
	 * @param direction
	 *            the direction to enforce the settings
	 * @param config
	 * @return a new config array with enforced settings
	 */
	public double[] settingsEnforcer(int direction, double[] config) {
		double[] enorcedConfig = new double[config.length];
		for (int i = 0; i < enorcedConfig.length; i++) {
			enorcedConfig[i] = config[i];
		}

		if (this.getAngularUnit() == Settings.DEG) {
			if (direction == Controller.CONFIGURATION_TO_MODEL) {
				enorcedConfig[2] = Math.toRadians(enorcedConfig[2]);
				enorcedConfig[12] = Math.toRadians(enorcedConfig[12]);
				enorcedConfig[19] = Math.toRadians(enorcedConfig[19]);
				enorcedConfig[20] = Math.toRadians(enorcedConfig[20]);
			}
			// everything else: already in degrees
		} else {
			if (direction == Controller.CONFIGURATION_TO_MODEL) {
				enorcedConfig[2] = Math.toRadians(enorcedConfig[2]);
				enorcedConfig[12] = Math.toRadians(enorcedConfig[12]);
				enorcedConfig[19] = Math.toRadians(enorcedConfig[19]);
				enorcedConfig[20] = Math.toRadians(enorcedConfig[20]);
			} else if (direction == Controller.CONFIGURATION_TO_VIEW) {
				enorcedConfig[2] = Math.toRadians(enorcedConfig[2]);
				enorcedConfig[12] = Math.toRadians(enorcedConfig[12]);
				enorcedConfig[19] = Math.toRadians(enorcedConfig[19]);
				enorcedConfig[20] = Math.toRadians(enorcedConfig[20]);
			} else {
				enorcedConfig[2] = Math.toDegrees(enorcedConfig[2]);
				enorcedConfig[12] = Math.toDegrees(enorcedConfig[12]);
				enorcedConfig[19] = Math.toDegrees(enorcedConfig[19]);
				enorcedConfig[20] = Math.toDegrees(enorcedConfig[20]);
			}
		}

		if (this.getElectromagneticRadiationUnit() == Settings.FREQUENCY) {
			if (direction == Controller.CONFIGURATION_TO_MODEL) {
				// already using wavelength
			} else if (direction == Controller.CONFIGURATION_TO_VIEW) {
				enorcedConfig[0] = AntennaArrayFunctions.frequency(enorcedConfig[0]);
			} else {
				enorcedConfig[0] = AntennaArrayFunctions.wavelength(enorcedConfig[0]);
			}
		}
		// else: already using wavelength

		return enorcedConfig;
	}

	/**
	 * <pre>
	 * Sets all input elements of a given simulation to the according values stored in the configuration object with the given simIndex.
	 * </pre>
	 * 
	 * @param simIndex
	 */
	public void setInputValues(int simIndex) {
		SimPanel simPanel = this.view.simPanel.get(simIndex);
		double[] config = this.settingsEnforcer(Controller.CONFIGURATION_TO_VIEW,
				this.config.get(simIndex).getConfig());

		simPanel.inputPanel.geometryPanel.cbGeometry.setSelectedIndex((int) config[3]);
		simPanel.inputPanel.geometryPanel.tfNumberOfHorizontalRadiators.setValue((int) config[4]);
		simPanel.inputPanel.geometryPanel.tfHorizontalSpacing.setValue(config[5]);
		simPanel.inputPanel.geometryPanel.tfNumberOfVerticalRadiators.setValue((int) config[6]);
		simPanel.inputPanel.geometryPanel.tfVerticalSpacing.setValue(config[7]);
		simPanel.inputPanel.geometryPanel.tfNumberOfRadiators.setValue((int) config[8]);
		simPanel.inputPanel.geometryPanel.tfRadius.setValue(config[9]);
		simPanel.inputPanel.geometryPanel.tfSideLength.setValue(config[10]);

		simPanel.inputPanel.geometryPanel.hideFields();
		simPanel.inputPanel.geometryPanel.registerListeners();

		simPanel.inputPanel.propertiesPanel.tfWavelengthFrequency.setValue(config[0]);
		simPanel.inputPanel.propertiesPanel.cbRadiatorType.setSelectedIndex((int) config[1]);
		simPanel.inputPanel.propertiesPanel.tfRadiatorAngle.setValue(config[2]);
		simPanel.inputPanel.propertiesPanel.sliderRadiatorAngle.setValue((int) config[2]);
		if (((int) config[16]) == AntennaArray.ON) {
			simPanel.inputPanel.propertiesPanel.rbOn.setSelected(true);
		} else {
			simPanel.inputPanel.propertiesPanel.rbOff.setSelected(true);
		}
		simPanel.inputPanel.propertiesPanel.tfVerticalIntercept.setValue(config[17]);

		simPanel.inputPanel.propertiesPanel.hideFields();
		simPanel.inputPanel.propertiesPanel.registerListeners();

		if (((int) config[11]) == AntennaArray.ON) {
			simPanel.inputPanel.mainLobePanel.rbOn.setSelected(true);
		} else {
			simPanel.inputPanel.mainLobePanel.rbOff.setSelected(true);
		}
		simPanel.inputPanel.mainLobePanel.tfMainLobeAngle.setValue(config[12]);
		simPanel.inputPanel.mainLobePanel.sliderMainLobeAngle.setValue((int) config[12]);

		simPanel.inputPanel.mainLobePanel.hideFields();
		simPanel.inputPanel.mainLobePanel.registerListeners();

		if (((int) config[13]) == AntennaArray.ON) {
			simPanel.inputPanel.sideLobePanel.rbOn.setSelected(true);
		} else {
			simPanel.inputPanel.sideLobePanel.rbOff.setSelected(true);
		}
		simPanel.inputPanel.sideLobePanel.cbWindowFunction.setSelectedIndex((int) config[14]);
		simPanel.inputPanel.sideLobePanel.tfAttenuationOffset.setValue(config[15]);

		simPanel.inputPanel.sideLobePanel.hideFields();
		simPanel.inputPanel.sideLobePanel.registerListeners();

		if (((int) config[18]) == AntennaArray.POLAR) {
			simPanel.outputPanel.diagramPanel.rbDiagramTypePolar.setSelected(true);
		} else {
			simPanel.outputPanel.diagramPanel.rbDiagramTypeCartesian.setSelected(true);
		}
		simPanel.outputPanel.diagramPanel.tfFrom.setValue(config[19]);
		simPanel.outputPanel.diagramPanel.tfTo.setValue(config[20]);
		simPanel.outputPanel.diagramPanel.tfPoints.setValue((int) config[21]);
		simPanel.outputPanel.diagramPanel.sliderPoints.setValue((int) config[21]);
		simPanel.outputPanel.diagramPanel.tfMaxGain.setValue((int) config[22]);
		simPanel.outputPanel.diagramPanel.tfMinGain.setValue((int) config[23]);

		simPanel.outputPanel.diagramPanel.hideFields();
		simPanel.outputPanel.diagramPanel.registerListeners();

		// Period is not implemented in the ui (config[24])
	}

	/**
	 * <pre>
	 * Gets the values of all input elements of the currently selected simulation.
	 * Returns a cofig array containing all the configuration values.
	 * </pre>
	 * 
	 * @return an array containing all the configuration values
	 */
	public double[] getInputValues() {
		SimPanel simPanel = this.view.simPanel.get(simIndex);
		double[] config = new double[25];

		config[0] = simPanel.inputPanel.propertiesPanel.tfWavelengthFrequency.getValue();
		config[1] = simPanel.inputPanel.propertiesPanel.cbRadiatorType.getSelectedIndex();
		config[2] = simPanel.inputPanel.propertiesPanel.tfRadiatorAngle.getValue();

		config[3] = simPanel.inputPanel.geometryPanel.cbGeometry.getSelectedIndex();
		config[4] = simPanel.inputPanel.geometryPanel.tfNumberOfHorizontalRadiators.getValue();
		config[5] = simPanel.inputPanel.geometryPanel.tfHorizontalSpacing.getValue();
		config[6] = simPanel.inputPanel.geometryPanel.tfNumberOfVerticalRadiators.getValue();
		config[7] = simPanel.inputPanel.geometryPanel.tfVerticalSpacing.getValue();
		config[8] = simPanel.inputPanel.geometryPanel.tfNumberOfRadiators.getValue();
		config[9] = simPanel.inputPanel.geometryPanel.tfRadius.getValue();
		config[10] = simPanel.inputPanel.geometryPanel.tfSideLength.getValue();

		if (simPanel.inputPanel.mainLobePanel.rbOn.isSelected()) {
			config[11] = AntennaArray.ON;
		} else {
			config[11] = AntennaArray.OFF;
		}
		config[12] = simPanel.inputPanel.mainLobePanel.tfMainLobeAngle.getValue();

		if (simPanel.inputPanel.sideLobePanel.rbOn.isSelected()) {
			config[13] = AntennaArray.ON;
		} else {
			config[13] = AntennaArray.OFF;
		}
		config[14] = simPanel.inputPanel.sideLobePanel.cbWindowFunction.getSelectedIndex();
		config[15] = simPanel.inputPanel.sideLobePanel.tfAttenuationOffset.getValue();

		if (simPanel.inputPanel.propertiesPanel.rbOn.isSelected()) {
			config[16] = AntennaArray.ON;
		} else {
			config[16] = AntennaArray.OFF;
		}
		config[17] = simPanel.inputPanel.propertiesPanel.tfVerticalIntercept.getValue();

		if (simPanel.outputPanel.diagramPanel.rbDiagramTypePolar.isSelected()) {
			config[18] = AntennaArray.POLAR;
		} else {
			config[18] = AntennaArray.CARTESIAN;
		}
		config[19] = simPanel.outputPanel.diagramPanel.tfFrom.getValue();
		config[20] = simPanel.outputPanel.diagramPanel.tfTo.getValue();
		config[21] = simPanel.outputPanel.diagramPanel.tfPoints.getValue();
		config[22] = simPanel.outputPanel.diagramPanel.tfMaxGain.getValue();
		config[23] = simPanel.outputPanel.diagramPanel.tfMinGain.getValue();

		// set the Period to 2.0s (not implemented in the ui)
		config[24] = 2.0;

		config = this.settingsEnforcer(Controller.VIEW_TO_CONFIGURATION, config);
		return config;
	}

	/**
	 * <pre>
	 * Validates all the config values.
	 * </pre>
	 * 
	 * @param config
	 *            the configuration array to be validated
	 * @return 0, if all configuration values are valid
	 */
	public int configAuditor(double[] config) {
		// Formalities
		if (config[0] < 1e-3 || config[0] > 300e9) {
			return -1;
		} else if (config[1] != AntennaArray.ISOTROPIC && config[1] != AntennaArray.DIPOLE) {
			return -1;
		} else if (config[2] < -90.0 || config[2] > 90.0) {
			return -1;
		} else if (config[3] != AntennaArray.LINEAR && config[3] != AntennaArray.GRID
				&& config[3] != AntennaArray.CIRCLE && config[3] != AntennaArray.SQUARE
				&& config[3] != AntennaArray.CUSTOM) {
			return -1;
		} else if (config[4] < 1.0 || config[4] > 100.0) {
			return -1;
		} else if (config[5] < 1e-3 || config[5] > 1e3) {
			return -1;
		} else if (config[6] < 1.0 || config[6] > 100.0) {
			return -1;
		} else if (config[7] < 1e-3 || config[7] > 1e3) {
			return -1;
		} else if (config[8] < 1.0 || config[8] > 100.0) {
			return -1;
		} else if (config[9] < 1e-3 || config[9] > 1e3) {
			return -1;
		} else if (config[10] < 1e-3 || config[10] > 1e3) {
			return -1;
		} else if (config[11] != AntennaArray.OFF && config[11] != AntennaArray.ON) {
			return -1;
		} else if (config[12] < 0.0 || config[12] > 180.0) {
			return -1;
		} else if (config[13] != AntennaArray.OFF && config[13] != AntennaArray.ON) {
			return -1;
		} else if (config[14] != AntennaArray.RECTANGULAR && config[14] != AntennaArray.DOLPH_CHEBYSHEV
				&& config[14] != AntennaArray.SINE && config[14] != AntennaArray.HANN
				&& config[14] != AntennaArray.HAMMING && config[14] != AntennaArray.TRIANGULAR
				&& config[14] != AntennaArray.PARZEN && config[14] != AntennaArray.BLACKMAN
				&& config[14] != AntennaArray.WELCH) {
			return -1;
		} else if (config[15] < 0.0 || config[15] > 200.0) {
			return -1;
		} else if (config[16] != AntennaArray.OFF && config[16] != AntennaArray.ON) {
			return -1;
		} else if (config[17] < -1e3 || config[17] > 1e3) {
			return -1;
		} else if (config[18] != AntennaArray.CARTESIAN && config[18] != AntennaArray.POLAR) {
			return -1;
		} else if (config[19] < 0.0 || config[19] > 360.0) {
			return -1;
		} else if (config[20] < 0.0 || config[20] > 360.0) {
			return -1;
		} else if (config[21] < 100.0 || config[21] > 1700) {
			return -1;
		} else if (config[22] < -1e3 || config[22] > 0.0) {
			return -1;
		} else if (config[23] < -1e3 || config[23] > 0.0) {
			return -1;
		} else if (config[24] < 1.0 || config[24] > 10) {
			return -1;
		}

		// Radiator count
		if (config[3] == AntennaArray.GRID) {
			if ((config[4] * config[6]) > 100) {
				return 1;
			}
		}

		// Radiator count - Square Array
		if (config[3] == AntennaArray.SQUARE) {
			if (((int) config[8]) % 4 != 0) {
				return 2;
			}
		}

		// Window Function Parameter
		switch ((int) config[14]) {
		case AntennaArray.DOLPH_CHEBYSHEV:
			if (config[15] < 0.0 || config[15] > 200.0) {
				return 3;
			}
			break;
		case AntennaArray.SINE:
			if (config[15] < 0.0 || config[15] > 1.0) {
				return 4;
			}
			break;
		}

		// Angle
		if (config[19] >= config[20]) {
			return 5;
		}

		// Gain
		if (config[22] <= config[23]) {
			return 6;
		}

		return 0;
	}

	/**
	 * <pre>
	 * Checks if the reflector is placed on top of a radiator.
	 * </pre>
	 * 
	 * @param config
	 * @return 0, if the reflector is not on top of a radiator
	 */
	public int reflectorAuditor(double[] config) {
		double[][] radiatorParameters = this.model.getRadiators(this.simIndex);

		for (int i = 0; i < radiatorParameters.length; i++) {
			if (radiatorParameters[i][1] == config[17]) {
				return 1;
			}
		}

		return 0;
	}

	/**
	 * <pre>
	 * Returns the configuration array at the arrayIndex position.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @return an array containing all the configuration values
	 */
	public double[] getConfig(int arrayIndex) {
		return this.config.get(arrayIndex).getConfig();
	}

	/**
	 * <pre>
	 * Returns the name of the simulation at the arrayIndex position.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @return the name of a simulation as a String
	 */
	public String getName(int arrayIndex) {
		return this.config.get(arrayIndex).getName();
	}

	/**
	 * <pre>
	 * Validates the content of a loaded simulation (*.csv file).
	 * If the content is valid:
	 * - Calls the corresponding method to initialize a new simulation
	 * - Displays a message in the status bar
	 * </pre>
	 * 
	 * @param content
	 *            the content from the loaded *.csv file
	 */
	public void loadSimulation(String content) {
		String errorMsg = "Error: Could not open the simulation.";
		String[] input = content.split("[\n]+");

		if (input.length < 2 || input.length > 102) {
			this.displayError(errorMsg);
			return;
		}

		String name = input[0];
		String[] inputConfig = input[1].split("[,]");
		String[][] inputRadiators = new String[input.length - 2][];
		for (int i = 2; i < input.length; i++) {
			inputRadiators[i - 2] = input[i].split("[,]");
		}

		if (inputConfig.length != 25) {
			this.displayError(errorMsg);
			return;
		}

		for (int i = 0; i < inputRadiators.length; i++) {
			if (inputRadiators[i].length != 6) {
				this.displayError(errorMsg);
				return;
			}
		}

		double[] config = new double[inputConfig.length];
		for (int i = 0; i < inputConfig.length; i++) {
			try {
				config[i] = Double.parseDouble(inputConfig[i]);
			} catch (Exception e) {
				this.displayError(errorMsg);
				return;
			}
		}

		double[][] radiatorParameters = new double[inputRadiators.length][6];
		for (int i = 0; i < inputRadiators.length; i++) {
			for (int j = 0; j < inputRadiators[i].length; j++) {
				try {
					radiatorParameters[i][j] = Double.parseDouble(inputRadiators[i][j]);
				} catch (Exception e) {
					this.displayError(errorMsg);
					return;
				}
			}
		}

		if (this.configAuditor(config) != 0) {
			this.displayError(errorMsg);
			return;
		}

		switch ((int) config[3]) {
		case AntennaArray.LINEAR:
			if ((int) config[4] != radiatorParameters.length) {
				this.displayError(errorMsg);
				return;
			}
			break;
		case AntennaArray.GRID:
			if ((((int) config[4]) * ((int) config[6])) != radiatorParameters.length) {
				this.displayError(errorMsg);
				return;
			}
			break;
		case AntennaArray.CIRCLE:
			if (config[8] != radiatorParameters.length) {
				this.displayError(errorMsg);
				return;
			}
			break;
		case AntennaArray.SQUARE:
			if (config[8] != radiatorParameters.length || (((int) config[8]) % 4) != 0) {
				this.displayError(errorMsg);
				return;
			}
			break;
		}

		if ((int) config[3] != AntennaArray.LINEAR) {
			if ((int) config[11] == AntennaArray.ON || (int) config[13] == AntennaArray.ON) {
				this.displayError(errorMsg);
				return;
			}
		}

		if ((int) config[14] == AntennaArray.SINE) {
			if (config[15] < 0.0 || config[15] > 1.0) {
				this.displayError(errorMsg);
				return;
			}
		}

		this.newSimulation(Controller.LOAD, name, radiatorParameters, config);

		this.displayMsg("Info: The simulation '" + name + "' has been loaded successfully.");
	}

	/**
	 * <pre>
	 * Prepares the parameters of the currently selected simulation to be saved in a *.csv file.
	 * Returns the content properly formatted to be saved in a *.csv file.
	 * </pre>
	 * 
	 * @return the content to be saved in a *.csv file
	 */
	public String saveSimulation() {
		String name = this.config.get(this.simIndex).getName();

		String config = "";
		double[] configValues = this.config.get(this.simIndex).getConfig();
		for (int i = 0; i < configValues.length; i++) {
			config += String.valueOf(configValues[i]);
			if (i != configValues.length - 1) {
				config += ",";
			}
		}

		String radiators = "";
		double[][] radiatorsValues = this.model.getRadiators(this.simIndex);
		for (int i = 0; i < radiatorsValues.length; i++) {
			for (int j = 0; j < radiatorsValues[i].length; j++) {
				radiators += String.valueOf(radiatorsValues[i][j]);
				if (j != radiatorsValues[i].length - 1) {
					radiators += ",";
				}
			}
			if (i != radiatorsValues.length - 1) {
				radiators += "\n";
			}
		}

		if (!radiators.equals("")) {
			return name + "\n" + config + "\n" + radiators;
		} else {
			return name + "\n" + config;
		}
	}

	/**
	 * <pre>
	 * Copies the diagram of the currently selected simulation to the clipboard.
	 * </pre>
	 */
	public void copyDiagram() {
		this.view.simPanel.get(this.simIndex).outputPanel.diagramPanel.chartPanel.doCopy();
		this.displayMsg("Info: The diagram has been copied to the clipboard.");
	}

	/**
	 * <pre>
	 * If the radiator count is not 100 already:
	 * - Calls the corresponding method to add a new radiator
	 * - Calls the corresponding method to convert the geometry to 'Custom'
	 * - Calls the corresponding method to update the simulation
	 * </pre>
	 * 
	 * @param x
	 *            the x-value of the position
	 * @param y
	 *            the y-value of the position
	 */
	public void addRadiator(double x, double y) {
		if (this.model.getRadiatorCount(this.simIndex) == 100) {
			this.displayError("[" + this.getName(this.simIndex) + "] " + "Error: " + this.errorMsgs[0]);
			return;
		}
		this.model.addRadiator(this.simIndex, x, y);
		this.convertToCustom();
		this.updateSimulation(Controller.RADIATOR_UPDATE);
	}

	/**
	 * <pre>
	 * - Calls the corresponding method to remove the radiators at the given indices
	 * - Calls the corresponding method to convert the geometry to 'Custom'
	 * - Calls the corresponding method to update the simulation
	 * </pre>
	 * 
	 * @param radiatorIndices
	 *            an array containing the radiator indices to be removed
	 */
	public void removeRadiators(int[] radiatorIndices) {
		this.model.removeRadiators(this.simIndex, radiatorIndices);
		this.convertToCustom();
		this.updateSimulation(Controller.RADIATOR_UPDATE);
	}

	/**
	 * <pre>
	 * - Calls the corresponding method to update the radiators position
	 * - Calls the corresponding method to convert the geometry to 'Custom'
	 * - Calls the corresponding method to update the simulation
	 * </pre>
	 * 
	 * @param radiatorIndex
	 *            the index of the radiator to be relocated
	 * @param x
	 *            the new x-value of the position
	 * @param y
	 *            the new y-value of the position
	 */
	public void updateRadiatorPosition(int radiatorIndex, double x, double y) {
		this.model.updateRadiatorPosition(this.simIndex, radiatorIndex, x, y);
		this.convertToCustom();
		this.updateSimulation(Controller.RADIATOR_UPDATE);
	}

	/**
	 * <pre>
	 * - Calls the corresponding method to set the 'hidden' state of the radiators at the given indices
	 * - Calls the corresponding method to update the simulation
	 * </pre>
	 * 
	 * @param radiatorIndices
	 *            an array containing the radiator indices to be changed
	 * @param state
	 *            the 'hidden' state
	 */
	public void setRadiatorsState(int[] radiatorIndices, boolean state) {
		this.model.setRadiatorsState(this.simIndex, radiatorIndices, state);
		this.updateSimulation(Controller.RADIATOR_UPDATE);
	}

	/**
	 * <pre>
	 * - Calls the corresponding method to update the phase of the radiators at the given indices
	 * - Calls the corresponding method to update the simulation
	 * </pre>
	 * 
	 * @param radiatorIndices
	 *            an array containing the radiator indices to be changed
	 * @param p
	 *            the new value of the phase
	 */
	public void updateRadiatorsPhase(int[] radiatorIndices, double p) {
		this.model.updateRadiatorsPhase(this.simIndex, radiatorIndices, Math.toRadians(p),
				this.config.get(this.simIndex).getConfig());
		this.updateSimulation(Controller.RADIATOR_UPDATE);
	}

	/**
	 * <pre>
	 * - Calls the corresponding method to update the amplitude of the radiators at the given indices
	 * - Calls the corresponding method to update the simulation
	 * </pre>
	 * 
	 * @param radiatorIndices
	 *            an array containing the radiator indices to be changed
	 * @param a
	 *            the new value of the amplitude
	 */
	public void updateRadiatorsAmplitude(int[] radiatorIndices, double a) {
		this.model.updateRadiatorsAmplitude(this.simIndex, radiatorIndices, a);
		this.updateSimulation(Controller.RADIATOR_UPDATE);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to update the period.
	 * </pre>
	 * 
	 * @param config
	 *            the config array containing the new period
	 */
	public void updatePeriod(double[] config) {
		this.model.updatePeriod(this.simIndex, config);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to calculate the phases of all the radiators in a linear array.
	 * </pre>
	 * 
	 * @param config
	 *            the config array containing the main lobe angle
	 */
	public void calcRadiatorsPhase(double[] config) {
		this.model.calcRadiatorsPhase(this.simIndex, this.settingsEnforcer(Controller.CONFIGURATION_TO_MODEL, config));
	}

	/**
	 * <pre>
	 * Calls the corresponding method to apply a window function to all the amplitudes of a linear array.
	 * </pre>
	 * 
	 * @param config
	 *            the config array containing the window function and its parameter
	 */
	public void applyWindowFunction(double[] config) {
		this.model.applyWindowFunction(this.simIndex, this.settingsEnforcer(Controller.CONFIGURATION_TO_MODEL, config));
	}

	/**
	 * <pre>
	 * Enforces the main lobe angle and/or the side lobe attenuation by calling the method calcRadiatorsPhase() and/or applyWindowFunction().
	 * </pre>
	 * 
	 * @param config
	 */
	public void antennaLobeEnforcer(double[] config) {
		if (config[11] == 1.0) {
			this.calcRadiatorsPhase(config);
		}

		if (config[13] == 1.0) {
			this.applyWindowFunction(config);
		}
	}

	/**
	 * <pre>
	 * Disables the main lobe angle and the side lobe attenuation without triggering an event.
	 * </pre>
	 */
	public void disableAntennaLobe() {
		if (this.config.get(this.simIndex).getConfig()[11] != AntennaArray.OFF) {
			this.view.simPanel.get(this.simIndex).inputPanel.mainLobePanel.rbOff
					.removeItemListener(this.view.simPanel.get(this.simIndex).inputPanel.mainLobePanel.ilMainLobeState);
			this.view.simPanel.get(this.simIndex).inputPanel.mainLobePanel.rbOff.setSelected(true);
			this.view.simPanel.get(this.simIndex).inputPanel.mainLobePanel.rbOff
					.addItemListener(this.view.simPanel.get(this.simIndex).inputPanel.mainLobePanel.ilMainLobeState);
			this.view.simPanel.get(this.simIndex).inputPanel.mainLobePanel.hideFields();
		}

		if (this.config.get(this.simIndex).getConfig()[13] != AntennaArray.OFF) {
			this.view.simPanel.get(this.simIndex).inputPanel.sideLobePanel.rbOff
					.removeItemListener(this.view.simPanel.get(this.simIndex).inputPanel.sideLobePanel.ilSideLobeState);
			this.view.simPanel.get(this.simIndex).inputPanel.sideLobePanel.rbOff.setSelected(true);
			this.view.simPanel.get(this.simIndex).inputPanel.sideLobePanel.rbOff
					.addItemListener(this.view.simPanel.get(this.simIndex).inputPanel.sideLobePanel.ilSideLobeState);
			this.view.simPanel.get(this.simIndex).inputPanel.sideLobePanel.hideFields();
		}
	}

	/**
	 * <pre>
	 * Converts the geometry to 'Custom' without resetting all the radiators nor triggering an event.
	 * </pre>
	 */
	public void convertToCustom() {
		if (this.config.get(this.simIndex).getConfig()[3] != AntennaArray.CUSTOM) {
			this.view.simPanel.get(this.simIndex).inputPanel.geometryPanel.cbGeometry.removeItemListener(
					this.view.simPanel.get(this.simIndex).inputPanel.geometryPanel.ilResetSimulation);
			this.view.simPanel.get(this.simIndex).inputPanel.geometryPanel.cbGeometry
					.setSelectedIndex(AntennaArray.CUSTOM);
			this.view.simPanel.get(this.simIndex).inputPanel.geometryPanel.cbGeometry
					.addItemListener(this.view.simPanel.get(this.simIndex).inputPanel.geometryPanel.ilResetSimulation);
			this.view.simPanel.get(this.simIndex).inputPanel.geometryPanel.hideFields();
			this.view.simPanel.get(this.simIndex).inputPanel.tabbedPane.setSelectedIndex(0);
			this.disableAntennaLobe();
			this.displayMsg("Warning: The geometry was set to 'Custom'.");
		}
	}

	/**
	 * <pre>
	 * Checks the input values for changes.
	 * </pre>
	 * 
	 * @param config
	 *            the read in values from the ui
	 * @return 0, if nothing has changed
	 */
	public int checkForChanges(double[] config) {
		double[] currentConfig = this.config.get(this.simIndex).getConfig();

		for (int i = 0; i < config.length; i++) {
			if (config[i] != currentConfig[i]) {
				return -1;
			}
		}

		return 0;
	}

	/**
	 * <pre>
	 * Calls the corresponding method to display an error message on the status bar.
	 * </pre>
	 * 
	 * @param errorMsg
	 *            the message to be displayed
	 */
	public void displayError(String errorMsg) {
		this.view.statusBar.displayError(errorMsg);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to display a message on the status bar.
	 * </pre>
	 * 
	 * @param message
	 *            the message to be displayed
	 */
	public void displayMsg(String message) {
		this.view.statusBar.displayMsg(message);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to clear the displayed message/error on the status bar.
	 * </pre>
	 */
	public void resetStatusBar() {
		this.view.statusBar.reset();
	}

}
