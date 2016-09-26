package eu.vicci.ecosystem.standalone.controlcenter.android.activities.general;

import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.listeners.CloseClickListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.listeners.NextClickListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.services.HelpReminderService;

/**
 * The abstract activity for navigational activities.
 */
public abstract class NavigationalActivity extends BreadcrumbsActivity {

	private Boolean rootActivity = false;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.help, menu);
		HelpReminderService.setActivity(this);
		HelpReminderService.setHelpMenuItem(menu.getItem(0));
		HelpReminderService.startService();
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (!rootActivity)
			this.overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
	}

	/**
	 * Sets the close click listener.
	 * 
	 * @param fromActivity
	 *            the new close click listener
	 */
	public void setCloseClickListener(NavigationalActivity fromActivity) {
		Button btnClose = (Button) findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new CloseClickListener(fromActivity));
	}

	/**
	 * Sets the next click listener.
	 * 
	 * @param fromActivity
	 *            the from activity
	 * @param toCls
	 *            the to cls
	 */
	@SuppressWarnings("rawtypes")
	public void setNextClickListener(NavigationalActivity fromActivity,
			Class toCls) {
		Button btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new NextClickListener(fromActivity, toCls));
	}

	/**
	 * Sets a custom next click listener.
	 * 
	 * @param fromActivity
	 *            the from activity
	 * @param listener
	 *            the listener
	 */
	public void setCustomNextClickListener(NavigationalActivity fromActivity,
			OnClickListener listener) {
		Button btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(listener);
	}

	/**
	 * Creates a gesture overlay.
	 * 
	 * @param viewGroup
	 *            the view group
	 * @param gestureListener
	 *            the gesture listener
	 */
	public void createGestureOverlay(ViewGroup viewGroup,
			OnGesturePerformedListener gestureListener) {
		// add a gesture overlay view to recognize the arbitrary gestures
		GestureOverlayView gestureOverlayView = new GestureOverlayView(
				this.getApplicationContext());
		gestureOverlayView.setFadeOffset(0);
		gestureOverlayView.setGestureColor(Color.TRANSPARENT);
		gestureOverlayView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		gestureOverlayView.addOnGesturePerformedListener(gestureListener);
		viewGroup.addView(gestureOverlayView, 0);
	}

	/**
	 * Gets the gesture library.
	 * 
	 * @param id
	 *            the id
	 * @return the gesture library
	 */
	public GestureLibrary getGestureLibrary(int id) {
		// gesture containing the different possible gestures (swipe, circle,
		// ...)
		GestureLibrary gestureLib = GestureLibraries.fromRawResource(this, id);
		if (!gestureLib.load()) {
			Log.w("com.android.gesture", "could not load gesture library");
			finish();
		}
		return gestureLib;
	}

	/**
	 * Checks if is root activity.
	 * 
	 * @return the boolean
	 */
	public Boolean isRootActivity() {
		return rootActivity;
	}

	/**
	 * Sets the root activity.
	 * 
	 * @param rootActivity
	 *            the new root activity
	 */
	public void setRootActivity(Boolean rootActivity) {
		this.rootActivity = rootActivity;
	}

}
