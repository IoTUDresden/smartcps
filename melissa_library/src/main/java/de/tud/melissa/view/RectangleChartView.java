package de.tud.melissa.view;

/**
 * Rectangle chart views
 * 
 * @author Tobias Hafermalz <s5900703@mail.zih.tu-dresden.de>
 */
public interface RectangleChartView extends ChartView {

	/**
	 * Sets the begin value (0 to 100)
	 * 
	 * @param beginValue
	 */
	public void setBegin(final float beginValue);

	/**
	 * Sets the end value (0 to 100)
	 * 
	 * @param endValue
	 */
	public void setEnd(final float endValue);

}
