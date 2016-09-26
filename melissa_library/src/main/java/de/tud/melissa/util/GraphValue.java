package de.tud.melissa.util;

import de.tud.melissa.visualization.GraphVisualiszation;

/**
 * Contains a x and a y value. The y value is of double. Used to set or add a
 * value of {@link GraphVisualiszation}.
 * 
 * @author Andreas Hippler
 * 
 * @param <T>
 *            the type of the x value
 * 
 */
public class GraphValue<T> {

	private T x;
	private double y;

	/**
	 * @param x
	 *            the x value
	 * @param y
	 *            the y value
	 */
	public GraphValue(T x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x value
	 */
	public T getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x value to set
	 */
	public void setX(T x) {
		this.x = x;
	}

	/**
	 * @return the y value
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y value to set
	 */
	public void setY(double y) {
		this.y = y;
	}
}
