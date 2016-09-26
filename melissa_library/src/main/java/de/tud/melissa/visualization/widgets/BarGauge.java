package de.tud.melissa.visualization.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import de.tud.melissa.R;
import de.tud.melissa.view.SingleBarView;
import de.tud.melissa.visualization.ContinuousVisualization;

/**
 * Bar visualization gauge
 * 
 * @author Tobias Hafermalz <s5900703@mail.zih.tu-dresden.de>
 */
public class BarGauge extends ContinuousVisualization {

	/** Log and identification tag */
	public static final String TAG = PercentageGauge.class.getSimpleName();

	/** Relative ideal gauge position */
	public static final float IDEAL_GAUGE_POSITION = 1.1f;

	/** Screen width relative gauge thickness */
	public static final float RELATIVE_THICKNESS = 0.2f;

	/** Background bar for full range visualization */
	protected SingleBarView mForeground;

	/** Background bar for full range visualization */
	protected SingleBarView mBackground;

	/** Ideal bar for ideal range visualization */
	protected SingleBarView mIdeal;

	protected TextView mLabelIdealBegin;
	protected TextView mLabelIdealEnd;
	protected TextView mLabelValue;

	/** Color for value bar */
	private int mForegroundColor;

	/** Color for full range bar */
	private int mBackgroundColor;

	/** Color for ideal bar */
	private int mIdealColor;

	/** Color for labels */
	private int mLabelColor;

	/**
	 * @param context
	 */
	public BarGauge(Context context) {
		super(context);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public BarGauge(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context);

		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PercentageGauge);

		mForegroundColor = typedArray.getColor(R.styleable.PercentageGauge_melissa_foregroundColor, 0x99000000);

		mBackgroundColor = typedArray.getColor(R.styleable.PercentageGauge_melissa_backgroundColor, 0x55000000);

		mLabelColor = typedArray.getColor(R.styleable.PercentageGauge_melissa_textColor, 0x99000000);

		mIdealColor = typedArray.getColor(R.styleable.PercentageGauge_melissa_idealColor, 0x55000000);

		mHasIdeal = typedArray.getBoolean(R.styleable.PercentageGauge_melissa_showIdeal, false);

		mIdealBegin = typedArray.getInt(R.styleable.PercentageGauge_melissa_idealBegin, 0);

		mIdealEnd = typedArray.getInt(R.styleable.PercentageGauge_melissa_idealEnd, 100);

		typedArray.recycle();
	}

	private void init(Context context) {
		View.inflate(context, R.layout.bar_gauge, this);

		mForeground = (SingleBarView) findViewById(R.id.foreground);
		mBackground = (SingleBarView) findViewById(R.id.background);

		mIdeal = (SingleBarView) findViewById(R.id.ideal);

		mLabelIdealBegin = (TextView) findViewById(R.id.labelIdealBegin);
		mLabelIdealEnd = (TextView) findViewById(R.id.labelIdealEnd);

		mLabelValue = (TextView) findViewById(R.id.labelValue);
	}

	/** {@inheritDoc} */
	@Override
	protected void onFinishInflate() {
		mForeground.setColor(mForegroundColor);

		mBackground.setColor(mBackgroundColor);
		mBackground.setEnd(100);

		mIdeal.setColor(mIdealColor);

		mLabelIdealBegin.setTextColor(mLabelColor);
		mLabelIdealEnd.setTextColor(mLabelColor);

		mLabelValue.setTextColor(mLabelColor);
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
		super.onSizeChanged(width, height, oldWidth, oldHeight);
	}

	@Override
	public void setValue(Double d) {
		float value = d.floatValue();

		mLabelValue.setText(String.format("%.1f", d));
		mLabelIdealBegin.setText(String.format("%.1f", mMinValue));
		mLabelIdealEnd.setText(String.format("%.1f", mMaxValue));

		if (value < mMinValue || value > mMaxValue) {
			value = value < mMinValue ? mMinValue : mMaxValue;
			Log.w(TAG, value + " as a value is not in range");
		}

		if (mMinValue == mMaxValue)
			value = 1;
		else
			value = (value - mMinValue) / (mMaxValue - mMinValue);

		mForeground.setEnd(value * 100);

		int idealBarHeight = mIdeal.getMeasuredHeight();
		int labelHeight = mLabelValue.getMeasuredHeight();

		float topLabelMargin = (1 - value) * idealBarHeight - labelHeight / 2;

		if (topLabelMargin < labelHeight / 2)
			topLabelMargin = labelHeight / 2;

		if (topLabelMargin > idealBarHeight - labelHeight)
			topLabelMargin = idealBarHeight - labelHeight;

		MarginLayoutParams mlp = (MarginLayoutParams) mLabelValue.getLayoutParams();
		mlp.setMargins(mlp.leftMargin, (int) topLabelMargin, mlp.rightMargin, mlp.bottomMargin);

		postInvalidate();

		if (hasIdeal())
			showIdeal();
		else
			hideIdeal();
	}

	/** Hide ideal bar */
	private void hideIdeal() {
		mIdeal.setVisibility(GONE);
	}

	private void showIdeal() {
		mIdeal.setVisibility(VISIBLE);

		float from = mIdealBegin;
		float to = mIdealEnd;

		if (from < mMinValue || from > mMaxValue) {
			from = from < mMinValue ? mMinValue : mMaxValue;
			Log.w(TAG, from + " as a value is not in range");
		}

		if (to < mMinValue || to > mMaxValue) {
			to = to < mMinValue ? mMinValue : mMaxValue;
			Log.w(TAG, to + " as a value is not in range");
		}
		if (mMinValue == mMaxValue)
			from = 1;
		else
			from = (from - mMinValue) / (mMaxValue - mMinValue);

		if (mMinValue == mMaxValue)
			to = 1;
		else
			to = (to - mMinValue) / (mMaxValue - mMinValue);

		mIdeal.setBegin(from * 100);
		mIdeal.setEnd(to * 100);
	}

}
