package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import de.tud.melissa.util.GraphValue;
import de.tud.melissa.visualization.widgets.TimeLineChart;
import eu.vicci.driver.robot.location.NamedLocation;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.SmartCPSActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.DashboardContentManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardDevice;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardValueObject;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DeviceType;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.Sensor;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientMap;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientRobot;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ItemDatabaseHandler;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.view.AddMapDialog;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.view.FragmentCallback;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.view.MapFragment;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.view.NavBaseActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.device.ValueObject;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.device.ValueReceiver;
import eu.vicci.ecosystem.standalone.controlcenter.android.visualization.VisualizationType;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;

/**
 * DeviceDetail Activity. Shows Details for a Device. This Activity includes a
 * MapFragment See
 * {@link eu.vicci.ecosystem.standalone.controlcenter.android.robot.view.MapFragment}
 * wich shows the Client Map.
 * 
 * @author André Kühnert
 * 
 */
public class DashboardDeviceDetailActivity extends SmartCPSActivity implements FragmentCallback, Observer {
	/**
	 * String which identifies the deviceUid in the Intent
	 */
	public static final String INTENT_DEVICE_UID = "deviceUid";
	private String noValueString = "-";
	private DashboardDevice device;
	private FrameLayout mapFrameLayout;
	private FrameLayout visualizationFrameLayout;
	private AddMapDialog addMapDialog;
	private Sensor sensor;
	private boolean isSensorDevice; // if the Device is a Sensor, this will be
									// true

	// TODO the MapFragmet should be refactored or something. Or use the miniMap
	// instead.
	// The scale makes it possible to see anything at all, otherwise, the map is
	// only visible,
	// if the fragment is big enough
	private final float mapScale = 7f;

	private TextView nameTextView;
	private TextView locationTextView;
	private TextView boundaryMaxValueTextView;
	private TextView boundaryMinValueTextView;
	private TextView idealMaxValueTextView;
	private TextView idealMinValueTextView;

