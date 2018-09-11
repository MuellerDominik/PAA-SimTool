package pro2E.model;

/**
 * <pre>
 * The <b><code>AntennaDiagram</code></b> class calculates the normalized power and the normalized element factor power and stores them.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class AntennaDiagram {

	private Complex[] s, se;
	private double[] sAbs, sNorm, seAbs, seNorm;
	private double sAbsMax, seAbsMax;

	/**
	 * <pre>
	 * - Instantiates an AntennaDiagram object with the given array of Radiator objects and the config array
	 * - Creates an array containing the normalized power
	 * - Creates an array containing the normalized element factor power
	 * </pre>
	 * 
	 * @param r
	 *            an array of Radiator objects
	 * @param config
	 */
	public AntennaDiagram(Radiator[] r, double[] config) {
		double startAng, stopAng;

		// config[18]: diagramType
		if (((int) config[18]) == AntennaArray.CARTESIAN) {
			startAng = config[19];
			stopAng = config[20];
		} else {
			// polar diagram is always plotted from 0 to 360 degrees
			startAng = 0.0;
			stopAng = AntennaArrayFunctions.T;
		}

		Complex[] sEntireRange;

		// config[16]: reflectorState
		if (((int) config[16]) == AntennaArray.OFF) {
			// config[0]: wavelength | config[1]: radiatorType | config[2]: radiatorAng
			// config[21]: points
			this.s = AntennaArrayFunctions.s(r, config[0], (int) config[1], config[2], startAng, stopAng,
					(int) config[21]);
			sEntireRange = AntennaArrayFunctions.s(r, config[0], (int) config[1], config[2], 0.0,
					AntennaArrayFunctions.T, (int) config[21]);
		} else {
			// config[0]: wavelength | config[1]: radiatorType | config[2]: radiatorAng
			// config[17]: reflectorOffset | config[21]: points
			this.s = AntennaArrayFunctions.sReflector(r, config[0], (int) config[1], config[2], config[17], startAng,
					stopAng, (int) config[21]);
			sEntireRange = AntennaArrayFunctions.sReflector(r, config[0], (int) config[1], config[2], config[17], 0.0,
					AntennaArrayFunctions.T, (int) config[21]);
		}
		this.sAbs = Complex.abs(this.s);
		double[] sAbsEntireRange = Complex.abs(sEntireRange);
		this.sAbsMax = MatlabFunctions.max(sAbsEntireRange);
		this.sNorm = AntennaArrayFunctions.norm(this.sAbs, this.sAbsMax);

		// Radiator Type (polar diagram from 0 to 360 degrees)
		this.se = new Complex[1000];
		double[] rotationAng = MatlabFunctions.linspace(0.0, AntennaArrayFunctions.T, se.length);
		for (int i = 0; i < se.length; i++) {
			// config[0]: wavelength | config[1]: radiatorType | config[2]: radiatorAng
			this.se[i] = AntennaArrayFunctions.se(config[0], (int) config[1], config[2], rotationAng[i]);
		}
		this.seAbs = Complex.abs(se);
		this.seAbsMax = MatlabFunctions.max(seAbs);
		this.seNorm = AntennaArrayFunctions.norm(seAbs, seAbsMax);
	}

	/**
	 * <pre>
	 * Returns the normalized power.
	 * </pre>
	 * 
	 * @return an array containing the normalized power
	 */
	public double[] getNormalizedPower() {
		return this.sNorm;
	}

	/**
	 * <pre>
	 * Returns the normalized element factor power.
	 * </pre>
	 * 
	 * @return an array containing the normalized element factor power
	 */
	public double[] getNormalizedElementPower() {
		return this.seNorm;
	}

}
