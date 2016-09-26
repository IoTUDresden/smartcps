/**
 * 
 */
package de.tud.melissa.visualization.widgets;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import de.tud.melissa.util.LabelFormatter;
import de.tud.melissa.util.TimeLabelFormatter;
import de.tud.melissa.view.BarChartView;

/**
 * Bar chart to visualize time continuous values and their time.
 * 
 * @author Andreas Hippler
 * 
 */
public class TimeBarChart extends BarChartView<Date> {

	/**
	 * @param context
	 * @param dataCount the number of x values
	 */
	public TimeBarChart(Context context, int dataCount) {
		this(context, null, dataCount);
	}

	/**
	 * @param context
	 * @param attributeSet
	 * @param dataCount the number of x values
	 */
	public TimeBarChart(Context context, AttributeSet attributeSet, int dataCount) {
		super(context, attributeSet, dataCount);
	}

	@Override
	protected double convertValueX(Date x) {
		return x.getTime();
	}

	@Override
	protected LabelFormatter getLabelFormatter() {
		return new TimeLabelFormatter();
	}
}
