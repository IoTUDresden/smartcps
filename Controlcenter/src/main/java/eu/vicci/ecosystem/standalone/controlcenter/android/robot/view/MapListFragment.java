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
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.controller.SwipeDismissTouchListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientMap;

public class MapListFragment extends Fragment{
	
	private LayoutInflater inflater;
	private LinearLayout listContainer;
	private ViewGroup mapList;
	private AddMapDialog addMapDialog;
	private ImageButton addMapButton;
	private ImageButton collapseMapsButton;
	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		this.inflater = inflater;
		listContainer = (LinearLayout)inflater.inflate(R.layout.robo_view_fragment_map_list,
				container, false);
		mapList = (ViewGroup) listContainer.findViewById(R.id.map_list);
		
		//init list: items to list when already some exist
		List<ClientMap> maps = SmartCPS_Impl.getNavigator().getMaps();
		for(ClientMap map : maps){
			this.pushListEntry(map);
		}
		
	    return listContainer;
	 }
	 
	 @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
	    //on click listener for the addMap dialog
		addMapDialog = new AddMapDialog();
		addMapButton = (ImageButton)getActivity().findViewById(R.id.add_map_button);
		addMapButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 addMapDialog.show(getFragmentManager(),"add_map_fragment_dialog");	
			}
	    });
		
		//getActivity().findViewById(R.id.add_map_button);
		
		//on click listener for the collapse map list button
		collapseMapsButton = (ImageButton)getActivity().findViewById(R.id.collapse_maps_button);
		final LinearLayout mapListView = (LinearLayout)getActivity().findViewById(R.id.map_list);
		collapseMapsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 if (mapListView.getVisibility()==View.GONE){
					 ListAnimator.expand(mapListView, collapseMapsButton);
		         }else{
		             ListAnimator.collapse(mapListView, collapseMapsButton);
		         }
			}
		});
		
		//init list: items to list when already some exist
		List<ClientMap> maps = SmartCPS_Impl.getNavigator().getMaps();
		if(maps.size() == 0){
			collapseMapsButton.setVisibility(View.INVISIBLE);
		}
	  }
	 
	/**
	* adds an item to the poi list, UI is updated immediately
	*/
	public void pushListEntry(ClientMap clientMap){
		
		//initially button is hidden
		if(collapseMapsButton != null){
			if(collapseMapsButton.getVisibility()==View.INVISIBLE){
				//Quickfix
				collapseMapsButton.setVisibility(View.VISIBLE);
			}
		}
		
		//build entry
		RelativeLayout item = this.buildEntry(clientMap);		
		//add entry to list
		mapList.addView(item);
		mapList.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	/**
	* updates an item of the map list, after editing it
	*/
	public void updateListEntry(int index, ClientMap map){
		//remove the old entry
		mapList.removeViewAt(index);
		
		//build the list entry for the robot
		RelativeLayout item = this.buildEntry(map);
		
		//add new entry
		mapList.addView(item, index);
		mapList.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	/**
	* builds an UI list entry for the passed map
	*/
	public RelativeLayout buildEntry(ClientMap map){
		//get the entry-layout
		final RelativeLayout item = (RelativeLayout) inflater.inflate(R.layout.robo_view_map_list_item, null);
		//get the sub elements
		ImageView icon = (ImageView)item.findViewById(R.id.map_icon);
		TextView name = (TextView)item.findViewById(R.id.context_map_name);
		final ImageButton editButton = (ImageButton)item.findViewById(R.id.map_edit_button);
		
		//set elements
		name.setText(map.getName());
		
		//set map img 
		icon.setImageResource(R.drawable.ic_map);
		
		//set click listeners
		editButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//retireve Name of robot from UI
				RelativeLayout item = (RelativeLayout)v.getParent();
				TextView nameView = (TextView) item.findViewById(R.id.context_map_name);
				String name = nameView.getText().toString();
				
				//retieve index by name
				int index = SmartCPS_Impl.getNavigator().getIndexOfMapByName(name);
				EditMapDialog editDialog = new EditMapDialog();
				Bundle args = new Bundle();
			    args.putInt("mapListIndex", index);
			    editDialog.setArguments(args);
				editDialog.show(getFragmentManager(),"edit_map_fragment_dialog");
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
		   				TextView nameView = (TextView) item.findViewById(R.id.context_map_name);
		   				String name = nameView.getText().toString();
		   				
		   				//retieve index by name
		   				int index = SmartCPS_Impl.getNavigator().getIndexOfMapByName(name);
		   				//delete in model
		   				SmartCPS_Impl.getNavigator().deleteMap(index);
		   				//delete in UI
		   				mapList.removeView(item);
		   				//when last element deleted hide collapse/expand button and show default map image
		   				if(SmartCPS_Impl.getNavigator().getMaps().size()<1){
	   						collapseMapsButton.setVisibility(View.INVISIBLE);
	   						((NavBaseActivity)getActivity()).showMapDefault();
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


