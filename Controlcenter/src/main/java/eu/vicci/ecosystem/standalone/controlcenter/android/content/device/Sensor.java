package eu.vicci.ecosystem.standalone.controlcenter.android.content.device;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import eu.vicci.driver.robot.location.NamedLocation;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientMap;
import eu.vicci.ecosystem.standalone.controlcenter.android.visualization.VisualizationType;

/**
 * sensor device for displaying informations from the 
 * {@link eu.vicci.ecosystem.standalone.controlcenter.android.semiwa.SemiwaConnection}
 * @author André Kühnert
 *
 */
public class Sensor extends DashboardDevice {
	private static final long serialVersionUID = 3374133235369683038L;

	private static final List<String> allowedOptions = DeviceOption.getOptionsArray(DeviceOption.CAN_CHANGE_MIN_MAX,
			DeviceOption.CAN_CHANGE_ROOM, DeviceOption.CAN_HIDE, DeviceOption.CAN_CHANGE_VISUALISATION,
			DeviceOption.CAN_RENAME);
	private static final String[] optionStrings = allowedOptions.toArray(new String[allowedOptions.size()]);
	private ClientMap clientMap;
	private NamedLocation poi;

	/**
	 * default constructor
	 * @param name - name of the sensor
	 * @param uId - the uId of the sensor
	 * @param valueObject - stores the values for this sensor
	 */
	public Sensor(String name, String uId, DashboardValueObject valueObject) {
		super(name, uId, valueObject);
		clientMap = null;
	}

	@Override
	public DeviceType getDeviceType() {
		return DeviceType.Sensor;
	}

	@Override
	public boolean hasDeviceOption(String option) {
		return allowedOptions.contains(option);
	}

	@Override
	public String[] getAllowedOptions() {
		return optionStrings;
	}

	@Override
	public VisualizationType getDefaultVisualizationType() {
		return getValueObject().getUnitDescription().equals("Humidity") ? VisualizationType.PercentageGauge
				: VisualizationType.Numerical;
	}

	@Override
	public VisualizationType getDetailVisualizationType() {
		return VisualizationType.TimeLineChart;
	}

	/**
	 * Gets the Client Map for this Sensor
	 * See {@link eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientMap} 
	 * @return clientMap - null if no Map is set
	 */
	public ClientMap getClientMap() {
		return clientMap;
	}

	/**
	 * Sets the clientMap
	 * See {@link eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientMap} 
	 * @param clientMap
	 */
	public void setClientMap(ClientMap clientMap) {
		this.clientMap = clientMap;
	}

	/**
	 * Sets a Poi, where the sensor is located
	 * See {@link eu.vicci.driver.robot.location.NamedLocation}
	 * @param location
	 */
	public void setPoi(NamedLocation location) {
		this.poi = location;
	}

	/**
	 * Gets the Poi, where the sensor is located
	 * See {@link eu.vicci.driver.robot.location.NamedLocation}
	 * @return namedLocation - null if no poi is set
	 */
	public NamedLocation getPoi() {
		return poi;
	}

	//serilization execute this, to write the object
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeUTF(getName());
		out.writeObject(getValueObject());
		out.writeUTF(getLocationName());
		out.writeObject(getVisualizationType());
		out.writeUTF(uid);
		out.writeBoolean(isVisible());
	}

	//serilization execute this, to read the object
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		setName(ois.readUTF());
		valueObject = (DashboardValueObject) ois.readObject();
		setLocationName(ois.readUTF());
		setVisualizationType((VisualizationType) ois.readObject());
		uid = ois.readUTF();
		setVisible(ois.readBoolean());
	}
}
