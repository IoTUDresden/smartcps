package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete;

import java.io.Serializable;

import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.BoundedContextGroup;

/**
 * The AgeContextGroup.
 */
public class AgeContextGroup extends BoundedContextGroup<Integer> implements Serializable {

	private static final long serialVersionUID = 962785242149267674L;

	/**
	 * Instantiates a new age context group.
	 *
	 * @param name the name
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 */
	public AgeContextGroup(String name, Integer lowerBound, Integer upperBound) {
		super(name, lowerBound, upperBound);
	}

}
