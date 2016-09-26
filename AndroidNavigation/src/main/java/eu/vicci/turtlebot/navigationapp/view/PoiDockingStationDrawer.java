package eu.vicci.turtlebot.navigationapp.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;
import eu.vicci.robot.android.navigation.R;

public class PoiDockingStationDrawer {

	private View mapView;
	private String text = "";
	private Paint paint = null;
	private float x,y;
	
	private final float boden_hight = 15;
	private final float boden_width = 25;
	
	public PoiDockingStationDrawer(View v, String name, Point p){
	    this.mapView = v;  
		this.text = name;
	    this.x = p.x;
	    this.y = p.y+50;
	    paint = new Paint();
	    paint.setColor(Color.GRAY);
	      
	    //paint.setAlpha(200);
	    paint.setAntiAlias(true);
	    paint.setDither(true);
	}
	
	public void draw(Canvas canvas){
		//draw text
		float f = paint.measureText(text);
		paint.setColor(Color.BLACK);
		canvas.drawText(text, x-f/2, y+boden_hight+4, paint);
		
		//draw poi icon background
		//paint.setColor(Color.GRAY);
		//r = new RectF(x-boden_width/2,y-boden_hight/2,x+boden_width/2,y+boden_hight/2);
		//canvas.drawRect(r, paint);
		
		//draw icon
		Bitmap darkPoi = BitmapFactory.decodeResource(mapView.getResources(), R.drawable.ic_poi_loadingstation_dark);
		//canvas.drawBitmap(darkPoi, x - darkPoi.getWidth()/2,y - darkPoi.getHeight()/2, paint);
		canvas.drawBitmap(darkPoi, x - darkPoi.getWidth()/2,y - darkPoi.getHeight(), paint);
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public void setPos(Point p){
		x = p.x;
		y = p.y+50;
	}
	
}