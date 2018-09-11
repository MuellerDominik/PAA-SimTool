package pro2E.ui.SpecializedTextFields;

import java.util.Locale;

/**
 * <pre>
 * The <code>EngineeringUtil</code> class contains methods to handle engineering
 * notation.
 * </pre>
 * 
 * @author Stephan Fahrni
 */
public class EngineeringUtil {
	private final static int PREFIX_OFFSET = 5;
	private final static String[] PREFIX_ARRAY = { "f", "p", "n", "µ", "m", "", "k", "M", "G", "T" };

	private final static char[] PREFIX_TEST_ARRAY = { 'f', 'p', 'n', 'u', 'µ', 'm', 'k', 'K', 'M', 'G', 'T' };

	private final static int[] PREFIX_EXP_ARRAY = { -15, -12, -9, -6, -6, -3, 3, 3, 6, 9, 12 };

	/**
	 * <pre>
	 * Converts a <code>double</code> into an 'engineering notated'
	 * <code>String</code>.
	 * </pre>
	 * 
	 * @param val
	 *            The value to be converted
	 * @param dp
	 *            The number of decimal places
	 */
	public static String convert(double val, int dp) {
		// If the value is zero, then simply return 0 with the correct number of
		// dp
		if (val == 0)
			return String.format("%." + dp + "f", 0.0);

		// If the value is negative, make it positive so the log10 works
		double posVal = (val < 0) ? -val : val;
		double log10 = Math.log10(posVal);

		// Determine how many orders of 3 magnitudes the value is
		int count = (int) Math.floor(log10 / 3);

		// Calculate the index of the prefix symbol
		int index = count + PREFIX_OFFSET;

		// Scale the value into the range 1<=val<1000
		val /= Math.pow(10, count * 3);

		if (index >= 0 && index < PREFIX_ARRAY.length) {
			// If a prefix exists use it to create the correct string
			return String.format(Locale.US, "%." + dp + "f%s", val, PREFIX_ARRAY[index]);
		} else {
			// If no prefix exists just make a string of the form 000e000
			return String.format(Locale.US, "%." + dp + "fe%d", val, count * 3);
		}
	}

	/**
	 * <pre>
	 * Parses an 'engineering notated' <code>String</code> for a
	 * <code>double</code> value.
	 * </pre>
	 * 
	 * @param str
	 *            The String to be parsed
	 */
	public static double parse(String str) {
		if (str.contains("e") || str.contains("E")) {
			return Double.parseDouble(str);
		}

		return parse(str.toCharArray());
	}

	/**
	 * <pre>
	 * Parses an 'engineering notated' <code>String</code> for a
	 * <code>double</code> value. Strings with more decimal places
	 * than dp are not allowed and throw an exception.
	 * </pre>
	 * 
	 * @param str
	 *            The String to be parsed
	 * @param dp
	 *            The number of decimal places
	 */
	public static double parse(String str, int dp) throws NumberFormatException {
		int dpIndex = str.indexOf('.');
		char[] chars = str.toCharArray();
		int numberOfDp = 0;

		if (dpIndex >= 0 && dpIndex < chars.length) {
			for (int i = dpIndex + 1; i < chars.length; i++) {
				if (Character.isDigit(chars[i])) {
					numberOfDp++;
				} else {
					i = chars.length - 1;
				}
			}
		}

		if (numberOfDp > dp) {
			throw new NumberFormatException("To many decimal places");
		}

		if (str.contains("e") || str.contains("E")) {
			return Double.parseDouble(str);
		}

		return parse(str.toCharArray());
	}

	/**
	 * <pre>
	 * Parses an 'engineering notated' <code>char[]</code> for a
	 * <code>double</code> value.
	 * </pre>
	 * 
	 * @param chars
	 *            The char[] to be parsed
	 */
	public static double parse(char[] chars) throws NumberFormatException {
		int exponent = 0;
		double value = 0;

		boolean gotChar = false; // Set to true once any non-whitespace has been
									// found
		boolean gotMinus = false; // Set to true once a minus character has been
									// found
		boolean gotDP = false; // Set to true once a decimal place character has
								// been found
		boolean gotPrefix = false; // Set to true once a prefix character has
									// been found
		boolean gotDigit = false; // Set to true once a digit character has been
									// found

		// Search for start of string
		int start = 0;
		while (start < chars.length) {
			if (chars[start] != ' ' && chars[start] != '\t')
				break;
			start++;
		}

		if (start == chars.length)
			throw new NumberFormatException("Empty string");

		// Search for end of string
		int end = chars.length - 1;
		while (end >= 0) {
			if (chars[end] != ' ' && chars[end] != '\t')
				break;
			end--;
		}

		// Iterate through characters
		CharLoop: for (int c = start; c <= end; c++) {
			// Check for a minus symbol
			if (chars[c] == '-') {
				if (gotChar)
					throw new NumberFormatException("Can only have minus symbol at the start");
				if (gotMinus)
					throw new NumberFormatException("Too many minus symbols");
				gotMinus = true;
				continue CharLoop;
			}

			gotChar = true;

			// Check for a numerical digit
			if (chars[c] >= '0' && chars[c] <= '9') {
				if (gotPrefix || gotDP)
					exponent--;
				if (gotPrefix && gotDP)
					throw new NumberFormatException(
							"Cannot have digits after prefix when number includes decimal point");
				value *= 10;
				value += chars[c] - '0';
				gotDigit = true;
				continue CharLoop;
			}

			// Check for a decimal place
			if (chars[c] == '.') {
				if (gotDP)
					throw new NumberFormatException("Too many decimal points");
				if (gotPrefix)
					throw new NumberFormatException("Cannot have decimal point after prefix");
				gotDP = true;
				continue CharLoop;
			}

			// Check for a match with a prefix character
			for (int p = 0; p < PREFIX_TEST_ARRAY.length; p++) {
				if (PREFIX_TEST_ARRAY[p] == chars[c]) {
					if (gotPrefix)
						throw new NumberFormatException("Too many prefixes");
					exponent += PREFIX_EXP_ARRAY[p];
					gotPrefix = true;
					continue CharLoop;
				}
			}

			// All other characters are invalid
			throw new NumberFormatException("Invalid character '" + chars[c] + "'");
		}

		// Check if any digits were found
		if (!gotDigit)
			throw new NumberFormatException("No digits");

		// Apply negation if required
		if (gotMinus)
			value *= -1;

		return value * Math.pow(10, exponent);
	}

	public static char[] getPrefixTestArray() {
		return PREFIX_TEST_ARRAY;
	}
}
