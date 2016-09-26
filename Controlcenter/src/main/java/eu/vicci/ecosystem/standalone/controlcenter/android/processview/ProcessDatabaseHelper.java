package eu.vicci.ecosystem.standalone.controlcenter.android.processview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProcessDatabaseHelper extends SQLiteOpenHelper {

  public static final String TABLE_PROCESSES = "processes";
  public static final String TABLE_INSTANCES = "instances";
  public static final String TABLE_PORTS = "ports";
  public static final String COLUMN_KEY = "_key";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_DESCRIPTION = "description";
  public static final String COLUMN_TYPE = "type";
  public static final String COLUMN_STATE = "state";

  private static final String DATABASE_NAME = "processt1db.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String CREATE_TABLE_PROCESS = "create table "
      + TABLE_PROCESSES + "(" + COLUMN_KEY
      + " integer primary key autoincrement, " + COLUMN_ID
      + " text not null, " + COLUMN_NAME
      + " text not null, " + COLUMN_TYPE
      + " text not null, " + COLUMN_DESCRIPTION
      + " text not null);";
  
  private static final String CREATE_TABLE_INSTANCE = "create table "
	      + TABLE_INSTANCES + "(" + COLUMN_KEY
	      + " integer primary key autoincrement, " + COLUMN_ID
	      + " text not null, " + COLUMN_NAME
	      + " text not null, " + COLUMN_TYPE
	      + " text not null, " + COLUMN_DESCRIPTION
	      + " text not null, " + COLUMN_STATE
	      + " text not null);";
  
  private static final String CREATE_TABLE_PORTS = "create table "
	      + TABLE_PORTS + "(" + COLUMN_KEY
	      + " integer primary key autoincrement, " + COLUMN_ID
	      + " text not null, " + COLUMN_NAME
	      + " text not null, " + COLUMN_TYPE
	      + " text not null);";

  public ProcessDatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
	  database.execSQL(CREATE_TABLE_INSTANCE);
	  database.execSQL(CREATE_TABLE_PROCESS);
	  database.execSQL(CREATE_TABLE_PORTS);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//    Log.w(ProcessDataBaseHelper.class.getName(),
//        "Upgrading database from version " + oldVersion + " to "
//            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROCESSES);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTANCES);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PORTS);
    onCreate(db);
  }

} 