package pro2E.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * <pre>
 * The <b><code>Model</code></b> class contains a list of AntenaArray objects. Each element in the list corresponds to a simulation and therefore a different antenna array.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class Model extends Observable {

	private List<AntennaArray> antennaArray = new ArrayList<>();

	/**
	 * <pre>
	 * Adds a new AntennaArray object to the antennaArray list by creating one using the given config.
	 * </pre>
	 * 
	 * @param config
	 */
	public void addAntennaArray(double[] config) {
		this.antennaArray.add(new AntennaArray(config));
	}

	/**
	 * <pre>
	 * Removes an AntennaArray object at the given index.
	 * </pre>
	 * 
	 * @param arrayIndex
	 */
	public void removeAntennaArray(int arrayIndex) {
		this.antennaArray.remove(arrayIndex);
	}

	/**
	 * <pre>
	 * Adds a new AntennaArray object to the antennaArray list by creating one using the radiator parameters from an existing one at the given index.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @param config
	 */
	public void duplicateAntennaArray(int arrayIndex, double[] config) {
		double[][] radiatorParameters = this.getRadiators(arrayIndex);
		this.antennaArray.add(new AntennaArray(radiatorParameters, config));
	}

	/**
	 * <pre>
	 * Adds a new AntennaArray object to the antennaArray list by creating one using the given radiator parameters from a loaded file.
	 * </pre>
	 * 
	 * @param radiatorParameters
	 * @param config
	 */
	public void loadAntennaArray(double[][] radiatorParameters, double[] config) {
		this.antennaArray.add(new AntennaArray(radiatorParameters, config));
	}

	/**
	 * <pre>
	 * Resets an object in the antennaArray list by creating a new AntennaArray object at the given index.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @param config
	 */
	public void resetAntennaArray(int arrayIndex, double[] config) {
		this.antennaArray.set(arrayIndex, new AntennaArray(config));
	}

	/**
	 * <pre>
	 * - Calls the corresponding method to create the diagram
	 * - Calls the corresponding method to notify the observers
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @param config
	 */
	public void createDiagram(int arrayIndex, double[] config) {
		this.antennaArray.get(arrayIndex).createDiagram(config);
		this.notifyObservers(arrayIndex);
	}

	/**
	 * <pre>
	 * - Marks this object as changed
	 * - Notifies the registered observers and supplies the given arrayIndex as an Object object
	 * </pre>
	 * 
	 * @param arrayIndex
	 */
	public void notifyObservers(int arrayIndex) {
		super.setChanged();
		super.notifyObservers(arrayIndex);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to add a radiator to an AntennaArray object at the given index.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @param x
	 * @param y
	 */
	public void addRadiator(int arrayIndex, double x, double y) {
		this.antennaArray.get(arrayIndex).addRadiator(x, y);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to remove radiators from an AntennaArray object at the given index.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @param radiatorIndices
	 */
	public void removeRadiators(int arrayIndex, int[] radiatorIndices) {
		this.antennaArray.get(arrayIndex).removeRadiators(radiatorIndices);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to set the 'hidden' state of radiators from an AntennaArray object at the given index.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @param radiatorIndices
	 * @param state
	 */
	public void setRadiatorsState(int arrayIndex, int[] radiatorIndices, boolean state) {
		this.antennaArray.get(arrayIndex).setRadiatorsState(radiatorIndices, state);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to update a radiators position from an AntennaArray object at the given index.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @param radiatorIndex
	 * @param x
	 * @param y
	 */
	public void updateRadiatorPosition(int arrayIndex, int radiatorIndex, double x, double y) {
		this.antennaArray.get(arrayIndex).updateRadiatorPosition(radiatorIndex, x, y);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to update the phase of radiators from an AntennaArray object at the given index.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @param radiatorIndices
	 * @param p
	 * @param config
	 */
	public void updateRadiatorsPhase(int arrayIndex, int[] radiatorIndices, double p, double[] config) {
		this.antennaArray.get(arrayIndex).updateRadiatorsPhase(radiatorIndices, p, config);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to update the amplitude of radiators from an AntennaArray object at the given index.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @param radiatorIndices
	 * @param a
	 */
	public void updateRadiatorsAmplitude(int arrayIndex, int[] radiatorIndices, double a) {
		this.antennaArray.get(arrayIndex).updateRadiatorsAmplitude(radiatorIndices, a);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to update the period of an AntennaArray object at the given index.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @param config
	 */
	public void updatePeriod(int arrayIndex, double[] config) {
		this.antennaArray.get(arrayIndex).updatePeriod(config);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to calculate and update the phase of radiators from an AntennaArray object at the given index.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @param config
	 */
	public void calcRadiatorsPhase(int arrayIndex, double[] config) {
		this.antennaArray.get(arrayIndex).calcRadiatorsPhase(config);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to calculate and update the amplitude of radiators from an AntennaArray object at the given index.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @param config
	 */
	public void applyWindowFunction(int arrayIndex, double[] config) {
		this.antennaArray.get(arrayIndex).applyWindowFunction(config);
	}

	/**
	 * <pre>
	 * Calls the corresponding method to fetch the normalized power from the AntennaDiagram class and returns it.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @return
	 */
	public double[] getNormalizedPower(int arrayIndex) {
		return this.antennaArray.get(arrayIndex).getNormalizedPower();
	}

	/**
	 * <pre>
	 * Calls the corresponding method to fetch the normalized element factor power from the AntennaDiagram class and returns it.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @return
	 */
	public double[] getNormalizedElementPower(int arrayIndex) {
		return this.antennaArray.get(arrayIndex).getNormalizedElementPower();
	}

	/**
	 * <pre>
	 * Calls the corresponding method to fetch all the radiator parameters and returns them.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @return
	 */
	public double[][] getRadiators(int arrayIndex) {
		return this.antennaArray.get(arrayIndex).getRadiators();
	}

	/**
	 * <pre>
	 * Calls the corresponding method to fetch the radiator count and returns it.
	 * </pre>
	 * 
	 * @param arrayIndex
	 * @return
	 */
	public int getRadiatorCount(int arrayIndex) {
		return this.antennaArray.get(arrayIndex).getRadiatorCount();
	}

}
