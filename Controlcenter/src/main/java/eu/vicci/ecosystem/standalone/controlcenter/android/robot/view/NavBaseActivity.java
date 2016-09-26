package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;


import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import eu.vicci.driver.robot.location.NamedLocation;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientMap;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientRobot;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ItemDatabaseHandler;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.Navigator;
import eu.vicci.turtlebot.navigationapp.helperclasses.PoiManager;

/**
* The Entry & main view for robot-navigation and location functionality
* on a map 
* 
*/
public class NavBaseActivity extends Activity implements FragmentCallback{
	
	private RobotListFragment roboListFrag;
	private PoiListFragment poiListFrag;
	private MapListFragment mapListFrag;
	private AddMapDialog addMapDialog;
	
	public static ItemDatabaseHandler database; //source of the database content
	
	
	/**
	* Called when the Activity is created and initiates Objects and Listeners 
	*/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nav_base);
		addMapDialog = new AddMapDialog();
		
		//add robot list fragment
		roboListFrag = new RobotListFragment();
        FragmentTransaction robotTrans = getFragmentManager().beginTransaction();
        robotTrans.add(R.id.robo_view_robot_list, roboListFrag);
        robotTrans.commit();
        
		//add poi list fragment
		poiListFrag = new PoiListFragment();
        FragmentTransaction poiTrans = getFragmentManager().beginTransaction();
        poiTrans.add(R.id.robo_view_poi_list, poiListFrag);
        poiTrans.commit();
        
		//add map list fragment
		mapListFrag = new MapListFragment();
        FragmentTransaction mapTrans = getFragmentManager().beginTransaction();
        mapTrans.add(R.id.robo_view_map_list, mapListFrag);
        mapTrans.commit();
        
        //show map when already one is loaded
        if(SmartCPS_Impl.getNavigator().getActiveMap()!=null){
    		//show map in fragment
    		MapFragment mapFrag = new MapFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.robo_view_map, mapFrag);
            ft.commit();
        }else{
        	//show default add map image
        	this.showMapDefault();
        }
        
        //initialize and open the database
      	database = new ItemDatabaseHandler(this);
      	database.open();
      	
      	//get items from the database and put them into the lists
      	//KP SEUS 2014: database implemented for any kind of items, only Pois loaded
      	//List<ClientRobot> robots = database.getRobots();
      	List<NamedLocation> pois = database.getPois();
      	//List<ClientMap> maps = database.getMaps();
      	
      	//set item lists in Navigator
      	//KP SEUS 2014: database implemented for any kind of items, only Pois loaded
      	//Navigator.setRobots(robots);
      	Navigator.setPois(pois);
      	//Navigator.setMaps(maps);
      	
      	PoiManager.setPois(pois);
	}
	
	/**
	* Show default image in the map frame
	*/
	public void showMapDefault(){
		FrameLayout frame = (FrameLayout)findViewById(R.id.robo_view_map);
		frame.removeAllViews();
		View contentImage = getLayoutInflater().inflate(R.layout.robo_view_default_map_frame, null);
		
		
		//set click listeners
		contentImage.findViewById(R.id.default_map_view).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//show addMap Dialog
				addMapDialog.show(getFragmentManager(),"add_map_fragment_dialog");	
			}
	    });
		
    	frame.addView(contentImage);
	}
	
	/**
	* Called when the ActionBar is created for Activity 
	*/
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	/**
	* Called when Item in ActionBar is selected
	*/
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	* callback methods are triggered when an item is added to one of the 3 lists:
	*/
	@Override
	public void addRobot(ClientRobot clientRobot) {
		//TODO: delete Map as Client
		
		//add to model
		SmartCPS_Impl.getNavigator().addRobot(clientRobot);
		//update robot list fragment
		roboListFrag.pushListEntry(clientRobot);
		
	    //this will be moved to addMap() very soon: show map in fragment
		MapFragment mapFrag = new MapFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.robo_view_map, mapFrag);
        ft.commit();
	}

	@Override
	public void addPoi(NamedLocation poi) {
		//add to model
		SmartCPS_Impl.getNavigator().addPoi(poi);
		//update poi list fragment
		poiListFrag.pushListEntry(poi);
	}

	@Override
	public void addMap(ClientMap clientMap) {
		//add to model
		SmartCPS_Impl.getNavigator().addMap(clientMap);
		//update map list
		mapListFrag.pushListEntry(clientMap);
		
		//show map in fragment
		MapFragment mapFrag = new MapFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.robo_view_map, mapFrag);
        ft.commit();
	}
	
	
	/**
	* callback methods triggered when an item is edited in one of the 3 lists
	*/
	@Override
	public void editRobot(int index, ClientRobot clientRobot) {
		SmartCPS_Impl.getNavigator().updateRobot(index, clientRobot);
		roboListFrag.updateListEntry(index, clientRobot);
	}

	@Override
	public void editPoi(int index, NamedLocation poi) {
		SmartCPS_Impl.getNavigator().updatePoi(index, poi);
		poiListFrag.updateListEntry(index, poi);
	}

	@Override
	public void editMap(int index, ClientMap clientMap) {
		SmartCPS_Impl.getNavigator().updateMap(index, clientMap);
		mapListFrag.updateListEntry(index, clientMap);
	}
	
	@Override
	protected void onResume() {
		//RobotManager.connectRobots();
		database.open();
	    super.onResume();
	}

	@Override
	protected void onPause() {
		//RobotManager.disconnectRobots();
		database.close();
	    super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		//RobotManager.disconnectRobots();
		database.close();
		super.onDestroy();
	}	
	
}
