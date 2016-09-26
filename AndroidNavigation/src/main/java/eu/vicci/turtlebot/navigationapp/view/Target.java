package eu.vicci.turtlebot.navigationapp.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import eu.vicci.robot.android.navigation.R;

public class Target {

		//private static final int CROSS_SIZE = 20;
	
	   private int x;
	   private int y;
	   private View mapView;

	   private Paint paint = null;
	
	   public Target(View v, int x, int y)
	   {	
		  this.mapView = v; 
	      this.x = x;
	      this.y = y+50;

	      paint = new Paint();
	      paint.setColor(Color.BLACK);
	      /*paintBrushRed.setAlpha(200);
	      paintBrushRed.setAntiAlias(true);
	      paintBrushRed.setDither(true);
	      paintBrushRed.setStrokeWidth(10);*/
	   }
	   
	
	   public void draw(Canvas canvas)
	   {
		  /*int c = CROSS_SIZE/2;
		  canvas.drawLine(x-c, y-c, x+c, y+c, paintBrushRed);
		  canvas.drawLine(x-c, y+c, x+c, y-c, paintBrushRed);*/
		  
		//draw target as a flag icon

	    Bitmap lightTurtle =BitmapFactory.decodeResource(mapView.getResources(), R.drawable.ic_poi_default_flag_dark);
	    canvas.drawBitmap(lightTurtle, x - lightTurtle.getWidth()/2,y - lightTurtle.getHeight()/2, paint);
	   }
	
	   public void setPos(int x, int y){
		   this.x = x;
		   this.y = y+50;
	   }
	
}
