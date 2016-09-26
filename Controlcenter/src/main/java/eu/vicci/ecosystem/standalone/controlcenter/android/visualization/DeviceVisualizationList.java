package eu.vicci.ecosystem.standalone.controlcenter.android.visualization;

import java.util.Date;
import java.util.Hashtable;

import android.content.Context;
import android.view.View;
import de.tud.melissa.util.GraphValue;
import de.tud.melissa.visualization.ContinuousVisualization;
import de.tud.melissa.visualization.DiscreteVisualization;
import de.tud.melissa.visualization.GraphVisualiszation;
import de.tud.melissa.visualization.Visualization;
import de.tud.melissa.visualization.widgets.BarChart;
import de.tud.melissa.visualization.widgets.BarGauge;
import de.tud.melissa.visualization.widgets.CircularGauge;
import de.tud.melissa.visualization.widgets.DoorStateFlipper;
import de.tud.melissa.visualization.widgets.IdealPieChart;
import de.tud.melissa.visualization.widgets.Numerical;
import de.tud.melissa.visualization.widgets.PercentageGauge;
import de.tud.melissa.visualization.widgets.RangeBarChart;
import de.tud.melissa.visualization.widgets.SemiIdealPieChart;
import de.tud.melissa.visualization.widgets.TimeBarChart;
import de.tud.melissa.visualization.widgets.TimeLineChart;
import de.tud.melissa.visualization.widgets.WindowStateFlipper;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardDevice;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardValueObject;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DeviceType;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.Process;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.Robot;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.device.ValueReceiver;

/**
 * @author Dennis Metzger
 * 
 */
public class DeviceVisualizationList {

	/**
	 * get usable visualizations for a device. decision is made
	 * {@link DeviceType} and unitDescription of the given device.
	 * 
	 * @param d
	 *            the device
	 * @return {@link Hashtable} with name of {@link Visualization} as key and
	 *         {@link VisualizationType} as value
	 */
	public static Hashtable<String, VisualizationType> getVisualizationsFromDevice(DashboardDevice d) {
		Hashtable<String, VisualizationType> l = new Hashtable<String, VisualizationType>();
		switch (d.getDeviceType()) {
		case Process:
			l.put("Process Info", VisualizationType.ProcessInfo);
			break;
		case Robot:
			l.put("Robot Info", VisualizationType.RobotInfo);
		case Sensor:
			String unit = d.getValueObject().getUnitDescription();

			if (unit.equals("Humidity")) {
				l.put("Numerical", VisualizationType.Numerical);
				l.put("Percentage Gauge", VisualizationType.PercentageGauge);
				l.put("Time Line Chart", VisualizationType.TimeLineChart);
				l.put("Time Bar Chart", VisualizationType.TimeBarChart);
			} else if (unit.equals("degC") || unit.equals("Lux") || unit.equals("Barometer")) {
				l.put("Bar Chart", VisualizationType.BarChart);
				l.put("Circular Gauge", VisualizationType.CircularGauge);
				l.put("Bar Gauge", VisualizationType.BarGauge);
				l.put("Numerical", VisualizationType.Numerical);
				l.put("Range Bar Chart", VisualizationType.RangeBarChart);
				l.put("Time Line Chart", VisualizationType.TimeLineChart);
				l.put("Time Bar Chart", VisualizationType.TimeBarChart);
			}
		}
		return l;
	}

	/**
	 * Updates all states of the given visualization
	 * 
	 * @param v
	 * @param device
	 */
	@SuppressWarnings("unchecked")
	public static void updateVisualization(Visualization<?> v, DashboardDevice device) {
		if (v instanceof ContinuousVisualization)
			updateVisualization((ContinuousVisualization) v, device);
		else if (v instanceof GraphVisualiszation)
			updateVisualization((GraphVisualiszation<Date>) v, device);
		else if (v instanceof InfoVisusalization)
			updateVisualization((InfoVisusalization) v, device);
		else if (v instanceof DiscreteVisualization)
			updateVisualization((DiscreteVisualization) v, device);
	}

