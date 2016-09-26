package eu.vicci.ecosystem.standalone.controlcenter.android.semiwa;

/**
 * Stores the information of a Sensor-Device when a device is registered or the
 * connection is established
 * 
 * @author Andreas Hippler
 * 
 * 
 */
public class SemiwaSensor {
	private String ip;
	private String uID;
	private String arg;
	private String deviceUnit;
	private String deviceName;
	private String deviceType;
	private String payload;

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * sets the id
	 * 
	 * @param ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the uID
	 */
	public String getUId() {
		return "seMiWa:uid/" + uID;
	}

	/**
	 * sets the uID
	 * 
	 * @param uID
	 */
	public void setUId(String uID) {
		this.uID = uID;
	}

	/**
	 * @return the arg
	 */
	public String getArg() {
		return arg;
	}

	/**
	 * 
	 * sets the arg
	 * 
	 * @param arg
	 */
	public void setArg(String arg) {
		this.arg = arg;
	}

	/**
	 * @return the unit
	 */
	public String getDeviceUnit() {
		return deviceUnit;
	}

	/**
	 * sets the unit
	 * 
	 * @param deviceUnit
	 */
	public void setDeviceUnit(String deviceUnit) {
		this.deviceUnit = deviceUnit;
	}

	/**
	 * @return the name
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * sets the name
	 * 
	 * @param deviceName
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * @return the type
	 */
	public String getDeviceType() {
		return deviceType;
	}

	/**
	 * sets the type
	 * 
	 * @param deviceType
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * @return the payload
	 */
	public String getPayload() {
		return payload;
	}

	/**
	 * sets the payload
	 * 
	 * @param payload
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}
}
