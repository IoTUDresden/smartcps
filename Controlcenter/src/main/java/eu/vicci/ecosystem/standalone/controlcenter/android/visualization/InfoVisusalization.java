package eu.vicci.ecosystem.standalone.controlcenter.android.visualization;

import de.tud.melissa.visualization.Visualization;
import android.content.Context;
import android.util.AttributeSet;

/**
 * {@link Visualization} for displaying several Strings.
 * 
 * @author Andreas Hippler
 *
 */
public abstract class InfoVisusalization extends Visualization<String[]> {

	/**
	 * @param context
	 */
	public InfoVisusalization(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attributeSet
	 */
	public InfoVisusalization(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

}
