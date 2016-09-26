package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete;

import java.io.Serializable;

import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.StateContextGroup;

/**
 * The TechnicalSkillsContextGroup.
 */
public class TechnicalSkillsContextGroup extends StateContextGroup<String> implements Serializable {

	private static final long serialVersionUID = -3204073235588950962L;

	/**
	 * Instantiates a new technical skills context group.
	 *
	 * @param name the name
	 * @param value the value
	 */
	public TechnicalSkillsContextGroup(String name, String value) {
		super(name, value);
	}

}
