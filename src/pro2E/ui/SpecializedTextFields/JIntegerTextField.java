package pro2E.ui.SpecializedTextFields;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

/**
 * <pre>
 * The <code>JIntegerTextField</code> only allows correct formated integers.
 * It is also able to check whether the entered value is in a specified range.
 * </pre>
 * 
 * @author Simon Zumbrunnen
 * @author Dominik Mueller (minor changes)
 */
public class JIntegerTextField extends JTextField implements FocusListener {
	private static final long serialVersionUID = 1L;
	private JIntegerTextField txtField = this;
	private int minValue = -Integer.MAX_VALUE, maxValue = Integer.MAX_VALUE, value;
	private boolean emptyAllowed = false;
	private boolean edited = false;
	private boolean errorDisplayed = false;

	public JIntegerTextField(int col) {
		super(col);
		init();
	}

	private void init() {
		addKeyListener(new KeyAdapter() {

			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() != KeyEvent.VK_ENTER)
					edited = true;
				char character = e.getKeyChar();
				if (character == 'd' || character == 'f') {
					e.consume();
					return;
				}
				int offs = txtField.getCaretPosition();

				String tfText;

				if (txtField.getSelectedText() != null) {
					tfText = txtField.getText().substring(0, getSelectionStart()) + character
							+ txtField.getText().substring(txtField.getSelectionEnd());
				} else {
					tfText = txtField.getText().substring(0, offs) + character + txtField.getText().substring(offs);
				}

				try {
					if (character == '-' || character == '+' || character == 'e') {
						Integer.parseInt(tfText.trim() + "1");
					} else {
						Integer.parseInt(tfText.trim());
					}
				} catch (Exception ex) {
					e.consume();
				}
			}
		});
		addFocusListener(this);
	}

	public boolean verify() {
		int v = 0;
		if (txtField.getText().isEmpty() && isEmptyAllowed()) {
			return true;
		} else if (txtField.getText().isEmpty() && !isEmptyAllowed()) {
			errorMsg();
			return false;
		} else {
			try {
				v = Integer.parseInt(txtField.getText());
			} catch (NumberFormatException e) {
				errorMsg();
				return false;
			}
			if (v > maxValue || v < minValue) {
				errorMsg();
				return false;
			} else {
				if (edited) {
					setValue(v);
					edited = false;
				}

				return true;
			}
		}
	}

	public void setValue(int value) {
		this.value = value;
		edited = false;
		setText(String.valueOf(value));
	}

	public int getValue() {
		return value;
	}

	public void setRange(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		setToolTipText("" + String.valueOf(minValue) + " \u2264 Input \u2264 " + String.valueOf(maxValue));
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
		setToolTipText("" + String.valueOf(minValue) + " \u2264 Input \u2264 " + String.valueOf(maxValue));
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		setToolTipText("" + String.valueOf(minValue) + " \u2264 Input \u2264 " + String.valueOf(maxValue));
	}

	public boolean isEmptyAllowed() {
		return emptyAllowed;
	}

	public void setEmptyAllowed(boolean emptyAllowed) {
		this.emptyAllowed = emptyAllowed;
	}

	public void focusGained(FocusEvent e) {
		selectAll();
	}

	public void focusLost(FocusEvent e) {
		fireActionPerformed();
	}

	protected void fireActionPerformed() {
		if (verify())
			super.fireActionPerformed();
	}

	private void errorMsg() {
		if (errorDisplayed)
			return;
		errorDisplayed = true;
		final Color color = getForeground();
		setForeground(Color.red);
		requestFocus();
		javax.swing.Timer timer = new javax.swing.Timer(1500, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setForeground(color);
				setText(String.valueOf(value));
				edited = false;
				errorDisplayed = false;
			}
		});
		timer.setRepeats(false);
		timer.start();
	}
}
