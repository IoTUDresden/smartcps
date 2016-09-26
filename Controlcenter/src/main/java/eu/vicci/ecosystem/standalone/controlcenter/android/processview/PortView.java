package eu.vicci.ecosystem.standalone.controlcenter.android.processview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class PortView extends TextView{
	
	private Paint paint;
	private Paint strokePaint;
	private int viewWidth;
	private int viewHeight;
	private String portType = " ";

	public PortView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public PortView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init(){
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(5);
		paint.setColor(Color.LTGRAY);
		paint.setShadowLayer(30, 10, 10, Color.RED);
		
		strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		strokePaint.setStyle(Paint.Style.STROKE);
		strokePaint.setStrokeWidth(5);
		strokePaint.setColor(Color.DKGRAY);
		strokePaint.setShadowLayer(30, 10, 10, Color.RED);
		
		portType = " ";
	}
	
	@Override
	 protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
	     super.onSizeChanged(xNew, yNew, xOld, yOld);

	       // Account for padding
	       float xpad = (float)(getPaddingLeft() + getPaddingRight());
	       float ypad = (float)(getPaddingTop() + getPaddingBottom());
	     
	     viewWidth = xNew;
	     viewHeight = yNew;
	}
	
	@Override
	protected void onDraw(Canvas canvas){


			if (portType.contains("end")){
				canvas.drawCircle(viewWidth / 2, viewHeight / 2, viewWidth / 2, paint);
				canvas.drawCircle(viewWidth / 2, viewHeight / 2, viewWidth / 2, strokePaint);
			}
			else{
				canvas.drawCircle(viewWidth / 2, viewHeight / 2, viewWidth / 2, paint);
			}

		super.onDraw(canvas);
	}
	
	public void setPortType(String type){
		portType = type;
	}
	
	public String getPortType(){
		return portType;
	}
}
