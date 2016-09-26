package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.device;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Observable;

/**
 * Stores the values for a dashboard device
 * 
 * @author André Kühnert
 * @param <T>
 *            the type of the values
 * 
 */
public abstract class ValueObject<T extends Number & Serializable> extends Observable implements Serializable {
	private static final long serialVersionUID = -3259305965235920802L;

	private T minValue = highestValue();
	private T maxValue = lowestValue();

	private T idealValueMin = neutralValue();
	private T idealValueMax = neutralValue();

	private boolean hasIdealValue = false;

	private String unitDescription;
	private String unit;

	private ArrayDeque<ValueReceiver<T>> deque = new ArrayDeque<ValueReceiver<T>>();

	/**
	 * Property which changed for observer
	 * 
	 * @author Andreas Hippler
	 */
	public enum Property {
		/** min or max value changed */
		MIN_MAX,
		/** current value changed */
		VALUE,
		/** ideal values changed */
		IDEAL
	}

	/**
	 * default constructor. Possible Units descriptions from the Semiwa Server
	 * are: - degC - Lux - Barometer - Humidity With this informations, the unit
	 * will be automticaly set. (%)
	 * 
	 * @param unitDescription
	 */
	public ValueObject(String unitDescription) {
		this.unitDescription = unitDescription;
		unit = getUnitFromDescription(unitDescription);
	}

	/**
	 * @return the lowest value of the generic type
	 */
	protected abstract T lowestValue();

	/**
	 * @return the highest value of the generic type
	 */
	protected abstract T highestValue();

	/**
	 * @return a neutral value of the generic type for initialization
	 */
	protected abstract T neutralValue();

	/**
	 * Translate from description to real unit e.g. decC to °C
	 * 
	 * @param description
	 *            the description of the unit
	 * @return the unit
	 */
	protected abstract String getUnitFromDescription(String description);

	/**
	 * Gets the Unit Description
	 * 
	 * @return the unit description
	 */
	public String getUnitDescription() {
		return unitDescription;
	}

	/**
	 * Gets the MinValue.
	 * 
	 * @return the min value
	 */
	public T getMinValue() {
		return minValue;
	}

	/**
	 * Sets the MinValue. This is set automatically, if a new Value is added,
	 * with addReceiver(ValueReceiver). Fires property changed
	 * 
	 * @param minValue
	 */
	public void setMinValue(T minValue) {
		this.minValue = minValue;
		setChanged();
		notifyObservers(Property.MIN_MAX);
	}

	/**
	 * Gets the MaxValue.
	 * 
	 * @return the max value
	 */
	public T getMaxValue() {
		return maxValue;
	}

	/**
	 * Sets the MaxValue. This is set automatically, if a new Value is added,
	 * with addReceiver(ValueReceiver). Fires property changed.
	 * 
	 * @param maxValue
	 */
	public void setMaxValue(T maxValue) {
		this.maxValue = maxValue;
		setChanged();
		notifyObservers(Property.MIN_MAX);
	}

	/**
	 * Gets the current Value
	 * 
	 * @return value
	 */
	public T getCurrentValue() {
		try {
			return deque.getLast().getValue();
		} catch (NoSuchElementException nsee) {
			return null;
		}
	}

	/**
	 * Gets the Unit. E.g. %
	 * 
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Adds a new Value.
	 * 
	 * @param receiver
	 */
	public void addReceiver(ValueReceiver<T> receiver) {
		deque.addLast(receiver);
		T d = receiver.getValue();
		if (d.doubleValue() < minValue.doubleValue()) {
			minValue = d;
		} else if (d.doubleValue() > maxValue.doubleValue()) {
			maxValue = d;
		}

		setChanged();
		notifyObservers(Property.VALUE);
	}

	/**
	 * Gets the Ideal Max Value.
	 * 
	 * @return the end of the ideal value range
	 */
	public T getIdealValueMax() {
		return idealValueMax;
	}

	/**
	 * Gets the Ideal Min Value.
	 * 
	 * @return the start of the ideal value range
	 */
	public T getIdealValueMin() {
		return idealValueMin;
	}

	/**
	 * @return true, if the Ideal Max, the Ideal Min or both values are set
	 */
	public boolean hasIdealValue() {
		return hasIdealValue;
	}

	/**
	 * Sets the idealMax Value
	 * 
	 * @param idealValueMax
	 */
	public void setIdealValueMax(T idealValueMax) {
		hasIdealValue = true;
		this.idealValueMax = idealValueMax;
		setChanged();
		notifyObservers(Property.IDEAL);
	}

	/**
	 * Sets the idealMin Value
	 * 
	 * @param idealValueMin
	 */
	public void setIdealValueMin(T idealValueMin) {
		hasIdealValue = true;
		setChanged();
		notifyObservers(Property.IDEAL);
		this.idealValueMin = idealValueMin;
	}

	/**
	 * Gets the Information, if the Ideal Values, which are stored in the Value
	 * Object are exceeded
	 * 
	 * @return true, if exceeded
	 */
	public boolean exceedsIdealValues() {
		if (hasIdealValue
				&& (getCurrentValue().doubleValue() < idealValueMin.doubleValue() || getCurrentValue().doubleValue() > idealValueMax
						.doubleValue()))
			return true;
		return false;
	}

	/**
	 * Gets the ArrayDeque, which stores all values, which the value object has
	 * received.
	 * 
	 * @return the {@link ArrayDeque} with all values
	 */
	public ArrayDeque<ValueReceiver<T>> getDeque() {
		return deque;
	}
}
