package eu.vicci.ecosystem.standalone.controlcenter.android.robot.model;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.location.NamedLocation;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.view.AddMapDialog;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.view.FragmentCallback;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.view.NavBaseActivity;
import eu.vicci.turtlebot.navigationapp.helperclasses.Map;
import eu.vicci.turtlebot.navigationapp.helperclasses.RobotData;
import eu.vicci.turtlebot.navigationapp.helperclasses.RobotManager;


/**
* Navigator represents the state of the model for navigating robots including maps, robots and pois 
* each stored in a list and available for a variety of services independent of the UI
* 
*/
public class Navigator extends NavBaseActivity { //extend?
	
	//local lists for the items
	private static List<ClientRobot> robots;
	private static List<NamedLocation> pois;
	private static List<ClientMap> maps;
	
	
	//these pointer allow quick access to the currently selected models in the UI
	private int activeRobotIdx;
	private int activePoiIdx;
	private int activeMapIdx;
	
	//global variables used for loading maps
	private static Map map_getMap;
	private static FragmentCallback controller;
	
	
	public Navigator(){
		maps = new ArrayList<ClientMap>();
		robots = new ArrayList<ClientRobot>();
		pois = new ArrayList<NamedLocation>();
		
		activeRobotIdx = -1;
		activePoiIdx = -1;
		activeMapIdx = -1;
	}
	
	/*
	 * setItems
	 */
	
	public static void setRobots(List<ClientRobot> robots_new) {
		robots = robots_new;
	}
	
	public static void setPois(List<NamedLocation> pois_new) {
		pois = pois_new;
	}
	
	public static void setMaps(List<ClientMap> maps_new) {
		//if (!maps_new.isEmpty()) {
		//	ClientMap first_map = maps_new.get(0);
		//	loadMap(first_map.getName(), first_map.getSourceDir(), first_map.getIp(), String.valueOf(first_map.getPort()));
		//}
		maps = maps_new;
	}
	
	/*
	 * getItems
	 */
	
	public static List<ClientRobot> getRobots(){
		return robots;
	}
	
	public List<NamedLocation> getPois(){
		return pois;
	}
	
	public static List<ClientMap> getMaps(){
		return maps;
	}
	
	/*
	 * addItem
	 */
	
	public void addRobot(ClientRobot robot){
		NavBaseActivity.database.addRobot(robot); //add robot to database
		robots.add(robot);
		//++activeRobotIdx;
	}
	
	public void addPoi(NamedLocation poi){
		NavBaseActivity.database.addPoi(poi); //add poi to database
		pois.add(poi); //add poi locally
		++activePoiIdx;
	}
	
	public void addMap(ClientMap map){
		NavBaseActivity.database.addMap(map); //add map to database
		maps.add(map);
		++activeMapIdx;
	}
	
	/*
	 * deleteItem
	 */
	
