package pro2E.ui.simulation.output;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTick;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PolarAxisLocation;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import pro2E.controller.Controller;
import pro2E.model.MatlabFunctions;
import pro2E.model.Model;
import pro2E.ui.CustomBorder;
import pro2E.ui.SpecializedTextFields.JEngineeringTextField;
import pro2E.ui.SpecializedTextFields.JIntegerTextField;

/**
 * <pre>
 * The <b><code>DiagramPanel</code></b> class builds the diagram panel.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class DiagramPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Controller controller;

	private Color background = Color.WHITE;

	private int col = 10;

	private String title = "Normalized Power Pattern";

	private ChartTheme standardTheme = new StandardChartTheme("Standard");

	private Font standardFont = new Font("SansSerif", Font.PLAIN, 14);

	private Color lg = new Color(210, 210, 210);
	private Color lb = new Color(0, 114, 189);

	private BasicStroke stroke = new BasicStroke(1.0f);
	private BasicStroke seriesStroke = new BasicStroke(2.0f);

	private JFreeChart chart;
	public ChartPanel chartPanel = new ChartPanel(chart);

	private JPanel paramPanel = new JPanel();

	// Diagram Type
	public JRadioButton rbDiagramTypeCartesian = new JRadioButton("Cartesian");
	public JRadioButton rbDiagramTypePolar = new JRadioButton("Polar");

	private ButtonGroup btGroup = new ButtonGroup();

	// Angle
	private JLabel lbFrom = new JLabel("Angle From:");
	public JEngineeringTextField tfFrom = new JEngineeringTextField(col);
	private JLabel lbFromUnit = new JLabel("[°]");

	private JLabel lbTo = new JLabel("Angle To:");
	public JEngineeringTextField tfTo = new JEngineeringTextField(col);
	private JLabel lbToUnit = new JLabel("[°]");

	private JLabel lbPoints = new JLabel("Points:");
	public JIntegerTextField tfPoints = new JIntegerTextField(col);
	private JLabel lbPointsUnit = new JLabel("[ ]");

	public JSlider sliderPoints = new JSlider(100, 1700);

	// Normalized Power
	private JLabel lbMaxGain = new JLabel("Max Gain:");
	public JIntegerTextField tfMaxGain = new JIntegerTextField(col);
	private JLabel lbMaxGainUnit = new JLabel("[dB]");

	private JLabel lbMinGain = new JLabel("Min Gain:");
	public JIntegerTextField tfMinGain = new JIntegerTextField(col);
	private JLabel lbMinGainUnit = new JLabel("[dB]");

	private ItemListener ilDiagramType;
	private ActionListener alUpdateSimulation;
	private ChangeListener clPoints;
	private MouseWheelListener mwlPoints;

	/**
	 * <pre>
	 * Builds the diagram panel.
	 * </pre>
	 */
	public DiagramPanel() {
		setLayout(new BorderLayout());
		setBackground(background);

		add(this.chartPanel, BorderLayout.CENTER);

		this.paramPanel.setLayout(new GridBagLayout());
		this.paramPanel.setBorder(CustomBorder.createBorder("Diagram Settings"));
		add(this.paramPanel, BorderLayout.PAGE_END);
		this.paramPanel.setBackground(background);

		this.paramPanel.add(this.rbDiagramTypeCartesian, new GridBagConstraints(0, 1, 3, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.rbDiagramTypePolar, new GridBagConstraints(3, 1, 3, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.rbDiagramTypeCartesian.setBackground(background);
		this.rbDiagramTypePolar.setBackground(background);

		this.btGroup.add(rbDiagramTypeCartesian);
		this.btGroup.add(rbDiagramTypePolar);

		this.paramPanel.add(this.lbFrom, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.tfFrom, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.lbFromUnit, new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfFrom.setRange(0.0, 360.0);

		this.paramPanel.add(this.lbTo, new GridBagConstraints(3, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.tfTo, new GridBagConstraints(4, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.lbToUnit, new GridBagConstraints(5, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfTo.setRange(0.0, 360.0);

		this.paramPanel.add(this.lbPoints, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.tfPoints, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.lbPointsUnit, new GridBagConstraints(2, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfPoints.setRange(100, 1700);

		this.paramPanel.add(this.sliderPoints, new GridBagConstraints(3, 4, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.sliderPoints.setBackground(background);
		this.sliderPoints.setMajorTickSpacing(400);
		this.sliderPoints.setMinorTickSpacing(100);
		this.sliderPoints.setPaintTicks(true);
		this.sliderPoints.setPaintLabels(true);

		this.paramPanel.add(this.lbMaxGain, new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.tfMaxGain, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.lbMaxGainUnit, new GridBagConstraints(2, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfMaxGain.setRange(-1000, 0);

		this.paramPanel.add(this.lbMinGain, new GridBagConstraints(3, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.tfMinGain, new GridBagConstraints(4, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.paramPanel.add(this.lbMinGainUnit, new GridBagConstraints(5, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		this.tfMinGain.setRange(-1000, 0);

		this.ilDiagramType = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					hideFields();
					controller.updateSimulation(Controller.UPDATE);
				}
			}
		};

		this.alUpdateSimulation = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sliderPoints.setValue(tfPoints.getValue());
				controller.updateSimulation(Controller.UPDATE);
			}
		};

		this.clPoints = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (sliderPoints.getValueIsAdjusting()) {
					tfPoints.setValue(sliderPoints.getValue());
					controller.updateSimulation(Controller.UPDATE);
				}
			}
		};

		this.mwlPoints = new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 1) {
					int newValue = sliderPoints.getValue() + 1;
					if (newValue <= sliderPoints.getMaximum()) {
						sliderPoints.setValue(newValue);
						tfPoints.setValue(sliderPoints.getValue());
						controller.updateSimulation(Controller.UPDATE);
					}
				} else {
					int newValue = sliderPoints.getValue() - 1;
					if (newValue >= sliderPoints.getMinimum()) {
						sliderPoints.setValue(newValue);
						tfPoints.setValue(sliderPoints.getValue());
						controller.updateSimulation(Controller.UPDATE);
					}
				}
			}
		};
	}

	/**
	 * <pre>
	 * Prepares the XYLineChart.
	 * </pre>
	 */
	public void setupCartesianChart() {
		this.chart = ChartFactory.createXYLineChart(title, "Angle [°]", "Normalized Power [dB]", null,
				PlotOrientation.VERTICAL, false, false, false);
		this.chart.setBackgroundPaint(background);

		JFreeChartDPIFix.applyChartTheme(this.chart);

		XYPlot plot = (XYPlot) this.chart.getPlot();

		plot.setBackgroundAlpha(0f);

		plot.setDomainGridlinePaint(lg);
		plot.setRangeGridlinePaint(lg);

		plot.setRangeGridlineStroke(stroke);
		plot.setDomainGridlineStroke(stroke);

		// Domain Axis
		NumberAxis axis = (NumberAxis) plot.getDomainAxis();
		axis.setAxisLineVisible(false);
		axis.setTickMarkPaint(Color.BLACK);
		axis.setRange(this.tfFrom.getValue(), this.tfTo.getValue());

		// Range Axis
		axis = (NumberAxis) plot.getRangeAxis();
		axis.setRange(this.tfMinGain.getValue(), this.tfMaxGain.getValue() + 1); // maxGain + 1
		axis.setAxisLineVisible(false);
		axis.setTickMarkPaint(Color.BLACK);

		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, lb);
		renderer.setSeriesStroke(0, seriesStroke);

		updateChartPanel();
	}

	/**
	 * <pre>
	 * Prepares the PolarPlot.
	 * </pre>
	 */
	public void setupPolarChart() {
		PolarPlot plot = new PolarPlot(null, new NumberAxis(), new DefaultPolarItemRenderer()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<NumberTick> refreshAngleTicks() {
				List<NumberTick> ticks = new ArrayList<>();
				@SuppressWarnings("unused")
				String[] radLabels = { "  0", "  \u03c0/6", "  \u03c0/3", "\u03c0/2", "2\u03c0/3  ", "5\u03c0/6  ",
						"\u03c0  ", "7\u03c0/6  ", "4\u03c0/3  ", "3\u03c0/2", "  5\u03c0/3", "  11\u03c0/6" };
				String[] degLabels = { "  0", "  30", "  60", "90", "120  ", "150  ", "180  ", "210  ", "240  ", "270",
						"  300", "  330" };
				double tickVal = 30.0;
				for (double currentTickVal = 0.0; currentTickVal < 360.0; currentTickVal += tickVal) {
					ticks.add(new NumberTick(currentTickVal, degLabels[(int) (currentTickVal / 30.0)],
							calculateTextAnchor(currentTickVal), TextAnchor.CENTER, 0.0));
				}
				return ticks;
			}
		};
		this.chart = new JFreeChart(this.title, JFreeChart.DEFAULT_TITLE_FONT, plot, false);
		this.standardTheme.apply(chart);
		this.chart.setBackgroundPaint(background);

		plot.setAngleLabelFont(standardFont);

		JFreeChartDPIFix.applyChartTheme(this.chart);

		plot.setAngleOffset(0); // set 0 degrees to east
		plot.setCounterClockwise(true);
		plot.setAxisLocation(0, PolarAxisLocation.NORTH_RIGHT);

		plot.setAngleLabelsVisible(true);

		plot.setBackgroundAlpha(0f);

		plot.setAngleGridlineStroke(stroke);
		plot.setAngleGridlinePaint(lg);

		plot.setRadiusGridlinePaint(lg);
		plot.setRadiusGridlineStroke(stroke);

		plot.setOutlineVisible(false);

		NumberAxis axis = (NumberAxis) plot.getAxis();
		axis.setAxisLineVisible(false);
		axis.setTickMarksVisible(false);
		axis.setMinorTickMarksVisible(false);

		axis.setRange(this.tfMinGain.getValue(), 0.0);
		axis.setTickLabelInsets(new RectangleInsets(0.0, 0.0, 0.0, 10.0));
		axis.setTickUnit(new NumberTickUnit(-this.tfMinGain.getValue() / 5.0));

		DefaultPolarItemRenderer renderer = (DefaultPolarItemRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, lb);
		renderer.setSeriesStroke(0, seriesStroke);
		renderer.setShapesVisible(false);

		updateChartPanel();
	}

	/**
	 * <pre>
	 * Redraws the chart panel and therefore updates it.
	 * </pre>
	 */
	public void updateChartPanel() {
		this.remove(chartPanel);

		this.chartPanel = new ChartPanel(this.chart);

		this.chartPanel.setPopupMenu(null);
		this.chartPanel.setRangeZoomable(false);
		this.chartPanel.setDomainZoomable(false);

		this.chartPanel.setPreferredSize(new Dimension(800, 500));

		add(this.chartPanel, BorderLayout.CENTER);
		this.chartPanel.revalidate();
	}

	/**
	 * <pre>
	 * Sets the data points.
	 * </pre>
	 * 
	 * @param y
	 */
	public void setData(double[] y) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series = new XYSeries("NormalizedPowerPattern");
		double[] x;
		if (this.rbDiagramTypeCartesian.isSelected()) {
			x = MatlabFunctions.linspace(this.tfFrom.getValue(), this.tfTo.getValue(), y.length);
			for (int i = 0; i < y.length; i++) {
				series.add(x[i], y[i]);
			}
			dataset.addSeries(series);
			XYPlot plot = (XYPlot) this.chart.getPlot();
			plot.setDataset(dataset);
		} else {
			x = MatlabFunctions.linspace(0.0, 360.0, y.length);
			for (int i = 0; i < y.length; i++) {
				series.add(x[i], y[i]);
			}
			dataset.addSeries(series);
			PolarPlot plot = (PolarPlot) this.chart.getPlot();
			plot.setDataset(dataset);
		}
	}

	/**
	 * <pre>
	 * Hides/Shows input fields.
	 * </pre>
	 */
	public void hideFields() {
		this.lbFrom.setEnabled(false);
		this.tfFrom.setEnabled(false);
		this.lbFromUnit.setEnabled(false);

		this.lbTo.setEnabled(false);
		this.tfTo.setEnabled(false);
		this.lbToUnit.setEnabled(false);

		this.lbMaxGain.setEnabled(false);
		this.tfMaxGain.setEnabled(false);
		this.lbMaxGainUnit.setEnabled(false);

		if (this.rbDiagramTypeCartesian.isSelected()) {
			this.lbFrom.setEnabled(true);
			this.tfFrom.setEnabled(true);
			this.lbFromUnit.setEnabled(true);

			this.lbTo.setEnabled(true);
			this.tfTo.setEnabled(true);
			this.lbToUnit.setEnabled(true);

			this.lbMaxGain.setEnabled(true);
			this.tfMaxGain.setEnabled(true);
			this.lbMaxGainUnit.setEnabled(true);
		}
	}

	/**
	 * <pre>
	 * Registers all the corresponding listeners.
	 * </pre>
	 */
	public void registerListeners() {
		this.rbDiagramTypeCartesian.addItemListener(this.ilDiagramType);
		this.rbDiagramTypePolar.addItemListener(this.ilDiagramType);

		this.tfFrom.addActionListener(this.alUpdateSimulation);
		this.tfTo.addActionListener(this.alUpdateSimulation);

		this.tfPoints.addActionListener(this.alUpdateSimulation);
		this.sliderPoints.addChangeListener(this.clPoints);
		this.sliderPoints.addMouseWheelListener(this.mwlPoints);

		this.tfMaxGain.addActionListener(this.alUpdateSimulation);
		this.tfMinGain.addActionListener(this.alUpdateSimulation);
	}

	/**
	 * <pre>
	 * - Fetches the normalized power from the model
	 * - Calls the according method to set up a cartesian or polar chart
	 * - Calls the according method to set the data
	 * </pre>
	 * 
	 * @param model
	 * @param simIndex
	 */
	public void update(Model model, int simIndex) {
		double[] y = model.getNormalizedPower(simIndex);
		if (this.rbDiagramTypeCartesian.isSelected()) {
			this.setupCartesianChart();
		} else {
			this.setupPolarChart();
		}
		this.setData(y);
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

}
