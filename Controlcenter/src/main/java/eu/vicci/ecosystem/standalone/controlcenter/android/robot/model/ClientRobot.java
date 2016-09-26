package eu.vicci.ecosystem.standalone.controlcenter.android.robot.model;

import eu.vicci.driver.robot.Robot;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;
import eu.vicci.turtlebot.navigationapp.model.RobotType;

public class ClientRobot {
	//class robot support name, position, orientation
	private Robot robot;
	private RobotType type;
	private String ip;
	private String port;
	private String name;
	private Position position;
	private Orientation orientation;
	
	public ClientRobot(String name, String ip, String port, RobotType type){
		//this.robot = RobotManager.getInstance().addRobot(name, ip, port, useMap, type);
		this.ip = ip;
		this.port = port;
		this.type = type;
		this.name = name;
	}

	public Robot getRobot() {
		return robot;
	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}

	public RobotType getType() {
		return type;
	}

	public void setType(RobotType type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}
	
	public Orientation getOrientation() {
		return orientation;
	}
	
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
}
