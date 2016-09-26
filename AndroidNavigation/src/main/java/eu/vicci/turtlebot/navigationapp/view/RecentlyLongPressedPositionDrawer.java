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

public class RecentlyLongPressedPositionDrawer {

	private View mapView;
	private Paint paint = null;
	private float x,y;
	
	private final float boden_hight = 15;
	private final float boden_width = 25;
	
	public RecentlyLongPressedPositionDrawer(View v, Point p){
		this.mapView = v;
		this.x = p.x; //to be evaluated
	    this.y = p.y+70; //to be evaluated
		paint = new Paint();
	    paint.setColor(Color.GRAY);
	      
	    // paint.setAlpha(200);
	    paint.setAntiAlias(true);
	    paint.setDither(true);
	}
	
	public void draw(Canvas canvas){
		RectF r;
		
		//poi general background
		//paint.setColor(Color.GRAY);
		//r = new RectF(x-boden_width/2,y-boden_hight/2,x+boden_width/2,y+boden_hight/2);
		//canvas.drawRect(r, paint);
		
		//draw icon
	    Bitmap darkPoi = BitmapFactory.decodeResource(mapView.getResources(), R.drawable.ic_location_dark);
	    //canvas.drawBitmap(darkPoi, x - darkPoi.getWidth()/2,y - darkPoi.getHeight()/2, paint);
	    canvas.drawBitmap(darkPoi, x - darkPoi.getWidth()/2,y - darkPoi.getHeight(), paint);
	}
	
	public void setPos(Point p){
		x = p.x;
		y = p.y+70;
	}
	
}
