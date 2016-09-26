package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Fragment;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import eu.vicci.driver.robot.Robot;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.turtlebot.TurtleBot;
import eu.vicci.driver.youbot.YouBot;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;

public class CameraFragment extends Fragment{
	private MjpegView cv;
	SensorManager sensorManager = null;
	AsyncTask<String, Void, MjpegInputStream> doRead;
	String URL;
	
	private Robot robot;
	private String robotName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 			
		
		robotName = ((ControlActivity) getActivity()).getRobotName();
		int idx = SmartCPS_Impl.getNavigator().getIndexOfRoboByName(robotName);
		
		String ip = SmartCPS_Impl.getNavigator().getRobots().get(idx).getIp();
		String MjpegPort = "8181";
		URL = "http://"+ip+":"+ MjpegPort +"/stream?topic=/camera/rgb/image_color";

		switch(SmartCPS_Impl.getNavigator().getRobots().get(idx).getType()){
		case Turtlebot:
			robot = new TurtleBot(ip);
			break;
		case Youbot:
			robot = new YouBot(ip);
			break;
		default:
			break;
		}
		
		//connect robot if it is not connected yet
		if (!robot.getIsConnected()) {
			try {
				robot.connect();
				System.out.println("connected");
			} catch (NotConnectedException e) {
				e.printStackTrace();
			}
		}
		
		cv = new MjpegView(getActivity());// evtl. mit attribute set aufrufen?
		
        new DoRead().execute(URL);
        cv.startPlayback();
        
		return cv; // return View
	  }
	
    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground(String... url) {
            //TODO: if camera has authentication deal with it and don't just not work
        	String TAG = "#robot";
            HttpResponse res = null;
            DefaultHttpClient httpclient = new DefaultHttpClient();     
            Log.d(TAG, "1. Sending http request");
            try {
                res = httpclient.execute(new HttpGet(URI.create(url[0])));
                Log.d(TAG, "2. Request finished, status = " + res.getStatusLine().getStatusCode());
                if(res.getStatusLine().getStatusCode()==401){
                    //You must turn off camera User Access Control before this will work
                    return null;
                }
                return new MjpegInputStream(res.getEntity().getContent());  
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                Log.d(TAG, "Request failed-ClientProtocolException", e);
                //Error connecting to camera
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Request failed-IOException");
                //Error connecting to camera
            } catch (IllegalArgumentException e){
            	
            }

            return null;
        }

        protected void onPostExecute(MjpegInputStream result) {
            cv.setSource(result);
            cv.setDisplayMode(MjpegView.SIZE_FULLSCREEN);
            cv.showFps(true);
        }
    }
	
}
