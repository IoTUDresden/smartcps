package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientRobot;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.Navigator;

public class ControlActivity extends Activity implements SensorEventListener {

	private ImageButton collapseMinimapButton;
	private FrameLayout miniMapContainer;
	private SensorManager sensorManager = null;
	private ProgressBar batteryBar = null;
	private float[] mGravity;
	private float[] mMagnetic;
	float[] rot_mat = new float[9];
	float[] vec = new float[3];
	float[] vecZ = { 0f, 0f, 1f };
	private float xValue = 0, yValue = 0;
	private int array_length = 100;
	private float[] averageX = new float[array_length];
	private float[] averageY = new float[array_length];
	private int xCount = 0, yCount = 0;
	private ControlListFragment clFrag;
	private MapFragment mapFrag;
	private CameraFragment camFrag;
	private boolean gyroControlActive = false;
	private String robotName;
	private String activityName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_center);
		robotName = getIntent().getStringExtra("robotName");
		activityName = getString(R.string.title_activity_control) + ": "
				+ robotName;
		setTitle(activityName);

		// on click listener for the collapse mini map button
		collapseMinimapButton = (ImageButton) findViewById(R.id.collapse_minimap_button);
		miniMapContainer = (FrameLayout) findViewById(R.id.robo_view_mini_map);
		collapseMinimapButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (miniMapContainer.getVisibility() == View.GONE) {
					MapAnimator.expand(miniMapContainer, collapseMinimapButton);
				} else {
					MapAnimator.collapse(miniMapContainer,
							collapseMinimapButton);
				}
			}
		});
		
		//show mini map 
		MiniMapFragment miniMapFrag = new MiniMapFragment();
		FragmentTransaction mapTrans = getFragmentManager().beginTransaction();
		mapTrans.add(R.id.robo_view_mini_map, miniMapFrag);
		mapTrans.commit();

		FragmentTransaction controlTrans = getFragmentManager()
				.beginTransaction();
		clFrag = new ControlListFragment();
		controlTrans.add(R.id.control_fragment_controlList, clFrag);
		controlTrans.commit();

		FragmentTransaction camTrans = getFragmentManager().beginTransaction();

		ClientRobot robot = Navigator.getRobots().get(Navigator.getIndexOfRoboByName(robotName));
		
		//load stream if robot is turtlebot
		switch (robot.getType()) {
			case Turtlebot:
				CameraFragment camFrag = new CameraFragment();
				camTrans.add(R.id.robo_view_camera, camFrag);
				camTrans.commit();
				break;
			case Youbot:
				break;
			case Naobot:
				break;
			default:
				break;
		}
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);

		// Check battery state every 10 seconds
		Timer myTimer = new Timer();
		final Handler uiHandler = new Handler();
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				uiHandler.post(new Runnable() {
					@Override
					public void run() {
						synchronized (this) {
							int battery = RobotClientManager.getInstance()
									.getRobot().getBatteryChargeInPercentage();
							setBatteryState(battery);
						}
					}
				});
			};
		}, 0L, 10000);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.control, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (clFrag.isGyroControlActive()) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				mGravity = event.values.clone();
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				mMagnetic = event.values.clone();
				break;
			default:
				return;
			}
			if (mGravity != null && mMagnetic != null) {
				synchronized (this) {
					SensorManager.getRotationMatrix(rot_mat, null, mGravity,
							mMagnetic);
					vec[0] = rot_mat[0];
					vec[1] = rot_mat[3];
					vec[2] = rot_mat[6];
					xValue = getAngle(vec, vecZ);
					vec[0] = -rot_mat[1];
					vec[1] = -rot_mat[4];
					vec[2] = -rot_mat[7];
					yValue = getAngle(vec, vecZ);

					xValue = (Math.abs(xValue) < 0.15f) ? 0 : xValue * 3;
					yValue = (Math.abs(yValue) < 0.15f) ? 0 : yValue * 3;

					if (xValue > 1.3)
						xValue = 1.3f;
					if (yValue > 1)
						yValue = 1;
					if (xValue < -1.3)
						xValue = -1.3f;
					if (yValue < -1)
						yValue = -1;

					xValue = averageValueOfX(xValue);
					yValue = averageValueOfY(yValue);
					RobotClientManager.getInstance().getRobot()
							.doDrive(-yValue * 0.4f, -xValue * 0.6f);
				}
			}
		}
	}

	private float averageValueOfX(float x) {
		float result = 0;
		averageX[xCount] = x;

		for (int i = 0; i < averageX.length; i++)
			result += averageX[i];
		result /= averageX.length;

		if (xCount == averageX.length - 1) {
			xCount = 0;
		} else {
			xCount++;
		}
		return result;
	}

	private float averageValueOfY(float y) {
		float result = 0;
		averageY[yCount] = y;

		for (int i = 0; i < averageY.length; i++)
			result += averageY[i];
		result /= averageY.length;

		if (yCount == averageY.length - 1) {
			yCount = 0;
		} else {
			yCount++;
		}
		return result;
	}

	private float getAngle(float[] vec1, float[] vec2) {
		float abs1 = (float) Math.sqrt((double) (vec1[0] * vec1[0] + vec1[1]
				* vec1[1] + vec1[2] * vec1[2]));
		float abs2 = (float) Math.sqrt((double) (vec2[0] * vec2[0] + vec2[1]
				* vec2[1] + vec2[2] * vec2[2]));

		float skalar = vec1[0] * vec2[0] + vec1[1] * vec2[1] + vec1[2]
				* vec2[2];

		float erg = skalar / (abs1 * abs2);
		erg = (float) Math.acos((double) erg);
		erg = (float) ((double) erg - (Math.PI / 2.0));
		return erg;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	public boolean isGyroControlActive() {
		return gyroControlActive;
	}

	public void setGyroControlActive(boolean gyroControlActive) {
		this.gyroControlActive = gyroControlActive;
	}

	public void setBatteryState(int batteryState) {
		if (batteryState < 0) {
			setTitle(activityName);
			return;
		}
		if (batteryState > 100)
			batteryState = 100;
		setTitle(activityName + " (Power: " + batteryState + " %)");
	}

	@Override
	protected void onResume() {
		// RobotManager.connectRobots();
		super.onResume();
	}

	@Override
	protected void onPause() {
		// RobotManager.disconnectRobots();
		super.onPause();
		
		RobotClientManager.getInstance().disconnect();
		finish();
	}

	@Override
	protected void onDestroy() {
		//RobotManager.disconnectRobots();
		super.onDestroy();
	}

	@Override
	protected void onStop() {
//		RobotManager.disconnectRobots();
		super.onStop();
	}

	public String getRobotName() {
		return robotName;
	}
}
