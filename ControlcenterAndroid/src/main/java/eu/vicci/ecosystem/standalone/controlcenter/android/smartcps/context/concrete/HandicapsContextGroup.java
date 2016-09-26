package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete;

import java.io.Serializable;

import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.StateContextGroup;

/**
 * The HandicapsContextGroup.
 */
public class HandicapsContextGroup extends StateContextGroup<String> implements Serializable {

	private static final long serialVersionUID = 8969983758870252592L;

	/**
	 * Instantiates a new handicaps context group.
	 *
	 * @param name the name
	 * @param value the value
	 */
	public HandicapsContextGroup(String name, String value) {
		super(name, value);
	}

}
