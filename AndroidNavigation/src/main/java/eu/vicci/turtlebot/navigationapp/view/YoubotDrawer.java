package eu.vicci.turtlebot.navigationapp.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.View;
import eu.vicci.robot.android.navigation.R;

public class YoubotDrawer {

	private String text = "";
	private Paint paint = null;
	private float x,y;
	private boolean selected = false;
	private View mapView;
	
	private final float boden_hight = 15;
	private final float boden_width = 5;
	
	public YoubotDrawer(View v, String name,Point p, boolean selected){
	      this.mapView = v;
		  this.text = name;
	      this.x =  p.x;
	      this.y = p.y+50;
	      this.selected = selected;
			paint = new Paint();
	      paint.setColor(Color.GRAY);
	      
	     //paint.setAlpha(200);
	      paint.setAntiAlias(true);
	      paint.setDither(true);
	}
	
	public void draw(Canvas canvas){
		RectF r;

		//name of robot
		float f = paint.measureText(text);
		paint.setColor(Color.BLACK);
		canvas.drawText(text, x-f/2, y+boden_hight+16, paint);
		
		if(selected){
			paint.setColor(mapView.getResources().getColor(R.color.android_blue));
			paint.setAlpha(180);
			int oval_scale = 15;
			r = new RectF(x-boden_width/2-oval_scale,y-boden_hight/2-oval_scale,x+boden_width/2+oval_scale,y+boden_hight/2+oval_scale);
			canvas.drawOval(r, paint);
			
			//draw robot as an icon
	        Bitmap lightYou =BitmapFactory.decodeResource(mapView.getResources(), R.drawable.ic_robo_youbot);
	        canvas.drawBitmap(lightYou, x - lightYou.getWidth()/2,y - lightYou.getHeight()/2, paint);
		}else{
			//draw the youbot
			paint.setColor(Color.BLACK);			
			//draw robot as a icon
	        Bitmap darkYou = BitmapFactory.decodeResource(mapView.getResources(), R.drawable.ic_robo_youbot_dark);
	        canvas.drawBitmap(darkYou, x - darkYou.getWidth()/2,y - darkYou.getHeight()/2, paint);
		}
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setPos(Point p){
		x = p.x;
		y = p.y+50;
	}
	
}
