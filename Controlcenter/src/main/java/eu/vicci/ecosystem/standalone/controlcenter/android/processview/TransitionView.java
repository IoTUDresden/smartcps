package eu.vicci.ecosystem.standalone.controlcenter.android.processview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class TransitionView extends TextView{

	private int viewWidth;
	private int viewHeight;
	private Paint paint;
	private Paint arrowHeadPaint;
	private Rect rect;
	private Path arrowHead;
	private float textSize;
	
	public TransitionView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public TransitionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init(){
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		paint.setColor(Color.DKGRAY);
		paint.setShadowLayer(30, 10, 10, Color.RED);
		
		arrowHeadPaint = new Paint();
		arrowHeadPaint.setStyle(Paint.Style.FILL);
		arrowHeadPaint.setStrokeWidth(1);
		arrowHeadPaint.setColor(Color.DKGRAY);
		arrowHeadPaint.setShadowLayer(30, 10, 10, Color.RED);
		
		rect = new Rect();
		arrowHead = new Path();

	}
	
	@Override
	 protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
	     super.onSizeChanged(xNew, yNew, xOld, yOld);

	       // Account for padding
	       float xpad = (float)(getPaddingLeft() + getPaddingRight());
	       float ypad = (float)(getPaddingTop() + getPaddingBottom());
	     
	     viewWidth = xNew;
	     viewHeight = yNew;
	     
	     textSize = getTextSize();
	}
	
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//	   // Try for a width based on our minimum
////	   int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
//	   int minw = getPaddingLeft() + getPaddingRight() + 200;
//	   int w = resolveSizeAndState(minw, widthMeasureSpec, 1);
//
//	   // Whatever the width ends up being, ask for a height that would let the pie
//	   // get as big as it can
//	   int minh = (int)textSize + getPaddingBottom() + getPaddingTop();
//	   int h = resolveSizeAndState(viewHeight, heightMeasureSpec, 0);
//
//	   setMeasuredDimension(w, viewHeight);
//	}
	
	@Override
	protected void onDraw(Canvas canvas){
		
		int baseline = getLineBounds(0, rect);
		arrowHead.moveTo(viewWidth - 15, baseline + 5);
		arrowHead.lineTo(viewWidth - 15, baseline - 15);
		arrowHead.lineTo(viewWidth, baseline + 5);
		arrowHead.lineTo(viewWidth -15, baseline + 25);
		arrowHead.lineTo(viewWidth -15, baseline + 5);
		
		canvas.drawLine(0, baseline + 5, viewWidth, baseline + 5, paint);
		canvas.drawPath(arrowHead, arrowHeadPaint);
		super.onDraw(canvas);

	}
	
}
