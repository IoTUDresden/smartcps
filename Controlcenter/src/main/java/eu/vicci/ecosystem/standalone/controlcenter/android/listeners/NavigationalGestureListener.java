package eu.vicci.ecosystem.standalone.controlcenter.android.listeners;

import java.util.ArrayList;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.NavigationalActivity;

/**
 * The listener interface for receiving navigationalGesture events. The class
 * that is interested in processing a navigationalGesture event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addNavigationalGestureListener<code> method. When
 * the navigationalGesture event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see NavigationalGestureEvent
 */
public class NavigationalGestureListener implements OnGesturePerformedListener {

	protected static final String CIRCLE_CLOCKWISE = "CircleClockwise";
	protected static final String CIRCLE_COUNTER_CLOCKWISE = "CircleCounterClockwise";
	protected static final String LEFT2RIGHT = "Left2Right";
	protected static final String RIGHT2LEFT = "Right2Left";

	protected NavigationalActivity activity;
	protected GestureLibrary gestureLib;

	/**
	 * Instantiates a new navigational gesture listener.
	 * 
	 * @param activity
	 *            the activity
	 * @param gestureLib
	 *            the gesture lib
	 */
	public NavigationalGestureListener(NavigationalActivity activity,
			GestureLibrary gestureLib) {
		this.activity = activity;
		this.gestureLib = gestureLib;
	}

	@Override
	public void onGesturePerformed(GestureOverlayView gestureView,
			Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);

		if (predictions.size() > 0) {
			Prediction prediction = predictions.get(0);
			if (prediction.score > 1.0) {
				if (prediction.name.equals(RIGHT2LEFT)) {
					try {
						((Activity) this.activity).findViewById(R.id.btnNext)
								.performClick();
						this.activity.overridePendingTransition(
								R.anim.slide_in_right, R.anim.slide_out_left);
					} catch (NullPointerException e) {
					}
				} else if (prediction.name.equals(LEFT2RIGHT)) {
					if (!this.activity.isRootActivity()) {
						this.activity.onBackPressed();
						this.activity.overridePendingTransition(
								R.anim.slide_in_left, R.anim.slide_out_right);
					}
				} else if (prediction.name.equals(CIRCLE_CLOCKWISE)
						|| prediction.name.equals(CIRCLE_COUNTER_CLOCKWISE)) {
					try {
						((Activity) this.activity).findViewById(R.id.menu_help)
								.performClick();
					} catch (NullPointerException e) {
					}
				}
			}
		}
	}

}