	private static void updateVisualization(ContinuousVisualization cv, DashboardDevice device) {
		if (device.getValueObject() == null || device.getValueObject().getCurrentValue() == null)
			return;
		DashboardValueObject valueObject = device.getValueObject();
		cv.setUnit(valueObject.getUnit());
		if (valueObject.hasIdealValue())
			cv.setIdeals(valueObject.getIdealValueMin(), valueObject.getIdealValueMax(), true);
		else
			cv.showIdeal(false);
		cv.setMinAndMaxValues(valueObject.getMinValue(), valueObject.getMaxValue());
		cv.setValue(valueObject.getCurrentValue());
	}

	private static void updateVisualization(DiscreteVisualization dv, DashboardDevice device) {
		if (device.getValueObject() == null || device.getValueObject().getCurrentValue() == null)
			return;
		dv.setValue(device.getValueObject().getCurrentValue().intValue());
	}

	private static void updateVisualization(GraphVisualiszation<Date> gv, DashboardDevice device) {
		if (device.getValueObject() == null || device.getValueObject().getCurrentValue() == null)
			return;
		ValueReceiver<Double> vr = device.getValueObject().getDeque().getLast();
		gv.setValue(new GraphValue<Date>(vr.getDate(), vr.getValue()));
	}

	private static void updateVisualization(InfoVisusalization visualization, DashboardDevice device) {
		InfoVisusalization vis = (InfoVisusalization) visualization;
		String[] values = null;

		switch (device.getDeviceType()) {
		case Robot:
			Robot robot = (Robot) device;
			Context context = SmartCPS_Impl.getAppContext();
			values = new String[] { robot.getId(), 
					robot.isConnected() ? 
							context.getString(R.string.btnYes) : context.getString(R.string.btnNo),
					robot.hasGrabber() ? 
							context.getString(R.string.btnYes) : context.getString(R.string.btnNo)};
			break;
		case Process:
			Process process = (Process) device;
			values = new String[] { process.getProcessStateString(), process.getProcessType() };
			break;
		default:
			return;
		}
		vis.setValue(values);
	}

	private static Visualization<?> getGraphVisualization(Context context, DashboardDevice device) {
		GraphVisualiszation<Date> g;
		switch (device.getVisualizationType()) {
		case TimeLineChart:
			g = new TimeLineChart(context, null, 20);
			break;
		case TimeBarChart:
			g = new TimeBarChart(context, null, 20);
			break;
		default:
			return null;
		}
		g.setShowHorizontalLabels(false);
		g.setTextSize(10);
		g.setVerticalLabelsWidth(32);
		if (device.getValueObject().getDeque().size() > 0) {
			g.setValues(device.getValueObject().getLastDatesAndValues(19, 1));
		}
		return g;
	}

	/**
	 * Liefert die passende Darstellung für ein DashboardDevice im Dashboard
	 * 
	 * @param name
	 * @param context
	 * @param device
	 * @return null - wenn keine passende View gefunden wurde
	 */
	public static View getView(VisualizationType name, Context context, DashboardDevice device) {
		Visualization<?> v;
		switch (name) {
		case BarChart:
			v = new BarChart(context);
			break;
		case BarGauge:
			v = new BarGauge(context);
			break;
		case CircularGauge:
			v = new CircularGauge(context);
			break;
		case DoorStateFlipper:
			v = new DoorStateFlipper(context);
			break;
		case IdealPieChart:
			v = new IdealPieChart(context);
			break;
		case Numerical:
			v = new Numerical(context);
			break;
		case PercentageGauge:
			v = new PercentageGauge(context);
			break;
		case RangeBarChart:
			v = new RangeBarChart(context);
			break;
		case SemiIdealPieChart:
			v = new SemiIdealPieChart(context);
			break;
		case TimeLineChart:
		case TimeBarChart:
			v = getGraphVisualization(context, device);
			break;
		case WindowStateFlipper:
			v = new WindowStateFlipper(context);
			break;
		case RobotInfo:
			v = new RobotInfo(context);
			break;
		case ProcessInfo:
			v = new ProcessInfo(context);
			break;
		default:
			return null;
		}

		v.setPadding(10, 10, 10, 10);
		updateVisualization(v, device);
		return v;
	}
}