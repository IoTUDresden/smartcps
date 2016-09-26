package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.device;

import java.io.Serializable;
import java.util.Date;

/**
 * Interface for the Receiver of Sensor Data
 * @author André Kühnert
 * @param <T> the type of the value
 *
 */
public interface ValueReceiver<T> extends Serializable {
	
	/**
	 * Received Value
	 * @return the value
	 */
	T getValue();
	
	/**
	 * Date, when the Value was received
	 * @return the date
	 */
	Date getDate();
}
