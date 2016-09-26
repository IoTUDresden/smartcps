package de.tud.melissa.visualization.widgets;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.tud.melissa.R;
import de.tud.melissa.view.SingleBarView;
import de.tud.melissa.visualization.ContinuousVisualization;

/**
 * Time continuous bar chart. Displays variations in a time range. Every x
 * seconds a new bar is added, old bars are shifted left.
 * 
 * @author Tobias Hafermalz <s5900703@mail.zih.tu-dresden.de>
 */
public class RangeBarChart extends ContinuousVisualization {

	/** Log and identification tag */
	public static final String TAG = RangeBarChart.class.getSimpleName();

	/** Layout for past and current bars. */
	private LinearLayout mLayout;

	/** The current bar. */
	private SingleBarView mBar;

	/** Layout for time labels. */
	private LinearLayout mLabelLayout;

	/** TextView for the current time. */
	private TextView mLabelText;

	/** Layout holding the ideal bar and ideal texts. */
	private RelativeLayout mLayoutIdeal;

	/** TextView for the current value. */
	private TextView mLabelValue;

	/** Bar for displaying the ideal range. */
	private SingleBarView mIdeal;

	/** TextView showing the highest ideal value. */
	private TextView mLabelIdealBegin;

	/** TextView showing the lowest ideal value. */
	private TextView mLabelIdealEnd;

	/** Color for current and past bars. */
	private int mBarColor;

	/** Color for all TextViews. */
	private int mLabelColor;

	/** Color for the ideal bar. */
	private int mIdealColor;

	/** The context */
	private Context mContext;

	/** Time between two bars in milliseconds */
	private int TIME_STEPS = 10000;

	/** Time of the last update in milliseconds */
	private long mCurrentMilliseconds = 0;

	/** Time when the last bar was drawn */
	private long mStartTime = 0;

	/** The highest value in the current bar. */
	private float mHighestValue = -1;

	/** The lowest value in the current bar. */
	private float mLowestValue = -1;

	/** The rotation of the time TextViews in degrees. */
	private int LABEL_TEXT_ROTATION = -45;

	/**
	 * @param context
	 */
	public RangeBarChart(Context context) {
		super(context);

		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public RangeBarChart(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context);

		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PercentageGauge);

		mBarColor = typedArray.getColor(R.styleable.PercentageGauge_melissa_foregroundColor, 0x99000000);
		mLabelColor = typedArray.getColor(R.styleable.PercentageGauge_melissa_textColor, 0x99000000);
		mIdealColor = typedArray.getColor(R.styleable.PercentageGauge_melissa_idealColor, 0x99000000);

		mIdealBegin = typedArray.getInteger(R.styleable.PercentageGauge_melissa_idealBegin, 0);
		mIdealEnd = typedArray.getInteger(R.styleable.PercentageGauge_melissa_idealEnd, 0);
		mHasIdeal = typedArray.getBoolean(R.styleable.PercentageGauge_melissa_showIdeal, false);

