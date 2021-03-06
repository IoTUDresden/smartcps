package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.turtlebot.navigationapp.controller.MapController;
import eu.vicci.turtlebot.navigationapp.helperclasses.Map;
import eu.vicci.turtlebot.navigationapp.helperclasses.RobotManager;
import eu.vicci.turtlebot.navigationapp.view.MapView;
import eu.vicci.turtlebot.navigationapp.view.MiniMapView;

public class MiniMapFragment extends Fragment{
	 
	 private Map map;
	 private MiniMapView mv;
	 private MapController mc;
	 
	 
	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 			
		 mv = new MiniMapView(getActivity(), container);
		 
		 //set the map in the robotManager, as the mapController will use this map
		 map = SmartCPS_Impl.getNavigator().getActiveMap().getMap();
		 RobotManager.getInstance().setMap(map);
		 
		 mc = new MapController(getActivity(), (MapView) mv);
		 //makeTheViewUpdateEveryNSeconds();
		 
	    return mv;
	  }
	 
	  public void makeTheViewUpdateEveryNSeconds() {
			final Handler mHandler = new Handler();
			
			Runnable mUpdateTimeTask = new Runnable() { 
				public void run() {
					long ms = SystemClock.uptimeMillis();   
					mv.invalidate();
					mHandler.postAtTime(this, ms + 10);   
				} 
			};
			mHandler.postDelayed(mUpdateTimeTask, 10);
		}
}
