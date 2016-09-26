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
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientMap;
import eu.vicci.turtlebot.navigationapp.helperclasses.Map;

public class EditMapDialog extends DialogFragment {
	private String name, source, ip;
	private int port;
	private Map map;
	private ClientMap clientMap, newClientMap;
	private FragmentCallback controller;
	private LinearLayout dialogView;
	private int mapIndex;
	private EditText nameView, sourceView, ipView, portView;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		 controller = (FragmentCallback) getActivity();
		 
		//retrieve map by the passed index
		mapIndex = getArguments().getInt("mapListIndex", 0);
		clientMap = SmartCPS_Impl.getNavigator().getMaps().get(mapIndex); 
		 
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        dialogView = (LinearLayout) inflater.inflate(R.layout.robo_view_fragment_add_map_dialog, null);
        
        nameView = (EditText)dialogView.findViewById(R.id.map_name);        
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
				TextView feedback = (TextView) dialogView.findViewById(R.id.duplicate_map_name);
				//check first if name has changed
				if(!clientMap.getName().equals(s.toString())){
					if(!SmartCPS_Impl.getNavigator().checkMapName(s.toString())){
				   		feedback.setVisibility(View.VISIBLE);
					}else{
						feedback.setVisibility(View.GONE);
					}
				 }
			}
        });
         
        builder.setView(dialogView);
        builder.setTitle("Edit Map:")
        	   .setIcon(R.drawable.ic_edit_dark)
               .setPositiveButton("OK", new OkOnClickListener())
               .setNegativeButton("Cancel", new CancelOnClickListener());
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
    
   private final class CancelOnClickListener implements DialogInterface.OnClickListener {
	   public void onClick(DialogInterface dialog, int which) {
	   }
	}
   
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		//set the values of the dialog to the values of the clicked map
	    //name of the map
		nameView.setText(clientMap.getName());
		
		//source of the map
		sourceView = (EditText) dialogView.findViewById(R.id.map_source);		
		sourceView.setText(clientMap.getSourceDir());
		
		//ip of the map server
		ipView = (EditText) dialogView.findViewById(R.id.add_map_instance_ip);		
		ipView.setText(clientMap.getIp());
		
		//port of the map server
		portView = (EditText) dialogView.findViewById(R.id.add_map_instance_port);		
		portView.setText(Integer.toString(clientMap.getPort()));
	}
   
   
   
   private final class OkOnClickListener implements DialogInterface.OnClickListener {
	   public void onClick(DialogInterface dialog, int which) {
		   
		   //name of the map
		   name = nameView.getText().toString();

		   //source of the map
		   source = sourceView.getText().toString();
		   //ip of the map server
		   ip = ipView.getText().toString();
		   //port of the server
		   port = Integer.parseInt(portView.getText().toString());
		   
		   //get map from server
		   //map = getMap(ip, port);
		   //todo: get map from server not from the robot!
		   map = null;
			
		   //create our model map model
		   newClientMap = new ClientMap(name, source, ip, port, map);	
				   
		   //if name changed, check if it already exists
		   if(!clientMap.getName().equals(name)){
			   if(SmartCPS_Impl.getNavigator().checkMapName(name)){	   
				   //add item to fragment list
				   controller.editMap(mapIndex, newClientMap);
			   }else{
				   	Toast.makeText(getActivity().getApplicationContext(), "Map has not been edited...",
							   Toast.LENGTH_LONG).show();
			   }
		   }else{
			   //add item to fragment list
			   controller.editMap(mapIndex, newClientMap);
		   }
	   }
   } 
}
