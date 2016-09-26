package eu.vicci.ecosystem.standalone.controlcenter.android.activities.general;

import java.util.Observable;
import java.util.Observer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.HelpListActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.HomeActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.HumanTaskActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.SettingsActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpContent;
import eu.vicci.ecosystem.standalone.controlcenter.android.humantask.HumanTaskDataManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.humantask.HumanTaskDataManagerListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.semiwa.SemiwaConnection;
import eu.vicci.ecosystem.standalone.controlcenter.android.services.HelpReminderService;
import eu.vicci.process.client.android.ObservableSession;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.kpseus.connect.ServerConnector;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.handlers.HandlerFinishedListener;
import eu.vicci.process.model.util.messages.core.IHumanTaskRequest;

/**
 * Grundklasse für alle Activities von SmartCPS. <br>
 * <br>
 * Hällt alle Serververbindungen. Legt die Grundoptik fest. Enstellt die
 * Menupunkte: Server, Hilfe und Optionen.
 * 
 * @author Andreas Hippler
 */
public abstract class SmartCPSActivity extends BreadcrumbsActivity implements Observer, HandlerFinishedListener {
	private static final String CONNECTED_STRING = "connected";
	private static final String DISCONNECTED_STRING = "disconnected";
	
	protected static Boolean restartActivity = false;
	protected Menu menu;
	
	private SemiwaConnection semiwaConnection = SmartCPS_Impl.getSemiwaConnection();
	private ProcessEngineClient pec = ProcessEngineClient.getInstance();

	/**
	 * @return whether the activity have to restart or not 
	 */
	public static Boolean getRestartActivity() {
		return restartActivity;
	}

	/**
	 * Sets whether the activity have to restart or not
	 * 
	 * @param restartActivity
	 */
	public static void setRestartActivity(Boolean restartActivity) {
		HomeActivity.restartActivity = restartActivity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Common.setActivityTheme(this);
		super.onCreate(savedInstanceState);
		HelpContent.addGeneralHelpItems();
	}

	/** inflate server, settings and help menu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.services, menu);

		getMenuInflater().inflate(R.menu.settings_help, menu);

		HelpReminderService.setActivity(this);
		HelpReminderService.setHelpMenuItem(menu.findItem(R.id.menu_help));
		// HelpReminderService.startService();

		return true;
	}

	/**
	 * set connection status of services before displaying the menu, setup
	 * connections observers
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		this.menu = menu;

		semiwaConnection.deleteObservers();
		semiwaConnection.addObserver(this);

		ProcessEngineClient.getInstance().getOs().addObserver(this);
		ServerConnector.getInstance().getProcessEngineClient();
		HumanTaskDataManager.getInstance().addHumanTaskDataManagerListener(humanTaskDataManagerListener);			

		// update the menu items
		update(SemiwaConnection.getInstance(), null);
		update(pec.getOs(), null);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_services_semiwa:
			if (semiwaConnection.isConnected()) {
				semiwaConnection.disconnect();
				return true;
			}
			semiwaConnection.connect();
			return true;
		case R.id.menu_help:
			Common.startBreadcrumbsActivity(this, HelpListActivity.class);
			return true;
		case R.id.menu_settings:
			Common.startBreadcrumbsActivity(this, SettingsActivity.class);
			return true;
		case R.id.menu_services_processes:
			if (pec.isConnected()) {
				pec.disconnect();
			} else {
				pec.connect();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		if (getRestartActivity()) {
			setRestartActivity(false);
			Common.restartActivity(this);
		}
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		if (this.getClass() != HomeActivity.class)
			super.onBackPressed();
		this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
	private void updateSemiwaConnectionItem(){
		String title = getResources().getString(R.string.service_semiwa) + " "
				+ semiwaConnection.getConnectionState();
		if (semiwaConnection.isConnected()) {
			Log.e("Menu Semiwa Connection",
					"connection : " + String.valueOf(semiwaConnection.isConnected()));
			menu.findItem(R.id.menu_services_semiwa).setTitle(title).setIcon(R.drawable.connection_ok);
		} else {
			Log.e("Menu Semiwa Connection",
					"connection : " + String.valueOf(semiwaConnection.isConnected()));
			menu.findItem(R.id.menu_services_semiwa).setTitle(title).setIcon(R.drawable.connection_failed);
		}
	}
	
	private void updateProcessEngineClientItem(){
		String title = getResources().getString(R.string.service_processes);
		title = title + " "	+ (pec.isConnected() ? CONNECTED_STRING : DISCONNECTED_STRING);
		Log.e("Menu Process Connection", "connection : " + pec.isConnected());
		MenuItem processItem = menu.findItem(R.id.menu_services_processes);
		
		if (pec.isConnected()) {
			processItem.setTitle(title).setIcon(R.drawable.connection_ok);
		} else {
			processItem.setTitle(title).setIcon(R.drawable.connection_failed);
		}		
	}

	@Override
	public void update(final Observable observable, Object data) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (menu != null && observable instanceof SemiwaConnection) {
					updateSemiwaConnectionItem();
				} else if (menu != null && observable instanceof ObservableSession) {
					updateProcessEngineClientItem();
				}
			}
		});
	}
	
	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		
	}
	
	private HumanTaskDataManagerListener humanTaskDataManagerListener = new HumanTaskDataManagerListener() {		
		@Override
		public void onHumanTaskHandledByOther(IHumanTaskRequest humanTaskRequest) {	
			cancelNotification(humanTaskRequest);
		}
		
		@Override
		public void onHumanTaskAdded(IHumanTaskRequest humanTaskRequest) {
			showNotification(humanTaskRequest);					
		}
	};
	
	private void cancelNotification(IHumanTaskRequest humanTaskRequest){
		NotificationManager notifyMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		Integer id = HumanTaskDataManager.getInstance().getNotificationId(humanTaskRequest);
		if(id != null)
			notifyMgr.cancel(id.intValue());		
	}
	
	private void showNotification(IHumanTaskRequest request){
		String htName = request.getName();
		String htDesc = request.getDescription();
		int notifyID =  HumanTaskDataManager.getInstance().createNotificationId(request).intValue();
		long[] pattern = {500, 500};
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.home)
				.setContentTitle(htName)
				.setContentText(htDesc)
				.setAutoCancel(true)
				.setVibrate(pattern)
				.setLights(Color.BLUE, 500, 500)
				.setSound(alarmSound);	
		
		NotificationManager notifyMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		//New Intent to open the HumanTaskActivity on notification click which
		//needs the instanceID of the request
		Intent resultIntent = new Intent (this, HumanTaskActivity.class);
		String instanceId = request.getHumanTaskInstanceId();
		resultIntent.putExtra(HumanTaskDataManager.HUMAN_TASK_ID, instanceId);
		resultIntent.putExtra(HumanTaskDataManager.HUMAN_TASK_MESSAGE_TYPE, HumanTaskDataManager.HUMAN_TASK_REQUEST);
		
		//TaskStackBuilder to ensure back navigation 
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(HumanTaskActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		
		//pending intent contains the back stack and opens the activity
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(notifyID, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		
		notifyMgr.notify(notifyID, mBuilder.build());
	}
	

	
	
	
}
