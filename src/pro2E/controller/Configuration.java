package pro2E.controller;

import pro2E.model.AntennaArray;

/**
 * <pre>
 * The <b><code>Configuration</code></b> class stores the properties of an array.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class Configuration {

	// Simulation Name
	private String name;

	// Input - Geometry
	private int geometry = AntennaArray.LINEAR;
	private int numberOfRadiatorsX = 8;
	private double spacingX = 1.0;
	private int numberOfRadiatorsY = 5;
	private double spacingY = 1.0;
	private int numberOfRadiators = 8;
	private double radius = 4.0;
	private double sideLength = 4.0;

	// Input - Properties
	private double wavelength = 2.0;
	private int radiatorType = AntennaArray.ISOTROPIC;
	private double radiatorAng = 0.0; // in degrees
	private int reflectorState = AntennaArray.OFF;
	private double reflectorOffset = -0.5;

	// Input - Main Lobe
	private int mainLobeState = AntennaArray.OFF;
	private double mainLobeAng = 90.0; // in degrees

	// Input - Side Lobe
	private int sideLobeState = AntennaArray.OFF;
	private int windowFunc = AntennaArray.RECTANGULAR;
	private double windowFuncParam = 0.0;

	// Output - Diagram
	private int diagramType = AntennaArray.CARTESIAN;
	private double startAng = 0.0; // in degrees
	private double stopAng = 360.0; // in degrees
	private int points = 1000; // 100 to 1700
	private double maxGain = 0.0;
	private double minGain = -50.0;

	// Output - Wave Propagation
	// *not implemented in the ui*
	private double period = 2.0;

	/**
	 * <pre>
	 * Creates a default Configuration object.
	 * </pre>
	 * 
	 * @param name
	 */
	public Configuration(String name) {
		this.setName(name);
	}

	/**
	 * <pre>
	 * Creates a custom Configuration object.
	 * </pre>
	 * 
	 * @param name
	 * @param config
	 */
	public Configuration(String name, double[] config) {
		this.setName(name);
		this.setConfig(config);
	}

	/**
	 * <pre>
	 * Sets the attribute name to a certain name.
	 * </pre>
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * <pre>
	 * Sets all the attributes of a certain configuration object.
	 * </pre>
	 * 
	 * @param config
	 */
	public void setConfig(double[] config) {
		this.wavelength = config[0];
		this.radiatorType = (int) config[1];
		this.radiatorAng = config[2];
		this.geometry = (int) config[3];
		this.numberOfRadiatorsX = (int) config[4];
		this.spacingX = config[5];
		this.numberOfRadiatorsY = (int) config[6];
		this.spacingY = config[7];
		this.numberOfRadiators = (int) config[8];
		this.radius = config[9];
		this.sideLength = config[10];
		this.mainLobeState = (int) config[11];
		this.mainLobeAng = config[12];
		this.sideLobeState = (int) config[13];
		this.windowFunc = (int) config[14];
		this.windowFuncParam = config[15];
		this.reflectorState = (int) config[16];
		this.reflectorOffset = config[17];
		this.diagramType = (int) config[18];
		this.startAng = config[19];
		this.stopAng = config[20];
		this.points = (int) config[21];
		this.maxGain = config[22];
		this.minGain = config[23];
		this.period = config[24];
	}

	/**
	 * <pre>
	 * Returns the attribute name.
	 * </pre>
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * <pre>
	 * Returns an array containing all the attributes of a certain configuration object.
	 * </pre>
	 * 
	 * @return
	 */
	public double[] getConfig() {
		double[] config = new double[25];
		config[0] = this.wavelength;
		config[1] = this.radiatorType;
		config[2] = this.radiatorAng;
		config[3] = this.geometry;
		config[4] = this.numberOfRadiatorsX;
		config[5] = this.spacingX;
		config[6] = this.numberOfRadiatorsY;
		config[7] = this.spacingY;
		config[8] = this.numberOfRadiators;
		config[9] = this.radius;
		config[10] = this.sideLength;
		config[11] = this.mainLobeState;
		config[12] = this.mainLobeAng;
		config[13] = this.sideLobeState;
		config[14] = this.windowFunc;
		config[15] = this.windowFuncParam;
		config[16] = this.reflectorState;
		config[17] = this.reflectorOffset;
		config[18] = this.diagramType;
		config[19] = this.startAng;
		config[20] = this.stopAng;
		config[21] = this.points;
		config[22] = this.maxGain;
		config[23] = this.minGain;
		config[24] = this.period;
		return config;
	}

}
