package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import java.util.ArrayList;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.AdaptiveActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsParcelable;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.GalleryActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.NavigationalActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpContent;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpItem;
import eu.vicci.ecosystem.standalone.controlcenter.android.listeners.NavigationalGestureListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.listeners.NextClickTutControlListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.listeners.TutGalleryGestureListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.ContextManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.AgeContext;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.HandicapsContext;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.TechnicalSkillsContext;

/**
 * The tutorial activity for the controlling of devices.
 */
public class TutControlActivity extends NavigationalActivity implements
		GalleryActivity, AdaptiveActivity {

	private static Boolean restartActivity = false;
	private static BreadcrumbsParcelable breadcrumbHistory = new BreadcrumbsParcelable(
			new ArrayList<String>());
	public static Boolean galleryDone = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Common.setActivityTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tut_control);

		RelativeLayout relativeLayoutRoot = (RelativeLayout) findViewById(R.id.tutControl_relativeLayoutRoot);

		// add dashboards explanation gallery
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.tutControl_relativeLayout);
		createGestureOverlay(relativeLayout, new TutGalleryGestureListener(
				this, getGestureLibrary(R.raw.gestures),
				(TextView) findViewById(R.id.tutControl_txtCurrentStep),
				findViewById(R.id.tutControl_txtStep1),
				findViewById(R.id.tutControl_txtStep2),
				findViewById(R.id.tutControl_txtStep3)));

		TextView currentStep = (TextView) findViewById(R.id.tutControl_txtCurrentStep);
		currentStep.setText(String.format(
				getResources().getString(R.string.currentStep), 1, 3));

		doAdaptation();
		Common.formatTextViewsToHtml(relativeLayoutRoot);
		setCustomNextClickListener(this, new NextClickTutControlListener(this));
		setCloseClickListener(this);
		createGestureOverlay(relativeLayoutRoot,
				new NavigationalGestureListener(this,
						getGestureLibrary(R.raw.gestures)));

		breadcrumbHistory = getIntent().getExtras().getParcelable(PARCEL_KEY);
		breadcrumbHistory.addBreadcrumbHistoryItem("Gerätebedienung");
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

		TextView txtNormal1 = (TextView) findViewById(R.id.tutControl_txtNormal1);
		TextView txtStep1 = (TextView) findViewById(R.id.tutControl_txtStep1);
		TextView txtStep2 = (TextView) findViewById(R.id.tutControl_txtStep2);
		TextView txtStep3 = (TextView) findViewById(R.id.tutControl_txtStep3);

		if (ContextManager.getContextModel().hasContextPropertyByGroup(
				ageContext,
				ageContext != null ? ageContext
						.getGroupByName(AgeContext.CONTEXT_GROUP_CHILDREN)
						: null)) {
			txtNormal1
					.setText(Common
							.formatStringResourceToHtml(R.string.tutControl_txtNormal1_Children));
			txtStep1.setText(Common
					.formatStringResourceToHtml(R.string.tutControl_txtStep1_Children));
			txtStep2.setText(Common
					.formatStringResourceToHtml(R.string.tutControl_txtStep2_Children));
			txtStep3.setText(Common
					.formatStringResourceToHtml(R.string.tutControl_txtStep3_Children));
		} else {
			txtNormal1
					.setText(Common
							.formatStringResourceToHtml(R.string.tutControl_txtNormal1));
			txtStep1.setText(Common
					.formatStringResourceToHtml(R.string.tutControl_txtStep1));
			txtStep2.setText(Common
					.formatStringResourceToHtml(R.string.tutControl_txtStep2));
			txtStep3.setText(Common
					.formatStringResourceToHtml(R.string.tutControl_txtStep3));
		}

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
			// context sensitive help items
			if (ContextManager.getContextModel().hasContextProperty(
					handicapContext, HandicapsContext.CONTEXT_GROUP_MEMORY)) {
				if (ContextManager
						.getContextModel()
						.hasContextPropertyByGroup(
								ageContext,
								ageContext != null ? ageContext
										.getGroupByName(AgeContext.CONTEXT_GROUP_CHILDREN)
										: null)) {
					HelpContent
							.addHelpItem(new HelpItem(
									getString(R.string.help_howToProceed_txtHeadline),
									getString(R.string.help_howToProceed_contentHtml_Children)));
					HelpContent
							.addHelpItem(new HelpItem(
									getString(R.string.help_howToReturn_txtHeadline),
									getString(R.string.help_howToReturn_contentHtml_Children)));
				} else {
					HelpContent.addHelpItem(new HelpItem(
							getString(R.string.help_howToProceed_txtHeadline),
							getString(R.string.help_howToProceed_contentHtml)));
					HelpContent.addHelpItem(new HelpItem(
							getString(R.string.help_howToReturn_txtHeadline),
							getString(R.string.help_howToReturn_contentHtml)));
				}
			}

			// general help items for children, technically less skilled and
			// memory handicapped persons
			HelpContent.addGeneralAdaptedHelpItems();
		}
		if (ContextManager.getContextModel().hasContextPropertyByGroup(
				ageContext,
				ageContext != null ? ageContext
						.getGroupByName(AgeContext.CONTEXT_GROUP_CHILDREN)
						: null))
			HelpContent
					.addHelpItem(new HelpItem(
							getString(R.string.help_whatIsSmartCPS_txtHeadline),
							getString(R.string.help_whatIsSmartCPS_contentHtml_Children)));
		else
			HelpContent.addHelpItem(new HelpItem(
					getString(R.string.help_whatIsSmartCPS_txtHeadline),
					getString(R.string.help_whatIsSmartCPS_contentHtml)));
		/* adaptation end */
	}

	public BreadcrumbsParcelable getBreadcrumbHistory() {
		return breadcrumbHistory;
	}

	public void setBreadcrumbHistory(BreadcrumbsParcelable breadcrumbHistory) {
		TutControlActivity.breadcrumbHistory = breadcrumbHistory;
	}

	public static Boolean getRestartActivity() {
		return restartActivity;
	}

	public static void setRestartActivity(Boolean restartActivity) {
		TutControlActivity.restartActivity = restartActivity;
	}

	public Boolean getGalleryDone() {
		return galleryDone;
	}

	public void setGalleryDone(Boolean galleryDone) {
		TutControlActivity.galleryDone = galleryDone;
	}
}
