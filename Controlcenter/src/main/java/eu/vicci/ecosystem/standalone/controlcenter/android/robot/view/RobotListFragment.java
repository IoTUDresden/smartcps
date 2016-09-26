package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.controller.SwipeDismissTouchListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientRobot;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.Navigator;
import eu.vicci.turtlebot.navigationapp.controller.MapController;
import eu.vicci.turtlebot.navigationapp.helperclasses.RobotManager;

public class RobotListFragment extends Fragment {

	private LayoutInflater inflater;
    private LinearLayout robotList;
	private AddRobotDialog addRobotDialog;
    private ImageButton collapseRobotsButton;
	private LinearLayout robotListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
        LinearLayout listContainer = (LinearLayout) inflater.inflate(
                R.layout.robo_view_fragment_robot_list, container, false);
		robotList = (LinearLayout) listContainer.findViewById(R.id.robot_list);

		// init list: add for each robot an item to the list
		List<ClientRobot> robots = Navigator.getRobots();
		for (ClientRobot robo : robots) {
			this.pushListEntry(robo);
		}
		return listContainer;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// create add robot dialog
		addRobotDialog = new AddRobotDialog();

		// on click listener for the addRobot dialog
        ImageButton addRobotButton = (ImageButton) getActivity().findViewById(
                R.id.add_robot_button);
		addRobotButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Navigator.getMaps().size() > 0) {
                    if (!MapController.getRecentlyLongPressedLocationSet()) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please select a position on the map first!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        addRobotDialog.show(getFragmentManager(),
                                "add_robot_fragment_dialog");
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Please add a map first!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

		// on click listener for the collapse robot list button
		collapseRobotsButton = (ImageButton) getActivity().findViewById(
				R.id.collapse_robots_button);
		robotListView = (LinearLayout) getActivity().findViewById(
				R.id.robot_list);
		collapseRobotsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (robotListView.getVisibility() == View.GONE) {
					ListAnimator.expand(robotListView, collapseRobotsButton);
				} else {
					ListAnimator.collapse(robotListView, collapseRobotsButton);
				}
			}
		});

		// hide expand and collapse button when no item exist
		List<ClientRobot> robots = Navigator.getRobots();
		if (robots.size() == 0) {
			collapseRobotsButton.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * adds an item to the robot list, UI is updated immediately
	 */
	public void pushListEntry(ClientRobot clientRobot) {

		// initially button is hidden
		if (collapseRobotsButton != null) {
			if (collapseRobotsButton.getVisibility() == View.INVISIBLE) {
				collapseRobotsButton.setVisibility(View.VISIBLE);
			}
		}

		// build the list entry for the robot
		final RelativeLayout item = this.buildEntry(clientRobot);

		// add entry to list
		robotList.addView(item);
		robotList.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	/**
	 * updates an item to the robot list, after editing it
	 */
	public void updateListEntry(int index, ClientRobot clientRobot) {
		// remove the old entry
		robotList.removeViewAt(index);

		// build the list entry for the robot
		RelativeLayout item = this.buildEntry(clientRobot);

		// add new entry
		robotList.addView(item, index);
		robotList.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	/**
	 * builds an UI list entry for the passed robot
	 */
	public RelativeLayout buildEntry(ClientRobot clientRobot) {

		// get the entry-layout
		final RelativeLayout item = (RelativeLayout) inflater.inflate(
				R.layout.robo_view_robot_list_item, null);

		// get the sub elements
		ImageView icon = (ImageView) item.findViewById(R.id.robot_icon);
		TextView name = (TextView) item.findViewById(R.id.context_robot_name);
		ImageButton editButton = (ImageButton) item
				.findViewById(R.id.robot_edit_button);
		ImageButton videoButton = (ImageButton) item
				.findViewById(R.id.robot_video_button);

		// set elements
		if (clientRobot.getType() != null) {
			switch (clientRobot.getType()) {
			case Turtlebot:
				icon.setImageResource(R.drawable.ic_robo_turtlebot_light);
				break;
			case Youbot:
				icon.setImageResource(R.drawable.ic_robo_youbot_light);
				break;
			default:
				icon.setImageResource(R.drawable.ic_robo_default_light);
				break;
			}
		} else {
			// nao bot is not yet implemented so the type is set to null (null
			// can not be detected in java switch)
			icon.setImageResource(R.drawable.ic_robo_naobot_light);
			Toast.makeText(getActivity().getApplicationContext(),
					"Naobot is not yet implemented.", Toast.LENGTH_LONG).show();
		}
		name.setText(clientRobot.getName());

		editButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// retrieve Name of robot from UI
				RelativeLayout item = (RelativeLayout) v.getParent();
				TextView nameView = (TextView) item
						.findViewById(R.id.context_robot_name);
				String name = nameView.getText().toString();

				// retieve index by name
				int index = Navigator.getIndexOfRoboByName(
                        name);
				EditRobotDialog editDialog = new EditRobotDialog();
				Bundle args = new Bundle();
				args.putInt("robotListIndex", index);
				editDialog.setArguments(args);
				editDialog.show(getFragmentManager(),
						"edit_robot_fragment_dialog");
			}
		});

		videoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RelativeLayout item = (RelativeLayout) v.getParent();
				TextView nameView = (TextView) item
						.findViewById(R.id.context_robot_name);
				String name = nameView.getText().toString();
				int idx = Navigator.getIndexOfRoboByName(name);
				ClientRobot selectedRobot = null;
				if(idx >= 0){
					selectedRobot = Navigator.getRobots().get(idx);
				}
				
				if (selectedRobot != null) {
					RobotClientManager.getInstance().establishConnection(
							selectedRobot.getIp(), selectedRobot.getPort());
					Intent intent = new Intent(getActivity(),
							ControlActivity.class);
					intent.putExtra("robotName", name);
					startActivity(intent);
				} else {
					Log.wtf(getTag(), "could not find robot");
				}
			}
		});
		
		//add Listener to select a Robot from the List
		item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleRobot(v);
			}
		});
		
		// add swipe out gesture to delete an entry
		item.setOnTouchListener(new SwipeDismissTouchListener(item, null,
				new SwipeDismissTouchListener.DismissCallbacks() {

					@Override
					public void onDismiss(View view, Object token) {

						// retireve Name of robot from UI
						TextView nameView = (TextView) item
								.findViewById(R.id.context_robot_name);
						String name = nameView.getText().toString();

						// retieve index by name
						int index = Navigator
								.getIndexOfRoboByName(name);
						// delete in model
						SmartCPS_Impl.getNavigator().deleteRobot(index);
						// delete in UI
						robotList.removeView(item);
						// when last element deleted hide collapse/expand button
						if (Navigator.getRobots().size() < 1) {
							collapseRobotsButton.setVisibility(View.INVISIBLE);
						}

					}

					@Override
					public boolean canDismiss(Object token) {
						return true;
					}
				}));
		
		return item;
	}
	
	
	/**
	 * select or unselect robot in the list and map
	 */
	public void toggleRobot(View entry) {

		// retrieve Name of robot from UI
		RelativeLayout listItem = (RelativeLayout) entry;
		TextView nameView = (TextView) listItem
				.findViewById(R.id.context_robot_name);
		String name = nameView.getText().toString();

		// retrieve index by name
		int index = Navigator.getIndexOfRoboByName(
                name);
				
		ClientRobot selectedRobo = Navigator.getRobots().get(index);
		ClientRobot activeRobo = SmartCPS_Impl.getNavigator().getActiveRobot();
				
		//check if clicked robot is active robot
		if(selectedRobo != activeRobo){
			//reset
			unselectAllRobots();
			
			//backend
			RobotManager.getInstance().getRobotList().get(index).setSelected(true);
			
			//select 
			entry.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
			nameView.setTextColor(Color.WHITE);
			SmartCPS_Impl.getNavigator().setActiveRobot(index);
		}else{
			//unselect all
			unselectAllRobots();
		}		
	}
	
	/**
	 * reset all robot list entries
	 */
	public void unselectAllRobots(){
		
		SmartCPS_Impl.getNavigator().setActiveRobot(-1);
		//unselect in backend
		RobotManager.getInstance().setAllUnselected();

		int count = robotList.getChildCount();
		View v;
		for(int i=0; i<count; i++) {
		    v = robotList.getChildAt(i);
			v.setBackgroundColor(getResources().getColor(R.color.list_background_grey));
			((TextView)v.findViewById(R.id.context_robot_name)).setTextColor(getResources().getColor(R.color.list_item_text_grey));  
		}
	}
}
