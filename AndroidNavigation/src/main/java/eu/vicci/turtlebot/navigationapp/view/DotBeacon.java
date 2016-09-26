package eu.vicci.turtlebot.navigationapp.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class DotBeacon {

	private int circleSize = 5;
	
	private boolean selected = false;

	private String text = "";
	private Paint paint = null;
	private float x, y;

	public DotBeacon(String name, float x, float y, boolean selected) {
		this.text = name;
		this.x = x;
		this.y = y;
		this.selected = selected;
		paint = new Paint();
		paint.setColor(Color.GRAY);

		// paint.setAlpha(200);
		paint.setAntiAlias(true);
		paint.setDither(true);
	}

	public void draw(Canvas canvas) {

		// Dot
		if (!selected) {
			paint.setColor(Color.DKGRAY);
		} else {
			paint.setColor(Color.RED);
		}

		canvas.drawCircle(x, y, circleSize, paint);

		// Text
		//float f = paint.measureText(text);
		float text_height = Math.abs(paint.getFontMetrics().top - paint.getFontMetrics().leading)-2;
		paint.setColor(Color.WHITE);
		paint.setColor(Color.BLACK);
		canvas.drawText(text, x+circleSize + 3, y+text_height/2, paint);

	}

}
