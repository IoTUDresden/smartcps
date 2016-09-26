package eu.vicci.ecosystem.standalone.controlcenter.android.content.device;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import eu.vicci.driver.robot.util.Position;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientRobot;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.Navigator;
import eu.vicci.ecosystem.standalone.controlcenter.android.visualization.VisualizationType;

/**
 * Robot Device for Dashboard. Encapsulate the ClientRobot an provides
 * functionality for dashboard-informations of this robot.
 * 
 * @author André Kühnert
 * 
 */
public final class Robot extends DashboardDevice {
	private static final long serialVersionUID = -8471383625704663475L;

	private static final List<String> allowedOptions = DeviceOption.getOptionsArray(DeviceOption.CAN_RENAME,
			DeviceOption.CAN_CHANGE_VISUALISATION, DeviceOption.CAN_HIDE);
	private static final String[] optionStrings = allowedOptions.toArray(new String[allowedOptions.size()]);

	private ClientRobot clientRobot;

	/**
	 * Instantiates a new robot device. Can not be null.
	 * {@link eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientRobot}
	 * 
	 * @param clientRobot
	 */
	public Robot(ClientRobot clientRobot) {
		super(clientRobot.getName(), clientRobot.getIp(), null);
		this.clientRobot = clientRobot;
		uid = clientRobot.getIp();
	}

	@Override
	public DeviceType getDeviceType() {
		return DeviceType.Robot;
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
		return VisualizationType.RobotInfo;
	}

	/**
	 * Get´s the Port for the Robot-Connection
	 * 
	 * @return port
	 */
	public String getPort() {
		if (clientRobot != null)
			return clientRobot.getPort();
		return "";
	}

	/**
	 * is the Robot connected
	 * 
	 * @return true, if connected
	 */
	public boolean isConnected() {
		if (hasUnderlyingRobot())
			return clientRobot.getRobot().getIsConnected();
		return false;
	}

	/**
	 * gets the position of the robot
	 * 
	 * @return null - if no underlying robot is found or no position is set
	 */
	public Position getPosition() {
		if (hasUnderlyingRobot())
			return clientRobot.getRobot().getPosition();
		return null;
	}

	/**
	 * Checks, if the robot has a grabber
	 * 
	 * @return true, if the robot has a grabber
	 */
	public boolean hasGrabber() {
		return hasUnderlyingRobot() && clientRobot.getRobot().hasGrabber();
	}

	@Override
	public VisualizationType getDetailVisualizationType() {
		return null;
	}

	private boolean hasUnderlyingRobot() {
		return clientRobot != null && clientRobot.getRobot() != null;
	}

	//serilization execute this, to write the object
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeUTF(getId());
	}
	
	//serilization execute this, to read the object
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		String ip = ois.readUTF();

		SmartCPS_Impl.getNavigator();
		for (ClientRobot cr : Navigator.getRobots()) {
			if (ip.equals(cr.getIp())) {
				clientRobot = cr;
				return;
			}
		}
	}
}
