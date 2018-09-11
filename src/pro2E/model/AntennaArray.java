package pro2E.model;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * The <b><code>AntennaArray</code></b> class represents an antenna array and contains a list with all the radiators and the diagram of said array.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class AntennaArray {

	private List<Radiator> radiator = new ArrayList<>();
	private AntennaDiagram antennaDiagram;

	// Geometry
	public static final int LINEAR = 0;
	public static final int GRID = 1;
	public static final int CIRCLE = 2;
	public static final int SQUARE = 3;
	public static final int CUSTOM = 4;

	// Radiator Type
	public static final int ISOTROPIC = 0;
	public static final int DIPOLE = 1; // Half-Wave Dipole

	// Radiator State
	public static final int VISIBLE = 0;
	public static final int HIDDEN = 1;

	// State
	public static final int OFF = 0;
	public static final int ON = 1;

	// Diagram Type
	public static final int CARTESIAN = 0;
	public static final int POLAR = 1;

	// Window Function
	public static final int RECTANGULAR = 0;
	public static final int DOLPH_CHEBYSHEV = 1;
	public static final int SINE = 2; // with offset
	public static final int HANN = 3; // sine squared
	public static final int HAMMING = 4;
	public static final int TRIANGULAR = 5;
	public static final int PARZEN = 6;
	public static final int BLACKMAN = 7;
	public static final int WELCH = 8;

	/**
	 * <pre>
	 * Instantiates an AntennaArray object with the given radiator parameters array and the config array (used when a file is loaded).
	 * </pre>
	 * 
	 * @param radiatorParameters
	 * @param config
	 */
	public AntennaArray(double[][] radiatorParameters, double[] config) {
		this(config);
		// config[3]: geometry
		if ((int) config[3] == AntennaArray.CUSTOM) {
			this.setRadiators(radiatorParameters, config);
		} else {
			boolean state;
			for (int i = 0; i < radiatorParameters.length; i++) {
				// radiatorParameters[][5]: state
				if (radiatorParameters[i][5] == 0.0) {
					state = false;
				} else {
					state = true;
				}
				this.setRadiatorState(i, state);
				// radiatorParameters[][2]: amplitude
				this.updateRadiatorAmplitude(i, radiatorParameters[i][2]);
				// radiatorParameters[][3]: phase
				this.updateRadiatorPhase(i, radiatorParameters[i][3], config);
			}
		}
	}

	/**
	 * <pre>
	 * Instantiates an AntennaArray object using the given configuration array.
	 * </pre>
	 * 
	 * @param config
	 */
	public AntennaArray(double[] config) {
		// config[3]: geometry
		switch ((int) config[3]) {
		case LINEAR:
			this.createLinearArray(config);
			break;
		case GRID:
			this.createGridArray(config);
			break;
		case CIRCLE:
			this.createCircleArray(config);
			break;
		case SQUARE:
			this.createSquareArray(config);
			break;
		}
	}

	/**
	 * <pre>
	 * Creates a linear array using the given config.
	 * </pre>
	 * 
	 * @param config
	 */
	public void createLinearArray(double[] config) {
		// config[4]: numberOfRadiatorsX | config[5]: spacingX
		double[][] pos = AntennaArrayFunctions.linear((int) config[4], config[5]);
		this.initRadiators(pos);
	}

	/**
	 * <pre>
	 * Creates a grid array using the given config.
	 * </pre>
	 * 
	 * @param config
	 */
	public void createGridArray(double[] config) {
		// config[4]: numberOfRadiatorsX | config[6]: numberOfRadiatorsY | config[5]:
		// spacingX | config[7]: spacingY
		double[][] pos = AntennaArrayFunctions.grid((int) config[4], (int) config[6], config[5], config[7]);
		this.initRadiators(pos);
	}

	/**
	 * <pre>
	 * Creates a circle array using the given config.
	 * </pre>
	 * 
	 * @param config
	 */
	public void createCircleArray(double[] config) {
		// config[8]: numberOfRadiators | config[9]: radius
		double[][] pos = AntennaArrayFunctions.circle((int) config[8], config[9]);
		this.initRadiators(pos);
	}

	/**
	 * <pre>
	 * Creates a square array using the given config.
	 * </pre>
	 * 
	 * @param config
	 */
	public void createSquareArray(double[] config) {
		// config[8]: numberOfRadiators | config[9]: radius
		double[][] pos = AntennaArrayFunctions.square((int) config[8], config[10]);
		this.initRadiators(pos);
	}

	/**
	 * <pre>
	 * Initializes all the radiators using the given pos array (contains the coordinates of the radiators).
	 * </pre>
	 * 
	 * @param pos
	 */
	public void initRadiators(double[][] pos) {
		for (int i = 0; i < pos.length; i++) {
			this.radiator.add(new Radiator(pos[i][0], pos[i][1]));
		}
	}

	/**
	 * <pre>
	 * Create an AntennaDiagram object using the radiators and the given config.
	 * </pre>
	 * 
	 * @param config
	 */
	public void createDiagram(double[] config) {
		Radiator[] r = this.radiator.toArray(new Radiator[this.radiator.size()]);
		this.antennaDiagram = new AntennaDiagram(r, config);
	}

	/**
	 * <pre>
	 * Fetches the array containing the normalized power from the AntennaDiagram class and returns it.
	 * </pre>
	 * 
	 * @return array containing the normalized power
	 */
	public double[] getNormalizedPower() {
		return this.antennaDiagram.getNormalizedPower();
	}

	/**
	 * <pre>
	 * Fetches the array containing the normalized element factor power from the AntennaDiagram class and returns it.
	 * </pre>
	 * 
	 * @return array containing the normalized element factor power
	 */
	public double[] getNormalizedElementPower() {
		return this.antennaDiagram.getNormalizedElementPower();
	}

	/**
	 * <pre>
	 * Returns an array containing all the radiator parameters.
	 * </pre>
	 * 
	 * @return an array with the radiator parameters
	 */
	public double[][] getRadiators() {
		double[][] radiators = new double[this.radiator.size()][6];
		for (int i = 0; i < radiators.length; i++) {
			radiators[i] = this.radiator.get(i).getParameters();
		}
		return radiators;
	}

	/**
	 * <pre>
	 * Returns the radiator count.
	 * </pre>
	 * 
	 * @return the radiator count
	 */
	public int getRadiatorCount() {
		return this.radiator.size();
	}

	/**
	 * <pre>
	 * Sets the radiators of the array using the given radiator parameters array and config array.
	 * </pre>
	 * 
	 * @param radiatorParameters
	 * @param config
	 */
	public void setRadiators(double[][] radiatorParameters, double[] config) {
		boolean hidden;
		for (int i = 0; i < radiatorParameters.length; i++) {
			// radiatorParameters[][4]: hidden
			if (radiatorParameters[i][5] == 0.0) {
				hidden = false;
			} else {
				hidden = true;
			}
			// radiatorParameters[][0]: x | radiatorParameters[][1]: y |
			// radiatorParameters[][2]: a | radiatorParameters[][3]: p
			// config[24]: period
			this.radiator.add(new Radiator(radiatorParameters[i][0], radiatorParameters[i][1], radiatorParameters[i][2],
					radiatorParameters[i][3], hidden, config[24]));
		}
	}

	/**
	 * <pre>
	 * Adds a radiator to the array using the given x- and v-values of the position.
	 * </pre>
	 * 
	 * @param x
	 *            the x-value of the position
	 * @param y
	 *            the y-value of the position
	 */
	public void addRadiator(double x, double y) {
		this.radiator.add(new Radiator(x, y));
	}

	public void removeRadiators(int[] radiatorIndices) {
		for (int i = 0; i < radiatorIndices.length; i++) {
			// (- i): used to compensate for indices already removed
			this.radiator.remove(radiatorIndices[i] - i);
		}
	}

	/**
	 * <pre>
	 * Calls the corresponding method to set the 'hidden' state of the radiators at the given indices.
	 * </pre>
	 * 
	 * @param radiatorIndices
	 *            an array containing the radiator indices to be changed
	 * @param state
	 *            the 'hidden' state
	 */
	public void setRadiatorsState(int[] radiatorIndices, boolean state) {
		for (int i = 0; i < radiatorIndices.length; i++) {
			this.radiator.get(radiatorIndices[i]).setState(state);
		}
	}

	/**
	 * <pre>
	 * Calls the corresponding method to set the 'hidden' state of the radiator at the given index.
	 * </pre>
	 * 
	 * @param radiatorIndex
	 *            the radiator index to be changed
	 * @param state
	 *            the 'hidden' state
	 */
	public void setRadiatorState(int radiatorIndex, boolean state) {
		this.radiator.get(radiatorIndex).setState(state);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to update the radiators position.
	 * </pre>
	 * 
	 * @param radiatorIndex
	 *            the radiator index to be changed
	 * @param x
	 *            the new x-value of the position
	 * @param y
	 *            the new y-value of the position
	 */
	public void updateRadiatorPosition(int radiatorIndex, double x, double y) {
		this.radiator.get(radiatorIndex).setPosition(x, y);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to update the phase of the radiators at the given indices.
	 * </pre>
	 * 
	 * @param radiatorIndices
	 *            an array containing the radiator indices to be changed
	 * @param p
	 *            the new value of the phase
	 * @param config
	 */
	public void updateRadiatorsPhase(int[] radiatorIndices, double p, double[] config) {
		for (int i = 0; i < radiatorIndices.length; i++) {
			// config[24]: period
			this.radiator.get(radiatorIndices[i]).setPhase(p, config[24]);
		}
	}

	/**
	 * <pre>
	 * Calls the corresponding method to update the phase of the radiator at the given index.
	 * </pre>
	 * 
	 * @param radiatorIndex
	 *            the radiator index to be changed
	 * @param p
	 *            the new value of the phase
	 * @param config
	 */
	public void updateRadiatorPhase(int radiatorIndex, double p, double[] config) {
		this.radiator.get(radiatorIndex).setPhase(p, config[24]);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to update the amplitude of the radiators at the given indices.
	 * </pre>
	 *
	 * @param radiatorIndices
	 *            an array containing the radiator indices to be changed
	 * @param a
	 *            the new value of the amplitude
	 */
	public void updateRadiatorsAmplitude(int[] radiatorIndices, double a) {
		for (int i = 0; i < radiatorIndices.length; i++) {
			this.radiator.get(radiatorIndices[i]).setAmplitude(a);
		}
	}

	/**
	 * <pre>
	 * Calls the corresponding method to update the amplitude of the radiator at the given index.
	 * </pre>
	 * 
	 * @param radiatorIndex
	 *            the radiator index to be changed
	 * @param a
	 *            the new value of the amplitude
	 */
	public void updateRadiatorAmplitude(int radiatorIndex, double a) {
		this.radiator.get(radiatorIndex).setAmplitude(a);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to update the time delay for each radiator using the given period (in the config array).
	 * </pre>
	 * 
	 * @param config
	 */
	public void updatePeriod(double[] config) {
		for (int i = 0; i < this.radiator.size(); i++) {
			// config[24]: period
			this.radiator.get(i).updateTimeDelay(config[24]);
		}
	}

	/**
	 * <pre>
	 * Calls the corresponding method to calculate the phases for each radiator in a linear array.
	 * </pre>
	 * 
	 * @param config
	 */
	public void calcRadiatorsPhase(double[] config) {
		for (int i = 0; i < this.radiator.size(); i++) {
			// config[0]: wavelength | config[0]: mainLobeAng | config[24]: period
			this.radiator.get(i).calcPhase(config[0], config[12], config[24]);
		}
	}

	/**
	 * <pre>
	 * - Calculates the values of the selcted window function
	 * - Sets the amplitude of each radiator to the value from the window function
	 * </pre>
	 * 
	 * @param config
	 */
	public void applyWindowFunction(double[] config) {
		int numberOfRadiators = this.radiator.size();
		double[] a = new double[numberOfRadiators];

		switch ((int) config[14]) {
		case AntennaArray.RECTANGULAR:
			a = MatlabFunctions.rectwin(numberOfRadiators);
			break;
		case AntennaArray.DOLPH_CHEBYSHEV:
			a = MatlabFunctions.chebwin(numberOfRadiators, config[15]);
			break;
		case AntennaArray.SINE:
			a = MatlabFunctions.sinewin(numberOfRadiators, config[15]);
			break;
		case AntennaArray.HANN:
			a = MatlabFunctions.hann(numberOfRadiators);
			break;
		case AntennaArray.HAMMING:
			a = MatlabFunctions.hamming(numberOfRadiators);
			break;
		case AntennaArray.TRIANGULAR:
			a = MatlabFunctions.triang(numberOfRadiators);
			break;
		case AntennaArray.PARZEN:
			a = MatlabFunctions.parzenwin(numberOfRadiators);
			break;
		case AntennaArray.BLACKMAN:
			a = MatlabFunctions.blackman(numberOfRadiators);
			break;
		case AntennaArray.WELCH:
			a = MatlabFunctions.welch(numberOfRadiators);
			break;
		default:
			a = MatlabFunctions.rectwin(numberOfRadiators);
		}

		for (int i = 0; i < numberOfRadiators; i++) {
			this.radiator.get(i).setAmplitude(a[i]);
		}
	}

}
