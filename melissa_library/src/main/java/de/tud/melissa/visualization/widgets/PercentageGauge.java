package de.tud.melissa.visualization.widgets;

import java.util.Locale;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import de.tud.melissa.R;
import de.tud.melissa.view.AutoResizeTextView;
import de.tud.melissa.view.GaugeView;
import de.tud.melissa.visualization.ContinuousVisualization;

/**
 * Circular percentage visualization gauge with an optional ideal range
 * 
 * @author Peter Heisig <peter.heisig@tu-dresden.de>
 */
public class PercentageGauge extends ContinuousVisualization {

	/** Log and identification tag */
	public static final String TAG = PercentageGauge.class.getSimpleName();

	/** Relative ideal gauge position */
	public static final float IDEAL_GAUGE_POSITION = 1.1f;

	/** Screen width relative gauge thickness */
	public static final float RELATIVE_THICKNESS = 0.2f;

	/** Value label view */
	protected AutoResizeTextView mValueView;

	/** Value visualization gauge */
	protected GaugeView mForeground;

	/** Background gauge for full range visualization */
	protected GaugeView mBackground;

	/** Small gauge for ideal range */
	protected GaugeView mIdeal;

	/** Color for value gauge */
	private int mForegroundColor;

	/** Color for full range gauge */
	private int mBackgroundColor;

	/** Color for ideal gauge */
	private int mIdealColor;
	
	/** Color for value text */
	private int mNormalTextColor;
	
	/** Color for value text if not in ieadl bounds */
	private int mOutOfIdealTextColor = Color.RED;

	/**
	 * @param context
	 */
	public PercentageGauge(Context context) {
		super(context);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public PercentageGauge(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init(context);

		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PercentageGauge);

		mForegroundColor = typedArray.getColor(R.styleable.PercentageGauge_melissa_foregroundColor, 0x99000000);
		mBackgroundColor = typedArray.getColor(R.styleable.PercentageGauge_melissa_backgroundColor, 0x55000000);
		mIdealColor = typedArray.getColor(R.styleable.PercentageGauge_melissa_idealColor, 0x55000000);
		mHasIdeal = typedArray.getBoolean(R.styleable.PercentageGauge_melissa_showIdeal, false);
		mIdealBegin = typedArray.getInt(R.styleable.PercentageGauge_melissa_idealBegin, 0);
		mIdealEnd = typedArray.getInt(R.styleable.PercentageGauge_melissa_idealEnd, 100);

		typedArray.recycle();
	}
	
	private void init(Context context) {
		View.inflate(context, R.layout.percentage_gauge, this);

		mIdeal = (GaugeView) findViewById(R.id.ideal);
		mForeground = (GaugeView) findViewById(R.id.foreground);
		mBackground = (GaugeView) findViewById(R.id.background);
		mValueView = (AutoResizeTextView) findViewById(R.id.value);
		mValueView.setPadding(12, 0, 0, 0);
		mNormalTextColor = mValueView.getTextColors().getDefaultColor();
	}

	@Override
	protected void onFinishInflate() {
		mForeground.setColor(mForegroundColor);
		mForeground.setOpenAngle(90);
		mForeground.setBegin(0);

		mBackground.setColor(mBackgroundColor);
		mBackground.setOpenAngle(90);
		mBackground.setEnd(100);

		mIdeal.setVisibility(mHasIdeal ? View.VISIBLE : View.GONE);
		mIdeal.setColor(mIdealColor);
		mIdeal.setBegin(getIdealBegin());
		mIdeal.setEnd(getIdealEnd());
		mIdeal.setOpenAngle(90);

		mValueView.setTextColor(mForegroundColor);

		// small hack to setup thickness and padding
		onSizeChanged(getMeasuredWidth(), getMeasuredHeight(), getMeasuredWidth() - 1, getMeasuredHeight() - 1);
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
		super.onSizeChanged(width, height, oldWidth, oldHeight);

		int thickness = (int) (width * RELATIVE_THICKNESS);

		mIdeal.setThickness((int) (thickness * RELATIVE_THICKNESS));
		mIdeal.setPadding((int) (thickness * IDEAL_GAUGE_POSITION), (int) (thickness * IDEAL_GAUGE_POSITION), 0, 0);

		mForeground.setThickness(thickness);
		mBackground.setThickness(thickness);

		if (thickness < 1)
			mValueView.setTextSize(153);
		else
			mValueView.setTextSize(thickness);
		
		mValueView.setMaxWidth(width/2);
	}

	/**
	 * Set percentage value within zero and 100. If out of range, it will be set
	 * to the smallest respectively biggest possible value and a warning will be
	 * triggered
	 * 
	 * @param value
	 *            current percentage value
	 */
	public void setValue(Double value) {
		if (value < 0.0 || value > 100.0) {
			value = value < 0.0 ? 0.0 : 100.0;
			Log.w(TAG, value + " as a value is not in range");
		}

		mValueView.setText(String.format(Locale.US, "%.1f %s", value,mUnit));
		mForeground.setEnd(value.intValue());

		if (hasIdeal()) {
			showIdeal();
			if (value<=mIdealBegin || value >= mIdealEnd) {
				mValueView.setTextColor(mOutOfIdealTextColor);
			} else {
				mValueView.setTextColor(mNormalTextColor);
			}
		} else {
			hideIdeal();
			mValueView.setTextColor(mNormalTextColor);
		}

		postInvalidate();
	}

	/** Hide ideal gauge */
	private void hideIdeal() {
		mIdeal.setVisibility(INVISIBLE);
	}

	/**
	 * Show ideal gauge
	 * 
	 * @param from
	 *            ideal gauge begins
	 * @param to
	 *            ideal gauge ends
	 */
	private void showIdeal() {
		int from = (int) mIdealBegin;
		int to = (int) mIdealEnd;

		if (from < 0.0 || from > to || to > 100.0) {
			Log.e(TAG, "invalid ideal range");
		} else {
			mIdeal.setBegin(from);
			mIdeal.setEnd(to);
		}
		mIdeal.setVisibility(VISIBLE);
	}

	private int getIdealBegin() {
		return Math.round(mIdealBegin);
	}

	private int getIdealEnd() {
		return Math.round(mIdealEnd);
	}
}
