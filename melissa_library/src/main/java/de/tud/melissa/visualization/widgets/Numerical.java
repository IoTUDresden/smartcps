package de.tud.melissa.visualization.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import de.tud.melissa.R;
import de.tud.melissa.visualization.ContinuousVisualization;

/**
 * Class for numerical representation of sensor data.
 * 
 * @author Stefan Vogt <stefan.vogt1@tu-dresden.de>
 */
public class Numerical extends ContinuousVisualization {

	/** Log and identification tag */
	public static final String TAG = Numerical.class.getSimpleName();

	/** Current value label view */
	private TextView mValueCurrentTextView;

	/** Current value unit label view */
	private TextView mValueCurrentUnitTextView;

	/** Current value label view */
	private TextView mValueCurrentFractionPartTextView;

	/** Min value label view */
	private TextView mValueMinTextView;

	/** Min value unit label view */
	private TextView mValueMinUnitTextView;

	/** Min value decimal places label view */
	private TextView mValueMinFractionPartTextView;

	/** Max value label view */
	private TextView mValueMaxTextView;

	/** Max value unit label view */
	private TextView mValueMaxUnitTextView;

	/** Max value decimal places label view */
	private TextView mValueMaxFractionPartTextView;

	/** Color for normal current value */
	private int mValueCurrentColor;

	/** Color if under or over ideal values */
	private int mValueCurrentOutOfIdealColor = Color.RED;

	/**
	 * @param context
	 */
	public Numerical(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public Numerical(Context context, AttributeSet attrs) {
		super(context, attrs);

		View.inflate(context, R.layout.numerical, this);

		mValueCurrentTextView = (TextView) findViewById(R.id.ValueCurrentTextView);
		mValueCurrentColor = mValueCurrentTextView.getTextColors().getDefaultColor();
		mValueCurrentUnitTextView = (TextView) findViewById(R.id.ValueCurrentUnitTextView);
		mValueCurrentFractionPartTextView = (TextView) findViewById(R.id.ValueCurrentDecimalPlacesTextView);

		mValueMinTextView = (TextView) findViewById(R.id.ValueMinTextView);
		mValueMinUnitTextView = (TextView) findViewById(R.id.ValueMinUnitTextView);
		mValueMinFractionPartTextView = (TextView) findViewById(R.id.ValueMinDecimalPlacesTextView);

		mValueMaxTextView = (TextView) findViewById(R.id.ValueMaxTextView);
		mValueMaxUnitTextView = (TextView) findViewById(R.id.ValueMaxUnitTextView);
		mValueMaxFractionPartTextView = (TextView) findViewById(R.id.ValueMaxDecimalPlacesTextView);
	}

	@Override
	protected void onFinishInflate() {

		// mValueView.setTextColor(mValueCurrentColor);

		// small hack to setup thickness and padding
		onSizeChanged(getMeasuredWidth(), getMeasuredHeight(), getMeasuredWidth() - 1, getMeasuredHeight() - 1);
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
		super.onSizeChanged(width, height, oldWidth, oldHeight);

		// final int thickness = (int) (width * RELATIVE_THICKNESS);

		// mValueView.setTextSize(thickness);
	}

	/**
	 * 
	 * @param value
	 *            current value
	 */
	@Override
	public void setValue(Double value) {
		// set current value
		mValueCurrentTextView.setText(String.valueOf(value.intValue()) + ".");
		mValueCurrentFractionPartTextView.setText(fractionPart(value));
		mValueCurrentUnitTextView.setText(mUnit);

		if (hasIdeal() && (value <= mIdealBegin || value >= mIdealEnd)) {
			mValueCurrentTextView.setTextColor(mValueCurrentOutOfIdealColor);
			mValueCurrentFractionPartTextView.setTextColor(mValueCurrentOutOfIdealColor);
		} else {
			mValueCurrentTextView.setTextColor(mValueCurrentColor);
			mValueCurrentFractionPartTextView.setTextColor(mValueCurrentColor);
		}

		// set max value
		mValueMaxTextView.setText(String.valueOf((int) mMaxValue) + ".");
		mValueMaxFractionPartTextView.setText(fractionPart(mMaxValue));
		mValueMaxUnitTextView.setText(mUnit);

		// set min value
		mValueMinTextView.setText(String.valueOf((int) mMinValue) + ".");
		mValueMinFractionPartTextView.setText(fractionPart(mMinValue));
		mValueMinUnitTextView.setText(mUnit);

		postInvalidate();
	}

	/** get specified numbers of the fractions part of the number */
	private String fractionPart(double d) {
		int i = (int) (d * 10) % 10;
		return String.valueOf(i);
	}
}
