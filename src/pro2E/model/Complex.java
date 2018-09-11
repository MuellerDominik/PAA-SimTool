package pro2E.model;

/**
 * <pre>
 * The <b><code>Complex</code></b> class handles complex calculations and stores the real part and the imaginary part.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class Complex {
	public double re;
	public double im;

	/**
	 * <pre>
	 * Creates a Complex object with zero for the real and imaginary part.
	 * </pre>
	 */
	public Complex() {
		this(0.0, 0.0);
	}

	/**
	 * <pre>
	 * Creates a Complex object with the given real and imaginary part.
	 * </pre>
	 * 
	 * @param re
	 *            real part of the complex number
	 * @param im
	 *            imagainary part of the complex number
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	/**
	 * <pre>
	 * Creates a Complex object with the given real part and zero for the imaginary part.
	 * </pre>
	 * 
	 * @param re
	 *            real part of the complex number
	 */
	public Complex(double re) {
		this(re, 0.0);
	}

	/**
	 * <pre>
	 * Creates a Complex object with real and imaginary part from a given Complex object.
	 * </pre>
	 * 
	 * @param c
	 *            a complex number
	 */
	public Complex(Complex c) {
		this(c.re, c.im);
	}

	/**
	 * <pre>
	 * Calculates this complex number raised to the real power of n and returns the result as a new Complex object.
	 * </pre>
	 * 
	 * @param n
	 * @return the result as a new complex number
	 */
	public Complex pow(double n) {
		double abs = Math.pow(this.abs(), n);
		double ang = this.angle() * n;
		double re = abs * Math.cos(ang);
		double im = abs * Math.sin(ang);
		return new Complex(re, im);
	}

	/**
	 * <pre>
	 * Adds this complex number to a given complex number and returns the result as a new Complex object.
	 * </pre>
	 * 
	 * @param c
	 *            a complex number
	 * @return the result as a new complex number
	 */
	public Complex add(Complex c) {
		double re = this.re + c.re;
		double im = this.im + c.im;
		return new Complex(re, im);
	}

	/**
	 * <pre>
	 * Subtracts a given complex number from this complex number and returns the result as a new Complex object.
	 * </pre>
	 * 
	 * @param c
	 *            a complex number
	 * @return the result as a new complex number
	 */
	public Complex sub(Complex c) {
		double re = this.re - c.re;
		double im = this.im - c.im;
		return new Complex(re, im);
	}

	/**
	 * <pre>
	 * Multiplies a given complex number with this complex number and returns the result as a new Complex object.
	 * </pre>
	 * 
	 * @param c
	 *            a complex number
	 * @return the result as a new complex number
	 */
	public Complex mul(Complex c) {
		double re = this.re * c.re - this.im * c.im;
		double im = this.re * c.im + this.im * c.re;
		return new Complex(re, im);
	}

	/**
	 * <pre>
	 * Multiplies this complex number with a given real number and returns the result as a new Complex object.
	 * </pre>
	 * 
	 * @param x
	 *            a real number
	 * @return the result as a new complex number
	 */
	public Complex mul(double x) {
		double re = this.re * x;
		double im = this.im * x;
		return new Complex(re, im);
	}

	/**
	 * <pre>
	 * Divides this complex number by a given complex number and returns the result as a new Complex object.
	 * </pre>
	 * 
	 * @param c
	 *            a complex number
	 * @return the result as a new complex number
	 */
	public Complex div(Complex c) {
		double abs = this.abs() / c.abs();
		double ang = this.angle() - c.angle();
		double re = abs * Math.cos(ang);
		double im = abs * Math.sin(ang);
		return new Complex(re, im);
	}

	/**
	 * <pre>
	 * Divides this complex number by a given real number and returns the result as a new Complex object.
	 * </pre>
	 * 
	 * @param x
	 *            a real number
	 * @return the result as a new complex number
	 */
	public Complex div(double x) {
		double re = this.re / x;
		double im = this.im / x;
		return new Complex(re, im);
	}

	/**
	 * <pre>
	 * Calculates the absolute value of this complex number.
	 * </pre>
	 * 
	 * @return the absolute value as a real number
	 */
	public double abs() {
		return Math.hypot(re, im);
	}

	/**
	 * <pre>
	 * Calculates the angle of this complex number.
	 * </pre>
	 * 
	 * @return the angle as a real number
	 */
	public double angle() {
		return Math.atan2(im, re);
	}

	/**
	 * <pre>
	 * Calculates the angles of a given complex number array and returns an array containing those.
	 * </pre>
	 * 
	 * @param c
	 *            an array of complex numbers
	 * @return an array containing the calculated angles
	 */
	public static double[] angle(Complex[] c) {
		double[] res = new double[c.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = c[i].angle();
		}
		return res;
	}

	/**
	 * <pre>
	 * Calculates the absolute values of a given complex number array and returns an array containing those.
	 * </pre>
	 * 
	 * @param c
	 *            an array of complex numbers
	 * @return an array containing the calculated absolute values
	 */
	public static double[] abs(Complex[] c) {
		double[] res = new double[c.length];
		for (int i = 0; i < c.length; i++) {
			res[i] = c[i].abs();
		}
		return res;
	}

	/**
	 * <pre>
	 * Returns the real part of the given complex number.
	 * </pre>
	 * 
	 * @param c
	 *            a complex number
	 * @return the real part of the complex number
	 */
	public static double real(Complex c) {
		return c.re;
	}

	/**
	 * <pre>
	 * Returns the imaginary part of the given complex number.
	 * </pre>
	 * 
	 * @param c
	 *            a complex number
	 * @return the imaginary part of the complex number
	 */
	public static double imag(Complex c) {
		return c.im;
	}

	/**
	 * <pre>
	 * Returns the real part of this complex number.
	 * </pre>
	 * 
	 * @return the real part of this complex number
	 */
	public double real() {
		return re;
	}

	/**
	 * <pre>
	 * Returns the imaginary part of this complex number.
	 * </pre>
	 * 
	 * @return the imaginary part of this complex number
	 */
	public double imag() {
		return im;
	}

	/**
	 * <pre>
	 * Returns the complex conjugate of this complex number as a new Complex object.
	 * </pre>
	 * 
	 * @return the complex conjugate of this complex number
	 */
	public Complex conj() {
		return new Complex(re, -im);
	}

	/**
	 * <pre>
	 * Returns the real and the imaginary part as a String.
	 * </pre>
	 */
	public String toString() {
		if (this.im == 0) {
			return ("" + this.re);
		} else if (this.im > 0) {
			return (this.re + " + " + this.im + "i");
		}
		return (this.re + " - " + -this.im + "i");
	}

}
