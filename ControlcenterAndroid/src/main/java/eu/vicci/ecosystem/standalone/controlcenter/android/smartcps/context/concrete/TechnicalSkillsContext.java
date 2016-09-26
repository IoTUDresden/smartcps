package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete;

import java.io.Serializable;

import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.UserContext;

/**
 * The TechnicalSkillsContext.
 */
public class TechnicalSkillsContext extends UserContext<TechnicalSkillsContextGroup> implements Serializable {

	private static final long serialVersionUID = -3899024822193269227L;

	public static final String CLASS_NAME = "eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.TechnicalSkillsContext";
	public static final String VERBOSE_NAME = "Technische Versiertheit";
	public static final String CONTEXT_GROUP_LESS_TECHNICALLY_SKILLED = "Eher wenig technisch versiert";
	public static final String CONTEXT_GROUP_TECHNICALLY_SKILLED = "Eher technisch versiert";

	/**
	 * Instantiates a new technical skills context.
	 */
	public TechnicalSkillsContext() {
		this.setName(VERBOSE_NAME);
		addGroup(new TechnicalSkillsContextGroup("Eher wenig technisch versiert", CONTEXT_GROUP_LESS_TECHNICALLY_SKILLED));
		addGroup(new TechnicalSkillsContextGroup("Eher technisch versiert", CONTEXT_GROUP_TECHNICALLY_SKILLED));
	}

}
