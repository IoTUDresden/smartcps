package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientRobot;

public class EditRobotDialog extends DialogFragment {
	
	private int robotIndex;
	private String name, ip, port;
	private LinearLayout dialogView;
	private FragmentCallback controller;
	private ClientRobot robot;
	private ClientRobot newClientRobot;
	private EditText nameView, ipView, portView;
	private Position robot_position;
	private Orientation robot_orientation;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    controller = (FragmentCallback) getActivity();
	    
	    //retrieve robot by the passed index
	    robotIndex = getArguments().getInt("robotListIndex", 0);
	    robot = SmartCPS_Impl.getNavigator().getRobots().get(robotIndex);
	    
	    //get Location of the robot
	    robot_position = robot.getPosition();
	    robot_orientation = robot.getOrientation();
	    
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        dialogView = (LinearLayout) inflater.inflate(R.layout.robo_view_fragment_edit_robot_dialog, null);
        
        nameView = (EditText)dialogView.findViewById(R.id.edit_robot_instance_name);        
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
				TextView feedback = (TextView) dialogView.findViewById(R.id.duplicate_edit_robot_name);
				//check first if name has changed
				if(!robot.getName().equals(s.toString())){
					if(!SmartCPS_Impl.getNavigator().checkRoboName(s.toString())){
				   		feedback.setVisibility(View.VISIBLE);
					}else{
						feedback.setVisibility(View.GONE);
					}
				 }
			}
        });
        builder.setView(dialogView);
        builder.setTitle("Edit Robot:")
        	   .setIcon(R.drawable.ic_edit_dark)
               .setPositiveButton("OK", new OkOnClickListener())
               .setNegativeButton("Cancel", new CancelOnClickListener());
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		//set the values of the dialog to the values of the clicked robot
	    //name of the robot
		nameView.setText(robot.getName());
		
		//ip of the robot
		ipView = (EditText) dialogView.findViewById(R.id.edit_robot_instance_ip);		
		ipView.setText(robot.getIp());
		
		//port of the robot
		portView = (EditText) dialogView.findViewById(R.id.edit_robot_instance_port);		
		portView.setText(robot.getPort());
	}

    
   private final class CancelOnClickListener implements DialogInterface.OnClickListener {
	   public void onClick(DialogInterface dialog, int which) {
	   }
	}

   private final class OkOnClickListener implements DialogInterface.OnClickListener {
	   public void onClick(DialogInterface dialog, int which) {
		   	
		    //name of the robot
			name = nameView.getText().toString();
			
			//ip of the robot
			ip = ipView.getText().toString();
			
			//port of the robot
			port = portView.getText().toString();

			newClientRobot = new ClientRobot(name, ip, port, robot.getType());
			newClientRobot.setPosition(robot_position);
			newClientRobot.setOrientation(robot_orientation);
	        
		   //if name changed, check if it already exists
		   if(!robot.getName().equals(name)){
			   if(SmartCPS_Impl.getNavigator().checkRoboName(name)){	   
				   //add item to fragment list
				   controller.editRobot(robotIndex, newClientRobot);
			   }else{
				   	Toast.makeText(getActivity().getApplicationContext(), "Robot has not been edited...",
							   Toast.LENGTH_LONG).show();
			   }
		   }else{
			   //add item to fragment list
			   controller.editRobot(robotIndex, newClientRobot);
		   }
		}
   }	
}


