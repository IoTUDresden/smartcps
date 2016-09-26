package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.AdaptiveActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsParcelable;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.NavigationalActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;
import eu.vicci.ecosystem.standalone.controlcenter.android.fragments.TableRowCheckBoxFragment;
import eu.vicci.ecosystem.standalone.controlcenter.android.fragments.TableRowEditTextFragment;
import eu.vicci.ecosystem.standalone.controlcenter.android.fragments.TableRowSpinnerFragment;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpContent;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpItem;
import eu.vicci.ecosystem.standalone.controlcenter.android.listeners.AddNewServiceOnClickListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.listeners.NavigationalGestureListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.ContextManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.AgeContext;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.HandicapsContext;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.TechnicalSkillsContext;

/**
 * The activity for adding new services.
 */
@SuppressWarnings("javadoc")
public class AddNewServiceActivity extends NavigationalActivity implements AdaptiveActivity {

	private static BreadcrumbsParcelable breadcrumbHistory;
	private static Integer currentStep = 0;
	private static List<Fragment> fragments;
	private static List<CharSequence> stepDescriptions;
	private static Integer fragmentsPerPage = 0;
	private static TextView tvCurrentStep;
	private static Boolean fragmentsCreated = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Common.setActivityTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_service);
		doAdaptation();

		RelativeLayout relativeLayoutRoot = (RelativeLayout) findViewById(R.id.addNewService_relativeLayoutRoot);
		createGestureOverlay(relativeLayoutRoot, new NavigationalGestureListener(this,
				getGestureLibrary(R.raw.gestures)));

		breadcrumbHistory = (BreadcrumbsParcelable) (getIntent().getExtras() != null ? getIntent().getExtras()
				.getParcelable(PARCEL_KEY) : new BreadcrumbsParcelable(new ArrayList<String>()));
		// SHK clear history, to fit Android guideline to only display the
		// current activity name
		breadcrumbHistory.getBreadcrumbHistory().clear();
		breadcrumbHistory.addBreadcrumbHistoryItem("Neuen Dienst hinzuf�gen");
		initBreadcrumbs(breadcrumbHistory);

		if (!Common.isXLargeTablet(this))
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		if (!fragmentsCreated) {
			fragments = new ArrayList<Fragment>();
			stepDescriptions = new ArrayList<CharSequence>();

			// service name
			stepDescriptions.add(Common.formatStringResourceToHtml(R.string.addNewService_stepDescription1));
			Fragment tableRowServiceName = new TableRowEditTextFragment();
			Bundle argumentBundle = new Bundle();
			argumentBundle.putString(TableRowEditTextFragment.TEXT_KEY,
					getResources().getString(R.string.addNewService_txtName));
			argumentBundle.putInt(TableRowEditTextFragment.ID_KEY, 123456789);
			tableRowServiceName.setArguments(argumentBundle);
			fragments.add(tableRowServiceName);

			// favorites
			Fragment tableRowFavorite = new TableRowCheckBoxFragment();
			argumentBundle = new Bundle();
			argumentBundle.putString(TableRowCheckBoxFragment.TEXT_KEY,
					getResources().getString(R.string.addNewService_txtFavorite));
			argumentBundle.putInt(TableRowCheckBoxFragment.ID_KEY, 123456789);
			tableRowFavorite.setArguments(argumentBundle);
			fragments.add(tableRowFavorite);

			// device
			Fragment tableRowDevice = null;
			argumentBundle = new Bundle();

			tableRowDevice = new TableRowSpinnerFragment();
			argumentBundle.putString(TableRowSpinnerFragment.TEXT_KEY,
					getResources().getString(R.string.addNewService_txtDevice));
			argumentBundle.putInt(TableRowSpinnerFragment.ID_KEY, 123456789);
			argumentBundle.putInt(TableRowSpinnerFragment.ITEMS_KEY, R.array.addNewService_spnDeviceChoices);
			tableRowDevice.setArguments(argumentBundle);
			fragments.add(tableRowDevice);

			// effect
			stepDescriptions.add(Common.formatStringResourceToHtml(R.string.addNewService_stepDescription2));
			Fragment tableRowEffect = null;
			argumentBundle = new Bundle();

			tableRowEffect = new TableRowSpinnerFragment();
			argumentBundle.putString(TableRowSpinnerFragment.TEXT_KEY,
					getResources().getString(R.string.addNewService_txtEffect));
			argumentBundle.putInt(TableRowSpinnerFragment.ID_KEY, 123456789);
			argumentBundle.putInt(TableRowSpinnerFragment.ITEMS_KEY, R.array.addNewService_spnEffectChoices);
			tableRowEffect.setArguments(argumentBundle);
			fragments.add(tableRowEffect);

			// state
			stepDescriptions.add(Common.formatStringResourceToHtml(R.string.addNewService_stepDescription3));
			Fragment tableRowState = null;
			argumentBundle = new Bundle();

			tableRowState = new TableRowSpinnerFragment();
			argumentBundle.putString(TableRowSpinnerFragment.TEXT_KEY,
					getResources().getString(R.string.addNewService_txtState));
			argumentBundle.putInt(TableRowSpinnerFragment.ID_KEY, 123456789);
			argumentBundle.putInt(TableRowSpinnerFragment.ITEMS_KEY, R.array.addNewService_spnStateChoices);
			tableRowState.setArguments(argumentBundle);
			fragments.add(tableRowState);

			// state value
			Fragment tableRowStateValue = null;
			argumentBundle = new Bundle();

			tableRowStateValue = new TableRowSpinnerFragment();
			argumentBundle.putString(TableRowSpinnerFragment.TEXT_KEY,
					getResources().getString(R.string.addNewService_txtStateValue));
			argumentBundle.putInt(TableRowSpinnerFragment.ID_KEY, 123456789);
			argumentBundle.putInt(TableRowSpinnerFragment.ITEMS_KEY, R.array.addNewService_spnStateValueChoices);
			tableRowStateValue.setArguments(argumentBundle);
			fragments.add(tableRowStateValue);

			fragmentsCreated = true;
		}

		/* navigational adaptation: assistant based dialog management */
		if (Common.getBooleanPreference("settings_ui_assistant_based_navigation", false)
				|| Common.getBooleanPreference("settings_reel_menu_active", false)) {

			if (savedInstanceState == null && currentStep == 0) {
				Common.addFragments(R.id.addNewService_tableLayout, getFragmentManager(), fragments.get(0),
						fragments.get(1));
			}
			fragmentsPerPage = 2;
			Button btnAddNext = (Button) findViewById(R.id.btnNext);
			btnAddNext.setText(getResources().getString(R.string.btnNext));
			LinearLayout linearLayout = (LinearLayout) findViewById(R.id.addNewService_linearLayout);
			TextView tvDescription = (TextView) getLayoutInflater().inflate(R.xml._add_new_service_tv_description,
					linearLayout, false);
			linearLayout.addView(tvDescription, 1);
			tvCurrentStep = new TextView(this);
			tvCurrentStep.setGravity(Gravity.CENTER_HORIZONTAL);
			tvCurrentStep.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimensionPixelSize(R.dimen.tsNormal));
			tvCurrentStep.setText(String.format(getResources().getString(R.string.currentStep), currentStep + 1,
					fragments.size() % 2 == 0 ? fragments.size() / fragmentsPerPage
							: (int) (fragments.size() / fragmentsPerPage) + 1));
			linearLayout.addView(tvCurrentStep, 2);
			tvDescription.setText(stepDescriptions.get(0));
			btnAddNext.setOnClickListener(new AddNewServiceOnClickListener(fragmentsPerPage,
					R.id.addNewService_tableLayout, fragments, stepDescriptions, tvCurrentStep));
		} else {
			if (savedInstanceState == null && currentStep == 0)
				Common.addFragments(R.id.addNewService_tableLayout, getFragmentManager(), fragments);
		}
		/* adaptation end */
	}

	@Override
	public void onBackPressed() {
		if (currentStep > 0) {
			for (int i = currentStep * fragmentsPerPage; i < (currentStep + 1) * fragmentsPerPage; i++) {
				getFragmentManager().beginTransaction().remove(fragments.get(i)).commit();
			}
			// for some odd reason the table rows aren't removed by removing
			// their fragment :-(
			((ViewGroup) findViewById(R.id.addNewService_tableLayout)).removeAllViews();
			AddNewServiceActivity.decreaseCurrentStep();
			for (int i = currentStep * fragmentsPerPage; i < (currentStep + 1) * fragmentsPerPage; i++) {
				getFragmentManager().beginTransaction().add(R.id.addNewService_tableLayout, fragments.get(i)).commit();
			}
			((TextView) findViewById(R.id.addNewService_txtStepDescription)).setText(stepDescriptions.get(currentStep));
			tvCurrentStep.setText(String.format(getResources().getString(R.string.currentStep), currentStep + 1,
					fragments.size() % 2 == 0 ? fragments.size() / fragmentsPerPage
							: (int) (fragments.size() / fragmentsPerPage) + 1));
			if ((currentStep + 1) * fragmentsPerPage < fragments.size())
				((Button) findViewById(R.id.btnNext)).setText(getString(R.string.btnNext));
		} else {
			super.onBackPressed();
			fragmentsCreated = false;
		}
	}

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

			// context sensitive help items
			if (ContextManager.getContextModel().hasContextProperty(handicapContext,
					HandicapsContext.CONTEXT_GROUP_MEMORY)) {
				if (ContextManager.getContextModel().hasContextPropertyByGroup(ageContext,
						ageContext != null ? ageContext.getGroupByName(AgeContext.CONTEXT_GROUP_CHILDREN) : null)) {
					HelpContent.addHelpItem(new HelpItem(getString(R.string.help_howToProceed_txtHeadline),
							getString(R.string.help_howToProceed_contentHtml_Children)));
					HelpContent.addHelpItem(new HelpItem(getString(R.string.help_howToReturn_txtHeadline),
							getString(R.string.help_howToReturn_contentHtml_Children)));
				} else {
					HelpContent.addHelpItem(new HelpItem(getString(R.string.help_howToProceed_txtHeadline),
							getString(R.string.help_howToProceed_contentHtml)));
					HelpContent.addHelpItem(new HelpItem(getString(R.string.help_howToReturn_txtHeadline),
							getString(R.string.help_howToReturn_contentHtml)));
				}
			}

			// general help items for children, technically less skilled and
			// memory handicapped persons
			HelpContent.addGeneralAdaptedHelpItems();
		}
		HelpContent.addGeneralHelpItems();
		/* adaptation end */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}

	public BreadcrumbsParcelable getBreadcrumbHistory() {
		return breadcrumbHistory;
	}

	public void setBreadcrumbHistory(BreadcrumbsParcelable breadcrumbHistory) {
		AddNewServiceActivity.breadcrumbHistory = breadcrumbHistory;
	}

	public static Integer getCurrentStep() {
		return currentStep;
	}

	public static void setCurrentStep(Integer currentStep) {
		AddNewServiceActivity.currentStep = currentStep;
	}

	public static void increaseCurrentStep() {
		currentStep++;
	}

	public static void decreaseCurrentStep() {
		currentStep--;
	}

}
