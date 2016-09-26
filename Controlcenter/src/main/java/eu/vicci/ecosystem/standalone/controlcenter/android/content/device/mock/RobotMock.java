package eu.vicci.ecosystem.standalone.controlcenter.android.content.device.mock;

import eu.vicci.ecosystem.standalone.controlcenter.android.content.DashboardContentManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.Robot;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientRobot;
import eu.vicci.turtlebot.navigationapp.model.RobotType;

/**
 * Definitions of Robot Mocks
 * 
 * @author André Kühnert
 * 
 */
public class RobotMock {
	
	/**
	 * Gets a Robot Mock
	 * @param name the name of the robot
	 * @return Robot
	 */
	public static Robot getRobotMock(String name) {
		ClientRobot clientRobot = new ClientRobot("TestRobot " + name, "ich.lach.mich.tot", "0815", 
				RobotType.Turtlebot);		
		return new Robot(clientRobot);		
	}
	
	/**
	 * add some robot Mocks to the ContentManager
	 */
	public static void addRobotMocks(){
		ClientRobot clientRobot = new ClientRobot("Turtlebot", "192.168.0.51", "0815", 
				RobotType.Turtlebot);
		ClientRobot clientRobot2 = new ClientRobot("Youbot", "192.168.0.52", "0816", 
				RobotType.Youbot);
		Robot robot = new Robot(clientRobot);
		Robot robot2 = new Robot(clientRobot2);
		DashboardContentManager.getInstance().addDashboardDevice(robot);
		DashboardContentManager.getInstance().addDashboardDevice(robot2);
	}
}
