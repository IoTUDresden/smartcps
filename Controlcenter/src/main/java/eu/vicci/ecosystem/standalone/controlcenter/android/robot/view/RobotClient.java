package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import android.util.Log;

public class RobotClient extends WebSocketClient {

	private int batteryInfo=0;
	private int batteryVoltage=0;
	private final int batteryMinimalVoltage = 130;
	static Draft_10 draft = new Draft_10();
	private boolean connected=false;

	public RobotClient(String ip, String port) {	
		//super(URI.create("ws://10.42.0.1:9090"), draft);
		super(URI.create("ws://"+ip+":"+port), draft);
		Log.v("MyActivity","ws://"+ip+":"+port);
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onError(Exception arg0) {
		// TODO Auto-generated method stub
		int i = 0;

	}

	@Override
	public void onMessage(String arg0) {
		// TODO Auto-generated method stub
		if(arg0.contains("sensors")&&arg0.contains("core"))	{
			parseBatteryInfo(arg0);
		} else if(arg0.contains("events")&&arg0.contains("power_system")){
			parsePowerSystemInfo(arg0);
		}
		
	}

	private void parsePowerSystemInfo(String s) {
		s = s.substring(s.lastIndexOf("event\":")+7,s.length());
		s = s.substring(0,s.indexOf(","));
		try{
			batteryInfo = Integer.parseInt(s);
		} catch(Exception e){			
		}
	}

	private void parseBatteryInfo(String s) {
		
		s = s.substring(s.lastIndexOf("battery\":")+10,s.length());
		s = s.substring(0,s.indexOf(","));
		try{
			batteryVoltage = Integer.parseInt(s);
		} catch(Exception e){			
		}
	}
	
	public int getBatteryChargeInPercentage(){
		return (int)((((float)batteryVoltage-(float)batteryMinimalVoltage)/35.0f)*100f);
	}

	@Override
	public void onOpen(ServerHandshake arg0) {
		connected=true;
		Log.v("MyActivity","Connected");
		subscribeToSensorInfo();
		subscribeToPowerSystem();
	}
	
	public void subscribeToSensorInfo(){
		String message = "{'op': 'subscribe', 'topic': '/mobile_base/sensors/core'}";
		message  = message.replace("'", "\"");
		send(message );
	}
	
	public void subscribeToPowerSystem(){
		String message = "{'op': 'subscribe', 'topic': '/mobile_base/events/power_system'}";
		message  = message.replace("'", "\"");
		send(message );
	}

	public void doMoveForward(){
		doPublishCommandVelocity(0.1f,0.0f,0.0f,0.0f,0.0f,0.0f);
	}
	
	public void doMoveBackward(){
		doPublishCommandVelocity(-0.1f,0.0f,0.0f,0.0f,0.0f,0.0f);
	}
	
	public void doMoveLeft(){
		doPublishCommandVelocity(0.0f,0.0f,0.0f,0.0f,0.0f,0.5f);
	}
	
	public void doMoveRight(){
		doPublishCommandVelocity(-0.01f,0.0f,0.0f,0.0f,0.0f,-0.5f);
	}
	
	public void doDriveForward(){
		String message = "{'op': 'publish', 'topic': '/turtle1/command_velocity', 'msg':{'linear':2,'angular':0.0}}";
		message  = message.replace("'", "\"");
		send(message );
	}
	
	public void doDriveLeft(){
		String message = "{'op': 'publish', 'topic': '/turtle1/command_velocity', 'msg':{'linear':0.0,'angular':2.0}}";
		message  = message.replace("'", "\"");
		send(message );
	}
	public void doDriveRight(){
		String message = "{'op': 'publish', 'topic': '/turtle1/command_velocity', 'msg':{'linear':0.0,'angular':-2.0}}";
		message  = message.replace("'", "\"");
		send(message );
	}
	public void doDriveBackward(){
		String message = "{'op': 'publish', 'topic': '/turtle1/command_velocity', 'msg':{'linear':-2.0,'angular':0.0}}";
		message  = message.replace("'", "\"");
		send(message );
	}
	/*		FOR TURTLESIM
	public void doDrive(float linear, float angular){
		String message = "{'op': 'publish', 'topic': '/turtle1/command_velocity', 'msg':{'linear':"+linear+",'angular':"+angular+"}}";
		message  = message.replace("'", "\"");
		send(message );		
	}*/
	
	//		FOR TURTLEBOT
	public void doDrive(float linear, float angular){
		doPublishCommandVelocity(linear,0.0f,0.0f,0.0f,0.0f,angular);
	}
	
	public void setAccelerationLinear(float linear){
		if(!connected) return;
		String message = "{'op': 'publish', 'topic': '/velocity_smoother/accel_lim_v', 'msg':{}}";
		message  = message.replace("'", "\"");
        send(message );		
	}
	
	
	public void doPublishCommandVelocity(float lin_x, float lin_y, float lin_z, float ang_x , float ang_y, float ang_z){
		if(!connected) return;
		String message = "{'op': 'publish', 'topic': '/mobile_base/commands/velocity', 'msg':{'linear':{'x':"+lin_x+",'y':"+lin_y+",'z':"+lin_z+"},'angular':{'x':"+ang_x+",'y':"+ang_y+",'z':"+ang_z+"}}}";
		//String message = "{'op': 'publish', 'topic': '/key_cmd_vel', 'msg':{'linear':{'x':"+lin_x+",'y':"+lin_y+",'z':"+lin_z+"},'angular':{'x':"+ang_x+",'y':"+ang_y+",'z':"+ang_z+"}}}";
		message  = message.replace("'", "\"");
        send(message );
	}
	
	public void doPublishCommandLED(int value){
		if(!connected) return;
		String message = "{'op': 'publish', 'topic': '/mobile_base/commands/led1', 'msg':{'value':"+value+"}}";
        message  = message.replace("'", "\"");
        send(message );
	}
	
	
}
