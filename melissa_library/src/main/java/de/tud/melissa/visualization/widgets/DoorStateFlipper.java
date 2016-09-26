package de.tud.melissa.visualization.widgets;

import android.content.Context;
import android.util.AttributeSet;
import de.tud.melissa.R;
import de.tud.melissa.visualization.DiscreteVisualization;

/**
 * Class that enables flipping between different door states
 * 
 * @author Andreas Hippler
 */
public class DoorStateFlipper extends DiscreteVisualization {

	/** Log and identification tag */
	public static final String TAG = DoorStateFlipper.class.getSimpleName();

	/**
	 * @param context
	 */
	public DoorStateFlipper(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param set
	 */
	public DoorStateFlipper(Context context, AttributeSet set) {
		super(context, set);
	}

	@Override
	protected int getNumStates() {
		return 4;
	}

	private static final int[] images = { R.raw.door_unknown, R.raw.door_open_blue, R.raw.door_closed_blue,
			R.raw.door_locked_blue };

	@Override
	protected int getItmageResourceID(int state) {
		return images[state];
	}
}