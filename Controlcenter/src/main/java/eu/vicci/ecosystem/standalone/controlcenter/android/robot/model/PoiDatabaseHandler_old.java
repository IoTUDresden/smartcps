package eu.vicci.ecosystem.standalone.controlcenter.android.robot.model;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import eu.vicci.driver.robot.location.NamedLocation;
import eu.vicci.driver.robot.location.Site;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;
import eu.vicci.driver.turtlebot.location.TurtleBotDockingStation;


public class PoiDatabaseHandler_old {
	
	private SQLiteDatabase database;
	private PoiDatabaseControler_old db_controler;
	
	//Array with all column names of TABLE_POIS
	private String[] all_columns = {
			PoiDatabaseControler_old.POI_NAME,
			PoiDatabaseControler_old.POI_DESCRIPTION,
			PoiDatabaseControler_old.POI_POSITION_X,
			PoiDatabaseControler_old.POI_POSITION_Y,
			PoiDatabaseControler_old.POI_POSITION_Z,
			PoiDatabaseControler_old.POI_ORIENTATION_X,
			PoiDatabaseControler_old.POI_ORIENTATION_Y,
			PoiDatabaseControler_old.POI_ORIENTATION_Z,
			PoiDatabaseControler_old.POI_ORIENTATION_W};
	
	
	public PoiDatabaseHandler_old(Context context) {
		db_controler = new PoiDatabaseControler_old(context);
	}
		
	public void open() throws SQLException {
		database = db_controler.getWritableDatabase();
	}
	
	public void close() {
		db_controler.close();
	}

	//method to add a new poi to the database
	public void addPoi(NamedLocation poi) {
		ContentValues values = new ContentValues(); //keeps the values of the poi
		
		//add the vales of the poi
		values.put(PoiDatabaseControler_old.POI_NAME, poi.getName());
		if (poi instanceof Site) { //distinguish between Sites and DockingStations
			values.put(PoiDatabaseControler_old.POI_DESCRIPTION, ((Site)poi).getDescription());
		} else {
			values.put(PoiDatabaseControler_old.POI_DESCRIPTION, PoiDatabaseControler_old.default_description);
		}
		values.put(PoiDatabaseControler_old.POI_POSITION_X, poi.getPosition().getX());
		values.put(PoiDatabaseControler_old.POI_POSITION_Y, poi.getPosition().getY());
		values.put(PoiDatabaseControler_old.POI_POSITION_Z, poi.getPosition().getZ());
		values.put(PoiDatabaseControler_old.POI_ORIENTATION_X, poi.getOrientation().getO_X());
		values.put(PoiDatabaseControler_old.POI_ORIENTATION_Y, poi.getOrientation().getO_Y());
		values.put(PoiDatabaseControler_old.POI_ORIENTATION_Z, poi.getOrientation().getO_Z());
		values.put(PoiDatabaseControler_old.POI_ORIENTATION_W, poi.getOrientation().getO_W());
		
		//store the poi and close the database
		long index = database.insert(PoiDatabaseControler_old.TABLE_POIS, null, values);
	}

	//method to delete a certain poi
	public void deletePoi(String name) {
		database.delete(PoiDatabaseControler_old.TABLE_POIS, PoiDatabaseControler_old.POI_NAME+" = '"+name+"'", null);
	}
	
	//method to update a certain poi
	public void updatePoi(NamedLocation poi, String name_old) {
		if (poi instanceof Site) { //distinguish between Sites and DockingStations
			//update the entry with _id == index
			database.execSQL("UPDATE "+PoiDatabaseControler_old.TABLE_POIS+" SET "
					+PoiDatabaseControler_old.POI_NAME+" = '"+poi.getName()+"', "
					+PoiDatabaseControler_old.POI_DESCRIPTION+" = '"+((Site)poi).getDescription()+"', "
					+PoiDatabaseControler_old.POI_POSITION_X+" = "+poi.getPosition().getX()+", "
					+PoiDatabaseControler_old.POI_POSITION_Y+" = "+poi.getPosition().getX()+", "
					+PoiDatabaseControler_old.POI_POSITION_Z+" = "+poi.getPosition().getX()+", "
					+PoiDatabaseControler_old.POI_ORIENTATION_X+" = "+poi.getOrientation().getO_X()+", "
					+PoiDatabaseControler_old.POI_ORIENTATION_Y+" = "+poi.getOrientation().getO_Y()+", "
					+PoiDatabaseControler_old.POI_ORIENTATION_Z+" = "+poi.getOrientation().getO_Z()+", "
					+PoiDatabaseControler_old.POI_ORIENTATION_W+" = "+poi.getOrientation().getO_W()
					+" WHERE "+PoiDatabaseControler_old.POI_NAME+" = '"+name_old+"';");
		} else {
			//update the entry with _id == index
			database.execSQL("UPDATE "+PoiDatabaseControler_old.TABLE_POIS+" SET "
					+PoiDatabaseControler_old.POI_NAME+" = '"+poi.getName()+"', "
					+PoiDatabaseControler_old.POI_DESCRIPTION+" = '"+PoiDatabaseControler_old.default_description+"', "
					+PoiDatabaseControler_old.POI_POSITION_X+" = "+poi.getPosition().getX()+", "
					+PoiDatabaseControler_old.POI_POSITION_Y+" = "+poi.getPosition().getX()+", "
					+PoiDatabaseControler_old.POI_POSITION_Z+" = "+poi.getPosition().getX()+", "
					+PoiDatabaseControler_old.POI_ORIENTATION_X+" = "+poi.getOrientation().getO_X()+", "
					+PoiDatabaseControler_old.POI_ORIENTATION_Y+" = "+poi.getOrientation().getO_Y()+", "
					+PoiDatabaseControler_old.POI_ORIENTATION_Z+" = "+poi.getOrientation().getO_Z()+", "
					+PoiDatabaseControler_old.POI_ORIENTATION_W+" = "+poi.getOrientation().getO_W()
					+" WHERE "+PoiDatabaseControler_old.POI_NAME+" = '"+name_old+"';");
		}
	}
	
	//method that returns all the pois stored in the database
	public List<NamedLocation> getPois() {
		List<NamedLocation> pois = new ArrayList<NamedLocation>(); //List for the result
		
		Cursor cursor = database.query(PoiDatabaseControler_old.TABLE_POIS, all_columns, null, null, null, null, null); //provides access to the result of the database query
		cursor.moveToFirst(); //set cursor to first item
		while (!cursor.isAfterLast()) { //for all items
			//get values of the poi
			String name = cursor.getString(0);
			String description = cursor.getString(1);
			Position position = new Position(cursor.getLong(2), cursor.getLong(3), cursor.getLong(4));
			Orientation orientation = new Orientation(cursor.getLong(5), cursor.getLong(6), cursor.getLong(7), cursor.getLong(8));
			if (description.equals(PoiDatabaseControler_old.default_description)) { //distinguish between Sites and DockingsStations
				TurtleBotDockingStation poi = new TurtleBotDockingStation(name, position, orientation);
				pois.add(poi); //add poi to pois
			} else {
				Site poi = new Site(name, position, orientation);
				poi.setDescription(description);
				pois.add(poi);
			}
			cursor.moveToNext();
		}
		cursor.close();
		return pois;
	}
	
} 