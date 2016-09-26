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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import eu.vicci.driver.robot.location.Site;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;
import eu.vicci.driver.turtlebot.location.TurtleBotDockingStation;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.Navigator;
import eu.vicci.turtlebot.navigationapp.controller.MapController;

public class AddPoiDialog extends DialogFragment {
	
	private FragmentCallback controller;
	private LinearLayout dialogView;
	private EditText nameView;
	private String name, desc;
	private Position poi_position;
	private Orientation poi_orientation;
	private Spinner spinner;
	
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		controller = (FragmentCallback) getActivity();
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        dialogView = (LinearLayout) inflater.inflate(R.layout.robo_view_fragment_add_poi_dialog, null);
        
        //add drop down list for poi category
        spinner = (Spinner) dialogView.findViewById(R.id.poi_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.poi_array, R.layout.robo_view_poi_spinner);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
                
        //set correct default name       
        nameView = (EditText)dialogView.findViewById(R.id.poi_name);
        //String poiNum = Integer.toString(SmartCPS_Impl.getNavigator().getPois().size() +1);
        nameView.setText(Navigator.getMinPoiName());
        
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
				TextView feedback = (TextView) dialogView.findViewById(R.id.duplicate_poi_name);
				if(!SmartCPS_Impl.getNavigator().checkPoiName(s.toString())){
			   		feedback.setVisibility(View.VISIBLE);
				}else{
					feedback.setVisibility(View.GONE);
				}
			}	
        });
         
        builder.setView(dialogView);
        builder.setTitle("Point of Interest Properties:")
        	   .setIcon(R.drawable.ic_add_poi_marker_dark)
               .setPositiveButton("Add Point of Interest", new OkOnClickListener())
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
			//get data for new poi
			name = nameView.getText().toString();	
			
			//maybe check whether an equal poi is in the list of pois
			if(SmartCPS_Impl.getNavigator().checkPoiName(name)){
						
				//room = ((EditText) dialogView.findViewById(R.id.poi_room)).getText().toString();
				desc = ((EditText) dialogView.findViewById(R.id.poi_description)).getText().toString();
				
				//set Location of the poi
				//poi_position = new Position(0,0,0);
				//poi_orientation = new Orientation(0,0,0,0);
				poi_position = MapController.getRecentlyLongPressedLocation().getPosition(); //position of the recently touched location
				poi_orientation = MapController.getRecentlyLongPressedLocation().getOrientation(); //orientation of the recently touched location
				
				//set type of poi
				String typeString =  (String) spinner.getSelectedItem();
				if(typeString.equals("Dockingstation")){
					TurtleBotDockingStation poi = new TurtleBotDockingStation(name, poi_position, poi_orientation);
					controller.addPoi(poi);
				} else {
					Site poi = new Site(name, poi_position, poi_orientation);
					poi.setDescription(desc);
					controller.addPoi(poi);
				}
				
				//undraw recently long pressed location
				MapController.setRecentlyLongPressedLocationSet(false);
				
			}else{
			   	Toast.makeText(getActivity().getApplicationContext(), "No Poi has been added...",
						   Toast.LENGTH_LONG).show();
		   	}
	   }
   }

}

