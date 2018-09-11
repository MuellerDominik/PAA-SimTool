package pro2E.ui.simulation.output;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTick;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PolarAxisLocation;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import pro2E.model.MatlabFunctions;
import pro2E.model.Model;

/**
 * <pre>
 * The <b><code>RadiatorTypePanel</code></b> class builds the radiator type panel.
 * </pre>
 * 
 * @author pro2E - Team3
 *
 */
public class RadiatorTypePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Color background = Color.WHITE;

	private String title = "Element Factor Power Pattern";

	private ChartTheme standardTheme = new StandardChartTheme("Standard");

	private Font standardFont = new Font("SansSerif", Font.PLAIN, 14);

	private Color lg = new Color(210, 210, 210);
	private Color lb = new Color(0, 114, 189);

	private BasicStroke stroke = new BasicStroke(1.0f);
	private BasicStroke seriesStroke = new BasicStroke(2.0f);

	private JFreeChart chart;
	public ChartPanel chartPanel = new ChartPanel(chart);

	/**
	 * <pre>
	 * Builds the radiator type panel.
	 * </pre>
	 */
	public RadiatorTypePanel() {
		setLayout(new BorderLayout());
		add(this.chartPanel, BorderLayout.CENTER);
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

		axis.setRange(-40.0, 0.0);
		axis.setTickLabelInsets(new RectangleInsets(0.0, 0.0, 0.0, 10.0));
		axis.setTickUnit(new NumberTickUnit(10.0));

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
		x = MatlabFunctions.linspace(0.0, 360.0, y.length);
		for (int i = 0; i < y.length; i++) {
			series.add(x[i], y[i]);
		}
		dataset.addSeries(series);
		PolarPlot plot = (PolarPlot) this.chart.getPlot();
		plot.setDataset(dataset);
	}

	/**
	 * <pre>
	 * - Fetches the normalized element factor power from the model
	 * - Calls the method to set up a polar chart
	 * - Calls the method to set the data
	 * </pre>
	 * 
	 * @param model
	 * @param simIndex
	 */
	public void update(Model model, int simIndex) {
		double[] y = model.getNormalizedElementPower(simIndex);
		this.setupPolarChart();
		this.setData(y);
	}

}
