package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context;

import java.io.Serializable;

/**
 * The BoundedContextGroup.
 *
 * @param <E> the element type
 */
public abstract class BoundedContextGroup<E> extends ContextGroup implements Serializable {
	
	private static final long serialVersionUID = 3053988567478157425L;
	// bounds are inclusive! hence, group intervals need to be disjunct!
	private E lowerBound = null;
	private E upperBound = null;
	
	/**
	 * Instantiates a new bounded context group.
	 *
	 * @param name the name
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 */
	public BoundedContextGroup(String name, E lowerBound, E upperBound) {
		super(name);
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	/**
	 * Gets the lower bound.
	 *
	 * @return the lower bound
	 */
	public E getLowerBound() {
		return lowerBound;
	}

	/**
	 * Sets the lower bound.
	 *
	 * @param lowerBound the new lower bound
	 */
	public void setLowerBound(E lowerBound) {
		this.lowerBound = lowerBound;
	}

	/**
	 * Gets the upper bound.
	 *
	 * @return the upper bound
	 */
	public E getUpperBound() {
		return upperBound;
	}

	/**
	 * Sets the upper bound.
	 *
	 * @param upperBound the new upper bound
	 */
	public void setUpperBound(E upperBound) {
		this.upperBound = upperBound;
	}

}
