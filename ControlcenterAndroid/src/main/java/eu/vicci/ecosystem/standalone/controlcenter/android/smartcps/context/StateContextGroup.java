package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context;

import java.io.Serializable;

/**
 * The Class StateContextGroup.
 * 
 * @param <E>
 *            the element type
 */
public abstract class StateContextGroup<E> extends ContextGroup implements Serializable {

	private static final long serialVersionUID = -1253370132183054746L;
	private E value = null;

	/**
	 * Instantiates a new state context group.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public StateContextGroup(String name, E value) {
		super(name);
		this.value = value;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public E getValue() {
		return this.value;
	}

}
