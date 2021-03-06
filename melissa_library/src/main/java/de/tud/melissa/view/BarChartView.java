/**
 * 
 */
package de.tud.melissa.view;

import android.content.Context;
import android.util.AttributeSet;

import com.jjoe64.graphview.BarGraphView;

import de.tud.melissa.visualization.GraphVisualiszation;

/**
 * @author Andreas Hippler
 *
 * @param <T> the type of the y-values
 */
public abstract class BarChartView<T> extends GraphVisualiszation<T> {
	/**
	 * @param context
	 * @param dataCount the number of x-values
	 */
	public BarChartView(Context context, int dataCount) {
		this(context, null, dataCount);
	}
	
	/**
	 * @param context
	 * @param attributeSet
	 * @param dataCount the number of x-values
	 */
	public BarChartView(Context context, AttributeSet attributeSet, int dataCount) {
		super(context, attributeSet, dataCount);
		graphView = new BarGraphView(context, "");
		graphView.setCustomLabelFormatter(getLabelFormatter());
		graphView.setScrollable(true);
		graphView.setDisableTouch(true);
		addView(graphView);
	}
	
	@Override
	protected abstract double convertValueX(T x);
}
