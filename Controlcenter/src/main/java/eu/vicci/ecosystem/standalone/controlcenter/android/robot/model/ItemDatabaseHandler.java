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
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.view.AddMapDialog;
import eu.vicci.turtlebot.navigationapp.helperclasses.Map;
import eu.vicci.turtlebot.navigationapp.model.RobotType;


public class ItemDatabaseHandler {
	
	private SQLiteDatabase database;
	private ItemDatabaseControler db_controler;
	
	//Array with all column names of TABLE_POIS
	private String[] all_columns_robots = {
			ItemDatabaseControler.ROBOT_NAME,
			ItemDatabaseControler.ROBOT_TYPE,
			ItemDatabaseControler.ROBOT_IP,
			ItemDatabaseControler.ROBOT_PORT,
			ItemDatabaseControler.ROBOT_POSITION_X,
			ItemDatabaseControler.ROBOT_POSITION_Y,
			ItemDatabaseControler.ROBOT_POSITION_Z,
			ItemDatabaseControler.ROBOT_ORIENTATION_X,
			ItemDatabaseControler.ROBOT_ORIENTATION_Y,
			ItemDatabaseControler.ROBOT_ORIENTATION_Z,
			ItemDatabaseControler.ROBOT_ORIENTATION_W};
	
	//Array with all column names of TABLE_POIS
	private String[] all_columns_pois = {
			ItemDatabaseControler.POI_NAME,
			ItemDatabaseControler.POI_DESCRIPTION,
			ItemDatabaseControler.POI_POSITION_X,
			ItemDatabaseControler.POI_POSITION_Y,
			ItemDatabaseControler.POI_POSITION_Z,
			ItemDatabaseControler.POI_ORIENTATION_X,
			ItemDatabaseControler.POI_ORIENTATION_Y,
			ItemDatabaseControler.POI_ORIENTATION_Z,
			ItemDatabaseControler.POI_ORIENTATION_W};
	
	private String[] all_columns_maps = {
			ItemDatabaseControler.MAP_NAME,
			ItemDatabaseControler.MAP_SOURCEDIR,
			ItemDatabaseControler.MAP_IP,
			ItemDatabaseControler.MAP_PORT};
	
	
	
	public ItemDatabaseHandler(Context context) {
		db_controler = new ItemDatabaseControler(context);
	}
		
	public void open() throws SQLException {
		database = db_controler.getWritableDatabase();
	}
	
	public void close() {
		db_controler.close();
	}

	/*
	 *  addItem
	 */
	
	//method to add a new robot to the robot table
	public void addRobot(ClientRobot robot) {
		ContentValues values = new ContentValues(); //keeps the values of the robot
		
		//add the values of the robot
		values.put(ItemDatabaseControler.ROBOT_NAME, robot.getName());
		values.put(ItemDatabaseControler.ROBOT_TYPE, robot.getType().toString());
		values.put(ItemDatabaseControler.ROBOT_IP, robot.getIp());
		values.put(ItemDatabaseControler.ROBOT_PORT, robot.getPort());
		values.put(ItemDatabaseControler.ROBOT_POSITION_X, robot.getPosition().getX());
		values.put(ItemDatabaseControler.ROBOT_POSITION_Y, robot.getPosition().getY());
		values.put(ItemDatabaseControler.ROBOT_POSITION_Z, robot.getPosition().getZ());
		values.put(ItemDatabaseControler.ROBOT_ORIENTATION_X, robot.getOrientation().getO_X());
		values.put(ItemDatabaseControler.ROBOT_ORIENTATION_Y, robot.getOrientation().getO_Y());
		values.put(ItemDatabaseControler.ROBOT_ORIENTATION_Z, robot.getOrientation().getO_Z());
		values.put(ItemDatabaseControler.ROBOT_ORIENTATION_W, robot.getOrientation().getO_W());
		
		//store the robot
		long index = database.insert(ItemDatabaseControler.TABLE_ROBOTS, null, values);
	}
	
	//method to add a new poi to the poi table
	public void addPoi(NamedLocation poi) {
		ContentValues values = new ContentValues(); //keeps the values of the poi
		
		//add the values of the poi
		values.put(ItemDatabaseControler.POI_NAME, poi.getName());
		if (poi instanceof Site) { //distinguish between Sites and DockingStations
			values.put(ItemDatabaseControler.POI_DESCRIPTION, ((Site)poi).getDescription());
		} else {
			values.put(ItemDatabaseControler.POI_DESCRIPTION, ItemDatabaseControler.default_description);
		}
		values.put(ItemDatabaseControler.POI_POSITION_X, poi.getPosition().getX());
		values.put(ItemDatabaseControler.POI_POSITION_Y, poi.getPosition().getY());
		values.put(ItemDatabaseControler.POI_POSITION_Z, poi.getPosition().getZ());
		values.put(ItemDatabaseControler.POI_ORIENTATION_X, poi.getOrientation().getO_X());
		values.put(ItemDatabaseControler.POI_ORIENTATION_Y, poi.getOrientation().getO_Y());
		values.put(ItemDatabaseControler.POI_ORIENTATION_Z, poi.getOrientation().getO_Z());
		values.put(ItemDatabaseControler.POI_ORIENTATION_W, poi.getOrientation().getO_W());
		
		//store the poi
		long index = database.insert(ItemDatabaseControler.TABLE_POIS, null, values);
	}
	
