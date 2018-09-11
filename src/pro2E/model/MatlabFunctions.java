package pro2E.model;

/**
 * <pre>
 * The <b><code>MatlabFunctions</code></b> class contains a set of static methods which are equivalent to the corresponding Matlab functions.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class MatlabFunctions {

	/**
	 * <pre>
	 * Calculates and returns the inverse hyperbolic sine of the given x.
	 * </pre>
	 * 
	 * @param x
	 * @return the inverse hyperbolic sine of x
	 */
	public static double asinh(double x) {
		return Math.log(x + Math.hypot(x, 1.0));
	}

	/**
	 * <pre>
	 * Calculates and returns the inverse hyperbolic cosine of the given x.
	 * - x must be greater or equal to 1
	 * - x < 1 will return NaN
	 * </pre>
	 * 
	 * @param x
	 * @return the inverse hyperbolic cosine of x
	 */
	public static double acosh(double x) {
		return Math.log(x + Math.sqrt(Math.pow(x, 2.0) - 1.0));
	}

	/**
	 * <pre>
	 * Not available in Matlab.
	 * Used to calculate the Dolph-Chebyshev window function parameters.
	 * </pre>
	 * 
	 * @param n
	 * @param x
	 * @return
	 */
	public static double chebyPoly(int n, double x) {
		if (Math.abs(x) <= 1.0) {
			return Math.cos(n * Math.acos(x));
		}
		return Math.cosh(n * acosh(x));
	}

	/**
	 * <pre>
	 * Time domain implementation of the Matlab chebwin() function.
	 * Returns the Chebyshev window for the given window length N and the given attenuation.
	 * </pre>
	 * 
	 * @param N
	 *            window length
	 * @param attn
	 *            attenuation
	 * @return
	 */
	public static double[] chebwin(int N, double attn) {
		double[] win = new double[N];
		double recipR = Math.pow(10.0, attn / 20.0);
		double x0 = Math.cosh(acosh(recipR) / (N - 1));
		double M = (N - 1) / 2.0;
		for (int n = 0; n < (N / 2 + 1); n++) {
			double sum = 0;
			for (int i = 1; i <= M; i++) {
				sum += chebyPoly(N - 1, x0 * Math.cos(i * Math.PI / N)) * Math.cos(2.0 * Math.PI * (n - M) * i / N);
			}
			win[n] = recipR + 2 * sum;
			win[N - n - 1] = win[n];
		}
		double max = max(win);
		for (int n = 0; n < N; n++) {
			win[n] /= max;
		}
		return win;
	}

	/**
	 * <pre>
	 * Returns the Hann window for the given window length N.
	 * </pre>
	 * 
	 * @param N
	 *            window length
	 * @return
	 */
	public static double[] hann(int N) {
		double[] win = new double[N];
		if (N == 1) {
			win[0] = 1.0;
			return win;
		}
		for (int i = 0; i < N; i++) {
			win[i] = Math.pow(Math.sin(Math.PI * i / (N - 1)), 2.0);
		}
		return win;
	}

	/**
	 * <pre>
	 * Returns the Hamming window for the given window length N.
	 * </pre>
	 * 
	 * @param N
	 *            window length
	 * @return
	 */
	public static double[] hamming(int N) {
		double[] win = new double[N];
		if (N == 1) {
			win[0] = 1.0;
			return win;
		}
		double alpha = 0.54;
		double beta = 1.0 - alpha;
		for (int i = 0; i < N; i++) {
			win[i] = alpha - beta * Math.cos(2.0 * Math.PI * i / (N - 1));
		}
		return win;
	}

	/**
	 * <pre>
	 * Returnd the Blackman window for the given window length N.
	 * Symmetric version (default in Matlab).
	 * 
	 * Forces end points [w(0) and w(N - 1)] to zero to avoid close-to-zero negative values caused by roundoff errors.
	 * Truncated coefficients used, as in Matlab.
	 * 
	 * "Exact Blackman" would be:
	 * a0 = 7938/18608
	 * a1 = 9240/18608
	 * a2 = 1430/18608
	 * </pre>
	 * 
	 * @param N
	 *            window length
	 * @return
	 */
	public static double[] blackman(int N) {
		double[] win = new double[N];
		if (N == 1) {
			win[0] = 1.0;
			return win;
		}
		double a = 0.16;
		double a0 = (1 - a) / 2;
		double a1 = 0.5;
		double a2 = a / 2;
		int M = (N + 1) / 2;
		for (int i = 0; i < M; i++) {
			if (i != 0) {
				win[i] = a0 - a1 * Math.cos(2 * Math.PI * i / (N - 1)) + a2 * Math.cos(4 * Math.PI * i / (N - 1));
			} else {
				win[i] = 0.0;
			}
			win[N - i - 1] = win[i];
		}
		return win;
	}

	/**
	 * <pre>
	 * Not available in Matlab.
	 * Returns the Welch window for the given window length N.
	 * </pre>
	 * 
	 * @param N
	 *            window length
	 * @return
	 */
	public static double[] welch(int N) {
		double[] win = new double[N];
		if (N == 1) {
			win[0] = 1.0;
			return win;
		}
		for (int i = 0; i < N; i++) {
			win[i] = 1.0 - Math.pow((i - (N - 1) / 2.0) / ((N - 1) / 2.0), 2.0);
		}
		return win;
	}

	/**
	 * <pre>
	 * Not available in Matlab.
	 * Returns the Sine window for the given window length N and the given offset x.
	 * - x >= 0 && x <= 1
	 * </pre>
	 * 
	 * @param N
	 *            window length
	 * @param x
	 *            offset
	 * @return
	 */
	public static double[] sinewin(int N, double x) {
		double[] win = new double[N];
		if (N == 1) {
			win[0] = 1.0;
			return win;
		}
		for (int i = 0; i < N; i++) {
			win[i] = x + (1 - x) * Math.sin(Math.PI * i / (N - 1));
		}
		return win;
	}

	/**
	 * <pre>
	 * Returns the Triangular window for the given window length N.
	 * </pre>
	 * 
	 * @param N
	 *            window length
	 * @return
	 */
	public static double[] triang(int N) {
		double[] win = new double[N];
		for (int i = 0; i < N; i++) {
			if (N % 2 == 0) {
				win[i] = 1.0 - Math.abs((i - (N - 1) / 2.0) / (N / 2.0));
			} else {
				win[i] = 1.0 - Math.abs((i - (N - 1) / 2.0) / ((N + 1) / 2.0));
			}
		}
		return win;
	}

	/**
	 * <pre>
	 * Returns the Parzen window for the given window length N.
	 * </pre>
	 * 
	 * @param N
	 *            window length
	 * @return
	 */
	public static double[] parzenwin(int N) {
		double[] win = new double[N];
		double n = -(N - 1) / 2.0;
		for (int i = 0; i < N; i++) {
			if (Math.abs(n) >= 0 && Math.abs(n) <= ((N - 1) / 4.0)) {
				win[i] = 1.0 - 6 * Math.pow(Math.abs(n) / (N / 2.0), 2.0) + 6 * Math.pow(Math.abs(n) / (N / 2.0), 3.0);
			} else {
				win[i] = 2.0 * Math.pow(1 - Math.abs(n) / (N / 2.0), 3.0);
			}
			n += 1.0;
		}
		return win;
	}

	/**
	 * <pre>
	 * Returns the Rectangular window for the given window length N.
	 * </pre>
	 * 
	 * @param N
	 *            window length
	 * @return
	 */
	public static double[] rectwin(int N) {
		double[] win = new double[N];
		for (int i = 0; i < N; i++) {
			win[i] = 1.0;
		}
		return win;
	}

	/**
	 * <pre>
	 * Rounds the given x to the nearest integer toward zero and returns it.
	 * </pre>
	 * 
	 * @param x
	 * @return
	 */
	public static double fix(double x) {
		if (x < 0) {
			return Math.ceil(x);
		}
		return Math.floor(x);
	}

	/**
	 * <pre>
	 * Returns the minimum value of the given array x.
	 * </pre>
	 * 
	 * @param x
	 *            an array containing double numbers
	 * @return
	 */
	public static double min(double[] x) {
		double min = x[0];
		for (int i = 1; i < x.length; i++) {
			if (x[i] < min) {
				min = x[i];
			}
		}
		return min;
	}

	/**
	 * <pre>
	 * Returns the maximum value of the given array x.
	 * </pre>
	 * 
	 * @param x
	 *            an array containing double numbers
	 * @return
	 */
	public static double max(double[] x) {
		double max = x[0];
		for (int i = 1; i < x.length; i++) {
			if (x[i] > max) {
				max = x[i];
			}
		}
		return max;
	}

	/**
	 * <pre>
	 * Returns an array with n evenly spaced values between x1 and x2.
	 * 
	 * Notes:
	 * - n = 1: Returns x2 (same as Matlab)
	 * </pre>
	 * 
	 * @param x1
	 *            start value
	 * @param x2
	 *            end value
	 * @param n
	 *            number of values
	 * @return
	 */
	public static double[] linspace(double x1, double x2, int n) {
		double[] x = new double[n];
		for (int i = 0; i < n; i++) {
			if (n != 1) {
				x[i] = (x2 - x1) / (n - 1) * i + x1;
			} else {
				x[0] = x2;
			}
		}
		return x;
	}

}
