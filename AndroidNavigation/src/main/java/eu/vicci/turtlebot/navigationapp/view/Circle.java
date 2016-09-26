package eu.vicci.turtlebot.navigationapp.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


// Will be used for Drawing Robots
public class Circle {

	   private int x;
	   private int y;
	   private int radius;

	   private Paint paintBrushBlue = null;
	   private Paint paintBrushGreen = null;

	   public Circle(int x, int y)
	   {
	      this.x = x;
	      this.y = y;
	      this.radius = 30;

	      paintBrushBlue = new Paint();
	      paintBrushBlue.setColor(Color.BLACK);
	      paintBrushBlue.setAlpha(200);

	      paintBrushGreen = new Paint();
	      paintBrushGreen.setColor(Color.CYAN);

	      paintBrushBlue.setAntiAlias(true);
	      paintBrushBlue.setDither(true);
	      paintBrushGreen.setAntiAlias(true);
	      paintBrushGreen.setDither(true);
	   }

	   public void draw(Canvas canvas)
	   {
	      canvas.drawCircle(x, y, radius, paintBrushBlue);
	      canvas.drawCircle(x, y, radius/2, paintBrushGreen);
	   }
	
	
	
}
