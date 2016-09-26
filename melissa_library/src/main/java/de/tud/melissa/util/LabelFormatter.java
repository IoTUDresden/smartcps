/**
 * 
 */
package de.tud.melissa.util;

import com.jjoe64.graphview.CustomLabelFormatter;

import de.tud.melissa.visualization.GraphVisualiszation;

/**
 * Abstract class to handle label formatting. Splits the formatLabel function of {@linkplain CustomLabelFormatter}
 * into two functions.
 * 
 * @author Andreas Hippler
 * 
 */
public abstract class LabelFormatter implements CustomLabelFormatter {
	
	@Override
	public String formatLabel(double value, boolean isValueX) {
		return isValueX ? formatXValue(value) : formatYValue(value);
	}

	/**
	 * Formats the x value for displaying in the {@link GraphVisualiszation}
	 * 
	 * @param x_value the value for the x-axis
	 * @return the formated text
	 */
	protected abstract String formatXValue(double x_value);

	/**
	 * Formats the y value for displaying in the {@link GraphVisualiszation}
	 * 
	 * @param y_value the value for the y-axis
	 * @return the formated text
	 */
	protected abstract String formatYValue(double y_value);
}
