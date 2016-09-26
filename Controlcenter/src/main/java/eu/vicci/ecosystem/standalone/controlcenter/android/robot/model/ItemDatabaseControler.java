package eu.vicci.ecosystem.standalone.controlcenter.android.robot.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ItemDatabaseControler extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "items.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_ROBOTS = "robots"; //name of robot table
	public static final String TABLE_POIS = "pois"; //name of poi table
	public static final String TABLE_MAPS = "maps"; //name of map table
	
	
	//columns of the robot table
	public static final String ROBOT_NAME = "name";
	public static final String ROBOT_TYPE = "type";
	public static final String ROBOT_IP = "ip";
	public static final String ROBOT_PORT = "port";
	public static final String ROBOT_POSITION_X = "position_x";
	public static final String ROBOT_POSITION_Y = "position_y";
	public static final String ROBOT_POSITION_Z = "position_z";
	public static final String ROBOT_ORIENTATION_X = "orientation_x";
	public static final String ROBOT_ORIENTATION_Y = "orientation_y";
	public static final String ROBOT_ORIENTATION_Z = "orientation_z";
	public static final String ROBOT_ORIENTATION_W = "orientation_w";
	
	//columns of the poi table
	public static final String POI_NAME = "name";
	public static final String POI_DESCRIPTION = "description";
	public static final String POI_POSITION_X = "position_x";
	public static final String POI_POSITION_Y = "position_y";
	public static final String POI_POSITION_Z = "position_z";
	public static final String POI_ORIENTATION_X = "orientation_x";
	public static final String POI_ORIENTATION_Y = "orientation_y";
	public static final String POI_ORIENTATION_Z = "orientation_z";
	public static final String POI_ORIENTATION_W = "orientation_w";
	
	//columns of the map table
	public static final String MAP_NAME = "name";
	public static final String MAP_SOURCEDIR = "sourceDir";
	public static final String MAP_IP = "ip";
	public static final String MAP_PORT = "port";
	
	
	public static final String default_description = "InstanceOfDockingStation"; //default poi description for DockingStations
	
	
	public ItemDatabaseControler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//create poi table
		String create_robot_list = "CREATE TABLE "+TABLE_ROBOTS+"("
				+ROBOT_NAME+" TEXT,"
				+ROBOT_TYPE+" TEXT,"
				+ROBOT_IP+" TEXT,"
				+ROBOT_PORT+" TEXT,"
				+ROBOT_POSITION_X+" REAL,"
				+ROBOT_POSITION_Y+" REAL,"
				+ROBOT_POSITION_Z+" REAL,"
				+ROBOT_ORIENTATION_X+" REAL,"
				+ROBOT_ORIENTATION_Y+" REAL,"
				+ROBOT_ORIENTATION_Z+" REAL,"
				+ROBOT_ORIENTATION_W+" REAL);";
		db.execSQL(create_robot_list);
		
		//create poi table
		String create_poi_list = "CREATE TABLE "+TABLE_POIS+"("
				+POI_NAME+" TEXT,"
				+POI_DESCRIPTION+" TEXT,"
				+POI_POSITION_X+" REAL,"
				+POI_POSITION_Y+" REAL,"
				+POI_POSITION_Z+" REAL,"
				+POI_ORIENTATION_X+" REAL,"
				+POI_ORIENTATION_Y+" REAL,"
				+POI_ORIENTATION_Z+" REAL,"
				+POI_ORIENTATION_W+" REAL);";
		db.execSQL(create_poi_list);
		
		//create map table
		String create_map_list = "CREATE TABLE "+TABLE_MAPS+"("
				+MAP_NAME+" TEXT,"
				+MAP_SOURCEDIR+" TEXT,"
				+MAP_IP+" TEXT,"
				+MAP_PORT+" INTEGER);";
		db.execSQL(create_map_list);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_ROBOTS);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_POIS);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_MAPS);
		onCreate(db);
	}

}
