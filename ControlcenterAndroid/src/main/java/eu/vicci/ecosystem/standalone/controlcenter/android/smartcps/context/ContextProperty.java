package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context;

import java.io.Serializable;

/**
 * The ContextProperty.
 */
@SuppressWarnings("rawtypes")
public class ContextProperty implements Serializable {
	
	private static final long serialVersionUID = -7529755543070179301L;
	private Context context;
	private Object value;
	
	/**
	 * Instantiates a new context property.
	 *
	 * @param context the context
	 * @param value the value
	 */
	public ContextProperty(Context context, Object value) {
		this.context = context;
		this.value = value;
	}

	/**
	 * Gets the context.
	 *
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * Sets the context.
	 *
	 * @param context the new context
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "(" + this.context.getName() + ", " + this.value.toString() + ")";
	}

}
