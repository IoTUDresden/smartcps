package eu.vicci.ecosystem.standalone.controlcenter.android.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javatuples.Quartet;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.GalleryActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;

/**
 * The listener interface for receiving tutGalleryGesture events. The class that
 * is interested in processing a tutGalleryGesture event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addTutGalleryGestureListener<code> method. When
 * the tutGalleryGesture event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see TutGalleryGestureEvent
 */
public class TutGalleryGestureListener implements OnGesturePerformedListener {

	private static final String CIRCLE_CLOCKWISE = "CircleClockwise";
	private static final String CIRCLE_COUNTER_CLOCKWISE = "CircleCounterClockwise";
	private static final String LEFT2RIGHT = "Left2Right";
	private static final String RIGHT2LEFT = "Right2Left";

	private GalleryActivity activity;
	private GestureLibrary gestureLib;
	private TextView currentStep;
	private List<View> reelMenuImageViews;

	/**
	 * Instantiates a new tut gallery gesture listener.
	 * 
	 * @param activity
	 *            the activity
	 * @param gestureLib
	 *            the gesture lib
	 * @param currentStep
	 *            the current step
	 * @param reelMenuImageViews
	 *            the reel menu image views
	 */
	public TutGalleryGestureListener(GalleryActivity activity,
			GestureLibrary gestureLib, TextView currentStep,
			View... reelMenuImageViews) {
		this.activity = activity;
		this.gestureLib = gestureLib;
		this.currentStep = currentStep;
		this.reelMenuImageViews = new ArrayList<View>();
		this.reelMenuImageViews.addAll(Arrays.asList(reelMenuImageViews));
	}

	@Override
	public void onGesturePerformed(GestureOverlayView gestureView,
			Gesture gesture) {
		Quartet<View, View, View, View> visibleImageViewsEnvironment = Common
				.getVisibleViewsEnvironment(this.reelMenuImageViews);
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);

		if (visibleImageViewsEnvironment.getValue3() == null)
			this.activity.setGalleryDone(true);

		if (predictions.size() > 0) {
			Prediction prediction = predictions.get(0);
			if (prediction.score > 1.0) {
				if (prediction.name.equals(RIGHT2LEFT)) {
					if (visibleImageViewsEnvironment.getValue2() != null) {
						visibleImageViewsEnvironment.getValue1()
								.startAnimation(
										AnimationUtils.loadAnimation(
												(Activity) activity,
												R.anim.slide_out_left));
						visibleImageViewsEnvironment.getValue1().setVisibility(
								View.INVISIBLE);
						visibleImageViewsEnvironment.getValue2().setVisibility(
								View.VISIBLE);
						visibleImageViewsEnvironment.getValue2()
								.startAnimation(
										AnimationUtils.loadAnimation(
												(Activity) activity,
												R.anim.slide_in_right));
						this.currentStep.setText(String.format(
								((Activity) activity).getResources().getString(
										R.string.currentStep),
								this.reelMenuImageViews
										.indexOf(visibleImageViewsEnvironment
												.getValue2()) + 1,
								this.reelMenuImageViews.size()));
					}
				} else if (prediction.name.equals(LEFT2RIGHT)) {
					if (visibleImageViewsEnvironment.getValue0() != null) {
						visibleImageViewsEnvironment.getValue1()
								.startAnimation(
										AnimationUtils.loadAnimation(
												(Activity) activity,
												R.anim.slide_out_right));
						visibleImageViewsEnvironment.getValue1().setVisibility(
								View.INVISIBLE);
						visibleImageViewsEnvironment.getValue0().setVisibility(
								View.VISIBLE);
						visibleImageViewsEnvironment.getValue0()
								.startAnimation(
										AnimationUtils.loadAnimation(
												(Activity) activity,
												R.anim.slide_in_left));
						this.currentStep.setText(String.format(
								((Activity) activity).getResources().getString(
										R.string.currentStep),
								this.reelMenuImageViews
										.indexOf(visibleImageViewsEnvironment
												.getValue0()) + 1,
								this.reelMenuImageViews.size()));
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
