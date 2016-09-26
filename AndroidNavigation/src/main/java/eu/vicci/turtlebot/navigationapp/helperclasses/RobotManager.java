package eu.vicci.turtlebot.navigationapp.helperclasses;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Point;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.location.Location;
import eu.vicci.driver.robot.location.UnnamedLocation;
import eu.vicci.turtlebot.navigationapp.controller.MapController;
import eu.vicci.turtlebot.navigationapp.model.RobotType;

public class RobotManager {
	
	private static RobotManager center;
	
	private static List<RobotData> robotList = new LinkedList<RobotData>();
	private Map map;

	
	private RobotManager() {
		
	}
	
	public static RobotManager getInstance(){
		if(center==null){
			center = new RobotManager();
		}
		return center;
	}
	
	public void addRobot(String name, String ip, int port, boolean useMap, RobotType type, UnnamedLocation location){
		RobotData robot_data = new RobotData(name, ip,port,useMap,type);
		robot_data.setLocation(location);
		robotList.add(robot_data);
	}

	public List<RobotData> getRobotList() {
		return robotList;
	}
	
	public static void setRobotList(List<RobotData> robot_list) {
		robotList = robot_list;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map_new) {
		this.map = map_new;
	}
	
	public void setAllUnselected(){
		for(RobotData data : robotList){
			data.setSelected(false);
		}
		MapController.setRecentlySelectedRobotNameSet(false);
		
	}
	
	public RobotData getSelectedRobot(){
		for(RobotData data: robotList){
			if(data.isSelected())return data;
		}
		return null;
	}
	
	public void selectRobot(Point p) {
		for(RobotData data: robotList){
			Location robotLocation = data.getLocation();
			Point robotLocationBitmapCoordinates = CoordinatesTranslator.worldToBitmapCoordinates(robotLocation);
			if(arePointsCloseToEachOther(robotLocationBitmapCoordinates, p,15)){
				data.setSelected(true);
				//set robot name es recently selected robot name
		        MapController.setRecentlySelectedRobotName(data.getName());
				MapController.setRecentlySelectedRobotNameSet(true);
				
				return;
			}
		}
	}
	
	private boolean arePointsCloseToEachOther(Point p1,Point p2, int tolerance){
		if(p1==null||p2==null) return false;
		boolean result;
		//System.out.println(p1+" VS "+p2);
		result = Math.abs(p1.x - p2.x) <= tolerance;
		result &= Math.abs(p1.y - p2.y) <= tolerance;
		return result;
	}
	
	public boolean isARobotSelected(){
		for(RobotData data: robotList) {
			if(data.isSelected()) return true;
		}
		return false;
	}
	
	public static void deleteRobot(String name) {
		for (int i=0; i<robotList.size(); i++) {
			if (robotList.get(0).getName() == name) {
				robotList.remove(i);
			}
		}
	}
	
	//method to disconnect all robots
	public static void disconnectRobots() {
		for (int i=0; i<robotList.size(); i++) {
			RobotData robot = robotList.get(i);
			if (robot.getRobot().getIsConnected()) {
				try {
					robotList.get(i).getRobot().disconnect();
				} catch (NotConnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	//method to connect all robots
	public static void connectRobots() {
		for (int i=0; i<robotList.size(); i++) {
			RobotData robot = robotList.get(i);
			if (!robot.getRobot().getIsConnected()) {
				try {
					robotList.get(i).getRobot().connect();
				} catch (NotConnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
