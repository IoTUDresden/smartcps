package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;

public class TouchControlFragment extends Fragment{
	private LayoutInflater inflater;
	private RelativeLayout touchControlView;
	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 			
			this.inflater = inflater;
			touchControlView = (RelativeLayout)inflater.inflate(R.layout.control_view_touchcontrol,
					container, false);
		    return touchControlView;
	  }

}
