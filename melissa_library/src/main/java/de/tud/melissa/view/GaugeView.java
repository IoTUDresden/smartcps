package de.tud.melissa.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
	 * Gauge view
	 * 
	 * @author Peter Heisig <peter.heisig@tu-dresden.de>
	 */
	public class GaugeView extends View {

		/** View boundaries */
		private RectF mBounds = new RectF(0, 0, 100, 100);

		/** Gauge paint */
		private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		/** Gauge thickness (pixels) */
		private int mThickness = 50;

		/** Gauge value (percentage) */
		private float mEnd = 100;

		/** Gauge beginning (percentage) */
		private float mStart = 0;

		/** Gauge fill color */
		private int mColor = 0x77000000;

		/** Circle's open area (angle) */
		private int mOpenAngle = 90;
		
		/** Circle's open area start (angle) */
		private int mOpenAnglePosition = 0;

		/**
		 * @param context
		 * @param attrs
		 */
		public GaugeView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		/** {@inheritDoc} */
		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			mPaint.setColor(mColor);
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeWidth(mThickness);

			final float angle = (3.6f - (mOpenAngle / 100.f));

			final float angleBegin = mOpenAnglePosition + mOpenAngle + angle * mStart;
			final float angleSweep =  angle * (mEnd - mStart);

			canvas.drawArc(mBounds, angleBegin, angleSweep, false, mPaint);
		}

		/** {@inheritDoc} */
		@Override
		protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
			final int horizontalPadding = getPaddingLeft() + getPaddingRight();
			final int verticalPadding = getPaddingTop() + getPaddingBottom();
			final float halfThickness = mThickness / 2.0f;

			mBounds = new RectF(getPaddingLeft() + halfThickness, getPaddingTop() + halfThickness, width
					- horizontalPadding - halfThickness, height - verticalPadding - halfThickness);
		}

		/** {@inheritDoc} */
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			final int givenHeight = MeasureSpec.getSize(heightMeasureSpec);
			final int givenWidth = MeasureSpec.getSize(widthMeasureSpec);
			final int horizontalPadding = getPaddingLeft() + getPaddingRight();
			final int verticalPadding = getPaddingTop() + getPaddingBottom();
			final int halfTheThickness = mThickness / 2;

			int minHeight = verticalPadding + halfTheThickness + getSuggestedMinimumHeight();
			int minWidth = horizontalPadding + halfTheThickness + getSuggestedMinimumWidth();

			int height = Math.min(givenHeight, minHeight);
			int width = Math.max(minWidth, givenWidth);
			int diameter = Math.max(height, width);

			setMeasuredDimension(diameter, diameter);
		}

		/**
		 * Set the circle's recessed angle
		 * 
		 * @param angle
		 *            recessed angle
		 */
		public void setOpenAngle(final int angle) {
			mOpenAngle = angle;
			postInvalidate();
		}
		
		/**
		 * Set the circle's recessed angle start position
		 * 
		 * @param start
		 *            recessed angle
		 */
		public void setOpenAnglePosition(final int start) {
			mOpenAnglePosition = start;
			postInvalidate();
		}

		/**
		 * Set percentage of begin
		 * 
		 * @param start
		 *            percentage
		 */
		public void setBegin(float start) {
			mStart = start;
			postInvalidate();
		}

		/**
		 * SSet percentage of end
		 * 
		 * @param end
		 *            percentage
		 */

		public void setEnd(float end) {
			mEnd = end;
			postInvalidate();
		}

		/**
		 * Gauge fill color
		 * 
		 * @param color
		 *            fill color
		 */
		public void setColor(final int color) {
			mColor = color;
			postInvalidate();
		}

		/**
		 * Gauge thickness
		 * 
		 * @param thickness
		 *            filled area thickness
		 */
		public void setThickness(int thickness) {
			mThickness = thickness;
			postInvalidate();
		}

	}