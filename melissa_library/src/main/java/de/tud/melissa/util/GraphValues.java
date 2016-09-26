/**
 * 
 */
package de.tud.melissa.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Containing lists for x and y values. y values are of Double.
 * 
 * @author Andreas
 * @param <T>
 *            the type of the y values
 * 
 */
public class GraphValues<T> implements Iterable<GraphValue<T>>{
	private List<T> x_values;
	private List<Double> y_values;
	private List<GraphValue<T>> values;

	/**
	 * New GraphValues object.
	 */
	public GraphValues() {
		x_values = new ArrayList<T>();
		y_values = new ArrayList<Double>();
		values = new ArrayList<GraphValue<T>>();
	}

	/**
	 * New GraphValues object. The lists are initialized with values
	 * 
	 * @param size
	 *            number of values
	 */
	public GraphValues(int size) {
		x_values = new ArrayList<T>(size);
		y_values = new ArrayList<Double>(size);
		values = new ArrayList<GraphValue<T>>(size);

		@SuppressWarnings("unchecked")
		T x = (T) new Object();
		Double y = 0d;
		GraphValue<T> v = new GraphValue<T>(x, y);

		while (x_values.size() < size) {
			x_values.add(x);
			y_values.add(y);
			values.add(v);
		}
	}

	/**
	 * Replace a value at position of index.
	 * 
	 * @param index
	 * @param x_value
	 * @param y_value
	 */
	public void set(int index, T x_value, Double y_value) {
		x_values.set(index, x_value);
		y_values.set(index, y_value);
		values.set(index, new GraphValue<T>(x_value, y_value));
	}

	/**
	 * Replace a value at position of index.
	 * 
	 * @param index
	 * @param value
	 */
	public void set(int index, GraphValue<T> value) {
		set(index, value.getX(), value.getY());
	}

	/**
	 * Add a new value at the and of the lists.
	 * 
	 * @param x_value
	 * @param y_value
	 */
	public void add(T x_value, Double y_value) {
		x_values.add(x_value);
		y_values.add(y_value);
		values.add(new GraphValue<T>(x_value, y_value));
	}

	/**
	 * Add a new value at the and of the lists.
	 * 
	 * @param value
	 */
	public void add(GraphValue<T> value) {
		add(value.getX(), value.getY());
	}

	/**
	 * @return the x values as list
	 */
	public List<T> getXValues() {
		return x_values;
	}

	/**
	 * @return the y values as list
	 */
	public List<Double> getYValues() {
		return y_values;
	}

	/**
	 * @return the values as list
	 */
	public List<GraphValue<T>> getValues() {
		return values;
	}

	/**
	 * @return the size
	 */
	public int size() {
		return x_values.size();
	}

	@Override
	public Iterator<GraphValue<T>> iterator() {
		return values.iterator();
	}
}
