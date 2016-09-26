package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

public class RobotClientManager {

	private static RobotClientManager manager =null;
	private RobotClient robotClient=null;
	
	private RobotClientManager(){
	}
	
	public static RobotClientManager getInstance(){
		if(manager==null) manager = new RobotClientManager();
		return manager;
	}
	
	public void establishConnection(String ip, String port){
		robotClient = new RobotClient(ip, port);
		robotClient.connect();
	}
	
	public RobotClient getRobot(){
		return robotClient;
	}
	
	public void disconnect(){
		robotClient.close();
	}
}
