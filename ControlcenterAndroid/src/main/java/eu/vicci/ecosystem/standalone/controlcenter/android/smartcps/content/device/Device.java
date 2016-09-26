package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.device;

import eu.vicci.ecosystem.system.app.Widget;
import eu.vicci.ecosystem.system.app.impl.ElementImpl;

import java.io.Serializable;

/**
 * The Device acting as a data source for a shape (in vicci terminology a
 * widget).
 * @param <T> type of the object which hold the values
 */
public abstract class Device<T> extends ElementImpl implements Widget, Serializable {
	private static final long serialVersionUID = 9207123794448445621L;

	private String name;
	protected String uid;
	private Object image = null;
	private String locationName;
	private Boolean isVisible = true;

	protected T valueObject;
	
	/**
	 * Instantiates a new device.
	 * 
	 * @param name
	 *            the name
	 * @param uid
	 *            the uid
	 * @param valueObject
	 *            the data reference object
	 */
	public Device(String name, String uid, T valueObject) {
		this.name = name;
		this.uid = uid;
		this.valueObject = valueObject;
		
		locationName = "Unbekannt";
	}

	/**
	 * gets the name of this device
	 * @return name
	 */
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the unique Id of this device
	 */
	public String getId() {
		return uid;
	}

	/**
	 * Gets the image.
	 * 
	 * @return the image
	 */
	public Object getImage() {
		return image;
	}

	/**
	 * Sets the image.
	 * 
	 * @param image
	 *            the new image
	 */
	public void setImage(Object image) {
		this.image = image;
	}
	
	
	/**
	 * Gets the valueObject
	 * 
	 * @return the valueObject
	 */
	public T getValueObject() {
		return valueObject;
	}
	
	@Override
	public int hashCode() {
		return uid.hashCode();
	}

	
	@Override
	public boolean equals(Object rhs) {
		if (rhs.getClass() == this.getClass())
			return this.getId().equals((((Device<?>) rhs).getId()));
		return false;
	}
	
	/**
	 * Gets the VisualizationType. This visualization is used in the gridview, 
	 * where all devices are displayed.
	 * @return VisualizationType
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * Sets the Location Name.
	 * Fires property changed.
	 * @param locationName
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
	/**
	 * Gets the visibility of the Device. If its set to true, 
	 * the device will be visible in the dashboard grid view
	 * @return true if visible
	 */
	public boolean isVisible() {
		return isVisible;
	}
	/**
	 * Sets the visibility of the Device. If its set to true, 
	 * the device will be visible in the dashboard grid view
	 * @param b - true -> visible
	 */
	public void setVisible(boolean b) {
		isVisible = b;
	}
	
	@Override
	public void update() {		
	}
}