	//method to add a new map to the map table
	public void addMap(ClientMap map) {
		ContentValues values = new ContentValues(); //keeps the values of the maps
		
		//add the values of the map
		values.put(ItemDatabaseControler.MAP_NAME, map.getName());
		values.put(ItemDatabaseControler.MAP_SOURCEDIR, map.getSourceDir());
		values.put(ItemDatabaseControler.MAP_IP, map.getIp());
		values.put(ItemDatabaseControler.MAP_PORT, map.getPort());
		
		//store the map
		long index = database.insert(ItemDatabaseControler.TABLE_MAPS, null, values);
	}

	/*
	 * deleteItem
	 */
	
	//method to delete a certain robot
	public void deleteRobot(String name) {
		database.delete(ItemDatabaseControler.TABLE_ROBOTS, ItemDatabaseControler.ROBOT_NAME+" = '"+name+"'", null);
	}
	
	//method to delete a certain poi
	public void deletePoi(String name) {
		database.delete(ItemDatabaseControler.TABLE_POIS, ItemDatabaseControler.POI_NAME+" = '"+name+"'", null);
	}
	
	//method to delete a certain map
	public void deleteMap(String name) {
		database.delete(ItemDatabaseControler.TABLE_MAPS, ItemDatabaseControler.MAP_NAME+" = '"+name+"'", null);
	}
	
	/*
	 * updateItem
	 */
	
	//method to update a certain robot
	public void updateRobot(ClientRobot robot, String name_old) {
		//update the entry with name == name_old
		database.execSQL("UPDATE "+ItemDatabaseControler.TABLE_ROBOTS+" SET "
				+ItemDatabaseControler.ROBOT_NAME+" = '"+robot.getName()+"', "
				+ItemDatabaseControler.ROBOT_TYPE+" = '"+robot.getType().toString()+"', "
				+ItemDatabaseControler.ROBOT_IP+" = '"+robot.getIp()+"', "
				+ItemDatabaseControler.ROBOT_PORT+" = '"+robot.getPort()+"', "
				+ItemDatabaseControler.ROBOT_POSITION_X+" = "+robot.getPosition().getX()+", "
				+ItemDatabaseControler.ROBOT_POSITION_Y+" = "+robot.getPosition().getX()+", "
				+ItemDatabaseControler.ROBOT_POSITION_Z+" = "+robot.getPosition().getX()+", "
				+ItemDatabaseControler.ROBOT_ORIENTATION_X+" = "+robot.getOrientation().getO_X()+", "
				+ItemDatabaseControler.ROBOT_ORIENTATION_Y+" = "+robot.getOrientation().getO_Y()+", "
				+ItemDatabaseControler.ROBOT_ORIENTATION_Z+" = "+robot.getOrientation().getO_Z()+", "
				+ItemDatabaseControler.ROBOT_ORIENTATION_W+" = "+robot.getOrientation().getO_W()
				+" WHERE "+ItemDatabaseControler.ROBOT_NAME+" = '"+name_old+"';");
	}
	
	//method to update a certain poi
	public void updatePoi(NamedLocation poi, String name_old) {
		if (poi instanceof Site) { //distinguish between Sites and DockingStations
			//update the entry with name == name_old
			database.execSQL("UPDATE "+ItemDatabaseControler.TABLE_POIS+" SET "
					+ItemDatabaseControler.POI_NAME+" = '"+poi.getName()+"', "
					+ItemDatabaseControler.POI_DESCRIPTION+" = '"+((Site)poi).getDescription()+"', "
					+ItemDatabaseControler.POI_POSITION_X+" = "+poi.getPosition().getX()+", "
					+ItemDatabaseControler.POI_POSITION_Y+" = "+poi.getPosition().getX()+", "
					+ItemDatabaseControler.POI_POSITION_Z+" = "+poi.getPosition().getX()+", "
					+ItemDatabaseControler.POI_ORIENTATION_X+" = "+poi.getOrientation().getO_X()+", "
					+ItemDatabaseControler.POI_ORIENTATION_Y+" = "+poi.getOrientation().getO_Y()+", "
					+ItemDatabaseControler.POI_ORIENTATION_Z+" = "+poi.getOrientation().getO_Z()+", "
					+ItemDatabaseControler.POI_ORIENTATION_W+" = "+poi.getOrientation().getO_W()
					+" WHERE "+ItemDatabaseControler.POI_NAME+" = '"+name_old+"';");
		} else {
			//update the entry with name == name_old
			database.execSQL("UPDATE "+ItemDatabaseControler.TABLE_POIS+" SET "
					+ItemDatabaseControler.POI_NAME+" = '"+poi.getName()+"', "
					+ItemDatabaseControler.POI_DESCRIPTION+" = '"+ItemDatabaseControler.default_description+"', "
					+ItemDatabaseControler.POI_POSITION_X+" = "+poi.getPosition().getX()+", "
					+ItemDatabaseControler.POI_POSITION_Y+" = "+poi.getPosition().getX()+", "
					+ItemDatabaseControler.POI_POSITION_Z+" = "+poi.getPosition().getX()+", "
					+ItemDatabaseControler.POI_ORIENTATION_X+" = "+poi.getOrientation().getO_X()+", "
					+ItemDatabaseControler.POI_ORIENTATION_Y+" = "+poi.getOrientation().getO_Y()+", "
					+ItemDatabaseControler.POI_ORIENTATION_Z+" = "+poi.getOrientation().getO_Z()+", "
					+ItemDatabaseControler.POI_ORIENTATION_W+" = "+poi.getOrientation().getO_W()
					+" WHERE "+ItemDatabaseControler.POI_NAME+" = '"+name_old+"';");
		}
	}
	
