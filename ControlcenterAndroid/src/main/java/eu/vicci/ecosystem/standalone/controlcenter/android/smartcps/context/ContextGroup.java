package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context;

import java.io.Serializable;

/**
 * The ContextGroup.
 */
public abstract class ContextGroup implements Serializable {
	
	private static final long serialVersionUID = 969746917411320391L;
	private String name;
	
	/**
	 * Instantiates a new context group.
	 *
	 * @param name the name
	 */
	public ContextGroup(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}

}
