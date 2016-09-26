package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete;

import java.io.Serializable;

import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.UserContext;

/**
 * The HandicapsContext.
 */
public class HandicapsContext extends UserContext<HandicapsContextGroup> implements Serializable {

	private static final long serialVersionUID = -2751672010169309906L;

	public static final String CLASS_NAME = "eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.HandicapsContext";
	public static final String VERBOSE_NAME = "Einschränkung";
	public static final String CONTEXT_GROUP_SIGHT = "Sehvermögen";
	public static final String CONTEXT_GROUP_MOTOR_SKILLS = "Motorik";
	public static final String CONTEXT_GROUP_MEMORY = "Gedächtnis";

	/**
	 * Instantiates a new handicaps context.
	 */
	public HandicapsContext() {
		this.setName(VERBOSE_NAME);
		addGroup(new HandicapsContextGroup("Sehvermögen", CONTEXT_GROUP_SIGHT));
		addGroup(new HandicapsContextGroup("Motorik", CONTEXT_GROUP_MOTOR_SKILLS));
		addGroup(new HandicapsContextGroup("Gedächtnis", CONTEXT_GROUP_MEMORY));
	}

}
