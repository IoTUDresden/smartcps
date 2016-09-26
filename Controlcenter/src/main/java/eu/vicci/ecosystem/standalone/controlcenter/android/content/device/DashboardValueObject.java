/**
 * 
 */
package eu.vicci.ecosystem.standalone.controlcenter.android.content.device;

import java.util.Date;
import java.util.Iterator;

import de.tud.melissa.util.GraphValues;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.device.ValueObject;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.device.ValueReceiver;

/**
 * Double implementation of the ValueObject
 * 
 * @author Andreas Hippler
 * 
 */
public class DashboardValueObject extends ValueObject<Double> {
	private static final long serialVersionUID = -5615453598688676756L;

	/**
	 * @param unitDescription
	 */
	public DashboardValueObject(String unitDescription) {
		super(unitDescription);
	}

	@Override
	protected Double lowestValue() {
		return Double.MIN_VALUE;
	}

	@Override
	protected Double highestValue() {
		return Double.MAX_VALUE;
	}

	@Override
	protected Double neutralValue() {
		return Double.valueOf(0);
	}

	@Override
	protected String getUnitFromDescription(String description) {
		if (description != null) {
			if (description.equals("degC"))
				return "°C";
			if (description.equals("Lux"))
				return "lx";
			if (description.equals("Barometer"))
				return "Pa";
			if (description.equals("Humidity"))
				return "%";
		}
		return "";
	}

	/**
	 * @param count
	 *            number of values
	 * @param start
	 * @return the values
	 */
	public GraphValues<Date> getLastDatesAndValues(int count, int start) {
		if (count < 0 || start < 0)
			throw new IllegalArgumentException();

		if (count > getDeque().size())
			count = getDeque().size();

		GraphValues<Date> gv = new GraphValues<Date>(count);
		Iterator<ValueReceiver<Double>> it = getDeque().descendingIterator();
		int i = 1;
		int j = 0;
		while (it.hasNext() && i <= count) {
			if (j < start) {
				j++;
				continue;
			}
			ValueReceiver<Double> vr = (ValueReceiver<Double>) it.next();
			gv.set(count - i, vr.getDate(), vr.getValue());
			i++;
		}

		return gv;
	}

	/**
	 * @return all values
	 */
	public GraphValues<Date> getAllDatesAndValues() {
		GraphValues<Date> gv = new GraphValues<Date>();

		Iterator<ValueReceiver<Double>> it = getDeque().iterator();
		while (it.hasNext()) {
			ValueReceiver<Double> vr = (ValueReceiver<Double>) it.next();
			gv.add(vr.getDate(), vr.getValue());
		}

		return gv;
	}
}
