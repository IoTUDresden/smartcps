package eu.vicci.turtlebot.navigationapp.model;

import org.java_websocket.drafts.Draft_10;

import android.os.AsyncTask;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.location.UnnamedLocation;
import eu.vicci.driver.youbot.YouBot;
import eu.vicci.turtlebot.navigationapp.helperclasses.RobotData;

public class YoubotModel extends AsyncTask<String, Void, Void> {

	static Draft_10 draft = new Draft_10();
	YouBot youbot;
	private UnnamedLocation lastLocation;
	private RobotData data;

	public YoubotModel(RobotData robot){
		this.data = robot;
	}
	
	@Override
	protected Void doInBackground(String... arg0) {
		youbot = new YouBot(data.getIp(),data.getPort());
		try {
			youbot.connect();
			while(!isCancelled()){
				UnnamedLocation newLocation = (UnnamedLocation) youbot.getLocation();
				if(newLocation!=null&&!newLocation.equals(lastLocation)){
					lastLocation = newLocation; 
					publishProgress();
				}
			}
			youbot.disconnect();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Void... arg) {
		data.setLocation(lastLocation);
	}

	public YouBot getYoubot() {
		return youbot;
	}
	
}


