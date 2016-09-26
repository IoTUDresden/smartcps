package eu.vicci.ecosystem.standalone.controlcenter.android.visualization;

import de.tud.melissa.visualization.Visualization;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardDevice;

/**
 * Possible {@link Visualization} for an {@link DashboardDevice}
 * 
 * @author Dennis Metzger
 *
 */
public enum VisualizationType {
	/**
	 * BarChart
	 */
	BarChart,
	
	/**
	 * BarGauge
	 */
	BarGauge,
	
	/**
	 * CircularGauge
	 */
	CircularGauge,
	
	/**
	 * DoorStateFlipper
	 */
	DoorStateFlipper,
	
	/**
	 * IdealPieChart
	 */
	IdealPieChart,
	
	/**
	 * Numerical
	 */
	Numerical,
	
	/**
	 * PercentageGauge
	 */
	PercentageGauge,
	
	/**
	 * RangeBarChart
	 */
	RangeBarChart, 
	
	/**
	 * SemiIdealPieChart
	 */
	SemiIdealPieChart, 
	
	/**
	 * TimeLineChart
	 */
	TimeLineChart, 
	
	/**
	 * TimeBarChart
	 */
	TimeBarChart, 
	
	/**
	 * WindowStateFlipper
	 */
	WindowStateFlipper,
	
	/**
	 * special visualization for robots
	 */
	RobotInfo,
	
	/**
	 * special visualization for processes
	 */
	ProcessInfo
}
