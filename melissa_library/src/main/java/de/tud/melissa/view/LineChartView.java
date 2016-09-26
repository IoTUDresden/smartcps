package de.tud.melissa.view;

import android.content.Context;
import android.util.AttributeSet;

import com.jjoe64.graphview.LineGraphView;

import de.tud.melissa.visualization.GraphVisualiszation;

/**
 * @author Andreas Hippler
 *
 * @param <T>
 */
public abstract class LineChartView<T> extends GraphVisualiszation<T> {

	/**
	 * @param context
	 * @param dataCount the number of x-values
	 */
	public LineChartView(Context context, int dataCount) {
		this(context, null, dataCount);
	}

	/**
	 * @param context
	 * @param attributeSet
	 * @param dataCount the number of x-values
	 */
	public LineChartView(Context context, AttributeSet attributeSet, int dataCount) {
		super(context, attributeSet, dataCount);
		graphView = new LineGraphView(context, "");
		graphView.setCustomLabelFormatter(getLabelFormatter());
		graphView.setScrollable(true);
		graphView.setDisableTouch(true);
		addView(graphView);
	}

	@Override
	protected abstract double convertValueX(T x);
}
