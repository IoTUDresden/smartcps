package eu.vicci.ecosystem.standalone.controlcenter.android.semiwa;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardValueObject;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.device.ValueObject;

/**
 * This class manages all semiwa connection nodes and is most importantly
 * responsible to sort in new sensor states into a queue for each node. <br>
 * <br>
 * 
 * Observable for new registered Devices.
 * 
 * @author Sergej Lopatkin, edit Andreas Hippler
 * 
 */
public class SemiwaManager extends Observable {

	private static SemiwaManager instance = null;

	protected SemiwaManager() {
	}

	/**
	 * @return instance of SemiwaManager
	 */
	public static SemiwaManager getInstance() {
		if (instance == null)
			instance = new SemiwaManager();
		return instance;
	}

	private Map<String, DashboardValueObject> queueMap = new HashMap<String, DashboardValueObject>();

	/**
	 * Register a new Device to store values from the server
	 * 
	 * @param uid
	 *            the UID of the Device
	 * @param vo
	 *            the {@link ValueObject} to add new {@link SemiwaSensorData}
	 */
	public void registerValueObject(String uid, DashboardValueObject vo) {
		if (!queueMap.containsKey(uid)) {
			queueMap.put(uid, vo);
//			Log.d("SemiwaManager", "Added UID: " + uid);
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * adds a new SemiwaSensorData to the ValueObject an Device, which is
	 * identified by uID of the dataRecord
	 * 
	 * @param dataRecord
	 *            the dataRecord from the Server
	 */
	public void addSemiwaSensorData(SemiwaSensorData dataRecord) {
		DashboardValueObject vo = queueMap.get(dataRecord.getUid());
		if (vo != null) {
			if (vo.getCurrentValue() == null || !vo.getCurrentValue().equals(dataRecord.getValue())) {
//				Log.d("SemiwaManager", "new value: " + dataRecord.getValue() + " for UID: " + dataRecord.getUid());
				vo.addReceiver(dataRecord);
			}
		}
	}

	/**
	 * delete all map entries and observers
	 */
	public void clear() {
		deleteObservers();
		queueMap.clear();
	}
}
