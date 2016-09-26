package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import eu.vicci.ecosystem.standalone.controlcenter.android.util.webcam.WebcamStreamListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WebcamView extends SurfaceView implements SurfaceHolder.Callback,
		WebcamStreamListener {

	private SurfaceHolder holder;

	public WebcamView(Context context) {
		super(context);
		initHolder();
		
	}
	private void initScreen(){
		Canvas canvas = holder.lockCanvas();
		if(canvas!=null){
			Paint grayPaint = new Paint();
			grayPaint.setColor(Color.GRAY);
			canvas.drawPaint(grayPaint);
			holder.unlockCanvasAndPost(canvas);
		}
	}
	public WebcamView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHolder();
	}

	public WebcamView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHolder();
	}

	private void initHolder() {
		holder = getHolder();
		holder.addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBitmapReceived(Bitmap bitmap) {
		Canvas canvas = holder.lockCanvas();
		if(canvas!=null){
			Rect src_rect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
			Rect dest_rect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
			canvas.drawBitmap(bitmap, src_rect, dest_rect, null);
			holder.unlockCanvasAndPost(canvas);
		}
	}


	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		initScreen();
	}

}
