package eu.vicci.turtlebot.navigationapp.helperclasses;

import android.graphics.Bitmap;
import eu.vicci.driver.robot.location.Location;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

public class Map {
	
	//private static final String TAG = Map.class.getSimpleName();
	
	private double x,y,z;
	private double o_x,o_y,o_z,o_w;
	private int width,height;
	private double resolution;
	
	private Bitmap bitmap;
	
	public Map(Bitmap bitmap, double x, double y, double z, double o_x, double o_y, double o_z, double o_w, int width, int height, double resolution){
		this.x = x;
		this.y = y;
		this.z = z;
		this.o_x = o_x;
		this.o_y = o_y;
		this.o_z = o_z;
		this.o_w = o_w;
		this.width = width;
		this.height = height;
		this.resolution = resolution;
		this.bitmap = bitmap;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double getO_x() {
		return o_x;
	}

	public double getO_y() {
		return o_y;
	}

	public double getO_z() {
		return o_z;
	}

	public double getO_w() {
		return o_w;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public double getResolution() {
		return resolution;
	}

	public Location getLocation(){
		Position p = new Position(x,y);
		Orientation o = new Orientation(o_z, o_w);
		return new Location(p,o) { };
	}
	
	public String toString(){
		String map_val = (bitmap==null)?"invalid":width+" x "+height;
		return "C:("+x+";"+y+";"+z+"), O:("+o_x+";"+o_y+";"+o_z+";"+o_w+"), Map: "+map_val+", Res: "+resolution;
	}

}
