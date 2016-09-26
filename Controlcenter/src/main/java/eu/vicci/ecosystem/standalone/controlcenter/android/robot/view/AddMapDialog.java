package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.controller.ConnectionDetector;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientMap;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.Navigator;
import eu.vicci.turtlebot.navigationapp.helperclasses.Map;
import eu.vicci.turtlebot.navigationapp.model.MapLoader;

public class AddMapDialog extends DialogFragment {
	private String name, source, ip, port;
	private Map map;
	private ClientMap clientMap;
	private FragmentCallback controller;
	private LinearLayout dialogView;
	private EditText nameView;
	private ConnectionDetector cd;
	private ProgressDialog loadDialog;
	public static final int CONNECTION_TIMEOUT = 4000;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		 controller = (FragmentCallback) getActivity();
		 cd = new ConnectionDetector((Context) controller);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        dialogView = (LinearLayout) inflater.inflate(R.layout.robo_view_fragment_add_map_dialog, null);
        
        //set correct default name       
        nameView = (EditText)dialogView.findViewById(R.id.map_name);
        //String mapNum = Integer.toString(SmartCPS_Impl.getNavigator().getMaps().size() +1);
        nameView.setText(Navigator.getMinMapName());
        
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
				if(!SmartCPS_Impl.getNavigator().checkMapName(s.toString())){
			   		feedback.setVisibility(View.VISIBLE);
				}else{
					feedback.setVisibility(View.GONE);
				}
			}	
        });
        
        builder.setView(dialogView);
        builder.setTitle("Map Properties:")
        	   .setIcon(R.drawable.ic_add_map_dark)
               .setPositiveButton("Add Map", new OkOnClickListener())
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
		   
		   	name = nameView.getText().toString();
		   	
		   	
		   	if(cd.check()){
			   	if(SmartCPS_Impl.getNavigator().checkMapName(name)){
				   	source = ((EditText) dialogView.findViewById(R.id.map_source)).getText().toString();
					   
					//ip of the map
					EditText ipView = (EditText) dialogView.findViewById(R.id.add_map_instance_ip);		
					ip = ipView.getText().toString();
					
					//port of the map
					EditText portView = (EditText) dialogView.findViewById(R.id.add_map_instance_port);		
					port = portView.getText().toString();
				   
					loadMap();
			   	}else{
				   	Toast.makeText(getActivity().getApplicationContext(), "Name exists already. No map has been added.",
							   Toast.LENGTH_LONG).show();
			   	}
		   	}else{
			   	Toast.makeText(getActivity().getApplicationContext(), "No Wifi connection!",
						   Toast.LENGTH_LONG).show();
		   	}
	   }
   } 
   
   public void loadMap() {
			
			AsyncTask<String, Integer, Boolean> MapLoader = new AsyncTask<String, Integer, Boolean>() {
				
				@Override
				protected void onPreExecute() 
				{
					new ProgressDialog((Context)controller);
					loadDialog = ProgressDialog.show((Context)controller, "", "Loading Map...");
				    super.onPreExecute();
				}
				
				@Override
				protected Boolean doInBackground(String... params) {
					
					//for testing at home
					/*map = null;
					return true;*/
					
					//get the map
					map = getMap(ip, port);
					if (map != null) {
						return true;
					} else {
						return false;
					}
				};
				
				@Override
			    protected void onPostExecute(Boolean result) {
					

				    if(loadDialog!=null && loadDialog.isShowing())
				    {
				        loadDialog.dismiss();
				    }
					
					if(result==true){
						//create our model map model
						clientMap = new ClientMap(name, source, ip, Integer.parseInt(port), map);	
					   
						//add item to fragment list
						controller.addMap(clientMap);
					}else{
						Toast.makeText(getActivity().getApplicationContext(), "Error while loading map.",
								   Toast.LENGTH_LONG).show();
					}
				}
	
			};
		MapLoader.execute();
   }
   
   public static Map getMap(String ip, String port) {
		if (ip.equals("") || port.equals(""))
			return null;
		MapLoader ml = new MapLoader(ip, port); // true means connection Test
		Thread t = new Thread(ml);
		t.start();
		long time = System.currentTimeMillis();
		long timer;
		while (!ml.isConnected()) {
			timer = System.currentTimeMillis() - time;
			if (timer < CONNECTION_TIMEOUT) {
				Thread.yield();
			} else {
				t.interrupt();
			}
		}
		if(ml.isConnected()==false) return null;
		time = System.currentTimeMillis();
		while (ml.getMap()==null) {
			timer = System.currentTimeMillis() - time;
			if (timer < CONNECTION_TIMEOUT) {
				Thread.yield();
			} else {
				t.interrupt();
			}
		}		
		
		t.interrupt();
		return ml.getMap();
		}
}

