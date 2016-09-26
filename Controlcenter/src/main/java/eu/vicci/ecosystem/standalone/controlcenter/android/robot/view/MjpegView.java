package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import java.io.IOException;

//import eu.vicci.turtlebot.controlapp.logic.RobotClient;
//import eu.vicci.turtlebot.controlapp.logic.RobotClientManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MjpegView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "MjpegView";

	public final static int POSITION_UPPER_LEFT = 9;
	public final static int POSITION_UPPER_RIGHT = 3;
	public final static int POSITION_LOWER_LEFT = 12;
	public final static int POSITION_LOWER_RIGHT = 6;

	public final static int SIZE_STANDARD = 1;
	public final static int SIZE_BEST_FIT = 4;
	public final static int SIZE_FULLSCREEN = 8;

	private MjpegViewThread thread;
	private MjpegInputStream mIn = null;
	private boolean showFps = false;
	private boolean mRun = false;
	private boolean surfaceDone = false;
	private Paint overlayPaint;
	private int overlayTextColor;
	private int overlayBackgroundColor;
	private int ovlPos;
	private int dispWidth;
	private int dispHeight;
	private int displayMode;
	private boolean videoEnable;

	public class MjpegViewThread extends Thread {
		private SurfaceHolder mSurfaceHolder;
		private int frameCounter = 0;
		private long start;
		private Bitmap ovl;
		private Bitmap ovl_button;

		public MjpegViewThread(SurfaceHolder surfaceHolder, Context context) {
			mSurfaceHolder = surfaceHolder;
		}

		private Rect destRect(int bmw, int bmh) {
			int tempx;
			int tempy;
			if (displayMode == MjpegView.SIZE_STANDARD) {
				tempx = (dispWidth / 2) - (bmw / 2);
				tempy = (dispHeight / 2) - (bmh / 2);
				return new Rect(tempx, tempy, bmw + tempx, bmh + tempy);
			}
			if (displayMode == MjpegView.SIZE_BEST_FIT) {
				float bmasp = (float) bmw / (float) bmh;
				bmw = dispWidth;
				bmh = (int) (dispWidth / bmasp);
				if (bmh > dispHeight) {
					bmh = dispHeight;
					bmw = (int) (dispHeight * bmasp);
				}
				tempx = (dispWidth / 2) - (bmw / 2);
				tempy = (dispHeight / 2) - (bmh / 2);
				return new Rect(tempx, tempy, bmw + tempx, bmh + tempy);
			}
			if (displayMode == MjpegView.SIZE_FULLSCREEN) {
				return new Rect(0, 0, dispWidth, dispHeight);
			}
			return null;
		}

		public void setSurfaceSize(int width, int height) {
			synchronized (mSurfaceHolder) {
				dispWidth = width;
				dispHeight = height;
			}
		}

		private Bitmap makeFpsOverlay(Paint p, String text) {
			//RobotClient rc = RobotClientManager.getInstance().getRobot();
			int turtle_battery = 0;//rc.getBatteryCharge();
			int laptop_battery = 0;//rc.getLaptopBatteryCharge();
			// text = "Bumper: "+rc.getBumper_left()+" " + rc.getBumper_center()
			// + " " + rc.getBumper_right()+ ", ";
			text = " TurtleBot: " + Integer.toString(turtle_battery) + "%, Laptop: " + Integer.toString(laptop_battery)
					+ "% ";
			Rect b = new Rect();
			p.getTextBounds(text, 0, text.length(), b);
			int bwidth = b.width() + 2;
			int bheight = b.height() + 2;
			Bitmap bm = Bitmap.createBitmap(bwidth, bheight, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(bm);
			p.setColor(overlayBackgroundColor);
			c.drawRect(0, 0, bwidth, bheight, p);
			p.setColor(overlayTextColor);
			c.drawText(text, -b.left + 1, (bheight / 2) - ((p.ascent() + p.descent()) / 2) + 1, p);
			return bm;
		}

		private Bitmap makeButtonOverlay(Paint p) {
			Bitmap bm = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);

			Canvas c = new Canvas(bm);
			p.setColor(Color.rgb(219, 4, 4));
			c.drawCircle(75, 75, 40, p);
			p.setColor(Color.WHITE);
			c.drawText("DISCONNECT", 40, 80, p);
			// c.drawRect(0, 0, 100, 100, p);

			// Picture picture = new Picture(R.drawable.);
			// c.drawPicture(picture);
			return bm;
		}

		private Bitmap makeAutodockButtonOverlay(Paint p) {
			//RobotClient rc = RobotClientManager.getInstance().getRobot();
			Bitmap bm = Bitmap.createBitmap(300, 90, Bitmap.Config.ARGB_8888);

			Canvas c = new Canvas(bm);
			p.setColor(Color.rgb(110, 110, 110));
			c.drawRect(0, 0, 300, 90, p);
			p.setColor(Color.WHITE);
			float temp = p.getTextSize();
			p.setTextSize(50);

//			if (rc.isDocking()) {
//				c.drawText("cancel", 72, 64, p);
//			} else {
//				c.drawText("Autodock", 42, 64, p);
//			}
			// c.drawRect(0, 0, 100, 100, p);
			p.setTextSize(temp);
			// Picture picture = new Picture(R.drawable.);
			// c.drawPicture(picture);
			return bm;
		}

		public void run() {

			Log.d("#justrobothings","run!");
			start = System.currentTimeMillis();
			PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.DST_OVER);
			Bitmap bm;
			int width;
			int height;
			Rect destRect;
			Canvas c = null;
			Paint p = new Paint();
			String fps;

			while (videoEnable && mRun) {
				if (surfaceDone) {
					try {
						c = mSurfaceHolder.lockCanvas();
						if (c != null) {
							//synchronized (mSurfaceHolder) {
								try {
									bm = mIn.readMjpegFrame();
									destRect = destRect(bm.getWidth(), bm.getHeight());
									c.drawColor(Color.WHITE);
									c.drawBitmap(bm, null, destRect, p);
									if (showFps) {
										p.setXfermode(mode);
										bm = mIn.readMjpegFrame();
										destRect = destRect(bm.getWidth(), bm.getHeight());
										p.setXfermode(null);
										frameCounter++;
										if ((System.currentTimeMillis() - start) >= 1000) {
											fps = String.valueOf(frameCounter) + " fps";
											frameCounter = 0;
											start = System.currentTimeMillis();
										}
									}
								} catch (IOException e) {
									e.getStackTrace();
									Log.d(TAG, "catch IOException hit in run", e);
								}
							//}
						}
					} finally {
						if (c != null) {
							mSurfaceHolder.unlockCanvasAndPost(c);
						}
					}
				}
			}
			while (!videoEnable && mRun) {
				if (surfaceDone) {
					try {
						c = mSurfaceHolder.lockCanvas();
						if (c != null) {
							synchronized (mSurfaceHolder) {
								bm = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
								bm.eraseColor(Color.rgb(158, 158, 158));
								destRect = destRect(bm.getWidth(), bm.getHeight());
								c.drawColor(Color.rgb(158, 158, 158));
								c.drawBitmap(bm, null, destRect, p);
								if (showFps) {
									p.setXfermode(mode);
									bm = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
									bm.eraseColor(Color.rgb(158, 158, 158));
									destRect = destRect(bm.getWidth(), bm.getHeight());
									if (ovl != null) {
										height = ((ovlPos & 1) == 1) ? destRect.top : destRect.bottom - ovl.getHeight();
										width = ((ovlPos & 8) == 8) ? destRect.left : destRect.right - ovl.getWidth();
										//c.drawBitmap(ovl, width, height, null);
										//ovl_button = makeButtonOverlay(overlayPaint);
										//c.drawBitmap(ovl_button, destRect.right - ovl_button.getWidth(),
										//		destRect.bottom - ovl_button.getHeight(), null);
										//c.drawBitmap(ovl_button, destRect.left,
										//		destRect.bottom - ovl_button.getHeight(), null);
//										RobotClient rc = RobotClientManager.getInstance().getRobot();
//										if (rc.isDockNear() || rc.isDocking()) {
//											Bitmap ovl_auto_docking_button = makeAutodockButtonOverlay(overlayPaint);
//											// in die Mitte zeichnen
//											c.drawBitmap(
//													ovl_auto_docking_button,
//													((destRect.right + destRect.left) / 2)
//															- ovl_auto_docking_button.getWidth() / 2,
//													destRect.top + 50, null);
//										}

									}
									p.setXfermode(null);
									frameCounter++;
									if ((System.currentTimeMillis() - start) >= 1000) {
										fps = String.valueOf(frameCounter) + " fps";
										frameCounter = 0;
										start = System.currentTimeMillis();
										ovl = makeFpsOverlay(overlayPaint, fps);
									}
								}
							}
						}
					} finally {
						if (c != null) {
							mSurfaceHolder.unlockCanvasAndPost(c);
						}
					}
				}
			}

		}

	}

	private void init(Context context) {
		Log.d("#robot","init context");
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		thread = new MjpegViewThread(holder, context);
		setFocusable(true);
		/*
		overlayPaint = new Paint();
		overlayPaint.setTextAlign(Paint.Align.LEFT);
		overlayPaint.setTextSize(12);
		overlayPaint.setTypeface(Typeface.DEFAULT);
		overlayTextColor = Color.WHITE;
		overlayBackgroundColor = Color.BLACK;
		ovlPos = MjpegView.POSITION_LOWER_RIGHT;
		*/
		displayMode = MjpegView.SIZE_STANDARD;
		dispWidth = getWidth();
		dispHeight = getHeight();
	}

	public void startPlayback() {
		Log.d("#robot","start playback");
		// boolean video = RobotClientManager.getInstance().isVideoSupportWanted();
		// if (video==true&&mIn==null) video = false;
		if (mIn != null && !thread.isAlive()) {
			mRun = true;
			videoEnable = true;
			thread.start();
			return;
		} /*else if (mIn == null && !thread.isAlive() && !video) {
			mRun = true;
			videoEnable = false;
			thread.start();
		}*/
	}

	public MjpegViewThread getThread() {
		return thread;
	}

	// private String error = "";

	public final void timerAlert() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				synchronized (this) {
					// error = "";
				}
			}
		}, 5000);
	}

	public void stopPlayback() {
		mRun = false;
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public MjpegView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void surfaceChanged(SurfaceHolder holder, int f, int w, int h) {
		thread.setSurfaceSize(w, h);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		surfaceDone = false;
		stopPlayback();
	}

	public MjpegView(Context context) {
		super(context);
		Log.d("#justrobotthings", "called constructor");
		init(context);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		surfaceDone = true;
	}

	public void showFps(boolean b) {
		showFps = b;
	}

	public void setSource(MjpegInputStream source) {
		mIn = source;
		startPlayback();
	}

	public void setOverlayPaint(Paint p) {
		overlayPaint = p;
	}

	public void setOverlayTextColor(int c) {
		overlayTextColor = c;
	}

	public void setOverlayBackgroundColor(int c) {
		overlayBackgroundColor = c;
	}

	public void setOverlayPosition(int p) {
		ovlPos = p;
	}

	public void setDisplayMode(int s) {
		displayMode = s;
	}

}