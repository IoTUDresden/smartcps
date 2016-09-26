package eu.vicci.ecosystem.standalone.controlcenter.android.processview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.TextView;

public class GatewayView extends TextView{

	private Paint paint;
	private Paint textPaint;
	private Path carot;
	private int viewWidth;
	private int viewHeight;
	private String gateType;
	private String label;
	
	public GatewayView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public GatewayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init(){
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(5);
		paint.setColor(Color.LTGRAY);
//		paint.setShadowLayer(30, 10, 10, Color.RED);
		
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(40);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setTextSkewX(-0.25f);
		textPaint.setColor(Color.BLACK);
		
		carot = new Path();
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
		carot.moveTo(0, viewHeight / 2);
		carot.lineTo(viewWidth / 2, viewHeight);
		carot.lineTo(viewWidth, viewHeight / 2);
		carot.lineTo(viewWidth / 2, 0);
		carot.lineTo(0,  viewHeight / 2);
		
		canvas.drawPath(carot, paint);
		canvas.drawText(label, viewWidth / 2, (viewHeight / 2) + 10, textPaint);
		
		
		super.onDraw(canvas);

	}
	
	public void setGateType(String type){
		gateType = type;
	}
	
	public String getGateType(){
		return gateType;
	}
	
	public void setLabel(String label){
		this.label = label;
	}
}

