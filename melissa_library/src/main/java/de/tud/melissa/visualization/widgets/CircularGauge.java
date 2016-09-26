package de.tud.melissa.visualization.widgets;

import java.util.Locale;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import de.tud.melissa.R;
import de.tud.melissa.view.AutoResizeTextView;
import de.tud.melissa.view.CircularSegmentView;
import de.tud.melissa.view.GaugeView;
import de.tud.melissa.visualization.ContinuousVisualization;

/**
 * Class for visualizing a circular (tachometer-like) gauge.
 * 
 * @author Tobias Hafermalz <s5900703@mail.zih.tu-dresden.de>
 */
public class CircularGauge extends ContinuousVisualization {

	/** Log and identification tag */
	@SuppressWarnings("unused")
	private static final String TAG = CircularGauge.class.getSimpleName();

	/** Screen width relative gauge thickness */
	private static final float RELATIVE_THICKNESS = 0.2f;

	/** Relative ideal gauge position */
	public static final float IDEAL_GAUGE_POSITION = 1.1f;

	/** Hand thickness */
	private static final float HAND_THICKNESS = 10.0f;

	/** Circular begin */
	private static final int mBegin = 150;

	/** Circular end */
	private static final int mAngel = 240;

	/** Arc for full range visualization */
	protected GaugeView mBackground;

	/** Circular segment for current value visualization */
	protected CircularSegmentView mHand;

	/** Arc for ideal visualization */
	protected GaugeView mIdeal;

	/** TextView for the label */
	private AutoResizeTextView mLabelValue;

	// /** Color for full range gauge */
	// private int mBackgroundColor;
	//
	// /** Color for hand circular fragment */
	// private int mHandColor;
	//
	// /** Color for the ideal arc. */
	// private int mIdealColor;
	//
	// /** Color for the label. */
	// private int mLabelColor;

	/**
	 * @param context
	 */
	public CircularGauge(Context context) {
		super(context);
		View.inflate(context, R.layout.circular_gauge, this);

		mHand = (CircularSegmentView) findViewById(R.id.hand);
		mBackground = (GaugeView) findViewById(R.id.background);

		mIdeal = (GaugeView) findViewById(R.id.ideal);
		mIdeal.setVisibility(INVISIBLE);

		mLabelValue = (AutoResizeTextView) findViewById(R.id.labelValue);
		mLabelValue.setSingleLine();

		mBackground.setOpenAngle(360 - mAngel);
		mBackground.setOpenAnglePosition(180 - mBegin);

		mHand.setAngleBegin((mBegin + mAngel / 2));
		mHand.setAngleSweep(HAND_THICKNESS);

		mIdeal.setOpenAngle(360 - mAngel);
		mIdeal.setOpenAnglePosition(180 - mBegin);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CircularGauge(Context context, AttributeSet attrs) {
		this(context);

		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PercentageGauge);

//		mBackgroundColor = typedArray.getColor(R.styleable.PercentageGauge_melissa_backgroundColor, 0x55000000);
//		mHandColor = typedArray.getColor(R.styleable.PercentageGauge_melissa_foregroundColor, 0x99000000);
//
//		mIdealColor = typedArray.getColor(R.styleable.PercentageGauge_melissa_idealColor, 0x99000000);
//		mLabelColor = typedArray.getColor(R.styleable.PercentageGauge_melissa_textColor, 0x99000000);
//
//		mIdealBegin = typedArray.getInteger(R.styleable.PercentageGauge_melissa_idealBegin, 0);
//		mIdealEnd = typedArray.getInteger(R.styleable.PercentageGauge_melissa_idealEnd, 0);
//		mHasIdeal = typedArray.getBoolean(R.styleable.PercentageGauge_melissa_showIdeal, false);

		typedArray.recycle();
	}

	@Override
	protected void onFinishInflate() {
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
		super.onSizeChanged(width, height, oldWidth, oldHeight);

		int thickness = (int) (width * RELATIVE_THICKNESS);

		mIdeal.setThickness((int) (thickness * RELATIVE_THICKNESS));
		mIdeal.setPadding((int) (thickness * IDEAL_GAUGE_POSITION), (int) (thickness * IDEAL_GAUGE_POSITION), 0, 0);

		mBackground.setThickness(thickness);

		if (thickness < 1)
			mLabelValue.setTextSize(153);
		else
			mLabelValue.setTextSize(thickness);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

	protected void setValue(float value) {
		mLabelValue.setText(String.format(Locale.US, "%.1f %s", value, mUnit));

		float percentageValue = (value - mMinValue) / (mMaxValue - mMinValue);
		float position = mBegin + percentageValue * mAngel - HAND_THICKNESS / 2;
		mHand.setAngleBegin(position);

		if (hasIdeal())
			showIdeal();
		else
			hideIdeal();

		postInvalidate();
	}

	/**
	 * Set current value
	 * 
	 * @param value
	 *            current value
	 */
	@Override
	public void setValue(Double value) {
		setValue(value.floatValue());
	}

	/** Hide ideal bar */
	protected void hideIdeal() {
		mIdeal.setVisibility(INVISIBLE);
	}

	/**
	 * Show ideal bar
	 * 
	 * @param from
	 *            ideal bar begins
	 * @param to
	 *            ideal bar ends
	 */
	protected void showIdeal() {
		float percentageBeginIdealValue = (mIdealBegin - mMinValue) / (mMaxValue - mMinValue);
		mIdeal.setBegin(percentageBeginIdealValue * 100);

		float percentageEndIdealValue = (mIdealEnd - mMinValue) / (mMaxValue - mMinValue);
		mIdeal.setEnd(percentageEndIdealValue * 100);

		mIdeal.setVisibility(VISIBLE);
	}
}
