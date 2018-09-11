package pro2E.model;

/**
 * <pre>
 * The <b><code>Radiator</code></b> class represents a radiator in an antenna array and therefore stores all its relevant parameters.
 * Furthermore it contains various methods to manipulate those parameters.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class Radiator {

	// Parameters
	private double x, y, a, p, t;
	private boolean hidden; // state

	/**
	 * <pre>
	 * Instantiates a Radiator object at a given position with an amplitude of one.
	 * </pre>
	 * 
	 * @param x
	 *            the x-value of the position
	 * @param y
	 *            the y-value of the position
	 */
	public Radiator(double x, double y) {
		this(x, y, 1.0);
	}

	/**
	 * <pre>
	 * Instantiates a Radiator object at a given position with a given amplitude.
	 * </pre>
	 * 
	 * @param x
	 *            the x-value of the position
	 * @param y
	 *            the y-value of the position
	 * @param a
	 *            the amplitude
	 */
	public Radiator(double x, double y, double a) {
		this.x = x;
		this.y = y;
		this.a = a;
	}

	/**
	 * <pre>
	 * Instantiates a Radiator object using all the given parameters.
	 * </pre>
	 * 
	 * @param x
	 *            the x-value of the position
	 * @param y
	 *            the y-value of the position
	 * @param a
	 *            the amplitude
	 * @param p
	 *            the phase
	 * @param hidden
	 *            the 'hidden' state
	 * @param period
	 *            the period
	 */
	public Radiator(double x, double y, double a, double p, boolean hidden, double period) {
		this.x = x;
		this.y = y;
		this.a = a;
		this.p = p;
		this.t = AntennaArrayFunctions.timeDelay(p, period);
		this.hidden = hidden;
	}

	/**
	 * <pre>
	 * Returns the x-value of the position.
	 * </pre>
	 * 
	 * @return
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * <pre>
	 * Returns the y-value of the position.
	 * </pre>
	 * 
	 * @return
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * <pre>
	 * Returns the amplitude.
	 * </pre>
	 * 
	 * @return
	 */
	public double getA() {
		return this.a;
	}

	/**
	 * <pre>
	 * Returns the phase.
	 * </pre>
	 * 
	 * @return
	 */
	public double getP() {
		return this.p;
	}

	/**
	 * <pre>
	 * Returns the time delay.
	 * </pre>
	 * 
	 * @return
	 */
	public double getTimeDelay() {
		return this.t;
	}

	/**
	 * <pre>
	 * Returns the 'hidden' state.
	 * </pre>
	 * 
	 * @return
	 */
	public boolean getState() {
		return this.hidden;
	}

	/**
	 * <pre>
	 * Returns an array containing all the parameters.
	 * </pre>
	 * 
	 * @return an array containing all the parameters
	 */
	public double[] getParameters() {
		double[] prm = new double[6];
		prm[0] = this.x;
		prm[1] = this.y;
		prm[2] = this.a;
		prm[3] = this.p;
		prm[4] = this.t;
		if (this.hidden) {
			prm[5] = 1.0;
		} else {
			prm[5] = 0.0;
		}
		return prm;
	}

	/**
	 * <pre>
	 * Sets the position attributes to the given x- and y-values of the position.
	 * </pre>
	 * 
	 * @param x
	 *            the x-value of the position
	 * @param y
	 *            the y-value of the position
	 */
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * <pre>
	 * Sets the amplitude attribute to the given amplitude.
	 * </pre>
	 * 
	 * @param a
	 *            the amplitude
	 */
	public void setAmplitude(double a) {
		this.a = a;
	}

	/**
	 * <pre>
	 * Sets the phase and time delay attributes by using the given phase and period.
	 * </pre>
	 * 
	 * @param p
	 *            the phase
	 * @param period
	 *            the period
	 */
	public void setPhase(double p, double period) {
		this.p = p;
		this.updateTimeDelay(period);
	}

	/**
	 * <pre>
	 * Sets the 'hidden' state attribute to the given state.
	 * </pre>
	 * 
	 * @param hidden
	 *            the 'hidden' state
	 */
	public void setState(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * <pre>
	 * - Calculates the phase by using the given parameters
	 * - Calls the corresponding method to update the time delay
	 * </pre>
	 * 
	 * @param wavelength
	 *            the wavelength
	 * @param mainLobeAng
	 *            the main lobe angle
	 * @param period
	 *            the period
	 */
	public void calcPhase(double wavelength, double mainLobeAng, double period) {
		this.p = AntennaArrayFunctions.phaseShift(this, mainLobeAng, wavelength);
		this.updateTimeDelay(period);
	}

	/**
	 * <pre>
	 * Updates the time delay by using the given period.
	 * </pre>
	 * 
	 * @param period
	 *            the period
	 */
	public void updateTimeDelay(double period) {
		this.t = AntennaArrayFunctions.timeDelay(this.p, period);
	}

}
