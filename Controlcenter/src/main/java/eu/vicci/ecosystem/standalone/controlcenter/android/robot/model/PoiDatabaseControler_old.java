package eu.vicci.ecosystem.standalone.controlcenter.android.robot.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class PoiDatabaseControler_old extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "pois.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_POIS = "pois"; //name of table
	
	//columns of the database
	public static final String POI_NAME = "name";
	public static final String POI_DESCRIPTION = "description";
	public static final String POI_POSITION_X = "position_x";
	public static final String POI_POSITION_Y = "position_y";
	public static final String POI_POSITION_Z = "position_z";
	public static final String POI_ORIENTATION_X = "orientation_x";
	public static final String POI_ORIENTATION_Y = "orientation_y";
	public static final String POI_ORIENTATION_Z = "orientation_z";
	public static final String POI_ORIENTATION_W = "orientation_w";
	
	public static final String default_description = "InstanceOfDockingStation"; //default poi description for DockingStations
	
	
	public PoiDatabaseControler_old(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String create_db = "CREATE TABLE "+TABLE_POIS+"("
				+POI_NAME+" TEXT,"
				+POI_DESCRIPTION+" TEXT,"
				+POI_POSITION_X+" REAL,"
				+POI_POSITION_Y+" REAL,"
				+POI_POSITION_Z+" REAL,"
				+POI_ORIENTATION_X+" REAL,"
				+POI_ORIENTATION_Y+" REAL,"
				+POI_ORIENTATION_Z+" REAL,"
				+POI_ORIENTATION_W+" REAL);";
		db.execSQL(create_db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_POIS);
		onCreate(db);
	}

}
