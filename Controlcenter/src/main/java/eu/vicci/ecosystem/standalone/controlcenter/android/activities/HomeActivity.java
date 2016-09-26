package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.SmartCPSActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.view.NavBaseActivity;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;

/**
 * The home dashboard activity.
 */
public class HomeActivity extends SmartCPSActivity {

	final Context context = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		registerHomeButtonListeners();

		if (!Common.isXLargeTablet(this))
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		if (getActionBar() != null)
			getActionBar().setTitle(R.string.title_activity_home);
	}

	private void registerHomeButtonListeners() {
		View view;

		// Robot Navigation
		view = findViewById(R.id.btn_home_turtlebot);
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), NavBaseActivity.class);
				startActivity(intent);
			}
		});

		// All devices
		view = findViewById(R.id.btn_home_alldevices);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DashboardActivity.class);
				startActivity(intent);
			}
		});

		// Processes
		// TODO: Why do you start with a generic activity called
		// ItemListActivity?
		// TODO: Is the ItemListActivty really necessary?
		view = findViewById(R.id.btn_home_process);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(v.getContext(),
				// ItemListActivity.class);
				// startActivity(intent);
				if (!ProcessEngineClient.getInstance().isConnected()) {
					showConnectionDialog();
				} else {
					Intent intent = new Intent(getApplicationContext(), ProcessMainActivity.class);
					startActivity(intent);
				}
			}

			private void showConnectionDialog() {
				Builder builder = new Builder(context);
				builder.setTitle(context.getString(R.string.noConnection));
				builder.setMessage("Make sure wifi is enabled, and the correct server IP is set.");
				builder.setPositiveButton("Connect now", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ProcessEngineClient.getInstance().connect();
					}
				});
				builder.setNegativeButton(context.getString(android.R.string.cancel), new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		// Help
		view = findViewById(R.id.btn_home_help);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), HelpListActivity.class);
				startActivity(intent);
			}
		});

		// Human Task
		view = findViewById(R.id.btn_home_humanTask);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), HumanTaskChooserActivity.class);
				startActivity(intent);
			}
		});

		// Left over button
		view = findViewById(R.id.btn_home_settings);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), SettingsActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed() {
		// bring the home screen to the front
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		
	}
}
