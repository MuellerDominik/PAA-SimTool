package pro2E;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import pro2E.controller.Controller;
import pro2E.model.Model;
import pro2E.ui.Utility;
import pro2E.ui.View;

/**
 * <pre>
 * The <b><code>PAASimTool</code></b> class creates a <b><code>JFrame</class></b> for the application.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class PAASimTool extends JFrame {

	private static final long serialVersionUID = 1L;

	// Frame Configurations
	public static final int RESIZABLE = 0;
	public static final int RESIZABLE_EXTENDED = 1;

	private int mode = RESIZABLE;

	/**
	 * <pre>
	 * Creates the application using the MVC-structure.
	 * </pre>
	 */
	public PAASimTool() {
		Model model = new Model();
		View view = new View(this);

		model.addObserver(view);
		add(view);
		setJMenuBar(view.menuBar);

		Controller controller = new Controller(model, view);

		view.setController(controller);

		DPIFixV3.scaleSwingFonts();
		SwingUtilities.updateComponentTreeUI(this);

		setPreferredSize(new Dimension(5 * DPIFixV3.screenSize.width / 6, 5 * DPIFixV3.screenSize.height / 6));

		switch (mode) {
		case RESIZABLE:
			pack();
			setMinimumSize(getPreferredSize());
			setSize(getPreferredSize());
			setResizable(true);
			break;
		case RESIZABLE_EXTENDED:
			pack();
			setMinimumSize(getPreferredSize());
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setResizable(true);
			break;
		}

		Dimension frameSize = getSize();
		if (frameSize.height > DPIFixV3.screenSize.height) {
			frameSize.height = DPIFixV3.screenSize.height;
		}
		if (frameSize.width > DPIFixV3.screenSize.width) {
			frameSize.width = DPIFixV3.screenSize.width;
		}
		setLocation((DPIFixV3.screenSize.width - frameSize.width) / 2,
				(DPIFixV3.screenSize.height - frameSize.height) / 2);
	}

	/**
	 * <pre>
	 * Creates an object of the <b><code>PAASimTool</code></b> class.
	 * </pre>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PAASimTool frame = new PAASimTool();
				frame.setTitle("Phased Array Antenna - SimTool | Team3");
				frame.setIconImage(Utility.loadResourceImage("icon.png"));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

}
