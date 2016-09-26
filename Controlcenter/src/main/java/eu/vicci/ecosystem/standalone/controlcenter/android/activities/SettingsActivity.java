package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.AdaptiveActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsParcelable;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsPreferenceActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.DashboardContentManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardDevice;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpContent;
import eu.vicci.ecosystem.standalone.controlcenter.android.semiwa.SemiwaConnection;
import eu.vicci.ecosystem.standalone.controlcenter.android.semiwa.SemiwaManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.ContentModel;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.ContextManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.ContextModel;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.AgeContext;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.HandicapsContext;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.TechnicalSkillsContext;

/**
 * The settings activity.
 */
@SuppressWarnings("javadoc")
public class SettingsActivity extends BreadcrumbsPreferenceActivity implements AdaptiveActivity {

	private static Context context;
	private static final boolean ALWAYS_SIMPLE_PREFS = false;
	private static BreadcrumbsParcelable breadcrumbHistory = new BreadcrumbsParcelable(new ArrayList<String>());
	public static Boolean restartActivity = false;

	public static final String KEY_PREF_SEMIWA = "prefSemiwa";
	public static final String KEY_PREF_PROCESS = "prefProcess";
	public static final String KEY_PREF_MAP = "prefMap";
	public static final String KEY_PREF_USER = "usersettings_active_user";
	public static final String KEY_PREF_MOCKING = "settings_dev_enable_mock_data";
	public static PreferenceFragment sFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Common.setActivityTheme(this);
		super.onCreate(savedInstanceState);
		doAdaptation();
		context = this;
		init();

