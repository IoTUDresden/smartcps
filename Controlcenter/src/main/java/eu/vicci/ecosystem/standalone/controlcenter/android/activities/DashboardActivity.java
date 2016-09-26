package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.SmartCPSActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.DashboardListAdapter;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.DashboardSortType;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.DashboardStickyGridHeadersAdapterWrapper;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.DashboardContentManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardDevice;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DeviceOption;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.Process;
import eu.vicci.ecosystem.standalone.controlcenter.android.persistence.AndroidPersistence;
import eu.vicci.ecosystem.standalone.controlcenter.android.processview.ItemDetailFragment;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.view.NavBaseActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.semiwa.SemiwaManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.sequence.SequenceInstanceActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.ContentModel;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.DashboardDialogBuilder;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.engine.core.IProcessInstanceInfo;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessInstanceInfoListHandler;

/**
 * <b>Dashboard Activity KP-SEUS 2014</b><br>
 * <br>
 * 
 * Sensoren, Prozesse und Roboter werden in Gridview dargestelt. Filtern und
 * Sortieren in der Actionbar. Optionen für einzelne Kacheln nach LongClick.
 * Detailansicht für Sensoren, wecheln zu Activities für Roboter und Sensoren
 * nach OnClick.
 * 
 * @author Andreas Hippler
 */
public class DashboardActivity extends SmartCPSActivity {
	private StickyGridHeadersGridView gridView;
	private DashboardListAdapter listAdapter;
	private DashboardActivity context;
	private DashboardDialogBuilder dialogBuilder;

	/** timer for GridView refresh */
	private Timer timer;

	/** refresh period for GridView */
	private static final long GRIDVIEW_REFRESH_PERIOD = 1500;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		context = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		getActionBar().setTitle(getString(R.string.btn_home_alldevices));

		// setup GridView and ListAdapter
		dialogBuilder = new DashboardDialogBuilder(this);
		listAdapter = new DashboardListAdapter(this);
		gridView = (StickyGridHeadersGridView) findViewById(R.id.grid_view);
		gridView.setAdapter(new DashboardStickyGridHeadersAdapterWrapper(listAdapter));
		gridView.setOnItemLongClickListener(new OnGridItemLongClickListener());
		gridView.setOnItemClickListener(new OnGridClickListener());

		if (ProcessEngineClient.getInstance().isConnected()) {
			ProcessInstanceInfoListHandler pilh = new ProcessInstanceInfoListHandler();
			pilh.addHandlerFinishedListener(this);
			ProcessEngineClient.getInstance().listProcessInstances(pilh);
		}

		// add SemiwaManager observer to get notified that a new device connects
		SemiwaManager.getInstance().addObserver(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dashboard_test, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_filter:
			dialogBuilder.showFilterDialog(listAdapter);
			return true;
		case R.id.menu_sort_by_name:
			listAdapter.sortBy(DashboardSortType.SORT_BY_NAME);
			return true;
		case R.id.menu_sort_by_type:
			listAdapter.sortBy(DashboardSortType.SORT_BY_TYPE);
			return true;
		case R.id.menu_sort_by_room:
			listAdapter.sortBy(DashboardSortType.SORT_BY_ROOM);
			return true;
		case R.id.menu_sort_by_none:
			listAdapter.sortBy(DashboardSortType.NOT_SORTED);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class OnGridClickListener implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			switchActivity((DashboardDevice) listAdapter.getItem(position));
		}
	}

	private class OnGridItemLongClickListener implements AdapterView.OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			final DashboardDevice device = (DashboardDevice) listAdapter.getItem(position);
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(getString(R.string.dashboard_options_title) + device.getName())
					.setItems(device.getAllowedOptions(), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String option = device.getAllowedOptions()[which];

							if (option.equals(DeviceOption.CAN_RENAME))
								dialogBuilder.showRennameDeviceDialog(listAdapter, device);
							if (option.equals(DeviceOption.CAN_CHANGE_VISUALISATION))
								dialogBuilder.showChangeVisualizationDialog(listAdapter, device);
							if (option.equals(DeviceOption.CAN_HIDE))
								listAdapter.removeDevice(device);
							if (option.equals(DeviceOption.CAN_CHANGE_MIN_MAX))
								dialogBuilder.showChangeIdealsDialog(device);
							if (option.equals(DeviceOption.CAN_CHANGE_ROOM))
								dialogBuilder.showChangeRoomDialog(device, listAdapter.getRooms());
						}
					}).setNegativeButton(getString(R.string.dashboard_filter_cancel), null);
			builder.create().show();
			return true;
		}
	}

	/**
	 * Reload listAdapter if Devices had been selected in Settings and start the
	 * GridView refresh timer.
	 */
	@Override
	protected void onResume() {
		super.onStart();

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		if (settings.getBoolean("device_list_changed", false)) {
			settings.edit().putBoolean("device_list_changed", false);
			listAdapter.contentManagerChanged();
		}

		timer = new Timer();
		timer.schedule(new DatasetChangedTimerTask(), GRIDVIEW_REFRESH_PERIOD, GRIDVIEW_REFRESH_PERIOD);
	}

	/** cancel GridView refresh timer */
	@Override
	protected void onPause() {
		timer.cancel();
		super.onPause();
	}

	/**
	 * Start SequenceInstanceActivity for Processes, to NavBaseActivity for
	 * Robots or to DashboardDeviceDetailActivity for Sensors after click on
	 * grid of the GridView.
	 * 
	 * @param device
	 *            the DashboardDevice which Grid was clicked
	 */
	private void switchActivity(final DashboardDevice device) {
		Intent intent;
		switch (device.getDeviceType()) {
		case Process:
			intent = new Intent(getApplicationContext(), SequenceInstanceActivity.class);
			intent.putExtra(ItemDetailFragment.EXTRA_MESSAGE_PROCESS_INSTANCEID,
					((Process) device).getProcessInstanceId());
			startActivity(intent);
			break;
		case Robot:
			intent = new Intent(getApplicationContext(), NavBaseActivity.class);
			startActivity(intent);
			break;
		default:
			intent = new Intent(getApplicationContext(), DashboardDeviceDetailActivity.class);
			intent.putExtra(DashboardDeviceDetailActivity.INTENT_DEVICE_UID, device.getId());
			startActivity(intent);
			break;
		}
	}

	/** TimerTask to run the GridView refresh in UIThread */
	private class DatasetChangedTimerTask extends TimerTask {
		public void run() {
			runOnUiThread(new Runnable() {
				public void run() {
					listAdapter.notifyDataSetChanged();
				}
			});
		}
	}

	/** semiwa devices to ContentManager */
	@Override
	public void update(Observable observable, Object data) {
		if (observable instanceof SemiwaManager) {
			listAdapter.contentManagerChanged();
		} else
			super.update(observable, data);
	}

	/** persist all Devices on stop */
	@Override
	protected void onStop() {
		AndroidPersistence.persistObject(DashboardContentManager.getInstance().getDashboardDevices(),
				ContentModel.class.getName() + MainActivity.DB_EXTENSION, this);
		super.onStop();
	}

	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		if (handler instanceof ProcessInstanceInfoListHandler) {
			@SuppressWarnings("unchecked")
			List<IProcessInstanceInfo> instances = (List<IProcessInstanceInfo>) arg;
			for (IProcessInstanceInfo pii : instances) {
				DashboardContentManager.getInstance().getDashboardDevices().add(new Process(pii));
			}
			listAdapter.contentManagerChanged();		
		}
	}
}