	// the trend chart
	private TimeLineChart trend;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard_device_detail);
		Intent intent = getIntent();
		addMapDialog = new AddMapDialog();
		setTextViews();
		String deviceId = intent.getStringExtra(INTENT_DEVICE_UID);
		device = DashboardContentManager.getInstance().getDashboardDeviceById(deviceId);

		getActionBar().setTitle(getString(R.string.deviceDetail_activity_name)+" "+device.getName());
		
		if (device.getDeviceType() == DeviceType.Sensor) {
			sensor = (Sensor) device;
			isSensorDevice = true;
		}

		setTextViewValues();
		mapFrameLayout = (FrameLayout) findViewById(R.id.sensor_view_map);
		visualizationFrameLayout = (FrameLayout) findViewById(R.id.sensor_view_chart);

		// Navigator throws nullpointer while adding if no database connection
		// exists
		if (NavBaseActivity.database == null) {
			NavBaseActivity.database = new ItemDatabaseHandler(this);
			NavBaseActivity.database.open();
		}

		if (isSensorDevice && sensor.getClientMap() != null)
			addMap(sensor.getClientMap());
		else
			showMapDefault();

		// load the detailVisualization and set the values
		setValues(device);
	}

	// default map with clicklistener, wich allows to add a map
	private void showMapDefault() {
		mapFrameLayout.removeAllViews();
		// TODO check for details http://stackoverflow.com/questions/23846146/
		View contentImage = getLayoutInflater().inflate(R.layout.robo_view_default_map_frame, null);

		// set click listeners
		contentImage.findViewById(R.id.default_map_view).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addMapDialog.show(getFragmentManager(), "add_map_fragment_dialog");
			}
		});
		mapFrameLayout.addView(contentImage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	private void setValues(DashboardDevice device) {
		if (device == null)
			return;
		TextView nameView = (TextView) findViewById(R.id.name);
		TextView locationView = (TextView) findViewById(R.id.location);
		nameView.setText(device.getName());
		locationView.setText(device.getLocationName());
		if (device.getDetailVisualizationType() == null)
			return;

		getDetailVisualization();
		visualizationFrameLayout.addView(trend);
	}

	// at the moment only time line chart
	private void getDetailVisualization() {
		if (VisualizationType.TimeLineChart != device.getDetailVisualizationType())
			return;

		trend = new TimeLineChart(this, null, 0);
		trend.setTextSize(20);
		trend.setValues(device.getValueObject().getAllDatesAndValues());

	}

	// FragmentCallback Implementation.
	// Only Map an Pois are used
	@Override
	public void addRobot(ClientRobot clientRobot) {
	}

	@Override
	public void editRobot(int index, ClientRobot clientRobot) {
	}

	// sets poi and stores in the sensor
	@Override
	public void addPoi(NamedLocation poi) {
		SmartCPS_Impl.getNavigator().addPoi(poi);
		if (isSensorDevice)
			sensor.setPoi(poi);
	}

	@Override
	public void editPoi(int index, NamedLocation poi) {
		SmartCPS_Impl.getNavigator().updatePoi(index, poi);
	}

	// adds the map and stores it to the device
	@Override
	public void addMap(ClientMap clientMap) {
		SmartCPS_Impl.getNavigator().addMap(clientMap);
		if (isSensorDevice)
			sensor.setClientMap(clientMap); // store the Map in the Device
		MapFragment mapFrag = new MapFragment(mapScale);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.sensor_view_map, mapFrag);
		ft.commit();
	}

	@Override
	public void editMap(int index, ClientMap clientMap) {
		SmartCPS_Impl.getNavigator().updateMap(index, clientMap);
	}

	private void setTextViews() {
		nameTextView = (TextView) findViewById(R.id.name);
		locationTextView = (TextView) findViewById(R.id.location);
		boundaryMaxValueTextView = (TextView) findViewById(R.id.boundary_max_value);
		boundaryMinValueTextView = (TextView) findViewById(R.id.boundary_min_value);
		idealMaxValueTextView = (TextView) findViewById(R.id.ideal_max_value);
		idealMinValueTextView = (TextView) findViewById(R.id.ideal_min_value);
	}

	private void setTextViewValues() {
		DashboardValueObject vo = device.getValueObject();
		nameTextView.setText(device.getName());
		locationTextView.setText(device.getLocationName());

		String tempValue = vo.getMaxValue() == Double.MAX_VALUE ? noValueString : String.format(Locale.US, "%.2f %s",
				vo.getMaxValue(), vo.getUnit());
		boundaryMaxValueTextView.setText(tempValue);

		tempValue = vo.getMinValue() == Double.MIN_VALUE ? noValueString : String.format(Locale.US, "%.2f %s",
				vo.getMinValue(), vo.getUnit());
		boundaryMinValueTextView.setText(tempValue);

		tempValue = vo.hasIdealValue() ? String.format(Locale.US, "%.1f %s", vo.getIdealValueMax(), vo.getUnit())
				: noValueString;
		idealMaxValueTextView.setText(tempValue);

		tempValue = vo.hasIdealValue() ? String.format(Locale.US, "%.1f %s", vo.getIdealValueMin(), vo.getUnit())
				: noValueString;
		idealMinValueTextView.setText(tempValue);
	}

	@Override
	protected void onPause() {
		device.getValueObject().deleteObserver(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		device.getValueObject().addObserver(this);
		super.onResume();
	}

	@Override
	public void update(Observable observable, final Object data) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (data == ValueObject.Property.MIN_MAX || data == ValueObject.Property.MIN_MAX) {
					setTextViewValues();
				}
				if (data == ValueObject.Property.VALUE && trend != null) {
					ValueReceiver<Double> vr = device.getValueObject().getDeque().getLast();
					trend.addValue(new GraphValue<Date>(vr.getDate(), vr.getValue()));
					// visualizationFrameLayout.invalidate();
				}
			}
		});
	}

	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		//not interested 		
	}
}
