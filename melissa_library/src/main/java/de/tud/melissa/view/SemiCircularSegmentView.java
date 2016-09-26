package de.tud.melissa.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Semi circular segment view
 *
 * @author Stefan Vogt <stefan.vogt1@tu-dresden.de>
 */
public class SemiCircularSegmentView extends View implements CircularChartView {

    /** Log and identification tag */
    public static final String TAG = SingleBarView.class.getSimpleName();

    private float mViewHeight;
    private float mViewWidth;

    /** View boundaries  */
    private RectF mBounds;

    /** Segment paint */
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /** Segment begin */
    private float mAngleBegin = 180;

    /** Segment value */
    private float mAngleSweep = 180;

    /** Segment fill color */
    private int mColor = 0x77000000;

    /**
     * @param context
     */
    public SemiCircularSegmentView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public SemiCircularSegmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(mColor);

        canvas.drawArc(mBounds, mAngleBegin, mAngleSweep, true, mPaint);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        final int horizontalPadding = getPaddingLeft() + getPaddingRight();
        final int verticalPadding   = getPaddingTop()  + getPaddingBottom();

        mViewHeight = height - verticalPadding;
        mViewWidth = width - horizontalPadding;

        mBounds = new RectF(0, 0, mViewWidth, mViewHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public void setAngleBegin(float angleBegin) {
        mAngleBegin = angleBegin;
        postInvalidate();
    }

    @Override
    public void setAngleSweep(float angleSweep) {
        mAngleSweep = angleSweep;
        postInvalidate();
    }

    /**
     * Set fill color
     *
     * @param color fill color
     */
    @Override
    public void setColor(final int color) {
        mColor = color;
        postInvalidate();
    }
}
