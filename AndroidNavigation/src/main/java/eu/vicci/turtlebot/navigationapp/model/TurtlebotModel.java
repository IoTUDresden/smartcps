package eu.vicci.turtlebot.navigationapp.model;

import org.java_websocket.drafts.Draft_10;

import android.os.AsyncTask;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.location.UnnamedLocation;
import eu.vicci.driver.turtlebot.TurtleBot;
import eu.vicci.turtlebot.navigationapp.helperclasses.RobotData;

public class TurtlebotModel extends AsyncTask<String, Void, Void> {

	
	//private static final String TAG = TurtlebotModel.class.getSimpleName();
	static Draft_10 draft = new Draft_10();
	TurtleBot turtle;
	private UnnamedLocation lastLocation;
	private RobotData data;

	public TurtlebotModel(RobotData robot){
		this.data = robot;
	}
	
	@Override
	protected Void doInBackground(String... arg0) {
		turtle = new TurtleBot(data.getIp(),data.getPort());
		try {
			turtle.connect();
			while(!isCancelled()){
				
				UnnamedLocation newLocation = (UnnamedLocation) turtle.getLocation();
				if(newLocation!=null&&!newLocation.equals(lastLocation)){
					lastLocation = newLocation; 
					publishProgress();
				}
			}
			turtle.disconnect();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Void... arg) {
		data.setLocation(lastLocation);
	}

	public TurtleBot getTurtle() {
		return turtle;
	}
	
}


