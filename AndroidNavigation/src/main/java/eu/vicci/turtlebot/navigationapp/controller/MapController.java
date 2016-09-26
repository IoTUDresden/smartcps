package eu.vicci.turtlebot.navigationapp.controller;


import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import android.view.View.OnTouchListener;
import eu.vicci.driver.robot.exception.CannotMoveToMovementTargetException;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.location.Location;
import eu.vicci.driver.turtlebot.TurtleBot;
import eu.vicci.turtlebot.navigationapp.helperclasses.CoordinatesTranslator;
import eu.vicci.turtlebot.navigationapp.helperclasses.Map;
import eu.vicci.turtlebot.navigationapp.helperclasses.RobotData;
import eu.vicci.turtlebot.navigationapp.helperclasses.RobotManager;
import eu.vicci.turtlebot.navigationapp.model.TurtlebotModel;
import eu.vicci.turtlebot.navigationapp.model.YoubotModel;
import eu.vicci.turtlebot.navigationapp.view.MapView;


@SuppressLint("NewApi")
public class MapController implements OnTouchListener , OnGestureListener, OnDoubleTapListener {

	Activity mainActivity;
	MapView mapView;
	
	private static Location recently_long_pressed_location;
	private static boolean recently_long_pressed_location_set = false;
	
	private static String recently_selected_robot_name = "";
	private static Boolean recently_selected_robot_name_set = false;
	
	
	//private static final String TAG = MapController.class.getSimpleName();
	
	public MapController(Activity robotActivity, MapView view){
		this.mapView = view;
		this.mainActivity = robotActivity;
		
		Map map = RobotManager.getInstance().getMap();
		if(map != null){
			mapView.initView(map);
			mapView.regiersterListener(this,this,this);
		}
		List<RobotData> robotList = RobotManager.getInstance().getRobotList();
		
		for(RobotData robot : robotList){
			switch(robot.getRobotType()){
			case Turtlebot:
				TurtlebotModel turtle = new TurtlebotModel(robot);
				turtle.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
				while(turtle.getTurtle()==null){
					Thread.yield();
				}
				robot.setRobot(turtle.getTurtle());
				break;
			case Youbot:
				YoubotModel youbot = new YoubotModel(robot);
				youbot.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
				while(youbot.getYoubot()==null){
					Thread.yield();
				}
				robot.setRobot(youbot.getYoubot());
				System.out.println("set Robot: ");
				break;
			}
		}	

	}

	float oldDistance=0;
	float originalScale = 0;
	PointF oldVector;
	float originalRotation = 0;
	private Object context;
	
	@Override
	public boolean onTouch(View v, MotionEvent e) {
		if(e.getPointerCount()==2){
			PointerCoords point1 = new PointerCoords();
			PointerCoords point2 = new PointerCoords();
			
			e.getPointerCoords(0, point1);
			e.getPointerCoords(1, point2);
			
			PointF vector = new PointF(point1.x-point2.x, point1.y-point2.y);
			
			float distance = (float)Math.sqrt(vector.x*vector.x+vector.y+vector.y);
			if(oldDistance!=0){
				mapView.setMapScale(originalScale + (distance-oldDistance)*0.01f);			
				//mapView.setRotation(originalRotation+ getAngleBetweenToVectors(oldVector,vector));
				//System.out.println("Angle = " +getAngleBetweenToVectors(oldVector,vector));
			} else {
				oldDistance = distance;
				originalScale = mapView.getMapScale();
				oldVector = new PointF(vector.x,vector.y);
				originalRotation = mapView.getMapRotation();  
			}
			mapView.invalidate();
			return true;
		}
		oldDistance = 0;
		return false;
	}
	
//	private float getAngleBetweenToVectors(PointF vec1, PointF vec2){
//		float norm = (float)(Math.sqrt(vec1.x*vec1.x+vec1.y*vec1.y)*Math.sqrt(vec2.x*vec2.x+vec2.y*vec2.y));
//		if(norm < 0.01) return 0;
//		return (float)(Math.acos((vec1.x*vec2.x+vec1.y*vec2.y)/norm)*(180/Math.PI));
//	}

	@Override
	public boolean onDown(MotionEvent event) {
		return true;
	}


	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return true;
	}


	@Override
	public void onLongPress(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		
		Point touchPoint = new Point((int)x,(int)y);
		
		recently_long_pressed_location = CoordinatesTranslator.transScreenToWorld(touchPoint, mapView);
		recently_long_pressed_location_set = true;
		// Vibrate for 500 milliseconds
		Vibrator v = (Vibrator) mainActivity.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(50);
	}


	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1,  float distanceX, float distanceY) {
		mapView.setMapTranslation(distanceX,distanceY);
		return true;
	}


	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	TurtleBot turtle;

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		
		Point touchPoint = new Point((int)x,(int)y);
		
		Point pointInBitmapCoordinates = mapView.transformPoint(touchPoint);
				
		boolean isRobotSelected = RobotManager.getInstance().isARobotSelected();
		
		if(isRobotSelected){
			Location target = CoordinatesTranslator.transScreenToWorld(touchPoint, mapView);
			try {
				RobotManager.getInstance().getSelectedRobot().setTarget(target);
				RobotManager.getInstance().getSelectedRobot().getRobot().moveToNonblocking(target);
			} catch (CannotMoveToMovementTargetException e1) {
				e1.printStackTrace();
			} catch (NotConnectedException e1) {
				e1.printStackTrace();
			}
		} else {
			//recently_tapped_location = CoordinatesTranslator.transScreenToWorld(touchPoint, mapView);
			//recently_tapped_location_set = true;
		}
		RobotManager.getInstance().setAllUnselected();
		RobotManager.getInstance().selectRobot(pointInBitmapCoordinates);
		return false;
	}


	@Override
	public boolean onDoubleTap(MotionEvent arg0) {
		RobotManager.getInstance().setAllUnselected();
		return false;
	}


	@Override
	public boolean onDoubleTapEvent(MotionEvent arg0) {
		return false;
	}


	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return false;
	}
	
	//method to get recently tapped location
	public static Location getRecentlyLongPressedLocation() {
		return recently_long_pressed_location;
	}
	
	//method to set recently tapped location
	public static void setRecentlyLongPressedLocation(Location location) {
		recently_long_pressed_location = location;
	}
	
	//method to get the state of recently_tapped_location_set
	public static boolean getRecentlyLongPressedLocationSet() {
		return recently_long_pressed_location_set;
	}
	
	//method to set the state of recently_tapped_location_set
	public static void setRecentlyLongPressedLocationSet(Boolean set) {
		recently_long_pressed_location_set = set;
	}
	
	//method to get the name of the recently selected robot
	public static String getRecentlySelectedRobotName() {
		return recently_selected_robot_name;
	}
	
	//method to get the name of the recently selected robot
	public static void setRecentlySelectedRobotName(String name) {
		recently_selected_robot_name = name;
	}
	
	//method to get the state of recently_selected_robot_name_set
	public static boolean getRecentlySelectedRobotNameSet() {
		return recently_selected_robot_name_set;
	}
		
	//method to set the state of recently_selected_robot_name_set
	public static void setRecentlySelectedRobotNameSet(Boolean set) {
		recently_selected_robot_name_set = set;
	}
	
}
