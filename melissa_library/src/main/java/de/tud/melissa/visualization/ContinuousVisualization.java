package de.tud.melissa.visualization;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Continuous range visualization
 * 
 * @author Peter Heisig, edit by Andreas Hippler
 */
public abstract class ContinuousVisualization extends Visualization<Double> {
	/** Minimal value */
	protected float mMinValue = 0;

	/** Maximum value */
	protected float mMaxValue = 0;

	/** The minimal ideal value. */
	protected float mIdealBegin = 0;

	/** The maximum ideal value. */
	protected float mIdealEnd = 0;

	/** Ideal display flag */
	protected boolean mHasIdeal = false;

	/** Unit of the visualization. */
	protected String mUnit = "";

	/**
	 * @param context
	 */
	public ContinuousVisualization(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attributeSet
	 */
	public ContinuousVisualization(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	/**
	 * set the ideal min and max value, and if they should be shown
	 * 
	 * @param min
	 * @param max
	 * @param hasIdeal
	 */
	public void setIdeals(Double min, Double max, boolean hasIdeal) {
		setIdeals(min.floatValue(), max.floatValue(), hasIdeal);
	}

	/**
	 * set the ideal min and max value, and if they should be shown
	 * 
	 * @param min
	 * @param max
	 * @param hasIdeal
	 */
	public void setIdeals(float min, float max, boolean hasIdeal) {
		setIdealBegin(min);
		setIdealEnd(max);
		showIdeal(hasIdeal);
	}

	/**
	 * set the low ideal value
	 * 
	 * @param d
	 */
	public void setIdealBegin(double d) {
		setIdealBegin(Double.valueOf(d).floatValue());
	}

	/**
	 * set the hight ideal value
	 * 
	 * @param d
	 */
	public void setIdealEnd(double d) {
		setIdealEnd(Double.valueOf(d).floatValue());
	}

	/**
	 * set the low ideal value
	 * 
	 * @param f
	 */
	public void setIdealBegin(float f) {
		mIdealBegin = f;
	}

	/**
	 * set the hight ideal value
	 * 
	 * @param f
	 */
	public void setIdealEnd(float f) {
		mIdealEnd = f;
	}

	/**
	 * set if the ideal value should be shown
	 * 
	 * @param b
	 */
	public void showIdeal(boolean b) {
		mHasIdeal = b;
	}

	/** if ideal values should be shown */
	protected boolean hasIdeal() {
		return mHasIdeal;
	}

	/**
	 * set the unit
	 * 
	 * @param s
	 */
	public void setUnit(String s) {
		mUnit = s;
	}

	/**
	 * set the min value
	 * 
	 * @param d
	 */
	public void setMaxValue(Double d) {
		setMaxValue(d.floatValue());
	}

	/**
	 * set the min value
	 * 
	 * @param f
	 */
	public void setMaxValue(float f) {
		mMaxValue = f;
	}

	/**
	 * set the max value
	 * 
	 * @param d
	 */
	public void setMinValue(Double d) {
		setMinValue(d.floatValue());
	}

	/**
	 * set the max value
	 * 
	 * @param f
	 */
	public void setMinValue(float f) {
		mMinValue = f;
	}

	/**
	 * set the min and max value
	 * 
	 * @param min
	 * @param max
	 */
	public void setMinAndMaxValues(float min, float max) {
		setMinValue(min);
		setMaxValue(max);
	}

	/**
	 * set the min and max value
	 * 
	 * @param min
	 * @param max
	 */
	public void setMinAndMaxValues(double min, double max) {
		setMinValue(min);
		setMaxValue(max);
	}

	@Override
	public abstract void setValue(Double value);
}
