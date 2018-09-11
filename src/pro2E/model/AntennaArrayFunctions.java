package pro2E.model;

/**
 * <pre>
 * The <b><code>AntennaArrayFunctions</code></b> class contains a set of static methods to calculate various antenna array parameters.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class AntennaArrayFunctions {

	public static final double C = 3e8;
	public static final double T = 2 * Math.PI;

	/**
	 * <pre>
	 * Calculates and returns the frequency from a given wavelength.
	 * </pre>
	 * 
	 * @param wavelength
	 * @return the frequency
	 */
	public static double frequency(double wavelength) {
		return (C / wavelength);
	}

	/**
	 * <pre>
	 * Calculates and returns the wavelength from a given frequency.
	 * </pre>
	 * 
	 * @param frequency
	 * @return the wavelength
	 */
	public static double wavelength(double frequency) {
		return (C / frequency);
	}

	/**
	 * <pre>
	 * Calculates and returns the free space wave number.
	 * </pre>
	 * 
	 * @param wavelength
	 * @return the free space wave number
	 */
	public static double k(double wavelength) {
		return (T / wavelength);
	}

	/**
	 * <pre>
	 * Calculates and returns the time delay of a radiator in dependence of the phase and period.
	 * </pre>
	 * 
	 * @param p
	 *            the phase of the radiator
	 * @param period
	 *            the period
	 * @return the time delay of a radiator
	 */
	public static double timeDelay(double p, double period) {
		double delay = period / T * (p - MatlabFunctions.fix(p / T) * T);
		return delay;
	}

	/**
	 * <pre>
	 * Calculates and return the phase of a radiator in dependence of the radiators position, the rotation angle and the wavelength.
	 * </pre>
	 * 
	 * @param r
	 *            a Radiator object
	 * @param rotationAng
	 *            the rotation angle
	 * @param wavelength
	 * @return the phase of the given radiator
	 */
	public static double phaseShift(Radiator r, double rotationAng, double wavelength) {
		double x = r.getX();
		double y = r.getY();
		return AntennaArrayFunctions.phaseShift(x, y, rotationAng, wavelength);
	}

	/**
	 * <pre>
	 * Calculates and return the phase of a radiator in dependence of the radiators position, the rotation angle and the wavelength.
	 * </pre>
	 * 
	 * @param x
	 *            the x-value of the position
	 * @param y
	 *            the y-value of the position
	 * @param rotationAng
	 *            the rotation angle
	 * @param wavelength
	 * @return the phase of the given radiator
	 */
	public static double phaseShift(double x, double y, double rotationAng, double wavelength) {
		double shift = k(wavelength) * Math.hypot(x, y) * Math.cos(rotationAng - Math.atan2(y, x));
		return shift;
	}

	/**
	 * <pre>
	 * Calculates and returns the complex element factor as a Complex object.
	 * </pre>
	 * 
	 * @param wavelength
	 * @param radiatorType
	 *            the radiator type
	 * @param radiatorAng
	 *            the angle of the radiator
	 * @param rotationAng
	 *            the rotation angle
	 * @return the complex element factor as a Complex object
	 */
	public static Complex se(double wavelength, int radiatorType, double radiatorAng, double rotationAng) {
		switch (radiatorType) {
		case AntennaArray.ISOTROPIC:
			return new Complex(1.0);
		case AntennaArray.DIPOLE:
			double epsilon = 1e-6;
			double diff = rotationAng - radiatorAng;
			// sin(diff) != 0 (to prevent a division by zero)
			// => diff != k * pi k = { 0, 1, 2 }
			if (Math.abs(diff) < epsilon || Math.abs(diff - Math.PI) < epsilon || Math.abs(diff - T) < epsilon) {
				return new Complex(0.0);
			} else {
				double elementFactor = Math.cos(Math.PI / 2 * Math.cos(diff)) / Math.sin(diff);
				return new Complex(elementFactor);
			}
		default:
			return new Complex(1.0);
		}
	}

	/**
	 * <pre>
	 * Calculates and returns the complex array factor as an array of Complex objects.
	 * </pre>
	 * 
	 * @param r
	 *            an array of Radiator objects
	 * @param wavelength
	 * @param radiatorType
	 *            the radiator type
	 * @param radiatorAng
	 *            the angle of the radiator
	 * @param startAng
	 *            the start angle
	 * @param stopAng
	 *            the stop angle
	 * @param points
	 *            the amount of points to calculate the array factor at
	 * @return the complex array factor as an array of Complex objects
	 */
	public static Complex[] s(Radiator[] r, double wavelength, int radiatorType, double radiatorAng, double startAng,
			double stopAng, int points) {
		Complex[] s = new Complex[points];
		double[] x = MatlabFunctions.linspace(startAng, stopAng, points);
		for (int i = 0; i < points; i++) {
			Complex c = new Complex();
			for (int j = 0; j < r.length; j++) {
				if (!r[j].getState()) {
					double re = r[j].getA() * Math.cos(phaseShift(r[j], x[i], wavelength) - r[j].getP());
					double im = r[j].getA() * Math.sin(phaseShift(r[j], x[i], wavelength) - r[j].getP());
					Complex tmp = new Complex(re, im);
					c = c.add(tmp);
				}
			}
			s[i] = se(wavelength, radiatorType, radiatorAng, x[i]).mul(c);
		}
		return s;
	}

	/**
	 * <pre>
	 * Calculates and returns the complex array factor as an array of Complex objects with the reflector enabled.
	 * </pre>
	 * 
	 * @param r
	 *            an array of Radiator object
	 * @param wavelength
	 * @param radiatorType
	 *            the radiator type
	 * @param radiatorAng
	 *            the angle of the radiator
	 * @param reflectorOffset
	 *            the offset of the reflector
	 * @param startAng
	 *            the start angle
	 * @param stopAng
	 *            the stop angle
	 * @param points
	 *            the amount of points to calculate the array factor at
	 * @return the complex array factor as an array of Complex objects with the
	 *         reflector enabled
	 */
	public static Complex[] sReflector(Radiator[] r, double wavelength, int radiatorType, double radiatorAng,
			double reflectorOffset, double startAng, double stopAng, int points) {
		Complex[] s = new Complex[points];
		double[] x = MatlabFunctions.linspace(startAng, stopAng, points);

		int numRadiators = r.length;

		for (int i = 0; i < r.length; i++) {
			if (r[i].getState()) {
				numRadiators--;
			}
		}

		double[] rX = new double[2 * numRadiators];
		double[] rY = new double[2 * numRadiators];
		double[] rA = new double[2 * numRadiators];
		double[] rP = new double[2 * numRadiators];

		int index = 0;
		for (int i = 0; i < r.length; i++) {
			if (!r[i].getState()) {
				rX[index] = r[i].getX();
				rX[numRadiators + index] = rX[index];

				rY[index] = r[i].getY();
				rY[numRadiators + index] = 2.0 * reflectorOffset - rY[index];

				rA[index] = r[i].getA();
				rA[numRadiators + index] = rA[index];

				rP[index] = r[i].getP();
				rP[numRadiators + index] = rP[index] + Math.PI;

				index++;
			}
		}

		for (int i = 0; i < points; i++) {
			Complex c = new Complex();
			// set the edges of the reflector to zero
			if ((x[i] != 0.0) && (x[i] != Math.PI) && (x[i] != (2 * Math.PI))) {
				for (int j = 0; j < numRadiators; j++) {
					if (x[i] < Math.PI) { // 0 - 180 degrees
						if (rY[j] > reflectorOffset) {
							double re = rA[j] * Math.cos(phaseShift(rX[j], rY[j], x[i], wavelength) - rP[j]);
							double im = rA[j] * Math.sin(phaseShift(rX[j], rY[j], x[i], wavelength) - rP[j]);
							Complex tmp = new Complex(re, im);
							tmp = se(wavelength, radiatorType, radiatorAng, x[i]).mul(tmp);
							c = c.add(tmp);

							re = rA[numRadiators + j]
									* Math.cos(phaseShift(rX[numRadiators + j], rY[numRadiators + j], x[i], wavelength)
											- rP[numRadiators + j]);
							im = rA[numRadiators + j]
									* Math.sin(phaseShift(rX[numRadiators + j], rY[numRadiators + j], x[i], wavelength)
											- rP[numRadiators + j]);
							tmp = new Complex(re, im);
							tmp = se(wavelength, radiatorType, -radiatorAng, x[i]).mul(tmp);
							c = c.add(tmp);
						}
					} else { // 180 - 360 degrees
						if (rY[j] < reflectorOffset) {
							double re = rA[j] * Math.cos(phaseShift(rX[j], rY[j], x[i], wavelength) - rP[j]);
							double im = rA[j] * Math.sin(phaseShift(rX[j], rY[j], x[i], wavelength) - rP[j]);
							Complex tmp = new Complex(re, im);
							tmp = se(wavelength, radiatorType, radiatorAng, x[i]).mul(tmp);
							c = c.add(tmp);

							re = rA[numRadiators + j]
									* Math.cos(phaseShift(rX[numRadiators + j], rY[numRadiators + j], x[i], wavelength)
											- rP[numRadiators + j]);
							im = rA[numRadiators + j]
									* Math.sin(phaseShift(rX[numRadiators + j], rY[numRadiators + j], x[i], wavelength)
											- rP[numRadiators + j]);
							tmp = new Complex(re, im);
							tmp = se(wavelength, radiatorType, -radiatorAng, x[i]).mul(tmp);
							c = c.add(tmp);
						}
					}
				}
			}
			s[i] = c;
		}

		return s;
	}

	/**
	 * <pre>
	 * Calculates and returns an array containing the normalized absolute values of the complex array factor.
	 * </pre>
	 * 
	 * @param sAbs
	 *            an array containing the absolute values of the complex array
	 *            factor
	 * @param sAbsMax
	 *            the maximum value of the absolute values of the complex array
	 *            factor over the entire 360 degrees
	 * @return an array containing the normalized absolute values of the complex
	 *         array factor
	 */
	public static double[] norm(double[] sAbs, double sAbsMax) {
		double[] sNorm = new double[sAbs.length];
		for (int i = 0; i < sNorm.length; i++) {
			sNorm[i] = 20 * Math.log10(sAbs[i] / sAbsMax);
		}
		return sNorm;
	}

	/**
	 * <pre>
	 * Calculates and returns an array containing nx coordinates of points on a line equally spaced with the given gap dx between the points.
	 * - nx must be greater than 0
	 * - dx must be greater than 0
	 * </pre>
	 * 
	 * @param nx
	 *            the amount of points
	 * @param dx
	 *            the gap between the points
	 * @return an array with the coordinates of the calculated points
	 */
	public static double[][] linear(int nx, double dx) {
		return grid(nx, 1, dx, 0.0);
	}

	/**
	 * <pre>
	 * Calculates and returns an array containing (nx * ny) coordinates of points on a grid equally spaced with the given gaps dx and dy between the points.
	 * - nx, ny must be greater than 0
	 * - dx, dy must be greater than 0
	 * </pre>
	 * 
	 * @param nx
	 *            the amount of points in the horizontal direction
	 * @param ny
	 *            the amount of points in the vertical direction
	 * @param dx
	 *            the gap between horizontal points
	 * @param dy
	 *            the gap between vertical points
	 * @return an array with the coordinates of the calculated points
	 */
	public static double[][] grid(int nx, int ny, double dx, double dy) {
		double[][] pos = new double[nx * ny][2];
		double xl = (nx - 1) * dx;
		double yl = (ny - 1) * dy;
		double[] x = MatlabFunctions.linspace(0.0, xl, nx);
		double[] y = MatlabFunctions.linspace(yl, 0.0, ny);
		for (int i = 0; i < ny; i++) {
			for (int j = 0; j < nx; j++) {
				pos[nx * i + j][0] = x[j] - xl / 2.0;
				pos[nx * i + j][1] = y[i] - yl / 2.0;
			}
		}
		return pos;
	}

	/**
	 * <pre>
	 * Calculates and returns an array containing n coordinates of points equally spaced on a circle with the given radius.
	 * - n must be greater than 0
	 * - r must be greater than 0
	 * </pre>
	 * 
	 * @param n
	 *            the amount of points
	 * @param r
	 *            the radius of the circle
	 * @return an array with the coordinates of the calculated points
	 */
	public static double[][] circle(int n, double r) {
		double[][] pos = new double[n][2];
		for (int i = 0; i < pos.length; i++) {
			pos[i][0] = r * Math.cos(T / n * i);
			pos[i][1] = r * Math.sin(T / n * i);
		}
		return pos;
	}

	/**
	 * <pre>
	 * Calculates and returns an array containing n coordinates of points equally spaced on a square with the given side length.
	 * - n must be equal to (k * 4), k E N
	 * - sideLength must be greater than 0
	 * </pre>
	 * 
	 * @param n
	 *            the amount of points
	 * @param sideLength
	 *            the side length
	 * @return an array with the coordinates of the calculated points
	 */
	public static double[][] square(int n, double sideLength) {
		double[][] pos = new double[n][2];
		int piecesPerSide = n / 4;
		double pieceLength = sideLength / piecesPerSide;
		double x = sideLength / 2 - pieceLength;
		double y = -sideLength / 2;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < piecesPerSide; j++) {
				if (i == 0 || i == 1) {
					pos[piecesPerSide * i + j][i % 2] = x + pieceLength;
				} else {
					pos[piecesPerSide * i + j][i % 2] = x - pieceLength;
				}
				if (i == 0 || i == 3) {
					pos[piecesPerSide * i + j][(i + 1) % 2] = y + j * pieceLength;
				} else {
					pos[piecesPerSide * i + j][(i + 1) % 2] = y - j * pieceLength;
				}
			}
			x = pos[piecesPerSide * i + piecesPerSide - 1][(i + 1) % 2];
			y = Math.pow(-1.0, (i + 1) % 2) * y;
		}
		return pos;
	}

}
