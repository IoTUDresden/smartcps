package eu.vicci.turtlebot.navigationapp.helperclasses;

import android.graphics.Point;
import eu.vicci.driver.robot.location.Location;
import eu.vicci.driver.robot.location.UnnamedLocation;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;
import eu.vicci.turtlebot.navigationapp.view.MapView;

public class CoordinatesTranslator {
	
	//private static final String TAG = CoordinatesTranslator.class.getSimpleName();
	
	
	public static Location transScreenToWorld(Point p,MapView view) {
		Point bitmap_point = view.transformPoint(p);
		Location c = bitmapToRelativeWorldCoordinates(bitmap_point);
		return c;
	}
	
	public static Point worldToBitmapCoordinates(Location loc){
		if(loc==null) return null;
		Point center = get00CoordinateOfMap();
		Position locationPosition = loc.getPosition();
		Map map = RobotManager.getInstance().getMap();
		Point offsetToCenter = new Point((int)(locationPosition.getX()/map.getResolution()),(int)(locationPosition.getY()/map.getResolution()));
		center.offset(offsetToCenter.x, offsetToCenter.y);
		return center;
	}
	
	
	private static Point get00CoordinateOfMap(){
		Point p = new Point();
		Map map = RobotManager.getInstance().getMap();
		double resolution = map.getResolution();
		p.x = (int)Math.abs((map.getX()) / resolution);
		p.y = (int)Math.abs((map.getY()) / resolution);
		return p;
	}
	
	public static UnnamedLocation bitmapToRelativeWorldCoordinates(Point p){
		double resolution = RobotManager.getInstance().getMap().getResolution();
		// origin
		Point origin = get00CoordinateOfMap();
		int o_x = origin.x;   
		int o_y = origin.y;
		
		//target
		int t_x = p.x;
		int t_y = p.y;
		
		//difference
		double delta_x = t_x - o_x;
		double delta_y = t_y - o_y;
		
		Position pos = new Position(delta_x*resolution,delta_y*resolution);
		Orientation o = new Orientation(0, 1);
		UnnamedLocation l = new UnnamedLocation(pos,o);
		return l;
	}
	
	
}
