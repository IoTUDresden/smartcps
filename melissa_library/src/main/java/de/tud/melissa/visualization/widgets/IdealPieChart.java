package de.tud.melissa.visualization.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import de.tud.melissa.R;
import de.tud.melissa.view.CircularSegmentView;
import de.tud.melissa.visualization.ContinuousVisualization;

/**
 * @author Tobias Hafermalz <s5900703@mail.zih.tu-dresden.de>
 */
public class IdealPieChart extends ContinuousVisualization {
	/** Log and identification tag */
	public static final String TAG = IdealPieChart.class.getSimpleName();

	/** Circular segments */
	protected CircularSegmentView mIdeal;
	protected CircularSegmentView mLess;
	protected CircularSegmentView mOver;

	private ImageView mIdealText;
	private ImageView mLessText;
	private ImageView mOverText;

	/** Color for areas */
	private int mIdealColor;
	private int mLessColor;
	private int mOverColor;

	/** Counter for different values */
	private int mIdealValues = 0;
	private int mLessValues = 0;
	private int mOverValues = 0;

	/**
	 * @param context
	 */
	public IdealPieChart(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public IdealPieChart(Context context, AttributeSet attrs) {
		super(context, attrs);

		View.inflate(context, R.layout.circular_ideal, this);

		mIdeal = (CircularSegmentView) findViewById(R.id.ideal);
		mLess = (CircularSegmentView) findViewById(R.id.less);
		mOver = (CircularSegmentView) findViewById(R.id.over);

		mIdealText = (ImageView) findViewById(R.id.label_ideal);
		mLessText = (ImageView) findViewById(R.id.label_less);
		mOverText = (ImageView) findViewById(R.id.label_over);

		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PercentageGauge);

		mIdealColor = Color.parseColor("#669900");
		mLessColor = Color.parseColor("#0099CC");
		mOverColor = Color.parseColor("#CC0000");

		typedArray.recycle();
	}

	@Override
	protected void onFinishInflate() {
		mIdeal.setColor(mIdealColor);
		mLess.setColor(mLessColor);
		mOver.setColor(mOverColor);

		mIdealText.setBackgroundColor(mIdealColor);
		mLessText.setBackgroundColor(mLessColor);
		mOverText.setBackgroundColor(mOverColor);
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
		super.onSizeChanged(width, height, oldWidth, oldHeight);
	}

	@Override
	public void setValue(Double d) {
		float value = d.floatValue();

		if (value < mIdealBegin)
			mLessValues++;
		else if (value > mIdealEnd)
			mOverValues++;
		else
			mIdealValues++;

		float amount = mIdealValues + mLessValues + mOverValues;
		float idealPercentage = mIdealValues / amount * 360;
		float lessPercentage = mLessValues / amount * 360;
		float overPercentage = mOverValues / amount * 360;

		mLess.setAngleBegin(0);
		mLess.setAngleSweep((int) Math.ceil(lessPercentage));

		mIdeal.setAngleBegin((int) Math.ceil(lessPercentage));
		mIdeal.setAngleSweep((int) Math.ceil(idealPercentage));

		mOver.setAngleBegin((int) Math.ceil((lessPercentage + idealPercentage)));
		mOver.setAngleSweep((int) Math.ceil(overPercentage));

		postInvalidate();
	}
}
