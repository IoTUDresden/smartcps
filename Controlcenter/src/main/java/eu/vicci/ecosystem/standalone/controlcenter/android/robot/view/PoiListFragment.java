package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import eu.vicci.driver.robot.location.DockingStation;
import eu.vicci.driver.robot.location.NamedLocation;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.controller.SwipeDismissTouchListener;
import eu.vicci.turtlebot.navigationapp.controller.MapController;

public class PoiListFragment extends Fragment{
	
	private LayoutInflater inflater;
	private LinearLayout listContainer;
	private LinearLayout poiList;
	private AddPoiDialog addPoiDialog;
	private ImageButton addPoiButton;
	private ImageButton collapsePoisButton;
	private LinearLayout poiListView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
		this.inflater = inflater;
		listContainer = (LinearLayout)inflater.inflate(R.layout.robo_view_fragment_poi_list,
				container, false);
		poiList = (LinearLayout) listContainer.findViewById(R.id.poi_list);
		
		//init list: items to list when already some exist
		List<NamedLocation> pois = SmartCPS_Impl.getNavigator().getPois();
		for(NamedLocation poi : pois){
			this.pushListEntry(poi);
		}
		
    	return listContainer;
    }
	 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//add on click listener for the addPoi dialog
		addPoiDialog = new AddPoiDialog();
		addPoiButton = (ImageButton)getActivity().findViewById(R.id.add_poi_button);
		addPoiButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(SmartCPS_Impl.getNavigator().getMaps().size() > 0){
					if (MapController.getRecentlyLongPressedLocationSet() == false) {
						Toast.makeText(getActivity().getApplicationContext(), "Please select a position on the map first!",
								   Toast.LENGTH_LONG).show();
					} else {
						addPoiDialog.show(getFragmentManager(),"add_poi_fragment_dialog");
					}
				}else{
					//addPoiDialog.show(getFragmentManager(),"add_poi_fragment_dialog");
					Toast.makeText(getActivity().getApplicationContext(), "Please add a map first!",
							   Toast.LENGTH_LONG).show();
			    }
			}
	    });
		
		//on click listener for the collapse poi list button
		collapsePoisButton = (ImageButton)getActivity().findViewById(R.id.collapse_pois_button);
		poiListView = (LinearLayout)getActivity().findViewById(R.id.poi_list);
		collapsePoisButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 if (poiListView.getVisibility()==View.GONE){
					 ListAnimator.expand(poiListView, collapsePoisButton);
		         }else{
		             ListAnimator.collapse(poiListView, collapsePoisButton);
		         }
			}
		});
		
		//init list: items to list when already some exist
		List<NamedLocation> pois = SmartCPS_Impl.getNavigator().getPois();
		if(pois.size()==0){
			collapsePoisButton.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	* adds an item to the poi list, UI is updated immediately
	*/
	public void pushListEntry(NamedLocation poi){
		
		//initially button is hidden
		if(collapsePoisButton != null){
			if(collapsePoisButton.getVisibility()==View.INVISIBLE){
				collapsePoisButton.setVisibility(View.VISIBLE);
			}
		}
		//build poi enty
		RelativeLayout item = this.buildEntry(poi);
		
		//add entry to list
		poiList.addView(item);
		poiList.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	/**
	* updates an item of the poi list, after editing it
	*/
	public void updateListEntry(int index, NamedLocation poi){
		//remove the old entry
		poiList.removeViewAt(index);
		
		//build the list entry for the robot
		RelativeLayout item = this.buildEntry(poi);
		
		//add new entry
		poiList.addView(item, index);
		poiList.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	/**
	* builds an UI list entry for the passed poi
	*/
	public RelativeLayout buildEntry(NamedLocation poi){
		
		//get the entry-layout
		final RelativeLayout item = (RelativeLayout) inflater.inflate(R.layout.robo_view_poi_list_item, null);
		
		//get the sub elements
		ImageView icon = (ImageView)item.findViewById(R.id.poi_icon);
		TextView name = (TextView)item.findViewById(R.id.context_poi_name);
		ImageButton editButton = (ImageButton)item.findViewById(R.id.poi_edit_button);
		
		//set elements
		name.setText(poi.getName());
		
		//set img by type
		if (poi instanceof DockingStation) {
			icon.setImageResource(R.drawable.ic_poi_loadingstation);
		} else {
			icon.setImageResource(R.drawable.ic_poi_default_marker);
		}
		
		
		editButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//retireve Name of robot from UI
				RelativeLayout item = (RelativeLayout)v.getParent();
				TextView nameView = (TextView) item.findViewById(R.id.context_poi_name);
				String name = nameView.getText().toString();
				
				//retieve index by name
				int index = SmartCPS_Impl.getNavigator().getIndexOfPoiByName(name);
				EditPoiDialog editDialog = new EditPoiDialog();
				Bundle args = new Bundle();
			    args.putInt("poiListIndex", index);
			    editDialog.setArguments(args);
				editDialog.show(getFragmentManager(),"edit_poi_fragment_dialog");
			}
	    });
		
		// add swipe out gesture to delete an entry
		item.setOnTouchListener(new SwipeDismissTouchListener(
                item,
                null,
                new SwipeDismissTouchListener.DismissCallbacks() {

                    @Override
                    public void onDismiss(View view, Object token) {

		                 //retireve Name of robot from UI
		   				TextView nameView = (TextView) item.findViewById(R.id.context_poi_name);
		   				String name = nameView.getText().toString();
		   				
		   				//retieve index by name
		   				int index = SmartCPS_Impl.getNavigator().getIndexOfPoiByName(name);
		   				//delete in model
		   				SmartCPS_Impl.getNavigator().deletePoi(index);
		   				//delete in UI
		   				poiList.removeView(item);
		   				//when last element deleted hide collapse/expand button
		   				if(SmartCPS_Impl.getNavigator().getPois().size()<1){
	   						collapsePoisButton.setVisibility(View.INVISIBLE);
		   				}
                    }

					@Override
					public boolean canDismiss(Object token) {
						return true;
					}
                }));
		
		return item;
	}
}
