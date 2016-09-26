package eu.vicci.ecosystem.standalone.controlcenter.android.content.device;

import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.device.Device;
import eu.vicci.ecosystem.standalone.controlcenter.android.visualization.VisualizationType;

/**
 * DashboardDevice - representiert alle Geräte für das Dashboard
 * 
 * @author André Kühnert
 * 
 */
public abstract class DashboardDevice extends Device<DashboardValueObject> {
	private static final long serialVersionUID = 3151704043856793514L;

	private VisualizationType visualizationType;

	/**
	 * Standard Konstruktor
	 * 
	 * @param name
	 * @param uId
	 * @param valueObject
	 *            - ValueObject, welches die Werte hält
	 */
	public DashboardDevice(String name, String uId, DashboardValueObject valueObject) {
		super(name, uId, valueObject);
		if (valueObject == null)
			this.valueObject = new DashboardValueObject(""); // avoid Null
																// Checks

		this.visualizationType = getDefaultVisualizationType();
	}

	/**
	 * Gets the current {@link VisualizationType}
	 * 
	 * @return the current {@link VisualizationType}
	 */
	public VisualizationType getVisualizationType() {
		return visualizationType;
	}

	/**
	 * Sets the VisualizationType. This visualization is used in the GridView,
	 * where all devices are displayed.
	 * 
	 * @param visualizationType 
	 * 
	 */
	public void setVisualizationType(VisualizationType visualizationType) {
		this.visualizationType = visualizationType;
	}

	/**
	 * Gets the DeviceType from the Object.
	 * 
	 * @return DeviceType
	 */
	public abstract DeviceType getDeviceType();

	/**
	 * Checks, if this device has the specific option
	 * 
	 * @param option
	 * @return true, if option is accessible
	 */
	public abstract boolean hasDeviceOption(String option);

	/**
	 * @return Array with the accessible options
	 */
	public abstract String[] getAllowedOptions();

	/**
	 * Gets the default visualization type
	 * 
	 * @return VisualizationType
	 */
	public abstract VisualizationType getDefaultVisualizationType();

	/**
	 * @return visualization for the detail view
	 */
	public abstract VisualizationType getDetailVisualizationType();

	/**
	 * Gibt an, ob Grenzwerte überschritten sind
	 * 
	 * @return true, wenn überschritten
	 */
	public boolean exceedsIdealValues() {
		return valueObject.exceedsIdealValues();
	}
}
