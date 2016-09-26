package de.tud.melissa.visualization.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import de.tud.melissa.R;
import de.tud.melissa.view.SemiCircularSegmentView;
import de.tud.melissa.visualization.ContinuousVisualization;

/**
 * Class for visualizing a half pie chart, dividing date into 3 categories.
 * 'target range', 'lower range' and 'upper range'.
 * 
 * @author Stefan Vogt <stefan.vogt1@tu-dresden.de>
 */
public class SemiIdealPieChart extends ContinuousVisualization {
	/** Log and identification tag */
	public static final String TAG = SemiIdealPieChart.class.getSimpleName();

	/** Circular segments */
	protected SemiCircularSegmentView mIdeal;
	protected SemiCircularSegmentView mLess;
	protected SemiCircularSegmentView mOver;

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
	public SemiIdealPieChart(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public SemiIdealPieChart(Context context, AttributeSet attrs) {
		super(context, attrs);

		View.inflate(context, R.layout.semi_circular_ideal, this);

		mIdeal = (SemiCircularSegmentView) findViewById(R.id.ideal);
		mLess = (SemiCircularSegmentView) findViewById(R.id.less);
		mOver = (SemiCircularSegmentView) findViewById(R.id.over);

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
		float idealPercentage = mIdealValues / amount * 180;
		float lessPercentage = mLessValues / amount * 180;
		float overPercentage = mOverValues / amount * 180;

		mLess.setAngleBegin(180);
		mLess.setAngleSweep((int) lessPercentage);

		mIdeal.setAngleBegin((int) lessPercentage + 180);
		mIdeal.setAngleSweep((int) idealPercentage);

		mOver.setAngleBegin((int) (lessPercentage + idealPercentage) + 180);
		mOver.setAngleSweep((int) overPercentage);

		postInvalidate();
	}
}
