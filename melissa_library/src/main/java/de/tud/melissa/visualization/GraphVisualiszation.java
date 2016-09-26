/**
 * 
 */
package de.tud.melissa.visualization;

import android.content.Context;
import android.util.AttributeSet;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;

import de.tud.melissa.util.GraphValue;
import de.tud.melissa.util.GraphValues;
import de.tud.melissa.util.LabelFormatter;

/**
 * 
 * 
 * @author Andreas Hippler
 * @param <T>
 *            the type of the x axis
 * 
 */
public abstract class GraphVisualiszation<T> extends Visualization<GraphValue<T>> {

	protected GraphView graphView;
	protected GraphViewSeries series;

	protected int dataCount;

	/**
	 * @param context
	 * @param dataCount
	 *            count of x-values to display
	 */
	public GraphVisualiszation(Context context, int dataCount) {
		this(context, null, dataCount);
	}

	/**
	 * @param context
	 * @param attributeSet
	 * @param dataCount
	 *            count of x-values to display
	 */
	public GraphVisualiszation(Context context, AttributeSet attributeSet, int dataCount) {
		super(context, attributeSet);
		this.dataCount = dataCount;
	}

	/**
	 * Adds a new value. when dataCount is reached others are shifted rightwards
	 * 
	 * @param value
	 *            the new value
	 * 
	 */
	@Override
	public void setValue(GraphValue<T> value) {
		if (series == null) {
			series = new GraphViewSeries(new GraphViewData[] { new GraphViewData(convertValueX(value.getX()),
					value.getY()) });
			graphView.addSeries(series);
			return;
		}
		series.appendData(new GraphViewData(convertValueX(value.getX()), value.getY()), false, dataCount);
		graphView.redrawAll();
	}

	/**
	 * Adds new values. Sets dataCount to size of list+1.
	 * 
	 * @param values
	 *            the new values
	 * 
	 */
	public void setValues(GraphValues<T> values) {
		GraphViewData[] data = new GraphViewData[values.size()];

		int i = -1;

		for (GraphValue<T> value : values) {
			data[++i] = new GraphViewData(convertValueX(value.getX()), value.getY());
		}
		
		dataCount = values.size();

		series = new GraphViewSeries(data);
		graphView.removeAllSeries();
		graphView.addSeries(series);
	}

	/**
	 * Adds a new value and increments dataCount.
	 * 
	 * @param value
	 *            the new value
	 * 
	 */
	public void addValue(GraphValue<T> value) {
		dataCount++;
		setValue(value);
	}

	protected abstract double convertValueX(T x);

	protected abstract LabelFormatter getLabelFormatter();

	/**
	 * Set whether labels are displayed or not
	 * 
	 * @param showLabel
	 */
	public void setShowHorizontalLabels(boolean showLabel) {
		graphView.setShowHorizontalLabels(showLabel);
	}

	/**
	 * remove all values
	 */
	public void clear() {
		graphView.removeAllSeries();
		series = null;
	}

	/**
	 * Set the width for labels of the y axis
	 * 
	 * @param width
	 */
	public void setVerticalLabelsWidth(int width) {
		graphView.getGraphViewStyle().setVerticalLabelsWidth(width);
	}

	/**
	 * Set the test size of the labels. <br>
	 * <br>
	 * Use setShowHorizontalLabels() to show y labels.
	 * 
	 * @param size
	 */
	public void setTextSize(int size) {
		graphView.getGraphViewStyle().setTextSize(size);
	}
}
