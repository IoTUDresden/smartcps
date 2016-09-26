package de.tud.melissa.view;

/**
 * Circular chart views
 * 
 * @author Tobias Hafermalz <s5900703@mail.zih.tu-dresden.de>
 */
public interface CircularChartView extends ChartView {

	/**
	 * Sets the angle to begin the circular view
	 * 
	 * @param angleBegin
	 */
	public void setAngleBegin(final float angleBegin);

	/**
	 * Sets the sweep angle
	 * 
	 * @param angleSweep
	 */
	public void setAngleSweep(final float angleSweep);

}
