package pro2E.controller;

/**
 * <pre>
 * The <b><code>Settings</code></b> class stores the settings of the application.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class Settings {

	// Angular Unit
	public static final int DEG = 0;
	public static final int RAD = 1;

	// Electromagnetic Radiation Unit
	public static final int WAVELENGTH = 0;
	public static final int FREQUENCY = 1;

	private int angularUnit = Settings.DEG;
	private int electromagneticRadiationUnit = Settings.WAVELENGTH;

	/**
	 * <pre>
	 * Sets the attributes of a Settings object.
	 * </pre>
	 * 
	 * @param angularUnit
	 * @param electromagneticRadiationUnit
	 */
	public void setSettings(int angularUnit, int electromagneticRadiationUnit) {
		this.angularUnit = angularUnit;
		this.electromagneticRadiationUnit = electromagneticRadiationUnit;
	}

	/**
	 * <pre>
	 * Sets the angularUnit attribute.
	 * </pre>
	 * 
	 * @param angularUnit
	 */
	public void setAngularUnit(int angularUnit) {
		this.angularUnit = angularUnit;
	}

	/**
	 * <pre>
	 * Sets the electromagneticRadiationUnit attribute.
	 * </pre>
	 * 
	 * @param electromagneticRadiationUnit
	 */
	public void setElectromagneticRadiationUnit(int electromagneticRadiationUnit) {
		this.electromagneticRadiationUnit = electromagneticRadiationUnit;
	}

	/**
	 * <pre>
	 * Returns the attribute angularUnit.
	 * </pre>
	 * 
	 * @return the angular unit
	 */
	public int getAngularUnit() {
		return this.angularUnit;
	}

	/**
	 * <pre>
	 * Returns the attribute electromagneticRadiationUnit.
	 * </pre>
	 * 
	 * @return the electromagnetic radiation unit
	 */
	public int getElectromagneticRadiationUnit() {
		return this.electromagneticRadiationUnit;
	}

}
