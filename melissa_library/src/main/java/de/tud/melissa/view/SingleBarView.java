package de.tud.melissa.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * View to display a single bar
 *
 * @author Tobias Hafermalz <s5900703@mail.zih.tu-dresden.de>
 */
public class SingleBarView extends View implements RectangleChartView {

    /** Log and identification tag */
    public static final String TAG = SingleBarView.class.getSimpleName();

    private float mViewHeight;
    private float mViewWidth;

    /** View boundaries  */
    private RectF mBounds;

    /** Gauge paint */
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /** Gauge begin value */
    private float mBeginValue = 0;

    /** Gauge end value */
    private float mEndValue = 100;

    /** Gauge fill color */
    private int mColor = 0x77000000;

    /**
     * @param context
     */
    public SingleBarView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public SingleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(mColor);

        mBounds = new RectF(
                0,
                mViewHeight * (100 - mEndValue) / 100,
                mViewWidth,
                mViewHeight * (100 - mBeginValue) / 100);

        canvas.drawRect(mBounds, mPaint);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        final int horizontalPadding = getPaddingLeft() + getPaddingRight();
        final int verticalPadding   = getPaddingTop()  + getPaddingBottom();

        mViewHeight = height - verticalPadding;
        mViewWidth = width - horizontalPadding;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setBegin(float beginValue) {
        if(beginValue < 0)
            beginValue = 0;

        mBeginValue = beginValue;
        postInvalidate();
    }

    @Override
    public void setEnd(float endValue) {
        if(endValue > 100)
            endValue = 100;

        mEndValue = endValue;
        postInvalidate();
    }

    /**
     * Set gauge fill color
     *
     * @param color fill color
     */
    @Override
    public void setColor(final int color) {
        mColor = color;
        postInvalidate();
    }

}
