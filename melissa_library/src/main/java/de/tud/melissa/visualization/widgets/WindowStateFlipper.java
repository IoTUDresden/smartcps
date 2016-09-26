package de.tud.melissa.visualization.widgets;

import android.content.Context;
import android.util.AttributeSet;
import de.tud.melissa.R;
import de.tud.melissa.visualization.DiscreteVisualization;

/**
 * Class that enables flipping between different window states
 * 
 * @author Andreas Hippler
 */
public class WindowStateFlipper extends DiscreteVisualization {
	/**
	 * Log and identification tag
	 */
	public static final String TAG = WindowStateFlipper.class.getName();

	/**
	 * @param context
	 */
	public WindowStateFlipper(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param set
	 */
	public WindowStateFlipper(Context context, AttributeSet set) {
		super(context, set);
	}

	@Override
	protected int getNumStates() {
		return 4;
	}

	private static final int[] images = { R.raw.window_unknown, R.raw.window_open_blue2, R.raw.window_closed_blue,
			R.raw.window_tilted };

	@Override
	protected int getItmageResourceID(int state) {
		return images[state];
	}
}