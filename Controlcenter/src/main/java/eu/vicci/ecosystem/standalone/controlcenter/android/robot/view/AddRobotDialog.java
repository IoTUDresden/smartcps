package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import eu.vicci.driver.robot.location.UnnamedLocation;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientMap;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientRobot;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.Navigator;
import eu.vicci.turtlebot.navigationapp.controller.MapController;
import eu.vicci.turtlebot.navigationapp.helperclasses.RobotManager;
import eu.vicci.turtlebot.navigationapp.model.RobotType;
public class AddRobotDialog extends DialogFragment {
	
	private String name, ip, port;
	private ClientMap clientMap;
	private boolean connecting= false;
	public static final int CONNECTION_TIMEOUT = 4000;
	private LinearLayout dialogView;
	private EditText nameView;
	private FragmentCallback controller;
	private RobotType robotType;
	private ClientRobot clientRobot;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    controller = (FragmentCallback) getActivity();
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        dialogView = (LinearLayout) inflater.inflate(R.layout.robo_view_fragment_add_robot_dialog, null);
        
        //set correct default name       
        nameView = (EditText) dialogView.findViewById(R.id.add_robot_instance_name);
        //String robotNum = Integer.toString(SmartCPS_Impl.getNavigator().getRobots().size() +1);
        nameView.setText(Navigator.getMinRobotName());
        
        //add type listener which checks if the name of the map already exists
        nameView.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				TextView feedback = (TextView) dialogView.findViewById(R.id.duplicate_robot_name);
				if(!SmartCPS_Impl.getNavigator().checkRoboName(s.toString())){
			   		feedback.setVisibility(View.VISIBLE);
				}else{
					feedback.setVisibility(View.GONE);
				}
			}	
        });
        
        builder.setView(dialogView);
        builder.setTitle("Robot Properties:")
        	   .setIcon(R.drawable.ic_add_robo_dark)
               .setPositiveButton("Add Robot", new OkOnClickListener())
               .setNegativeButton("Cancel", new CancelOnClickListener());
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
    
   private final class CancelOnClickListener implements DialogInterface.OnClickListener {
	   public void onClick(DialogInterface dialog, int which) {
	   }
	}

   private final class OkOnClickListener implements DialogInterface.OnClickListener {
	   public void onClick(DialogInterface dialog, int which) {
		   	
		   	//connect to robot
		    //name of the robot
			name = nameView.getText().toString();
			
			if(SmartCPS_Impl.getNavigator().checkRoboName(name)){
			
				//ip of the robot
				EditText ipView = (EditText) dialogView.findViewById(R.id.add_robot_instance_ip);		
				ip = ipView.getText().toString();
				
				//port of the robot
				EditText portView = (EditText) dialogView.findViewById(R.id.add_robot_instance_port);		
				port = portView.getText().toString();
				
				//set correct robot type
				robotType = null;
				RadioButton turtleRadio = (RadioButton) dialogView.findViewById(R.id.addRobot_checkbox_turtle);
				RadioButton naoRadio = (RadioButton) dialogView.findViewById(R.id.addRobot_checkbox_nao);
				RadioButton youRadio = (RadioButton) dialogView.findViewById(R.id.addRobot_checkbox_you);
	
				if(turtleRadio.isChecked()){
					robotType = RobotType.Turtlebot;
				}
				if(naoRadio.isChecked()){
					//nao bot not yet implemented as robottype
					//robotType = null;
				}
				if(youRadio.isChecked()){
					robotType = RobotType.Youbot;
				}
				if(!connecting) doConnect();
				
				//undraw recently long pressed location
				MapController.setRecentlyLongPressedLocationSet(false);
				
			}else{
			   	Toast.makeText(getActivity().getApplicationContext(), "No robot has been added...",
						   Toast.LENGTH_LONG).show();
		   	}
		}
   }	
   
   private void doConnect() {
		
	   
		AsyncTask<String, Integer, Boolean> task = new AsyncTask<String, Integer, Boolean>() {
			
			@Override
			protected Boolean doInBackground(String... params) {
				connecting = true;
				
				//get the current showed map and add to robot
				clientMap = SmartCPS_Impl.getNavigator().getActiveMap();
				
				if (clientMap != null) {
					//disablet for testing at home
					//connect to robot by using the same server as the actual map
					
					//set Location of the robot
					Position robot_position;
					Orientation robot_orientation;
					robot_position = MapController.getRecentlyLongPressedLocation().getPosition(); //position of the recently touched location
					robot_orientation = MapController.getRecentlyLongPressedLocation().getOrientation(); //orientation of the recently touched location
					
					if(robotType != null){
						RobotManager.getInstance().addRobot(name, clientMap.getIp(), clientMap.getPort(), true, robotType, new UnnamedLocation(robot_position, robot_orientation));
					}
				} else {
					connecting = false;
					return false;
				}				
				connecting = false;	
				return true;
			};
			
			@Override
		    protected void onPostExecute(Boolean result) {
				if(result==true){
					clientRobot = new ClientRobot(name, ip, port, robotType);
					
					//set Location of the robot
					clientRobot.setPosition(MapController.getRecentlyLongPressedLocation().getPosition()); //position of the recently touched location
					clientRobot.setOrientation(MapController.getRecentlyLongPressedLocation().getOrientation()); //orientation of the recently touched location
		         controller.addRobot(clientRobot);
				}
			}
		};
	task.execute();
					
	}

}

