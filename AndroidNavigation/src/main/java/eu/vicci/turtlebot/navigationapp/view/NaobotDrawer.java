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

public class NaobotDrawer {

	private String text = "";
	private Paint paint = null;
	private float x,y;
	private boolean selected = false;
	private View mapView;
	
	private final float boden_hight = 15;
	private final float boden_width = 25;
	
	public NaobotDrawer(View v, String name,Point p, boolean selected){
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

		//text
		float f = paint.measureText(text);
		//if(selected) {paint.setColor(Color.RED); paint.setAlpha(180);} else paint.setColor(Color.WHITE);
		paint.setColor(Color.WHITE);
		canvas.drawRect((x-f/2)-2, y+boden_hight/2-2+3, (x+f/2)+2, y+boden_hight+2+3, paint);
		paint.setColor(Color.BLACK);
		canvas.drawText(text, x-f/2, y+boden_hight+4, paint);
		
		if(selected){
			paint.setColor(Color.RED);
			paint.setAlpha(180);
			int oval_scale = 5;
			r = new RectF(x-boden_width/2-oval_scale,y-boden_hight/2-oval_scale,x+boden_width/2+oval_scale,y+boden_hight/2+oval_scale);
			canvas.drawRect(r, paint);
		}	
		
		/*
		//naobotuntergrund
		paint.setColor(Color.GRAY);

		r = new RectF(x-boden_width/2,y-boden_hight/2,x+boden_width/2,y+boden_hight/2);
		canvas.drawRect(r, paint);
		*/
		
		//draw the naobot
		paint.setColor(Color.BLACK);			
		//draw robot as an icon
		Bitmap darkNao = BitmapFactory.decodeResource(mapView.getResources(), R.drawable.ic_robo_naobot_dark);
		canvas.drawBitmap(darkNao, x - darkNao.getWidth()/2,y - darkNao.getHeight()/2, paint);
		

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
