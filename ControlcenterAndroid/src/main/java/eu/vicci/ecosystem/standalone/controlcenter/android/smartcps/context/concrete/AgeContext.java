package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete;

import java.io.Serializable;

import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.UserContext;

/**
 * The AgeContext.
 */
public class AgeContext extends UserContext<AgeContextGroup> implements Serializable {

	private static final long serialVersionUID = 5634916907760776744L;
	public static final String CLASS_NAME = "eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.AgeContext";
	public static final String VERBOSE_NAME = "Alter";
	public static final String CONTEXT_GROUP_CHILDREN = "Kinder";
	public static final String CONTEXT_GROUP_TEENAGERS_ADULTS = "Jugendliche & Erwachsene";
	public static final String CONTEXT_GROUP_ELDERLY = "Senioren";

	/**
	 * Instantiates a new age context.
	 */
	public AgeContext() {
		this.setName(VERBOSE_NAME);
		addGroup(new AgeContextGroup(CONTEXT_GROUP_CHILDREN, 10, 13));
		addGroup(new AgeContextGroup(CONTEXT_GROUP_TEENAGERS_ADULTS, 14, 64));
		// max age = infinity ;-)
		addGroup(new AgeContextGroup(CONTEXT_GROUP_ELDERLY, 65, Integer.MAX_VALUE));
	}

}