		if (!Common.isXLargeTablet(this))
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// SHK - get action bar for enabling navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(getResources().getString(R.string.title_activity_settings));
	}

	@Override
	public void onBackPressed() {
		// force refresh of the activities for theming reasons
		HomeActivity.setRestartActivity(true);
		DashboardActivity.setRestartActivity(true);
		super.onBackPressed();
	}

	// SHK - callback to handle navigation to parent activity
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void doAdaptation() {
		/* adaptation: check for age context -> different salutation */
		AgeContext ageContext = (AgeContext) ContextManager.getRegisteredContextByClass(AgeContext.class);
		TechnicalSkillsContext technicalSkillsContext = (TechnicalSkillsContext) ContextManager
				.getRegisteredContextByClass(TechnicalSkillsContext.class);
		HandicapsContext handicapContext = (HandicapsContext) ContextManager
				.getRegisteredContextByClass(HandicapsContext.class);

		HelpContent.removeAllHelpItems();

		if (ContextManager.getContextModel().hasContextPropertyByGroup(ageContext,
				ageContext != null ? ageContext.getGroupByName(AgeContext.CONTEXT_GROUP_CHILDREN) : null)
				|| ContextManager.getContextModel().hasContextProperty(technicalSkillsContext,
						TechnicalSkillsContext.CONTEXT_GROUP_LESS_TECHNICALLY_SKILLED)
				|| ContextManager.getContextModel().hasContextProperty(handicapContext,
						HandicapsContext.CONTEXT_GROUP_MEMORY)) {
			// general help items for children, technically less skilled and
			// memory handicapped persons
			HelpContent.addGeneralAdaptedHelpItems();
		}
		HelpContent.addGeneralHelpItems();
		/* adaptation end */
	}

	/**
	 * Inits the activity depending on some context.
	 */
	private void init() {
		if (isSimplePreferences(this)) {
			getFragmentManager().beginTransaction().add(android.R.id.content, new WholePreferenceFragment()).commit();
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean onIsMultiPane() {
		return Common.isXLargeTablet(this) && !isSimplePreferences(this);
	}

	private static boolean isSimplePreferences(Context context) {
		return ALWAYS_SIMPLE_PREFS || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
				|| !Common.isXLargeTablet(context);
	}

	/** {@inheritDoc} */
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (!isSimplePreferences(this)) {
			loadHeadersFromResource(R.layout._settings_headers, target);
		}
	}

	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				if (preference.getKey().equals("settings_ui_theme")
						&& !PreferenceManager.getDefaultSharedPreferences(SmartCPS_Impl.getAppContext())
								.getString(preference.getKey(), "0").equals(value.toString()))
					Common.showToast(
							SmartCPS_Impl.getAppContext().getResources().getString(R.string.settings_ui_theme_hint),
							SmartCPS_Impl.getAppContext());

				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
			} else {
				preference.setSummary(stringValue);
			}			

			return true;
		}
	};

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 * 
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	protected static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's current value.
		if (preference.getKey().equals("settings_ui_theme")) {
			sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
					Common.getStringPreference(preference.getKey(), "0"));
		}

		if (preference.getKey().equals(KEY_PREF_SEMIWA)) {
			sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
					Common.getStringPreference(preference.getKey(), "utututututut"));
		}
	}

	/**
	 * Creates the ui preference fragment.
	 * 
	 * @param fragment
	 *            the fragment
	 */
	public static void createUIPreferenceFragment(PreferenceFragment fragment) {
		fragment.addPreferencesFromResource(R.layout._settings_ui);
		bindPreferenceSummaryToValue(fragment.findPreference("settings_ui_theme"));
	}

	/**
	 * Creates the tutorial preference fragment.
	 * 
	 * @param fragment
	 *            the fragment
	 */
	public static void createTutorialPreferenceFragment(PreferenceFragment fragment) {
		fragment.addPreferencesFromResource(R.layout._settings_tutorial);
	}

	/**
	 * Creates the devices preference fragment.
	 * 
	 * @param fragment
	 *            the fragment
	 */
	public static void createDevicesPreferenceFragment(PreferenceFragment fragment) {
		fragment.addPreferencesFromResource(R.layout._settings_devices);
	}

	/**
	 * Creates the dev preference fragment.
	 * 
	 * @param fragment
	 *            the fragment
	 */
	public static void createDevPreferenceFragment(PreferenceFragment fragment) {
		fragment.addPreferencesFromResource(R.layout._settings_dev);
	}

	/**
	 * Creates the service IP menu preference fragment.
	 * 
	 * @param fragment
	 *            the fragment
	 */
	public static void createServiceMenuPreferenceFragment(PreferenceFragment fragment) {
		fragment.addPreferencesFromResource(R.layout._settings_services);
		PreferenceManager.setDefaultValues(context, R.layout._settings_services, false);
	}
	
	/**
	 * create the robot preference fragment
	 * @param fragment
	 */
	public static void createRobotPreferenceFragment(PreferenceFragment fragment){
		fragment.addPreferencesFromResource(R.layout._settings_robot);
		PreferenceManager.setDefaultValues(context, R.layout._settings_robot, false);
	}
	
	public static void createUserSettingsPreferenceFragment(PreferenceFragment fragment){
		fragment.addPreferencesFromResource(R.layout._settings_usersettings);
		PreferenceManager.setDefaultValues(context, R.layout._settings_usersettings, false);
		bindPreferenceSummaryToValue(fragment.findPreference(KEY_PREF_USER));
	}

	/**
	 * on click routine to relaunch the tutorial from the settings.
	 * 
	 * @param v
	 *            the v
	 */
	public void tutorialOnClick(View v) {
		/* adaptation: check for handicaps -> different tutorial */
		HandicapsContext handicapsContext = (HandicapsContext) ContextManager
				.getRegisteredContextByClass(HandicapsContext.class);
		if (ContextManager.getContextModel().hasContextProperty(handicapsContext, HandicapsContext.CONTEXT_GROUP_SIGHT)) {
			TutControlActivity.galleryDone = true;
			Intent intent = new Intent(v.getContext().getApplicationContext(), TutControlActivity.class);
			intent.putExtra("BreadcrumbHistory", new BreadcrumbsParcelable(new ArrayList<String>()));
			v.getContext().startActivity(intent);
		} else {
			TutControlActivity.galleryDone = true;
			Intent intent = new Intent(v.getContext().getApplicationContext(), TutChangeThemeActivity.class);
			intent.putExtra("BreadcrumbHistory", new BreadcrumbsParcelable(new ArrayList<String>()));
			v.getContext().startActivity(intent);
		}
		/* adaptation end */
	}

	/**
	 * on click routine for adding a new service.
	 * 
	 * @param v
	 *            the v
	 */
	public void devAddNewServiceOnClick(View v) {
		Common.startBreadcrumbsActivity(this, AddNewServiceActivity.class);
	}

	/**
	 * on click routine for resetting the whole app.
	 * 
	 * @param v
	 *            the v
	 */
	public void devResetOnClick(View v) {
		// remove the context model
		v.getContext().deleteFile(ContextModel.class.getName() + MainActivity.DB_EXTENSION);
		v.getContext().deleteFile(ContentModel.class.getName() + MainActivity.DB_EXTENSION);

		SemiwaConnection.getInstance().disconnect();
		SemiwaManager.getInstance().clear();
		DashboardContentManager.getInstance().clear();
		SemiwaConnection.getInstance().connect();

		// remove the preferences
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.clear();
		editor.commit();

		// restart the app
		MainActivity.setRestartApp(true);
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	/**
	 * on click routine for adding devices
	 * 
	 * @param v
	 *            the v
	 */
	public void addDevicesOnClick(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Wählen sie die Geräte, die angezeigt werden sollen");

		final LinkedList<DashboardDevice> list = new LinkedList<DashboardDevice>(DashboardContentManager.getInstance()
				.getDashboardDevices());

		String[] items = new String[list.size()];
		final boolean[] checkedItems = new boolean[list.size()];
		int i = -1;
		for (DashboardDevice d : list) {
			items[++i] = d.getName();
			checkedItems[i] = d.isVisible();
		}

		builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				checkedItems[which] = isChecked;
			}
		});

		builder.setNegativeButton("Cancel", null);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int i = -1;
				for (DashboardDevice d : list) {
					d.setVisible(checkedItems[++i]);
				}
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
				settings.edit().putBoolean("device_list_changed", true);
			}
		});
		builder.create().show();
	}

	/**
	 * The fragment containing all fragments for the smartphone version.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class WholePreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			createUIPreferenceFragment(this);
			createTutorialPreferenceFragment(this);
			createDevicesPreferenceFragment(this);
			createDevPreferenceFragment(this);
			createServiceMenuPreferenceFragment(this);
			createRobotPreferenceFragment(this);
		}
	}

	/**
	 * The UIPreferenceFragment.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class UIPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			createUIPreferenceFragment(this);
		}
	}

	/**
	 * The ReelMenuPreferenceFragment.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class ReelMenuPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
	}

	/**
	 * The TutorialPreferenceFragment.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class TutorialPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			createTutorialPreferenceFragment(this);
		}
	}

	/**
	 * The DevicesPreferenceFragment.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class DevicesPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			createDevicesPreferenceFragment(this);
		}
	}

	/**
	 * The DevPreferenceFragment.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class DevPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			createDevPreferenceFragment(this);
		}
	}

	/**
	 * The ServicePreferenceFragment.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class ServiceIpFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			createServiceMenuPreferenceFragment(this);
			sFrag = this;

			onSharedPreferenceChanged(null, "");
		}

		@Override
		public void onResume() {
			super.onResume();
			PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onPause() {
			PreferenceManager.getDefaultSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(this);
			super.onPause();
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
			updateSummary(sp, key);
		}
	}
	
	/**
	 * The Robot Pref Fragment
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class RobotPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			createRobotPreferenceFragment(this);
		}	
	}
	
	/**
	 * The User Settings Pref Fragment. Used for setting a user with given capabilities, 
	 * which is connected to proteus. 
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class UserSettingsPreferenceFragment extends PreferenceFragment {
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			createUserSettingsPreferenceFragment(this);
		}		
	}

	@Override
	public BreadcrumbsParcelable getBreadcrumbHistory() {
		return breadcrumbHistory;
	}

	@Override
	public void setBreadcrumbHistory(BreadcrumbsParcelable breadcrumbHistory) {
		SettingsActivity.breadcrumbHistory = breadcrumbHistory;
	}

	private static void updateSummary(SharedPreferences sp, String key) {
		if (key.equals(KEY_PREF_SEMIWA)) {
			Preference semiwaPref = sFrag.findPreference(key);
			// Set summary to be the user-description for the selected value
			semiwaPref.setSummary("dummy");
			semiwaPref.setSummary(sp.getString(key, ""));
		}

		if (key.equals(KEY_PREF_PROCESS)) {
			Preference processPref = sFrag.findPreference(key);
			// Set summary to be the user-description for the selected value
			processPref.setSummary("dummy");
			processPref.setSummary(sp.getString(key, ""));
		}

		if (key.equals(KEY_PREF_MAP)) {
			Preference processPref = sFrag.findPreference(key);
			// Set summary to be the user-description for the selected value
			processPref.setSummary("dummy");
			processPref.setSummary(sp.getString(key, ""));
		}
	}

}
