package eu.vicci.turtlebot.navigationapp.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class FlagBeacon {

	private final float flag_hight = 30;
	private final float flag_pennant_hight = 15;
	private final float boden_hight = 15;
	private final float boden_width = 30;
	private final float flag_pennant_width = boden_width/2;
	
	private boolean selected = false;
	
		private String text = "";
		private Paint paint = null;
		private float x,y;
	   
	public FlagBeacon(String name,float x, float y, boolean selected){
	      this.text = name;
	      this.x =  x;
	      this.y = y;
	      this.selected = selected;
			paint = new Paint();
	      paint.setColor(Color.GRAY);
	      
	     // paint.setAlpha(200);
	      paint.setAntiAlias(true);
	      paint.setDither(true);
	}
	
	public void draw(Canvas canvas){
		
		// Wimpel = pennant
		if(!selected) {
			paint.setColor(Color.DKGRAY);
		} else {
			paint.setColor(Color.RED);
		}
		
		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(x+1,y-flag_pennant_hight);
		path.lineTo(x+1+flag_pennant_width,y-flag_pennant_hight);
		path.lineTo(x+1,y-flag_hight);
		path.lineTo(x+1,y-flag_pennant_hight);
		path.close();
		
		canvas.drawPath(path, paint);

		
		// Flaggenuntergrund
		if(!selected) {
			paint.setColor(Color.DKGRAY);
		} else {
			paint.setColor(Color.RED);
		}
		RectF r = new RectF(x-boden_width/2,y-boden_hight/2,x+boden_width/2,y+boden_hight/2);
		canvas.drawOval(r, paint);
		
		// Wimpelstange
		paint.setColor(Color.BLACK);
		canvas.drawLine(x, y, x, y-flag_hight, paint);
		
		// Text
		float f = paint.measureText(text);
		paint.setColor(Color.WHITE);
		canvas.drawRect((x-f/2)-2, y+boden_hight/2-2+4, (x+f/2)+2, y+boden_hight+2+4, paint);
		paint.setColor(Color.BLACK);
		canvas.drawText(text, x-f/2, y+boden_hight+4, paint);
		
	}
	
	
}
