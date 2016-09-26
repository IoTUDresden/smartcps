package eu.vicci.ecosystem.standalone.controlcenter.android.listeners;

import android.view.View;
import android.view.View.OnClickListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.HomeActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;

/**
 * The listener interface for receiving closeClick events. The class that is
 * interested in processing a closeClick event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addCloseClickListener<code> method. When
 * the closeClick event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see CloseClickEvent
 */
public class CloseClickListener implements OnClickListener {

	private BreadcrumbsActivity fromActivity = null;

	/**
	 * Instantiates a new close click listener.
	 * 
	 * @param fromActivity
	 *            the from activity
	 */
	public CloseClickListener(BreadcrumbsActivity fromActivity) {
		this.fromActivity = fromActivity;
	}

	@Override
	public void onClick(View v) {
		Common.startBreadcrumbsActivity(this.fromActivity,
					HomeActivity.class);
	}

}