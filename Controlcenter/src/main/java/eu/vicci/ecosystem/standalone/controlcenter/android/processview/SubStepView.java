package eu.vicci.ecosystem.standalone.controlcenter.android.processview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.Button;

public class SubStepView extends Button{
	
	private Paint shapePaint;
	private Paint fillShapePaint;
	private Rect rect;
	private Rect fillRect;
	private RectF rectF;
	private RectF fillRectF;
	private int viewWidth;
	private int viewHeight;

	public SubStepView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public SubStepView(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}

	private void init(){
		shapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		shapePaint.setStyle(Paint.Style.STROKE);
		shapePaint.setStrokeWidth(1);
		shapePaint.setColor(Color.DKGRAY);
//		shapePaint.setShadowLayer(30, 10, 10, Color.RED);
		
		fillShapePaint = new Paint();
		fillShapePaint.setStyle(Paint.Style.FILL);
		fillShapePaint.setColor(Color.LTGRAY);
//		fillShapePaint.setShadowLayer(30, 0, 0, Color.RED);
		
		rect = new Rect();
		fillRect = new Rect();
		rectF = new RectF(rect);
		fillRectF = new RectF(fillRect);
	}
	
	@Override
	 protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
	     super.onSizeChanged(xNew, yNew, xOld, yOld);

	       // Account for padding
	       float xpad = (float)(getPaddingLeft() + getPaddingRight());
	       float ypad = (float)(getPaddingTop() + getPaddingBottom());

	       // Account for the label
//	       if (mShowText) xpad += mTextWidth;

//	       float ww = (float)w - xpad;
//	       float hh = (float)h - ypad;
//
//	       // Figure out how big we can make the pie.
//	       float diameter = Math.min(ww, hh);
	     
	     viewWidth = xNew;
	     viewHeight = yNew;
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		
		rectF.set(1, 1, viewWidth - 1, viewHeight - 1);
		fillRectF.set(2, 2, viewWidth - 2, viewHeight - 2);
		
		canvas.drawRoundRect(rectF, 10f, 10f, shapePaint);
		canvas.drawRoundRect(fillRectF, 10f, 10f, fillShapePaint);
		super.onDraw(canvas);

	}
}