		typedArray.recycle();
	}

	private void init(Context context) {
		mContext = context;

		View.inflate(context, R.layout.bar_chart, this);

		mBar = (SingleBarView) findViewById(R.id.foreground);
		mLayout = (LinearLayout) findViewById(R.id.barcharts);

		mLabelText = (TextView) findViewById(R.id.label_time);
		mLabelLayout = (LinearLayout) findViewById(R.id.labels);

		mLabelValue = (TextView) findViewById(R.id.labelValue);

		mIdeal = (SingleBarView) findViewById(R.id.ideal);
		mLayoutIdeal = (RelativeLayout) findViewById(R.id.layoutIdeal);
		mLabelIdealBegin = (TextView) findViewById(R.id.labelIdealBegin);
		mLabelIdealEnd = (TextView) findViewById(R.id.labelIdealEnd);
	}

	/** {@inheritDoc} */
	@Override
	protected void onFinishInflate() {
		mBar.setColor(mBarColor);
		mBar.setEnd(0);

		mLabelText.setRotation(LABEL_TEXT_ROTATION);
		mLabelText.setTextColor(mLabelColor);

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

		mLabelValue.setText(String.valueOf(value));

		if (value < mMinValue || value > mMaxValue) {
			value = value < mMinValue ? mMinValue : mMaxValue;
			Log.w(TAG, value + " as a value is not in range");
		}

		if (mMinValue == mMaxValue)
			value = 1;
		else
			value = (value - mMinValue) / (mMaxValue - mMinValue);

		// value bars
		if (mStartTime == 0)
			mStartTime = mCurrentMilliseconds;
		else if (mCurrentMilliseconds - mStartTime > TIME_STEPS) {
			if (mLowestValue != -1 && mHighestValue != -1) {
				mBar.setEnd(mHighestValue * 100);
				mBar.setBegin(mLowestValue * 100);
			}

			addBar();

			mHighestValue = value;
			mLowestValue = value;
			mStartTime = mCurrentMilliseconds;
		} else {
			if (value > mHighestValue)
				mHighestValue = value;
			if (value < mLowestValue || mLowestValue == -1)
				mLowestValue = value;
		}

		if (mLowestValue != -1 && mHighestValue != -1) {
			mBar.setEnd(mHighestValue * 100);
			mBar.setBegin(mLowestValue * 100);
		}

		// times
		Date date = new Date(mCurrentMilliseconds);
		String timeString = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date);

		mLabelText.setText(timeString);

		mLabelLayout.setPadding(mLayoutIdeal.getMeasuredWidth(), 0, mLabelValue.getMeasuredWidth(), 0);

		// ideal bar and text
		if (hasIdeal())
			showIdeal();
		else
			hideIdeal();

		// current value
		int barLayoutHeight = mLayout.getMeasuredHeight();
		int labelHeight = mLabelValue.getMeasuredHeight();

		float topLabelMargin = (1 - value) * barLayoutHeight - labelHeight / 2;

		if (topLabelMargin < labelHeight / 2)
			topLabelMargin = labelHeight / 2;

		if (topLabelMargin > barLayoutHeight - labelHeight)
			topLabelMargin = barLayoutHeight - labelHeight;

		MarginLayoutParams mlp = (MarginLayoutParams) mLabelValue.getLayoutParams();
		mlp.setMargins(mlp.leftMargin, (int) topLabelMargin, mlp.rightMargin, mlp.bottomMargin);

		postInvalidate();
	}

	/**
	 * @param milliseconds
	 */
	public void setMilliseconds(long milliseconds) {
		mCurrentMilliseconds = milliseconds;
	}

	/**
	 * Adds a new bar and shifts others to the left.
	 * 
	 */
	private void addBar() {
		// bar
		LinearLayout.LayoutParams oldLayoutParams = new LinearLayout.LayoutParams(mBar.getLayoutParams());

		SingleBarView bar = new SingleBarView(mContext);
		mBar = bar;
		mBar.setLayoutParams(oldLayoutParams);
		mBar.setColor(mBarColor);
		mLayout.addView(mBar);

		// text
		int textWidth = mLabelText.getMeasuredWidth();
		int textMargin = oldLayoutParams.width;

		MarginLayoutParams marginParams1 = new MarginLayoutParams(mLabelText.getLayoutParams());
		int marginRight = textWidth - textMargin;
		marginParams1.setMargins(0, 0, -marginRight, 0);

		LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(marginParams1);
		mLabelText.setLayoutParams(layoutParams1);

		TextView text = new TextView(mContext);
		text.setRotation(mLabelText.getRotation());
		mLabelText = text;
		mLabelText.setTextColor(mLabelColor);

		if (mLabelLayout.getChildCount() * (textWidth - marginRight) + textWidth > getChartWidth())
			mLabelLayout.removeView(mLabelLayout.getChildAt(0));

		mLabelLayout.addView(mLabelText);
	}

	/** Hide ideal bar */
	public void hideIdeal() {
		mHasIdeal = false;

		mIdeal.setVisibility(GONE);
		mLabelIdealBegin.setVisibility(GONE);
		mLabelIdealEnd.setVisibility(GONE);
	}

	/**
	 * Show ideal bar
	 * 
	 * @param from
	 *            ideal bar begins
	 * @param to
	 *            ideal bar ends
	 */
	private void showIdeal() {
		mIdeal.setVisibility(VISIBLE);
		mLabelIdealBegin.setVisibility(VISIBLE);
		mLabelIdealEnd.setVisibility(VISIBLE);

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

		mLabelIdealBegin.setText(String.valueOf(mIdealBegin));
		mLabelIdealEnd.setText(String.valueOf(mIdealEnd));

		int idealBarHeight = mIdeal.getMeasuredHeight();

		mLabelIdealBegin.setPadding(0, (int) (idealBarHeight - from * idealBarHeight), 0, 0);
		mLabelIdealEnd.setPadding(0, 0, 0, (int) (to * idealBarHeight));
	}

	/**
	 * Calculates the width of the bar layout.
	 * 
	 * @return The calculated width of the bar layout
	 */
	private float getChartWidth() {
		return mLayout.getMeasuredWidth() - mLayout.getPaddingLeft() + mLayout.getPaddingRight();
	}
}
