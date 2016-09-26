package de.tud.melissa.visualization;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import de.tud.melissa.R;

/**
 * Discrete range visualization
 * 
 * @author Andreas Hippler
 */
public abstract class DiscreteVisualization extends Visualization<Integer> {

	private ViewFlipper mViewFlipper;

	/**
	 * @param context
	 */
	public DiscreteVisualization(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attributeSet
	 */
	public DiscreteVisualization(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);

		View.inflate(context, R.layout.discrete_visualization, this);
		mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

		ImageView iv = null;

		for (int i = 0; i < getNumStates(); i++) {
			iv = new ImageView(context);
			iv.setImageResource(getItmageResourceID(i));
			mViewFlipper.addView(iv);
		}
	}

	/**
	 * Set the new state. Default state is 0.
	 * 
	 * @param value
	 *            current value
	 */
	public void setValue(Integer value) {
		if (value < getNumStates() && value > 0)
			mViewFlipper.setDisplayedChild(value);
		else
			mViewFlipper.setDisplayedChild(0);

		postInvalidate();
	}

	protected abstract int getNumStates();

	protected abstract int getItmageResourceID(int state);
}
