package eu.vicci.ecosystem.standalone.controlcenter.android.listeners;

import android.view.View;
import android.view.View.OnClickListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;

/**
 * The listener interface for receiving nextClick events. The class that is
 * interested in processing a nextClick event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addNextClickListener<code> method. When
 * the nextClick event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see NextClickEvent
 */
@SuppressWarnings("rawtypes")
public class NextClickListener implements OnClickListener {

	private BreadcrumbsActivity fromActivity = null;
	private Class toClass = null;

	/**
	 * Instantiates a new next click listener.
	 * 
	 * @param fromActivity
	 *            the from activity
	 * @param toClass
	 *            the to class
	 */
	public NextClickListener(BreadcrumbsActivity fromActivity, Class toClass) {
		this.fromActivity = fromActivity;
		this.toClass = toClass;
	}

	@Override
	public void onClick(View v) {
		Common.startBreadcrumbsActivity(this.fromActivity, toClass);
		this.fromActivity.overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
	}

}