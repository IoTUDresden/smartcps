package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.AdaptiveActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsParcelable;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.NavigationalActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpContent;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpItem;
import eu.vicci.ecosystem.standalone.controlcenter.android.listeners.NavigationalGestureListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.ContextManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.AgeContext;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.HandicapsContext;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.TechnicalSkillsContext;

/**
 * The tutorial activity for setting some theme.
 */
public class TutChangeThemeActivity extends NavigationalActivity implements
		AdaptiveActivity {

	private static BreadcrumbsParcelable breadcrumbHistory = new BreadcrumbsParcelable(
			new ArrayList<String>());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Common.setActivityTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tut_change_theme);

		RelativeLayout relativeLayoutRoot = (RelativeLayout) findViewById(R.id.tutChangeTheme_relativeLayoutRoot);
		Common.formatTextViewsToHtml(relativeLayoutRoot);
		setNextClickListener(this, TutControlActivity.class);
		setCloseClickListener(this);
		createGestureOverlay(relativeLayoutRoot,
				new NavigationalGestureListener(this,
						getGestureLibrary(R.raw.gestures)));

		ToggleButton tglLight = ((ToggleButton) findViewById(R.id.tutChangeTheme_tglLight));
		ToggleButton tglDark = ((ToggleButton) findViewById(R.id.tutChangeTheme_tglDark));

		if (Common.getStringPreference("settings_ui_theme", "0").equals("1")) {
			tglLight.setChecked(false);
			tglDark.setChecked(true);
		}
		tglLight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Common.setStringPreference("settings_ui_theme", "0");
				TutWelcomeActivity.setRestartActivity(true);
				TutEvaluationActivity.setRestartActivity(true);
				TutEvaluationAdditionalActivity.setRestartActivity(true);
				Common.restartActivity((Activity) v.getContext());
			}
		});

		tglDark.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Common.setStringPreference("settings_ui_theme", "1");
				TutWelcomeActivity.setRestartActivity(true);
				TutEvaluationActivity.setRestartActivity(true);
				TutEvaluationAdditionalActivity.setRestartActivity(true);
				Common.restartActivity((Activity) v.getContext());
			}
		});

		breadcrumbHistory = getIntent().getExtras().getParcelable(PARCEL_KEY);
		breadcrumbHistory.addBreadcrumbHistoryItem("Farbschema");
		initBreadcrumbs(breadcrumbHistory);

		if (!Common.isXLargeTablet(this))
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	protected void onResume() {
		doAdaptation();
		super.onResume();
	}

	public void doAdaptation() {
		/*
		 * adaptation: check for age, technical skills and handicap context ->
		 * different help items
		 */
		AgeContext ageContext = (AgeContext) ContextManager
				.getRegisteredContextByClass(AgeContext.class);
		TechnicalSkillsContext technicalSkillsContext = (TechnicalSkillsContext) ContextManager
				.getRegisteredContextByClass(TechnicalSkillsContext.class);
		HandicapsContext handicapContext = (HandicapsContext) ContextManager
				.getRegisteredContextByClass(HandicapsContext.class);
		Resources resources = SmartCPS_Impl.getAppContext().getResources();

		TextView txtNormal1 = (TextView) findViewById(R.id.tutChangeTheme_txtNormal1);

		if (ContextManager.getContextModel().hasContextPropertyByGroup(
				ageContext,
				ageContext != null ? ageContext
						.getGroupByName(AgeContext.CONTEXT_GROUP_CHILDREN)
						: null))
			txtNormal1
					.setText(Common
							.formatStringResourceToHtml(R.string.tutChangeTheme_txtNormal1_Children));
		else
			txtNormal1
					.setText(Common
							.formatStringResourceToHtml(R.string.tutChangeTheme_txtNormal1));

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
									resources
											.getString(R.string.help_howToProceed_txtHeadline),
									getString(R.string.help_howToProceed_contentHtml_Children)));
					HelpContent
							.addHelpItem(new HelpItem(
									resources
											.getString(R.string.help_howToReturn_txtHeadline),
									getString(R.string.help_howToReturn_contentHtml_Children)));
				} else {
					HelpContent.addHelpItem(new HelpItem(resources
							.getString(R.string.help_howToProceed_txtHeadline),
							getString(R.string.help_howToProceed_contentHtml)));
					HelpContent.addHelpItem(new HelpItem(resources
							.getString(R.string.help_howToReturn_txtHeadline),
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
		TutChangeThemeActivity.breadcrumbHistory = breadcrumbHistory;
	}

}
