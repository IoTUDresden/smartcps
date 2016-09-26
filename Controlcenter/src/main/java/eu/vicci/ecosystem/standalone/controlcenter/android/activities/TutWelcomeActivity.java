package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.RelativeLayout;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.AdaptiveActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsParcelable;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.NavigationalActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpContent;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpItem;
import eu.vicci.ecosystem.standalone.controlcenter.android.listeners.NavigationalGestureListener;

/**
 * The tutorial activity for welcoming the user.
 */
public class TutWelcomeActivity extends NavigationalActivity implements
		AdaptiveActivity {

	private static Boolean restartActivity = false;
	private static BreadcrumbsParcelable breadcrumbHistory;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Common.setActivityTheme(this);
		super.onCreate(savedInstanceState);
		// back gesture in the first screen doesn't shut down the app
		setRootActivity(true);
		setContentView(R.layout.activity_tut_welcome);
		doAdaptation();

		RelativeLayout relativeLayoutRoot = (RelativeLayout) findViewById(R.id.tutWelcome_relativeLayoutRoot);
		Common.formatTextViewsToHtml(relativeLayoutRoot);
		setNextClickListener(this, TutEvaluationActivity.class);
		createGestureOverlay(relativeLayoutRoot,
				new NavigationalGestureListener(this,
						getGestureLibrary(R.raw.gestures)));

		breadcrumbHistory = getIntent().getExtras() != null ? (BreadcrumbsParcelable) getIntent()
				.getExtras().getParcelable(PARCEL_KEY)
				: new BreadcrumbsParcelable(new ArrayList<String>());
		breadcrumbHistory.addBreadcrumbHistoryItem("Willkommen");
		initBreadcrumbs(breadcrumbHistory);

		if (!Common.isXLargeTablet(this))
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	protected void onResume() {
		doAdaptation();
		if (getRestartActivity()) {
			setRestartActivity(false);
			Common.restartActivity(this);
		}
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		// bring the home screen to the front
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	public void doAdaptation() {
		/*
		 * adaptation: welcome screen -> don't check for certain context -> set
		 * the most comprehensive adaptation
		 */
		Resources resources = SmartCPS_Impl.getAppContext().getResources();
		HelpContent.removeAllHelpItems();

		// context sensitive help items
		HelpContent.addHelpItem(new HelpItem(resources
				.getString(R.string.help_howToProceed_txtHeadline), resources
				.getString(R.string.help_howToProceed_contentHtml)));

		// general help items for children, technically less skilled and memory
		// handicapped persons
		HelpContent.addGeneralAdaptedHelpItems();
		HelpContent.addHelpItem(new HelpItem(
				getString(R.string.help_whatIsSmartCPS_txtHeadline),
				getString(R.string.help_whatIsSmartCPS_contentHtml)));
		/* adaptation end */
	}

	public BreadcrumbsParcelable getBreadcrumbHistory() {
		return breadcrumbHistory;
	}

	public void setBreadcrumbHistory(BreadcrumbsParcelable breadcrumbHistory) {
		TutWelcomeActivity.breadcrumbHistory = breadcrumbHistory;
	}

	public static Boolean getRestartActivity() {
		return restartActivity;
	}

	public static void setRestartActivity(Boolean restartActivity) {
		TutWelcomeActivity.restartActivity = restartActivity;
	}

}
