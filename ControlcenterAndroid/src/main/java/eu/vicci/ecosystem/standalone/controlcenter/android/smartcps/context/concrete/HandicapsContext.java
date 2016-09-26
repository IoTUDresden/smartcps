package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete;

import java.io.Serializable;

import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.UserContext;

/**
 * The HandicapsContext.
 */
public class HandicapsContext extends UserContext<HandicapsContextGroup> implements Serializable {

	private static final long serialVersionUID = -2751672010169309906L;

	public static final String CLASS_NAME = "eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.HandicapsContext";
	public static final String VERBOSE_NAME = "Einschr�nkung";
	public static final String CONTEXT_GROUP_SIGHT = "Sehverm�gen";
	public static final String CONTEXT_GROUP_MOTOR_SKILLS = "Motorik";
	public static final String CONTEXT_GROUP_MEMORY = "Ged�chtnis";

	/**
	 * Instantiates a new handicaps context.
	 */
	public HandicapsContext() {
		this.setName(VERBOSE_NAME);
		addGroup(new HandicapsContextGroup("Sehverm�gen", CONTEXT_GROUP_SIGHT));
		addGroup(new HandicapsContextGroup("Motorik", CONTEXT_GROUP_MOTOR_SKILLS));
		addGroup(new HandicapsContextGroup("Ged�chtnis", CONTEXT_GROUP_MEMORY));
	}

}
