package eu.vicci.ecosystem.standalone.controlcenter.android.listeners;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.AddNewServiceActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;

/**
 * The listener interface for receiving addNewServiceOnClick events. The class
 * that is interested in processing a addNewServiceOnClick event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addAddNewServiceOnClickListener<code> method. When
 * the addNewServiceOnClick event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see AddNewServiceOnClickEvent
 */
public class AddNewServiceOnClickListener implements OnClickListener {

	private Integer fragmentsPerPage;
	private Integer containerResId;
	private List<Fragment> fragments;
	private List<CharSequence> stepDescriptions;
	private TextView tvCurrentStep;

	/**
	 * Instantiates a new adds the new service on click listener.
	 * 
	 * @param fragmentsPerPage
	 *            the fragments per page
	 * @param containerResId
	 *            the container res id
	 * @param fragments
	 *            the fragments
	 * @param stepDescriptions
	 *            the step descriptions
	 * @param tvCurrentStep
	 *            the tv current step
	 */
	public AddNewServiceOnClickListener(Integer fragmentsPerPage,
			Integer containerResId, List<Fragment> fragments,
			List<CharSequence> stepDescriptions, TextView tvCurrentStep) {
		this.fragmentsPerPage = fragmentsPerPage;
		this.containerResId = containerResId;
		this.fragments = fragments;
		this.stepDescriptions = stepDescriptions;
		this.tvCurrentStep = tvCurrentStep;
	}

	@Override
	public void onClick(View v) {
		FragmentManager fragmentManager = ((Activity) v.getContext())
				.getFragmentManager();
		Integer currentStep = AddNewServiceActivity.getCurrentStep();
		if ((currentStep + 1) * fragmentsPerPage < fragments.size()) {
			for (int i = currentStep * fragmentsPerPage; i < (currentStep + 1)
					* fragmentsPerPage; i++) {
				fragmentManager.beginTransaction().remove(fragments.get(i))
						.commit();
			}
			// for some odd reason the table rows aren't removed by removing
			// their fragment :-(
			((ViewGroup) ((Activity) v.getContext())
					.findViewById(containerResId)).removeAllViews();
			AddNewServiceActivity.increaseCurrentStep();
			currentStep = AddNewServiceActivity.getCurrentStep();
			for (int i = currentStep * fragmentsPerPage; i < (currentStep + 1)
					* fragmentsPerPage; i++) {
				fragmentManager.beginTransaction()
						.add(this.containerResId, fragments.get(i)).commit();
			}
			((TextView) ((Activity) v.getContext())
					.findViewById(R.id.addNewService_txtStepDescription))
					.setText(stepDescriptions.get(currentStep));
			tvCurrentStep.setText(String.format(
					v.getResources().getString(R.string.currentStep),
					currentStep + 1,
					fragments.size() % 2 == 0 ? fragments.size()
							/ fragmentsPerPage
							: (int) (fragments.size() / fragmentsPerPage) + 1));
			if (!((currentStep + 1) * fragmentsPerPage < fragments.size()))
				((Button) v.findViewById(R.id.btnNext)).setText(v
						.getResources()
						.getString(R.string.addNewService_btnAdd));
		} else {
			Common.showToast(
					v.getResources().getString(R.string.notImplementedYet),
					v.getContext());
		}
	}

}
