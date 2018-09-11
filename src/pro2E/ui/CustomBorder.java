package pro2E.ui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * <pre>
 * The <b><code>CustomBorder</code></b> class contains static methods to create custom borders.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class CustomBorder {

	/**
	 * <pre>
	 * Creates a custom border around a panel.
	 * </pre>
	 * 
	 * @param title
	 * @return
	 */
	public static Border createBorder(String title) {
		Border loweredEtchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		Border titledBorder = BorderFactory.createTitledBorder(loweredEtchedBorder, " " + title + " ");
		return titledBorder;
	}

	/**
	 * <pre>
	 * Creates a horizontal line on top of the panel.
	 * </pre>
	 * 
	 * @return
	 */
	public static Border createHorizontalLine() {
		Border horizontalLine = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY);
		return horizontalLine;
	}

}
