package eu.vicci.turtlebot.navigationapp.helperclasses;

import eu.vicci.driver.robot.Robot;
import eu.vicci.driver.robot.location.Location;
import eu.vicci.driver.robot.location.UnnamedLocation;
import eu.vicci.driver.turtlebot.TurtleBot;
import eu.vicci.turtlebot.navigationapp.model.RobotType;

public class RobotData {

	private String name;
	private String ip;
	private int port;
	private boolean useMap=false;
	private UnnamedLocation location;
	private Location goal;
	private boolean selected = false;
	private TurtleBot turtlebot;
	private RobotType robotType;
	private Location target;
	
	private Robot robot;
	
	public RobotData(String name, String ip, int port){
		this.name = name;
		this.ip = ip;
		this.port = port;
	}
	
	public RobotData(String name, String ip, int port, boolean useMap,RobotType robotType){
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.useMap = useMap;
		this.robotType = robotType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUseMap() {
		return useMap;
	}

	public void setUseMap(boolean useMap) {
		this.useMap = useMap;
	}

	public UnnamedLocation getLocation() {
		return location;
	}

	public void setLocation(UnnamedLocation location) {
		this.location = location;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Location getGoal() {
		return goal;
	}

	public void setGoal(Location goal) {
		this.goal = goal;
	}

	public TurtleBot getTurtlebot() {
		return turtlebot;
	}

	public void setTurtlebot(TurtleBot turtlebot) {
		this.turtlebot = turtlebot;
	}

	public RobotType getRobotType() {
		return robotType;
	}

	public void setRobotType(RobotType robotType) {
		this.robotType = robotType;
	}

	public Robot getRobot() {
		return robot;
	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}
	
	public boolean isLocationValid(){
		return (location!=null);
	}

	public Location getTarget() {
		return target;
	}

	public void setTarget(Location target) {
		this.target = target;
	}
	
}
