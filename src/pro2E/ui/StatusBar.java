package pro2E.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * <pre>
 * The <b><code>StatusBar</code></b> class builds the status bar of the ui.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class StatusBar extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel lbStatus = new JLabel();

	private Color defaultForeground;

	private String spacer = "  ";

	private Timer timer;
	private int delay = 10000; // 10s

	/**
	 * <pre>
	 * Build the status bar of the ui.
	 * </pre>
	 */
	public StatusBar() {
		setBorder(CustomBorder.createHorizontalLine());
		setLayout(new BorderLayout());

		this.timer = new Timer(this.delay, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		this.timer.setRepeats(false);

		this.defaultForeground = this.lbStatus.getForeground();
		this.reset();

		add(this.lbStatus);
	}

	/**
	 * <pre>
	 * Displays an error message on the status bar.
	 * </pre>
	 * 
	 * @param errorMsg
	 */
	public void displayError(String errorMsg) {
		if (this.timer.isRunning()) {
			this.timer.stop();
		}
		this.lbStatus.setForeground(Color.RED);
		this.lbStatus.setText(this.spacer + errorMsg);
		this.timer.start();
	}

	/**
	 * <pre>
	 * Displays a message on the status bar.
	 * </pre>
	 * 
	 * @param message
	 */
	public void displayMsg(String message) {
		if (this.timer.isRunning()) {
			this.timer.stop();
		}
		this.lbStatus.setForeground(this.defaultForeground);
		this.lbStatus.setText(this.spacer + message);
		this.timer.start();
	}

	/**
	 * <pre>
	 * Clears the displayed message/error on the status bar.
	 * </pre>
	 */
	public void reset() {
		if (this.timer.isRunning()) {
			this.timer.stop();
		}
		this.lbStatus.setForeground(this.defaultForeground);
		this.lbStatus.setText(this.spacer + "Ready");
	}

}
