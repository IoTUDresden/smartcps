package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import java.util.ArrayList;

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
import eu.vicci.ecosystem.standalone.controlcenter.android.fragments.TutEvaluationAgeFragment;
import eu.vicci.ecosystem.standalone.controlcenter.android.fragments.TutEvaluationHandicapsFragment;
import eu.vicci.ecosystem.standalone.controlcenter.android.fragments.TutEvaluationTechnicalSkillsFragment;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpContent;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpItem;
import eu.vicci.ecosystem.standalone.controlcenter.android.listeners.NavigationalEvaluationGestureListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.listeners.NextClickTutEvaluationListener;

/**
 * The tutorial activity for setting certain context information.
 */
@SuppressWarnings("rawtypes")
public class TutEvaluationActivity extends NavigationalActivity implements
		AdaptiveActivity {

	private static Class toActivityClass;
	private static Boolean restartActivity = false;
	private static BreadcrumbsParcelable breadcrumbHistory = new BreadcrumbsParcelable(
			new ArrayList<String>());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Common.setActivityTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tut_evaluation);
		doAdaptation();

		// when finishing the activity or changing the device's orientation,
		// onCreate is launched again -> check for existing fragments!
		if (savedInstanceState == null) {
			if (Common.isXLargeTablet(this))
				Common.addFragments(
						R.id.tutEvaluation_containerEvaluationContext,
						getFragmentManager(), new TutEvaluationAgeFragment(),
						new TutEvaluationTechnicalSkillsFragment(),
						new TutEvaluationHandicapsFragment());
			else
				Common.addFragments(
						R.id.tutEvaluation_containerEvaluationContext,
						getFragmentManager(), new TutEvaluationAgeFragment(),
						new TutEvaluationTechnicalSkillsFragment());
		}

		setCustomNextClickListener(this, new NextClickTutEvaluationListener(
				this));
		RelativeLayout relativeLayoutRoot = (RelativeLayout) findViewById(R.id.tutEvaluation_relativeLayoutRoot);
		createGestureOverlay(relativeLayoutRoot,
				new NavigationalEvaluationGestureListener(this,
						getGestureLibrary(R.raw.gestures)));

		breadcrumbHistory = getIntent().getExtras().getParcelable(PARCEL_KEY);
		breadcrumbHistory.addBreadcrumbHistoryItem("Benutzereinschätzung");
		initBreadcrumbs(breadcrumbHistory);

		if (!Common.isXLargeTablet(this))
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	protected void onResume() {
		doAdaptation();
		if (getRestartActivity()) {
			setRestartActivity(false);
			// fragmentsAdded = false;
			Common.restartActivity(this);
		}
		super.onResume();
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
		HelpContent.addHelpItem(new HelpItem(resources
				.getString(R.string.help_howToReturn_txtHeadline), resources
				.getString(R.string.help_howToReturn_contentHtml)));

		// general help items for children, technically less skilled and memory
		// handicapped persons
		HelpContent.addGeneralAdaptedHelpItems();
		HelpContent.addHelpItem(new HelpItem(
				getString(R.string.help_whatIsSmartCPS_txtHeadline),
				getString(R.string.help_whatIsSmartCPS_contentHtml)));
		/* adaptation end */
	}

	public static Class getToActivityClass() {
		return toActivityClass;
	}

	public static void setToActivityClass(Class toActivityClass) {
		TutEvaluationActivity.toActivityClass = toActivityClass;
	}

	public BreadcrumbsParcelable getBreadcrumbHistory() {
		return breadcrumbHistory;
	}

	public void setBreadcrumbHistory(BreadcrumbsParcelable breadcrumbHistory) {
		TutEvaluationActivity.breadcrumbHistory = breadcrumbHistory;
	}

	public static Boolean getRestartActivity() {
		return restartActivity;
	}

	public static void setRestartActivity(Boolean restartActivity) {
		TutEvaluationActivity.restartActivity = restartActivity;
	}

}