	//method to update a certain map
	public void updateMap(ClientMap map, String name_old) {
		//update the entry with name == name_old
		database.execSQL("UPDATE "+ItemDatabaseControler.TABLE_MAPS+" SET "
				+ItemDatabaseControler.MAP_NAME+" = '"+map.getName()+"', "
				+ItemDatabaseControler.MAP_SOURCEDIR+" = '"+map.getSourceDir()+"', "
				+ItemDatabaseControler.MAP_IP+" = '"+map.getIp()+"', "
				+ItemDatabaseControler.MAP_PORT+" = "+map.getPort()
				+" WHERE "+ItemDatabaseControler.MAP_NAME+" = '"+name_old+"';");
	}
	
	/*
	 * getItems
	 */
	
	//TODO: currently only for RobotType Turtlebot and Youbot
	//method that returns all the robots stored in the database
	public List<ClientRobot> getRobots() {
		List<ClientRobot> robots = new ArrayList<ClientRobot>(); //List for the result
		
		Cursor cursor = database.query(ItemDatabaseControler.TABLE_ROBOTS, all_columns_robots, null, null, null, null, null); //provides access to the result of the database query
		cursor.moveToFirst(); //set cursor to first item
		while (!cursor.isAfterLast()) { //for all items
			//get values of the robot
			String name = cursor.getString(0);
			String type_string = cursor.getString(1);
			String ip = cursor.getString(2);
			String port = cursor.getString(3);
			Position position = new Position(cursor.getLong(4), cursor.getLong(5), cursor.getLong(6));
			Orientation orientation = new Orientation(cursor.getLong(7), cursor.getLong(8), cursor.getLong(9), cursor.getLong(10));
			
			RobotType type;
			if (type_string.equals("Turtlebot")) {
				type = RobotType.Turtlebot;
			} else {
				type = RobotType.Youbot;
			}
			ClientRobot robot = new ClientRobot(name, ip, port, type);
			robot.setPosition(position);
			robot.setOrientation(orientation);
			robots.add(robot);
			cursor.moveToNext();
		}
		cursor.close();
		return robots;
	}
	
	//TODO: currently only for DockingStation TurtlebotDockingStation
	//method that returns all the pois stored in the database
	public List<NamedLocation> getPois() {
		List<NamedLocation> pois = new ArrayList<NamedLocation>(); //List for the result
		
		Cursor cursor = database.query(ItemDatabaseControler.TABLE_POIS, all_columns_pois, null, null, null, null, null); //provides access to the result of the database query
		cursor.moveToFirst(); //set cursor to first item
		while (!cursor.isAfterLast()) { //for all items
			//get values of the poi
			String name = cursor.getString(0);
			String description = cursor.getString(1);
			Position position = new Position(cursor.getLong(2), cursor.getLong(3), cursor.getLong(4));
			Orientation orientation = new Orientation(cursor.getLong(5), cursor.getLong(6), cursor.getLong(7), cursor.getLong(8));
			if (description.equals(ItemDatabaseControler.default_description)) { //distinguish between Sites and DockingsStations
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
	
	//TODO: currently only for DockingStation TurtlebotDockingStation
	//method that returns all the maps stored in the database
	public List<ClientMap> getMaps() {
		List<ClientMap> maps = new ArrayList<ClientMap>(); //List for the result
		
		Cursor cursor = database.query(ItemDatabaseControler.TABLE_MAPS, all_columns_maps, null, null, null, null, null); //provides access to the result of the database query
		cursor.moveToFirst(); //set cursor to first item
		while (!cursor.isAfterLast()) { //for all items
			//get values of the robot
			String name = cursor.getString(0);
			String sourceDir = cursor.getString(1);
			String ip = cursor.getString(2);
			int port = cursor.getInt(3);
			Map map_map = AddMapDialog.getMap(ip, String.valueOf(port));
			ClientMap map = new ClientMap(name, sourceDir, ip, port, map_map);
			maps.add(map);
			cursor.moveToNext();
		}
		cursor.close();
		return maps;
	}
	
} 