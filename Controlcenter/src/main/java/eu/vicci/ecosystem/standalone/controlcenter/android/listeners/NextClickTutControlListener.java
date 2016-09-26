package eu.vicci.ecosystem.standalone.controlcenter.android.listeners;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.TutControlActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.TutReadyActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.ContextManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.AgeContext;

/**
 * The listener interface for receiving nextClickTutControl events. The class
 * that is interested in processing a nextClickTutControl event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addNextClickTutControlListener<code> method. When
 * the nextClickTutControl event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see NextClickTutControlEvent
 */
public class NextClickTutControlListener implements OnClickListener {

	private TutControlActivity fromActivity = null;

	/**
	 * Instantiates a new next click tut control listener.
	 * 
	 * @param fromActivity
	 *            the from activity
	 */
	public NextClickTutControlListener(TutControlActivity fromActivity) {
		this.fromActivity = fromActivity;
	}

	@Override
	public void onClick(View v) {
		/* adaptation: check for age context -> different salutation */
		AgeContext ageContext = (AgeContext) ContextManager.getRegisteredContextByClass(AgeContext.class);
		String hint;

		if (ContextManager.getContextModel().hasContextPropertyByGroup(ageContext,
				ageContext != null ? ageContext.getGroupByName(AgeContext.CONTEXT_GROUP_CHILDREN) : null))
			hint = v.getResources().getString(R.string.tutControl_hint_Children);
		else
			hint = v.getResources().getString(R.string.tutControl_hint);
		/* adaptation end */

		if (!this.fromActivity.getGalleryDone()) {
			Common.showButtonedToastDialog(hint, v.getResources().getString(R.string.btnOk), (Activity) fromActivity);
			this.fromActivity.setGalleryDone(true);
			return;
		}

		Common.startBreadcrumbsActivity(this.fromActivity, TutReadyActivity.class);
		/* adaptation end */

		this.fromActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
}
