package eu.vicci.ecosystem.standalone.controlcenter.android.listeners;

import android.view.View;
import android.view.View.OnClickListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.HomeActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.TutReadyActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;

/**
 * The listener interface for receiving nextClickTutReady events. The class that
 * is interested in processing a nextClickTutReelMenuExercise event implements
 * this interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addNextClickTutReadyListener<code> method. When
 * the nextClickTutReady event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see NextClickTutReelMenuExerciseEvent
 */
public class NextClickTutReadyListener implements OnClickListener {

	private TutReadyActivity fromActivity = null;

	/**
	 * Instantiates a new next click listener.
	 * 
	 * @param fromActivity
	 *            the from activity
	 */
	public NextClickTutReadyListener(TutReadyActivity fromActivity) {
		this.fromActivity = fromActivity;
	}

	@Override
	public void onClick(View v) {
		Common.startBreadcrumbsActivity(this.fromActivity,
					HomeActivity.class);
	}

}
