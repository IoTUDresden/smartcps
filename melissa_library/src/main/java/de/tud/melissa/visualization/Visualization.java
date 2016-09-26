package de.tud.melissa.visualization;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Root class of the Melissa-library.
 *
 * @author Andreas Hippler
 * 
 * @param <T> type for setting a new value
 * 
 * @see RelativeLayout
 */
public abstract class Visualization<T> extends RelativeLayout {
	/**
	 * @param context the Android context of the RelativeLayout
	 * 
	 * @see RelativeLayout
	 */
	public Visualization(Context context) {
		super(context);
	}
	
	/**
	 * @param context the Android context of the View
	 * @param attributeSet the AttributeSet for the RelativeLayout
	 * 
	 * @see RelativeLayout
	 */
	public Visualization(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}
	
	/**
	 * Set a new value for the visualization.<br>
	 * <br>
	 * The View will redraw after calling this method.
	 * 
	 * @param value the new value for the visualization
	 */
	public abstract void setValue(T value);
}
