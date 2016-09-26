package de.tud.melissa.visualization.widgets;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import de.tud.melissa.util.LabelFormatter;
import de.tud.melissa.util.TimeLabelFormatter;
import de.tud.melissa.view.LineChartView;

/**
 * Time line chart to visualize time continuous values and their time.
 * 
 * @author Andreas Hippler
 */
public class TimeLineChart extends LineChartView<Date> {
	/**
	 * @param context
	 * @param dataCount the number of x values
	 */
	public TimeLineChart(Context context, int dataCount) {
		super(context, null, dataCount);
	}

	/**
	 * @param context
	 * @param attributeSet
	 * @param dataCount the number of x values
	 */
	public TimeLineChart(Context context, AttributeSet attributeSet, int dataCount) {
		super(context, attributeSet, dataCount);
	}

	@Override
	protected double convertValueX(Date date) {
		return date.getTime();
	}

	@Override
	protected LabelFormatter getLabelFormatter() {
		return new TimeLabelFormatter();
	}
}
