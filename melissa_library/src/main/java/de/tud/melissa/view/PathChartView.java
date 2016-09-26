package de.tud.melissa.view;

/**
 * Time continuous views
 * 
 * @author Tobias Hafermalz <s5900703@mail.zih.tu-dresden.de>
 */
public interface PathChartView extends ChartView {

	/**
	 * Sets the current percentage value (0 to 100)
	 * 
	 * @param value
	 */
	public void setValue(final float value);

}
