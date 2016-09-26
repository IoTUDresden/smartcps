package eu.vicci.ecosystem.standalone.controlcenter.android.visualization;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;

public class RobotInfo extends InfoVisusalization {
	private TextView robotIpTextView;
	private TextView isConnectTextView;
	private TextView hasGrabberTextView;
	
	public RobotInfo(Context context) {
		this(context, null);
	}

	public RobotInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		View.inflate(context, R.layout.robot_info, this);
		isConnectTextView = (TextView)findViewById(R.id.robot_is_connected);
		robotIpTextView = (TextView)findViewById(R.id.robot_ip);
		hasGrabberTextView = (TextView)findViewById(R.id.robot_has_grabber);
	}

	/**
	 * String[] für die Werte. Werte müssen der folgenden Reihenfolge entsprechen
	 * 0: IP - 1: HasGrabber - 2: IsConnected
	 */
	@Override
	public void setValue(String[] value) {
		//setRobotIp(value);
		if(value == null || value.length < 3)
			return;
		robotIpTextView.setText(value[0]);
		hasGrabberTextView.setText(value[1]);
		isConnectTextView.setText(value[2]);
	}
	
	public void setRobotIp(String value){
		robotIpTextView.setText(value);
	}
	
	public void setHasGrabber(String value){
		hasGrabberTextView.setText(value);
	}
	
	public void setIsConnected(String value){
		isConnectTextView.setText(value);
	}

}