	public void deleteRobot(int index){
		//disconnect robot
		String robot_name = robots.get(index).getName(); //name of chosen robot
		List<RobotData> robotList = RobotManager.getInstance().getRobotList(); //robot list with RobotData
		RobotData robot = robotList.get(index);
		if (!robot.getName().equals(robot_name)) { //if robot with chosen index is not equal to robot in robot list
			for (int i=0; i<robotList.size(); i++) {
				if (robotList.get(i).getName().equals(robot_name)) {
					robot = robotList.get(i);
					break;
				}
			}
		}
		try { //try to disconnect
			robot.getRobot().disconnect();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		NavBaseActivity.database.deleteRobot(robots.get(index).getName()); //delete robot from database
		RobotManager.deleteRobot(robots.get(index).getName()); //delete robot from item list
		robots.remove(index);
		//--activeRobotIdx;
	}
	
	public void deletePoi(int index){
		NavBaseActivity.database.deletePoi(pois.get(index).getName()); //delete poi from database
		pois.remove(index); //delete poi locally
		--activePoiIdx;
		
	}
	
	public void deleteMap(int index){
		NavBaseActivity.database.deleteMap(maps.get(index).getName()); //delete map from database
		maps.remove(index);
		--activeMapIdx;
	}
	
	/*
	 * updateItem
	 */
	
	public void updateRobot(int index, ClientRobot robot){
		NavBaseActivity.database.updateRobot(robot, robots.get(index).getName()); //update robot in the database
		robots.set(index, robot);
	}
	
	public void updatePoi(int index, NamedLocation poi){
		NavBaseActivity.database.updatePoi(poi, pois.get(index).getName()); //update poi in the database
		pois.set(index, poi); //update poi locally
	}
	
	public void updateMap(int index, ClientMap map){
		NavBaseActivity.database.updateMap(map, maps.get(index).getName()); //update map in the database
		maps.set(index, map);
	}
	
	/*
	 * getIndexOfItemByName
	 */
	
	public static int getIndexOfRoboByName(String name){
		
		int index = 0;
		
		for(ClientRobot robo : robots){
			if(robo.getName().equals(name)){
				return index;
			}
			index++;
		}
		
		return -1;
	}
	
	public int getIndexOfPoiByName(String name){
		
		int index = 0;
		
		for(NamedLocation poi : pois){
			if(poi.getName().equals(name)){
				return index;
			}
			index++;
		}
		
		return -1;
	}
	
	public int getIndexOfMapByName(String name){
		
		int index = 0;
		
		for(ClientMap map : maps){
			if(map.getName().equals(name)){
				return index;
			}
			index++;
		}
		
		return -1;
	}
	
	/*
	 * checkItemName
	 */
	
	public boolean checkRoboName(String name){
		
		boolean flag = true; 
		for(ClientRobot robo : robots){
			if(robo.getName().equals(name)){
				flag = false;
			}
		}
		return flag;
	}
	
	public boolean checkPoiName(String name){
		
		boolean flag = true; 
		for(NamedLocation poi : pois){
			if(poi.getName().equals(name)){
				flag = false;
			}
		}
		return flag;
	}
	
	public boolean checkMapName(String name){
		
		boolean flag = true; 
		for(ClientMap map : maps){
			if(map.getName().equals(name)){
				flag = false;
			}
		}
		return flag;
	}
	
	/*
	 * getActiveItem
	 */
	
	public void setActiveRobot(int idx){
		activeRobotIdx = idx;
	}
	
	/*
	 * getActiveItem
	 */
	
	public ClientRobot getActiveRobot(){
		ClientRobot result = null;
		if(activeRobotIdx >= 0){
			result = robots.get(activeRobotIdx);
		}
		return result;
	}
	
	public NamedLocation getActivePoi(){
		NamedLocation result = null;
		if(activePoiIdx >= 0){
			result = pois.get(activePoiIdx);
		}
		return result;
	}
	
	public ClientMap getActiveMap(){
		ClientMap result = null;
		if(activeMapIdx >= 0){
			result = maps.get(activeMapIdx);
		}
		return result;
	}
	
	
	public static void loadMap(final String name, final String source, final String ip, final String port) {
		
		AsyncTask<String, Integer, Boolean> MapLoader = new AsyncTask<String, Integer, Boolean>() {
		
			@Override
			protected Boolean doInBackground(String... params) {
				
				//for testing at home
				/*map = null;
				return true;*/
				
				//get the map 
				map_getMap = AddMapDialog.getMap(ip, port);
				if (map_getMap != null) {
					return true;
				} else {
					return false;
				}
			};
			
			@Override
		    protected void onPostExecute(Boolean result) {
				if(result==true){
					//create our model map model
					//ClientMap clientMap = new ClientMap(name, source, ip, Integer.parseInt(port), map_getMap);	
				   
					//add item to fragment list
					//controller.addMap(clientMap);
				}
			}

		};
	MapLoader.execute();
	}
	
	/*
	 * getMinItemName
	 */
	
	public static String getMinRobotName() {
		List<String> robot_names = new ArrayList<String>(); //list for robot names
		int index = 1; //min index for robot name
		
		if (robots.isEmpty()) { //if no robots stored: return 1
			return "Robot "+Integer.toString(1);
		}
		
		for (int i=0; i<robots.size(); i++) { //get robot names
			robot_names.add(robots.get(i).getName());
		}
		while (robot_names.contains("Robot "+Integer.toString(index))) { //find min valid robot name
			index++;
		}
		return "Robot "+Integer.toString(index);
	}
	
	public static String getMinPoiName() {
		List<String> poi_names = new ArrayList<String>(); //list for poi names
		int index = 1; //min index for poi name
		
		if (pois.isEmpty()) { //if no pois stored: return 1
			return "Point of Interest "+Integer.toString(1);
		}
		
		for (int i=0; i<pois.size(); i++) { //get poi names
			poi_names.add(pois.get(i).getName());
		}
		while (poi_names.contains("Point of Interest "+Integer.toString(index))) { //find min valid poi name
			index++;
		}
		return "Point of Interest "+Integer.toString(index);
	}
	
	public static String getMinMapName() {
		List<String> map_names = new ArrayList<String>(); //list for map names
		int index = 1; //min index for map name
		
		if (maps.isEmpty()) { //if no maps stored: return 1
			return "Map "+Integer.toString(1);
		}
		
		for (int i=0; i<maps.size(); i++) { //get map names
			map_names.add(maps.get(i).getName());
		}
		while (map_names.contains("Map "+Integer.toString(index))) { //find min valid map name
			index++;
		}
		return "Map "+Integer.toString(index);
	}
	
}
