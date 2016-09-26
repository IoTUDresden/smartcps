package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;

public class ControlListFragment extends Fragment {
	private LayoutInflater inflater;
	private LinearLayout listContainer;
	private LinearLayout controlList;
	private ImageButton collapseControlButton;
	private LinearLayout controlListView;
	private Switch gyroSwitch;
	private Switch touchSwitch;
	private Button disconnectButton;
	private boolean gyroControlActive = false;
	private int batteryState;
	private RelativeLayout touchControl;
	private TextView batteryText;
	private ImageView imageView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		listContainer = (LinearLayout) inflater.inflate(
				R.layout.control_view_fragment_controllist, container, false);
		controlList = (LinearLayout) listContainer
				.findViewById(R.id.control_list);
		return listContainer;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RelativeLayout gyroItem = (RelativeLayout) inflater.inflate(
				R.layout.control_view_controllist_gyro, null);
//		batteryText = (TextView) getActivity().findViewById(
//				R.id.battery_state_valuetv);
		
		
		
		gyroSwitch = (Switch) gyroItem.findViewById(R.id.switch_gyrocontrol);
		gyroSwitch.setChecked(false);
		controlList.addView(gyroItem);
		gyroSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				setGyroControlActive(isChecked);
//				if (isChecked)
//					touchSwitch.setChecked(false);
			}
		});

//		RelativeLayout touchItem = (RelativeLayout) inflater.inflate(
//				R.layout.control_view_controllist_touch, null);
//		touchSwitch = (Switch) touchItem.findViewById(R.id.switch_touchcontrol);
//		controlList.addView(touchItem);
//
//		touchSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//				// do something, the isChecked will be
//				// true if the switch is in the On position
//				if (isChecked)
//					gyroSwitch.setChecked(false);
//			}
//		});
		
//		touchItem.setVisibility(View.GONE);

		ImageView imageView = (ImageView) getActivity().findViewById(
				R.id.portListAddButton);
		imageView.setOnTouchListener(new OnTouchListener()
        {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				return false;
			}
       });
		
		//disconnectButton = (Button) getActivity().findViewById(
		//		R.id.button_disconnect_robot);
		/*disconnectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
				RobotClientManager.getInstance().disconnect();
			}
		});*/
		//disconnectButton.setVisibility(View.GONE);

		// on click listener for the collapse robot list button
		collapseControlButton = (ImageButton) getActivity().findViewById(
				R.id.collapse_controls_button);
		controlListView = (LinearLayout) getActivity().findViewById(
				R.id.control_list);
		collapseControlButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (controlListView.getVisibility() == View.GONE) {
					ListAnimator.expand(controlListView, collapseControlButton);
				} else {
					ListAnimator.collapse(controlListView,
							collapseControlButton);
				}
			}
		});

	}

	public boolean isGyroControlActive() {
		return gyroControlActive;
	}

	public void setGyroControlActive(boolean gyroControlActive) {
		this.gyroControlActive = gyroControlActive;
	}

	public float getBatteryState() {
		return batteryState;
	}

}
