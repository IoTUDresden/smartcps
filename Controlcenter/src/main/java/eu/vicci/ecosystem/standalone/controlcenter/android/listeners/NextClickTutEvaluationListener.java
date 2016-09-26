package eu.vicci.ecosystem.standalone.controlcenter.android.listeners;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.ToggleButton;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.MainActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.TutChangeThemeActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.TutControlActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.TutEvaluationAdditionalActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.NavigationalActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;
import eu.vicci.ecosystem.standalone.controlcenter.android.persistence.AndroidPersistence;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.ContextManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.ContextModel;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.ContextProperty;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.AgeContext;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.HandicapsContext;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.TechnicalSkillsContext;

/**
 * The listener interface for receiving nextClickTutEvaluation events. The class
 * that is interested in processing a nextClickTutEvaluation event implements
 * this interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addNextClickTutEvaluationListener<code> method. When
 * the nextClickTutEvaluation event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see NextClickTutEvaluationEvent
 */
public class NextClickTutEvaluationListener implements OnClickListener {

	private NavigationalActivity fromActivity = null;

	/**
	 * Instantiates a new next click tut evaluation listener.
	 * 
	 * @param fromActivity
	 *            the from activity
	 */
	public NextClickTutEvaluationListener(NavigationalActivity fromActivity) {
		this.fromActivity = fromActivity;
	}

	@Override
	public void onClick(View v) {
		if (Common.isXLargeTablet(v.getContext())
				|| fromActivity instanceof TutEvaluationAdditionalActivity) {
			// get an existing ContextModel object (or create a new one if no
			// one's there)
			ContextModel contextModel = ContextManager.getContextModel();

			addEnteredContextPropertiesToContextModel(contextModel);
			AndroidPersistence.persistObject(contextModel, contextModel
					.getClass().getName() + MainActivity.DB_EXTENSION,
					fromActivity);

			/* adaptation: check for handicaps -> different tutorial */
			HandicapsContext handicapsContext = (HandicapsContext) ContextManager
					.getRegisteredContextByClass(HandicapsContext.class);
			if (contextModel.hasContextProperty(handicapsContext,
					HandicapsContext.CONTEXT_GROUP_SIGHT))
				Common.startBreadcrumbsActivity(fromActivity,
						TutControlActivity.class);
			else
				Common.startBreadcrumbsActivity(fromActivity,
						TutChangeThemeActivity.class);
			/* adaptation end */

			this.fromActivity.overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		} else {
			startActivity(TutEvaluationAdditionalActivity.class);
			this.fromActivity.overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		}
	}

	public void addEnteredContextPropertiesToContextModel(
			ContextModel contextModel) {
		// remove possibly existing context properties
		contextModel.removeAllContextProperties();

		// retrieve values from the defined ui elements
		List<ContextProperty> contextProperties = new ArrayList<ContextProperty>();

		if (Common.isXLargeTablet(this.fromActivity)) {
			// age
			contextProperties.add(new ContextProperty(ContextManager
					.getRegisteredContextByClass(AgeContext.class),
					((Spinner) this.fromActivity
							.findViewById(R.id.tutEvaluation_spnAge))
							.getSelectedItem().toString()));
			// technical skills
			contextProperties
					.add(new ContextProperty(
							ContextManager
									.getRegisteredContextByClass(TechnicalSkillsContext.class),
							((Spinner) this.fromActivity
									.findViewById(R.id.tutEvaluation_spnTechnicalSkills))
									.getSelectedItem().toString()));
		} else if (fromActivity instanceof TutEvaluationAdditionalActivity) {
			// age
			contextProperties.add(new ContextProperty(ContextManager
					.getRegisteredContextByClass(AgeContext.class),
					this.fromActivity.getIntent().getStringExtra("Age")));
			// technical skills
			contextProperties.add(new ContextProperty(ContextManager
					.getRegisteredContextByClass(TechnicalSkillsContext.class),
					this.fromActivity.getIntent().getStringExtra(
							"TechnicalSkills")));
		}

		// handicaps
		if (((ToggleButton) this.fromActivity
				.findViewById(R.id.tutEvaluation_tglHandicapsSight))
				.isChecked()) {
			contextProperties.add(new ContextProperty(ContextManager
					.getRegisteredContextByClass(HandicapsContext.class),
					HandicapsContext.CONTEXT_GROUP_SIGHT));
			Common.setBooleanPreference("settings_ui_colored_state_encoding",
					false);
			Common.setStringPreference("settings_ui_theme", "0");
		} else
			Common.setBooleanPreference("settings_ui_colored_state_encoding",
					true);

		if (((ToggleButton) this.fromActivity
				.findViewById(R.id.tutEvaluation_tglHandicapsMotorSkills))
				.isChecked()) {
			contextProperties.add(new ContextProperty(ContextManager
					.getRegisteredContextByClass(HandicapsContext.class),
					HandicapsContext.CONTEXT_GROUP_MOTOR_SKILLS));
			Common.setBooleanPreference("settings_reel_menu_active", true);
		} else
			Common.setBooleanPreference("settings_reel_menu_active", false);

		if (((ToggleButton) this.fromActivity
				.findViewById(R.id.tutEvaluation_tglHandicapsMemory))
				.isChecked()) {
			contextProperties.add(new ContextProperty(ContextManager
					.getRegisteredContextByClass(HandicapsContext.class),
					HandicapsContext.CONTEXT_GROUP_MEMORY));
			Common.setBooleanPreference("settings_ui_help_reminder", true);
		} else {
			Common.setBooleanPreference("settings_ui_help_reminder", false);
		}
		contextModel.addContextProperties(contextProperties);

		AgeContext ageContext = (AgeContext) ContextManager
				.getRegisteredContextByClass(AgeContext.class);
		TechnicalSkillsContext technicalSkillsContext = (TechnicalSkillsContext) ContextManager
				.getRegisteredContextByClass(TechnicalSkillsContext.class);
		HandicapsContext handicapContext = (HandicapsContext) ContextManager
				.getRegisteredContextByClass(HandicapsContext.class);

		if (contextModel.hasContextPropertyByGroup(
				ageContext,
				ageContext != null ? ageContext
						.getGroupByName(AgeContext.CONTEXT_GROUP_CHILDREN)
						: null)
				|| contextModel
						.hasContextProperty(
								technicalSkillsContext,
								TechnicalSkillsContext.CONTEXT_GROUP_LESS_TECHNICALLY_SKILLED)
				|| contextModel.hasContextProperty(handicapContext,
						HandicapsContext.CONTEXT_GROUP_MEMORY)
				|| contextModel.hasContextProperty(handicapContext,
						HandicapsContext.CONTEXT_GROUP_MOTOR_SKILLS)) {
			Common.setBooleanPreference(
					"settings_ui_assistant_based_navigation", true);
		} else
			Common.setBooleanPreference(
					"settings_ui_assistant_based_navigation", false);
	}

	@SuppressWarnings("rawtypes")
	public void startActivity(Class toClass) {
		Intent intent = new Intent(fromActivity.getApplicationContext(),
				toClass);
		intent.putExtra("BreadcrumbHistory",
				fromActivity.getBreadcrumbHistory());
		intent.putExtra("Age", ((Spinner) this.fromActivity
				.findViewById(R.id.tutEvaluation_spnAge)).getSelectedItem()
				.toString());
		intent.putExtra("TechnicalSkills", ((Spinner) this.fromActivity
				.findViewById(R.id.tutEvaluation_spnTechnicalSkills))
				.getSelectedItem().toString());
		fromActivity.startActivity(intent);
	}
}
