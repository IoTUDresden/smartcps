package eu.vicci.ecosystem.standalone.controlcenter.android.semiwa;

import java.util.Date;

import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.device.ValueReceiver;

/**
 * ValueReciver for a Double value. Used to parse the Server event when a new
 * value is sent. Interprets the payload as a double and stores the current
 * time.
 * 
 * @author André Kühnert
 * 
 */
public class SemiwaSensorData implements ValueReceiver<Double> {
	private static final long serialVersionUID = -4082970975543162150L;
	private String uid;
	private String payload;
	private Double value;

	private Date date = new Date();

	/**
	 * default constructor, payload or value has to be set later
	 */
	public SemiwaSensorData() {
	}

	/**
	 * Constructor with payload, wich will be the value as double
	 * 
	 * @param payload
	 */
	public SemiwaSensorData(String payload) {
		this.payload = payload;
	}

	/**
	 * @return the uID
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * sets the uID
	 * 
	 * @param uid
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * sets the payload
	 * 
	 * @param payload
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public Double getValue() {
		if (value == null)
			try {
				value = Double.parseDouble(payload);
			} catch (NumberFormatException ex) {
				value = Double.valueOf(0);
			}
		return value;
	}

	@Override
	public Date getDate() {
		return date;
	}
}
