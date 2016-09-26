package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.device.Device;

/**
 * The ContentModel.
 * Stores the DashboardDevices
 * @param <T> the type of the devices
 */
public class ContentModel<T extends Device<?>> {
	private LinkedHashMap<String,T> dashboardDevices = new LinkedHashMap<String,T>();

	/**
	 * @return Gets all Dashboard Devices which are present
	 */
	public LinkedList<T> getDashboardDevices() {
		return new LinkedList<T>(dashboardDevices.values());
	}
	
	/**
	 * Adds a dashboard device. If the Content Model already contains this device,
	 * the device will not be inserted. So each devices in the content model has no duplicates
	 *<br>
	 * It returns the the the device which is in the model.
	 * 
	 * @param device the device to add
	 * @return true, if device was added
	 */
	public T addDashboardDevice(T device) {
		if (dashboardDevices.containsKey(device.getId())) {
			return dashboardDevices.get(device.getId());
		}
		dashboardDevices.put(device.getId(),device);
		return device;
	}
	
	/**
	 * removes the specific device
	 * @param device
	 * @return true, if the device was removed
	 */
	public boolean removeDashboardDevice(T device){
		return dashboardDevices.remove(device.getId()) != null;
	}
	
	/**
	 * Gets a device, with the id of the device
	 * @param id
	 * @return null, if no device with this id was found
	 */
	public T getDashboardDeviceById(String id){		
		return dashboardDevices.get(id);
	}
	
	
	/**
	 * Remove all devices
	 */
	public void clear() {
		dashboardDevices.clear();
	}
}
