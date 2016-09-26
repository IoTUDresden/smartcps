package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import java.util.ArrayList;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.AdaptiveActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsParcelable;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.NavigationalActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpContent;
import eu.vicci.ecosystem.standalone.controlcenter.android.listeners.NavigationalGestureListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.listeners.NextClickTutReadyListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.ContextManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.AgeContext;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.HandicapsContext;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.TechnicalSkillsContext;

/**
 * The tutorial activity for telling the user, the tutorial is done.
 */
public class TutReadyActivity extends NavigationalActivity implements
		AdaptiveActivity {

	private static Boolean restartActivity = false;
	private static BreadcrumbsParcelable breadcrumbHistory;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Common.setActivityTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tut_ready);
		doAdaptation();

		RelativeLayout relativeLayoutRoot = (RelativeLayout) findViewById(R.id.tutReady_relativeLayoutRoot);
		Common.formatTextViewsToHtml(relativeLayoutRoot);
		setCustomNextClickListener(this, new NextClickTutReadyListener(this));
		createGestureOverlay(relativeLayoutRoot,
				new NavigationalGestureListener(this,
						getGestureLibrary(R.raw.gestures)));

		breadcrumbHistory = new BreadcrumbsParcelable(new ArrayList<String>());
		breadcrumbHistory.addBreadcrumbHistoryItem("Los geht's");
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

	public void doAdaptation() {
		/* adaptation: check for age context -> different salutation */
		AgeContext ageContext = (AgeContext) ContextManager
				.getRegisteredContextByClass(AgeContext.class);
		TechnicalSkillsContext technicalSkillsContext = (TechnicalSkillsContext) ContextManager
				.getRegisteredContextByClass(TechnicalSkillsContext.class);
		HandicapsContext handicapContext = (HandicapsContext) ContextManager
				.getRegisteredContextByClass(HandicapsContext.class);

		TextView txtNormal1 = (TextView) findViewById(R.id.tutReady_txtNormal1);

		if (ContextManager.getContextModel().hasContextPropertyByGroup(
				ageContext,
				ageContext != null ? ageContext
						.getGroupByName(AgeContext.CONTEXT_GROUP_CHILDREN)
						: null))
			txtNormal1
					.setText(Common
							.formatStringResourceToHtml(R.string.tutReady_txtNormal1_Children));
		else
			txtNormal1.setText(Common
					.formatStringResourceToHtml(R.string.tutReady_txtNormal1));

		// help content
		HelpContent.removeAllHelpItems();

		if (ContextManager.getContextModel().hasContextPropertyByGroup(
				ageContext,
				ageContext != null ? ageContext
						.getGroupByName(AgeContext.CONTEXT_GROUP_CHILDREN)
						: null)
				|| ContextManager
						.getContextModel()
						.hasContextProperty(
								technicalSkillsContext,
								TechnicalSkillsContext.CONTEXT_GROUP_LESS_TECHNICALLY_SKILLED)
				|| ContextManager.getContextModel().hasContextProperty(
						handicapContext, HandicapsContext.CONTEXT_GROUP_MEMORY)) {

			// general help items for children, technically less skilled and
			// memory handicapped persons
			HelpContent.addGeneralAdaptedHelpItems();
		}
		HelpContent.addGeneralHelpItems();
		/* adaptation end */
	}

	public BreadcrumbsParcelable getBreadcrumbHistory() {
		return breadcrumbHistory;
	}

	public void setBreadcrumbHistory(BreadcrumbsParcelable breadcrumbHistory) {
		TutReadyActivity.breadcrumbHistory = breadcrumbHistory;
	}

	public static Boolean getRestartActivity() {
		return restartActivity;
	}

	public static void setRestartActivity(Boolean restartActivity) {
		TutReadyActivity.restartActivity = restartActivity;
	}

}
