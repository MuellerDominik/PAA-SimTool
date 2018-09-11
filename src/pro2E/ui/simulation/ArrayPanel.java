package pro2E.ui.simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import pro2E.DPIFixV3;
import pro2E.controller.Controller;
import pro2E.model.AntennaArray;
import pro2E.model.MatlabFunctions;
import pro2E.model.Model;
import pro2E.ui.CustomBorder;

/**
 * <pre>
 * The <b><code>ArrayPanel</code></b> class builds the array panel, which draws the position of the radiators from the antenna array.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class ArrayPanel extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;

	private Color background = Color.WHITE;

	private Controller controller;

	private double[][] radiatorParameters;

	private double panelWidth;
	private double panelHeight;
	private double scale;
	private double originX;
	private double originY;

	private double panelDimensionX;
	private double panelDimensionY;
	private double originOffsetX;
	private double originOffsetY;

	private double wavelength;
	private int arrayType;
	private int numberOfRadiatorsX;
	private int numberOfRadiatorsY;
	private int numberOfRadiators;
	private int reflectorState;
	private double reflectorOffset;

	/**
	 * <pre>
	 * Builds the array panel.
	 * </pre>
	 */
	public ArrayPanel() {
		setBorder(CustomBorder.createBorder("Array"));
		setPreferredSize(new Dimension(2 * DPIFixV3.screenSize.width / 5, 5 * DPIFixV3.screenSize.height / 12));
		setBackground(background);

		addMouseListener(this);
	}

	/**
	 * <pre>
	 * When clicked, calls the corresponding method to add a radiator to the array at the clicked position.
	 * </pre>
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (e.getX() > 0.05 * this.panelWidth && e.getX() < 0.95 * this.panelWidth) {
				if (e.getY() > 0.08 * this.panelHeight && e.getY() < 0.95 * this.panelHeight) {
					double x = (e.getX() - ((int) originX)) / scale;
					double y = (((int) originY) - e.getY()) / scale;
					controller.addRadiator(x, y);
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// do nothing
	}

	/**
	 * <pre>
	 * Calculates the important values after an update and stores them as attributes.
	 * Whenever the application is resized later on the attributes are still available
	 * </pre>
	 */
	public void drawArray() {
		double[] xValues = new double[this.radiatorParameters.length + 1];
		double[] yValues;

		if (this.reflectorState == AntennaArray.ON) {
			yValues = new double[this.radiatorParameters.length + 2];
		} else {
			yValues = new double[this.radiatorParameters.length + 1];
		}

		for (int i = 0; i < yValues.length; i++) {
			if (i < this.radiatorParameters.length) {
				xValues[i] = this.radiatorParameters[i][0];
				yValues[i] = this.radiatorParameters[i][1];
			} else if (i == this.radiatorParameters.length) {
				xValues[i] = 0;
				yValues[i] = 0;
			} else if (i == this.radiatorParameters.length + 1) {
				yValues[i] = this.reflectorOffset;
			}
		}

		double minX = MatlabFunctions.min(xValues);
		double maxX = MatlabFunctions.max(xValues);
		double minY = MatlabFunctions.min(yValues);
		double maxY = MatlabFunctions.max(yValues);

		double deltaX = maxX - minX;
		double deltaY = maxY - minY;

		double recipUsedArea = 1.35;

		if (deltaX == 0 && deltaY == 0) {
			this.panelDimensionX = 4 * this.wavelength * recipUsedArea;
			this.panelDimensionY = 0.0;
			this.originOffsetX = 0.0;
			this.originOffsetY = 0.0;
		} else if (deltaX == 0.0) {
			this.panelDimensionX = 0.0;
			this.panelDimensionY = deltaY * recipUsedArea;
			this.originOffsetX = 0.0;
			this.originOffsetY = deltaY / 2 - Math.abs(minY);
		} else if (deltaY == 0.0) {
			this.panelDimensionX = deltaX * recipUsedArea;
			this.panelDimensionY = 0.0;
			this.originOffsetX = -(deltaX / 2 - Math.abs(minX));
			this.originOffsetY = 0.0;
		} else {
			this.panelDimensionX = deltaX * recipUsedArea;
			this.panelDimensionY = deltaY * recipUsedArea;
			this.originOffsetX = -(deltaX / 2 - Math.abs(minX));
			this.originOffsetY = deltaY / 2 - Math.abs(minY);
		}

		if (this.radiatorParameters.length < 2) {
			this.panelDimensionX = 4 * this.wavelength * recipUsedArea;
			this.panelDimensionY = 0.0;
			this.originOffsetX = 0.0;
			this.originOffsetY = 0.0;
		}

		repaint();
	}

	/**
	 * <pre>
	 * Draws the whole antena array.
	 * Redraws everything after the resizing of the application using the attributes.
	 * </pre>
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Dimension panelSize = super.getSize();
		this.panelWidth = panelSize.getWidth();
		this.panelHeight = panelSize.getHeight();

		if (this.panelDimensionX == 0) {
			this.scale = panelHeight / this.panelDimensionY;
		} else if (this.panelDimensionY == 0) {
			this.scale = panelWidth / this.panelDimensionX;
		} else {
			double xCompare = panelWidth / this.panelDimensionX;
			double yCompare = panelHeight / this.panelDimensionY;
			if (xCompare < yCompare) {
				this.scale = panelWidth / this.panelDimensionX;
			} else {
				this.scale = panelHeight / this.panelDimensionY;
			}
		}

		this.originX = panelWidth / 2.0 + this.originOffsetX * this.scale;
		this.originY = panelHeight / 2.0 + this.originOffsetY * this.scale;

		double objectDia = panelHeight * 0.02;

		Font font = getFont().deriveFont((float) (getFont().getSize2D() * DPIFixV3.fontFactor));
		g.setFont(font);
		double fontHeight = getFont().getSize2D();

		// Half-Wave Grid
		double deltaGrid = this.wavelength / 2.0 * this.scale;
		int numberOfGridlinesX = (int) (panelWidth / deltaGrid);
		int numberOfGridlinesY = (int) (panelHeight / deltaGrid);
		if (numberOfGridlinesX < 200 && numberOfGridlinesY < 200) {
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine((int) this.originX, (int) (0.08 * panelHeight), (int) this.originX, (int) (0.95 * panelHeight));
			g.drawLine((int) (0.05 * panelWidth), (int) this.originY, (int) (0.95 * panelWidth), (int) this.originY);
			double deltaGridN;
			for (int i = 0; i < numberOfGridlinesX; i++) {
				deltaGridN = (i + 1) * deltaGrid;
				if (this.originX + deltaGridN < 0.95 * panelWidth) {
					g.drawLine((int) (this.originX + deltaGridN), (int) (0.08 * panelHeight),
							(int) (this.originX + deltaGridN), (int) (0.95 * panelHeight));
				}
				if (this.originX - deltaGridN > 0.05 * panelWidth) {
					g.drawLine((int) (this.originX - deltaGridN), (int) (0.08 * panelHeight),
							(int) (this.originX - deltaGridN), (int) (0.95 * panelHeight));
				}
			}
			for (int i = 0; i < numberOfGridlinesY; i++) {
				deltaGridN = (i + 1) * deltaGrid;
				if (this.originY + deltaGridN < 0.95 * panelHeight) {
					g.drawLine((int) (0.05 * panelWidth), (int) (this.originY + deltaGridN), (int) (0.95 * panelWidth),
							(int) (this.originY + deltaGridN));
				}
				if (this.originY - deltaGridN > 0.08 * panelHeight) {
					g.drawLine((int) (0.05 * panelWidth), (int) (this.originY - deltaGridN), (int) (0.95 * panelWidth),
							(int) (this.originY - deltaGridN));
				}
			}
			g.setColor(Color.BLACK);
		}

		// Origin
		g.setColor(new Color(0, 114, 189));
		g.drawLine((int) (this.originX - objectDia / 2.0), (int) (this.originY - objectDia / 2.0),
				(int) (this.originX + objectDia / 2.0), (int) (this.originY + objectDia / 2.0));
		g.drawLine((int) (this.originX - objectDia / 2.0), (int) (this.originY + objectDia / 2.0),
				(int) (this.originX + objectDia / 2.0), (int) (this.originY - objectDia / 2.0));
		switch (this.arrayType) {
		case AntennaArray.CUSTOM:
		case AntennaArray.LINEAR:
			g.drawString("(0|0)", (int) (this.originX - 1.2 * fontHeight),
					(int) (this.originY - 0.1 * panelHeight + 0.5 * fontHeight));
			break;
		case AntennaArray.CIRCLE:
		case AntennaArray.SQUARE:
			g.drawString("(0|0)", (int) (this.originX + 0.05 * panelHeight), (int) (this.originY + 0.1 * panelHeight));
			break;
		case AntennaArray.GRID:
			break;
		}
		g.setColor(Color.BLACK);

		// Radiators
		for (int i = 0; i < this.radiatorParameters.length; i++) {
			if (this.radiatorParameters[i][5] == 1.0) {
				g.setColor(Color.LIGHT_GRAY);
			} else {
				g.setColor(Color.BLACK);
			}
			int x = (int) (this.originX + this.radiatorParameters[i][0] * this.scale - objectDia / 2.0);
			int y = (int) (this.originY - this.radiatorParameters[i][1] * this.scale - objectDia / 2.0);
			g.fillOval(x, y, (int) objectDia, (int) objectDia);

			switch (this.arrayType) {
			case AntennaArray.LINEAR:
				if (this.numberOfRadiatorsX > 12) {
					break;
				}
				g.drawString("(" + (i + 1) + ")", (int) (x + 0.02 * panelHeight), (int) (y + 0.09 * panelHeight));
				break;
			case AntennaArray.GRID:
				if (this.numberOfRadiatorsX > 12 || this.numberOfRadiatorsY > 5) {
					break;
				}
				g.drawString("(" + (i + 1) + ")", (int) (x + 0.02 * panelHeight), (int) (y + 0.09 * panelHeight));
				break;
			case AntennaArray.CIRCLE:
			case AntennaArray.SQUARE:
				if (this.numberOfRadiators > 12) {
					break;
				}
				g.drawString("(" + (i + 1) + ")", (int) (x + 0.02 * panelHeight), (int) (y + 0.09 * panelHeight));
				break;
			case AntennaArray.CUSTOM:
				if (this.radiatorParameters.length > 12) {
					break;
				}
				g.drawString("(" + (i + 1) + ")", (int) (x + 0.02 * panelHeight), (int) (y + 0.09 * panelHeight));
				break;
			}
		}

		g.setColor(Color.GRAY);

		// Reflector
		if (this.reflectorState == 1) {
			g.fillRect((int) (0.05 * panelWidth),
					(int) (this.originY - this.reflectorOffset * this.scale - objectDia / 2.0),
					(int) (0.9 * panelWidth), (int) objectDia);
		}

		g.setColor(Color.BLACK);
	}

	/**
	 * <pre>
	 * Sets the controller.
	 * </pre>
	 * 
	 * @param controller
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

	/**
	 * <pre>
	 * Fetches all the radiator parameters and calls the method to draw the array.
	 * </pre>
	 * 
	 * @param model
	 * @param simIndex
	 */
	public void update(Model model, int simIndex) {
		double[][] radiatorParameters = model.getRadiators(simIndex);
		int radiatorParametersLength;
		if (radiatorParameters.length == 0) {
			radiatorParametersLength = 0;
		} else {
			radiatorParametersLength = radiatorParameters[0].length;
		}
		this.radiatorParameters = new double[radiatorParameters.length][radiatorParametersLength];
		this.radiatorParameters = radiatorParameters;

		double[] config = this.controller.getConfig(simIndex);
		this.wavelength = config[0];
		this.arrayType = (int) config[3];
		this.numberOfRadiatorsX = (int) config[4];
		this.numberOfRadiatorsY = (int) config[6];
		this.numberOfRadiators = (int) config[8];
		this.reflectorState = (int) config[16];
		this.reflectorOffset = config[17];

		this.drawArray();
	}

}
