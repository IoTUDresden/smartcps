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
import eu.vicci.driver.robot.location.NamedLocation;
import eu.vicci.driver.robot.location.Site;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;
import eu.vicci.driver.turtlebot.location.TurtleBotDockingStation;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;

public class EditPoiDialog extends DialogFragment {
	
	private FragmentCallback controller;
	private LinearLayout dialogView;
	private String name, desc;
	private Position poi_position;
	private Orientation poi_orientation;
	private int poiIndex;
	private NamedLocation poi;
	private EditText nameView, descView;
	private Spinner spinner;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		controller = (FragmentCallback) getActivity();
		 
		//retrieve poi by the passed index
		poiIndex = getArguments().getInt("poiListIndex", 0);
		poi = SmartCPS_Impl.getNavigator().getPois().get(poiIndex);
		
		//get Location of the poi
		poi_position = poi.getPosition();
		poi_orientation = poi.getOrientation();
		
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
        
        nameView = (EditText)dialogView.findViewById(R.id.poi_name);        
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
				//check first if name has changed
				if(!poi.getName().equals(s.toString())){
					if(!SmartCPS_Impl.getNavigator().checkPoiName(s.toString())){
				   		feedback.setVisibility(View.VISIBLE);
					}else{
						feedback.setVisibility(View.GONE);
					}
				 }
			}
        });
        
        builder.setView(dialogView);
        builder.setTitle("Edit Point of Interest:")
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
		
		//set the values of the dialog to the values of the clicked poi
	    //name of the poi
		nameView.setText(poi.getName());
		
		//room of the poi
		//roomView = (EditText) dialogView.findViewById(R.id.poi_room);		
		//roomView.setText(poi.getRoom());
	
	
		int spinnerIndex = 0;
		if (poi instanceof Site) {
	        spinnerIndex = 0;       
	        //description of the poi
			descView = (EditText) dialogView.findViewById(R.id.poi_description);		
			descView.setText(((Site)poi).getDescription());
			
		} else {
			spinnerIndex = 1;
		}
		
		//set spinner field
		spinner.setSelection(spinnerIndex);
	}

   private final class OkOnClickListener implements DialogInterface.OnClickListener {
	   public void onClick(DialogInterface dialog, int which) {
		   	name = nameView.getText().toString(); //name of the poi
			//poi_position = new Position(0,0,0); //position of the poi
			//poi_orientation = new Orientation(0,0,0,0); //orienmtation of the poi
					   
			
			//create poi
			String typeString =  (String) spinner.getSelectedItem();
			if (typeString.equals("Dockingstation")){
				TurtleBotDockingStation newPoi = new TurtleBotDockingStation(name, poi_position , poi_orientation);
				
				//if name changed, check if it already exists
			    if(!poi.getName().equals(name)){
				   if(SmartCPS_Impl.getNavigator().checkPoiName(name)){	   
					 //add to the UI list
						controller.editPoi(poiIndex, newPoi);
				   }else{
					   	Toast.makeText(getActivity().getApplicationContext(), "Poi has not been edited...",
								   Toast.LENGTH_LONG).show();
				   }
			    }else{
			    	//add to the UI list
					controller.editPoi(poiIndex, newPoi);
			    }
			} else {
				Site newPoi = new Site(name, poi_position , poi_orientation);
				newPoi.setDescription(desc);
				
				//if name changed, check if it already exists
			    if(!poi.getName().equals(name)){
				   if(SmartCPS_Impl.getNavigator().checkPoiName(name)){	   
					 //add to the UI list
						controller.editPoi(poiIndex, newPoi);
				   }else{
					   	Toast.makeText(getActivity().getApplicationContext(), "Poi has not been edited...",
								   Toast.LENGTH_LONG).show();
				   }
			    }else{
			    	//add to the UI list
					controller.editPoi(poiIndex, newPoi);
			    }
			}
	   }
   }
}
