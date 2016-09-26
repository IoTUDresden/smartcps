package eu.vicci.turtlebot.navigationapp.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ViewGroup;
import eu.vicci.driver.robot.location.Location;
import eu.vicci.driver.robot.location.UnnamedLocation;
import eu.vicci.turtlebot.navigationapp.helperclasses.CoordinatesTranslator;
import eu.vicci.turtlebot.navigationapp.helperclasses.Map;
import eu.vicci.turtlebot.navigationapp.helperclasses.RobotData;
import eu.vicci.turtlebot.navigationapp.helperclasses.RobotManager;


@SuppressLint("NewApi")
public class MiniMapView extends MapView {

	private Map map;
	private GestureDetector mDetector;

	private Paint paint = null;
	private Rect src_rect, dest_rect;
	private int display_height, display_width;

	private float mapOffsetX=0,mapOffsetY=0;
	//private float mapScale=1.5f;
	private float mapScale=1.5f;
	private float mapRotation = 0;
	
	private Rect src_rect1, dest_rect1;
	private ViewGroup container;
	
	/**
	 * contructor for map views in a smaller view
	 * @param context
	 * @param width
	 * @param height
	 */
	public MiniMapView(Context context, ViewGroup container) {
		super(context);
		// Paint
		paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);

		// what part of the map to draw
		src_rect = new Rect();
		dest_rect = new Rect();
		
		this.container = container;
		
		//display_height = container.getHeight();
		//display_width = container.getWidth();
		display_height = 300;
		display_width = 300;
		
