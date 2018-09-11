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
 * The <code>JEngineeringTextField</code> can read 'engineering notated'
 * strings and only allows correct formated numbers. It is also able to check
 * whether the entered value is in a specified range.
 * </pre>
 * 
 * @author Simon Zumbrunnen
 * @author Dominik Mueller (minor changes)
 */
public class JEngineeringTextField extends JTextField implements FocusListener {
	private static final long serialVersionUID = 1L;
	private JEngineeringTextField txtField = this;
	private double minValue = -Double.MAX_VALUE, maxValue = Double.MAX_VALUE, value;
	private boolean emptyAllowed = false;
	private int digits = 3;
	private boolean edited = false;
	private boolean errorDisplayed = false;

	public JEngineeringTextField(int col) {
		super(col);
		init();
	}

	private void init() {
		addKeyListener(new KeyAdapter() {

			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() != KeyEvent.VK_ENTER)
					edited = true;
				char character = e.getKeyChar();

				int offs = txtField.getCaretPosition();

				String tfText;

				if (txtField.getSelectedText() != null) {
					tfText = txtField.getText().substring(0, getSelectionStart()) + character
							+ txtField.getText().substring(txtField.getSelectionEnd());
				} else {
					tfText = txtField.getText().substring(0, offs) + character + txtField.getText().substring(offs);
				}

				try {
					if (character == 'e' || (tfText.indexOf('e') >= 0 && character == '-')
							|| (tfText.length() == 1 && character == '-')) {
						EngineeringUtil.parse(tfText.trim() + "1", digits);
					} else {
						EngineeringUtil.parse(tfText.trim(), digits);
					}
				} catch (Exception ex) {
					e.consume();
				}
			}
		});
		addFocusListener(this);
	}

	public boolean verify() {
		double v = 0.0;
		if (txtField.getText().isEmpty() && isEmptyAllowed()) {
			return true;
		} else if (txtField.getText().isEmpty() && !isEmptyAllowed()) {
			errorMsg();
			return false;
		} else {
			try {
				v = EngineeringUtil.parse(txtField.getText(), digits);
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

	public void setValue(double value) {
		this.value = value;
		edited = false;
		setText(EngineeringUtil.convert(value, digits));
	}

	public double getValue() {
		return value;
	}

	public void setRange(double minValue, double maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		setToolTipText("" + EngineeringUtil.convert(minValue, digits) + " \u2264 Input \u2264 "
				+ EngineeringUtil.convert(maxValue, digits));
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
		setToolTipText("" + EngineeringUtil.convert(minValue, digits) + " \u2264 Input \u2264 "
				+ EngineeringUtil.convert(maxValue, digits));
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
		setToolTipText("" + EngineeringUtil.convert(minValue, digits) + " \u2264 Input \u2264 "
				+ EngineeringUtil.convert(maxValue, digits));
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
				setText(EngineeringUtil.convert(value, digits));
				edited = false;
				errorDisplayed = false;
			}
		});
		timer.setRepeats(false);
		timer.start();
	}
}
