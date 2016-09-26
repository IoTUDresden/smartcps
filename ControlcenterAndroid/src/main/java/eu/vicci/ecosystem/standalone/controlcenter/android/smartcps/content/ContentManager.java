package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.device.Device;

/**
 * The Singelton-Instance of ContentManager aggregating all content-related
 * information. It also gives access to the dashboard devices.
 * @param <D> the type of the devices
 */
public abstract class ContentManager<D extends Device<?>> {

	private ContentModel<D> contentModel;

	protected ContentManager() {
		this.contentModel = new ContentModel<D>();
	}

	/**
	 * fügt D direkt dem ContantModel hinzu. Devices können nicht doppelt
	 * vorkommen
	 * 
	 * @param device
	 *            das Device
	 * @return true, wenn Device hinzugefügt wurde
	 */
	public D addDashboardDevice(D device) {
		return contentModel.addDashboardDevice(device);
	}

	/**
	 * @return Liefert alle Devices, welche verfügbar sind
	 */
	public List<D> getDashboardDevices() {
		return contentModel.getDashboardDevices();
	}

	/**
	 * @return Liefert alle nicht ausgeblendeten Devices, welche verfügbar sind
	 */
	public List<D> getVisibleDashboardDevices() {
		LinkedList<D> s = contentModel.getDashboardDevices();
		if (s.size() > 0) {
			int index = s.size() - 1;
			for (ListIterator<D> it = s.listIterator(index); it.hasPrevious(); index--) {
				D d = (D) it.previous();
				if (!d.isVisible())
					s.remove(index);
			}
		}
		return s;
	}

	/**
	 * Entfernt ein bestimmtes D
	 * 
	 * @param device
	 *            das Devide
	 * @return true, wenn Device entfernt wurde
	 */
	public boolean removeDashboardDevice(D device) {
		return contentModel.removeDashboardDevice(device);
	}

	/**
	 * Liefert ein D anhand dessen Id
	 * 
	 * @param id
	 *            die UID
	 * @return null, wenn kein entsprechendes Device gefunden wurde
	 */
	public D getDashboardDeviceById(String id) {
		return contentModel.getDashboardDeviceById(id);
	}

	/**
	 * Setzt den ContentManager zurück
	 */
	public void clear() {
		contentModel.clear();
	}
}