		dest_rect.set(0, 0, display_width, display_height);
		setBackgroundColor(Color.BLUE);	
		
	}
	
	public void regiersterListener(OnTouchListener t, OnGestureListener gl,
		OnDoubleTapListener dtl) {
		//setOnTouchListener(t);
		//mDetector = new GestureDetector(this.getContext(), gl);
		//mDetector.setOnDoubleTapListener(dtl);
		//mDetector.setIsLongpressEnabled(true);
	}

	private Matrix mat;
	
	public void onDraw(Canvas canvas) {

		if(map!=null) {
			resetMatrixOfCanvas(canvas);
			canvas.drawBitmap(map.getBitmap(), src_rect1, dest_rect1, paint);
			//canvas.drawBitmap(map.getBitmap(), src_rect1, dest_rect1, paint);
		}
		drawRobots(canvas);
	}
	
	private void drawRobots(Canvas canvas){
		List<RobotData> robotList = RobotManager.getInstance().getRobotList();
		for(RobotData robot: robotList){
			switch(robot.getRobotType()){
			case Turtlebot:
				if(robot.isLocationValid())	drawTurtlebot(canvas,robot);
				if(robot.getTarget()!=null) drawTarget(canvas, robot.getTarget());
				break;
			case Youbot:
				if(robot.isLocationValid())	drawYoubot(canvas,robot);
				if(robot.getTarget()!=null) drawTarget(canvas, robot.getTarget());
				break;
			}
			resetMatrixOfCanvas(canvas);
		}
	}
	
	private void resetMatrixOfCanvas(Canvas canvas){
		//mat.setTranslate(getWidth()/2-mapOffsetX,  getHeight()/2-mapOffsetY);
		//mat.setTranslate(1100-mapOffsetX,  250-mapOffsetY);
		//mat.setTranslate(mapOffsetX,  mapOffsetY);
		//mat.preRotate(mapRotation);
		//mat.preScale(mapScale, mapScale);
		//canvas.setMatrix(mat);
	}
	
	private void drawTurtlebot(Canvas canvas,RobotData data) {
		UnnamedLocation robotPosition = data.getLocation();
		Point robotPositionOnBitmap = CoordinatesTranslator.worldToBitmapCoordinates(robotPosition);
		
		mat.postTranslate(-(map.getWidth()/2)*mapScale, (map.getHeight()/2)*mapScale);
		mat.postTranslate(robotPositionOnBitmap.x*mapScale, -robotPositionOnBitmap.y*mapScale);
		mat.preScale(1/mapScale, 1/mapScale);
		canvas.setMatrix(mat);
	
		TurtlebotDrawer turtle= new TurtlebotDrawer(this, data.getName(), new Point(0, 0), data.isSelected());		

		turtle.draw(canvas);
	}
	
	private void drawTarget(Canvas canvas, Location location){
		resetMatrixOfCanvas(canvas);
		Point locationOnBitmap = CoordinatesTranslator.worldToBitmapCoordinates(location);
		
		mat.postTranslate(-(map.getWidth()/2)*mapScale, (map.getHeight()/2)*mapScale);
		mat.postTranslate(locationOnBitmap.x*mapScale, -locationOnBitmap.y*mapScale);
		mat.preScale(1/mapScale, 1/mapScale);
		canvas.setMatrix(mat);
	
		Target t = new Target(this, 0, 0);
		t.draw(canvas);
	}
		
	private void drawYoubot(Canvas canvas, RobotData data){
		UnnamedLocation robotPosition = data.getLocation();
		Point robotPositionOnBitmap = CoordinatesTranslator.worldToBitmapCoordinates(robotPosition);
		
		mat.postTranslate(-(map.getWidth()/2)*mapScale, (map.getHeight()/2)*mapScale);
		mat.postTranslate(robotPositionOnBitmap.x*mapScale, -robotPositionOnBitmap.y*mapScale);
		mat.preScale(1/mapScale, 1/mapScale);
		canvas.setMatrix(mat);
		
		YoubotDrawer youbot= new YoubotDrawer(this, data.getName(), new Point(0, 0), data.isSelected());
		youbot.draw(canvas);
	}
	
	private Point getMiddleOfExploredMap(){
		Rect centerRect = findMapBoundingInBitmap();
		Point centerPoint = new Point(centerRect.centerX(),centerRect.centerY());
		return centerPoint;
	}

	
	private Rect findMapBoundingInBitmap() {
		int[] pixels = new int[map.getBitmap().getWidth()
				* map.getBitmap().getHeight()];
		int min_x = 0, max_x = 0, min_y = map.getBitmap().getWidth(), max_y = 0;
		map.getBitmap().getPixels(pixels, 0, map.getBitmap().getWidth(), 0, 0,
				map.getBitmap().getWidth(), map.getBitmap().getHeight());
		int i = 0;
		boolean x_min_found = false;
		for (int row = 0; row < map.getBitmap().getHeight(); row++) {
			for (int col = 0; col < map.getBitmap().getWidth(); col++) {
				if ((pixels[i] == Color.BLACK || pixels[i] == Color.WHITE)
						&& !x_min_found) {
					min_x = row;
					x_min_found = true;
				}
				if (pixels[i] == Color.BLACK || pixels[i] == Color.WHITE) {
					max_x = row;
					if (col < min_y)
						min_y = col;
					if (col > max_y)
						max_y = col;
				}
				i++;
			}
		}
		min_x *= 0.95;
		max_x *= 1.05;
		min_y *= 0.95;
		max_y *= 1.05;
		return new Rect(min_y, min_x, max_y, max_x);
	}

	public void setMapTranslation(float dx, float dy) {
		mapOffsetX += dx;
		mapOffsetY += dy;
	}
	
	public void addToMapScale(float ds) {
		mapScale += ds;
	}
	
	public float getMapRotation() {
		return mapRotation;
	}

	public void setMapRotation(float mapRotation) {
		this.mapRotation = mapRotation;
	}

	public float getMapScale() {
		return mapScale;
	}

	public void setMapScale(float mapScaleTo) {
			this.mapScale = mapScaleTo;	
	}

	public void initView(Map m) {
		this.map = m;
		src_rect = new Rect();
		Point centerOfMap = getMiddleOfExploredMap();
		
		src_rect.left = centerOfMap.x - map.getBitmap().getWidth()/2;  
		src_rect.right = centerOfMap.x + map.getBitmap().getWidth()/2;
		
		src_rect.top = centerOfMap.y - map.getBitmap().getHeight()/2;
		src_rect.bottom = centerOfMap.y + map.getBitmap().getHeight()/2;
		
		dest_rect.set(src_rect);
		
		mat = new Matrix();
		Bitmap bitmap = map.getBitmap();
		//src_rect1 = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		//dest_rect1 = new Rect(-bitmap.getWidth()/2,-bitmap.getHeight()/2,bitmap.getWidth()/2,bitmap.getHeight()/2);
		src_rect1 = new Rect(50, 130, 350, 400);
		dest_rect1 = new Rect(0, 0, 450, 450);
		
		invalidate();
	}
	
	public Point transformPoint(Point touchPoint){
		Matrix displayMatrix = new Matrix();
		displayMatrix.setTranslate(getWidth()/2-mapOffsetX,  getHeight()/2-mapOffsetY);
		displayMatrix.preScale(mapScale, -mapScale);
		displayMatrix.postTranslate(-(map.getWidth()/2)*mapScale, (map.getHeight()/2)*mapScale);
		
		Matrix inverse = new Matrix();
		if(displayMatrix.invert(inverse)){
			float[] touchPointArray = new float[] {touchPoint.x, touchPoint.y};
			inverse.mapPoints(touchPointArray);
			return new Point((int)touchPointArray[0],(int)touchPointArray[1]);
		} else {
			return null;
		}
	}

	public Rect getSrc_rect() {
		return src_rect;
	}

	public Rect getDest_rect() {
		return dest_rect;
	}

	public int getDisplay_height() {
		return display_height;
	}

	public int getDisplay_width() {
		return display_width;
	}
				
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//return mDetector.onTouchEvent(event);
		return true;
	}
	
}

