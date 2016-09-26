package eu.vicci.ecosystem.standalone.controlcenter.android.semiwa;

import java.util.Observable;

import com.google.gson.Gson;

import android.preference.PreferenceManager;
import android.util.Log;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.DashboardContentManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardDevice;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardValueObject;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.Sensor;

/**
 * @author Andreas Hippler
 * 
 * 
 * 
 */
//TODO semiwa not used anymore
//maybe switch to openhab?
public class SemiwaConnection extends Observable {

	private static final String PREFIX = "ws://";
	private static String URL = null;
	private static SemiwaConnection instance = null;
//	private WampConnection mConnection;
	private long newConnectionTimeout = 1000;
	private boolean willDisconnect = false;
	private String connectionState = "not connected";

	private static final Gson gson = new Gson();

	private SemiwaConnection() throws Exception {
		Log.d("SeMiWa", "Connecting to " + URL);
//		mConnection = new WampConnection();
		connect();
	}

	/**
	 * @return the SemiwaConnetion instance
	 */
	public static SemiwaConnection getInstance() {
		if (instance == null) {
			try {
				instance = new SemiwaConnection();
				Log.e("SemiwaConnection", "new instance()");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	/**
	 * @return if a connection is established
	 */
	public boolean isConnected() {
		return false;
//		return mConnection.isConnected();
	}

	/**
	 * @return the state of the connection as String (eg: connected,
	 *         disconnected, connection failed)
	 */
	public String getConnectionState() {
		//TODO Lokalisation
		return connectionState;
	}

	/**
	 * performs unsubscribe und disconnect
	 */
	public void disconnect() {
		willDisconnect = true;
//		mConnection.unsubscribe();
//		mConnection.disconnect();
	}

	/**
	 * perform a reconnect. <br>
	 * <br>
	 * notifies observers.
	 * 
	 */
	public void connect() {
		connectionState = "connecting...";
		setChangedAndNotifyObservers();
		// Connection to SeMiWaAndroidProxy
		URL = PREFIX
				+ PreferenceManager.getDefaultSharedPreferences(SmartCPS_Impl.getAppContext()).getString("prefSemiwa",
						"141.76.68.192:8098");
//		mConnection.connect(URL, new Wamp.ConnectionHandler() {
//			@Override
//			public void onOpen() {
//				connectionOnOpen();
//				connectionState = "connected";
//				setChangedAndNotifyObservers();
//				newConnectionTimeout = 1000;
//			}
//
//			@Override
//			public void onClose(int code, String reason) {
//				log("connect::onClose", reason);
//				if (!willDisconnect) {
//					connectionState = "connection failed";
//					new Timer().schedule(new TimerTask() {
//						@Override
//						public void run() {
//							connect();
//						}
//					}, newConnectionTimeout *= 2);
//				} else {
//					connectionState = "disconnected";
//				}
//				setChangedAndNotifyObservers();
//				willDisconnect = false;
//			}
//		});
	}

	private void connectionOnOpen() {
		log("connectionOnOpen", "Connected to\n" + URL);

//		mConnection.call("http://semiwa.org/open", String.class, new Wamp.CallHandler() {
//
//			@Override
//			public void onResult(Object result) {
//				openOnResult((String) result);
//			}
//
//			@Override
//			public void onError(String errorId, String errorInfo) {
//				Log.e("SeMiWa", errorInfo);
//			}
//		}, "OPEN");
	}

	// call http://semiwa.org/open
	private void openOnResult(String result) {
//		mConnection.subscribe("http://semiwa.org/register", String.class, new Wamp.EventHandler() {
//			@Override
//			public void onEvent(String topic, Object event) {
//				registerOnEvent((String) event);
//			}
//		});
//
//		mConnection.subscribe("http://semiwa.org/event", String.class, new Wamp.EventHandler() {
//			@Override
//			public void onEvent(String topic, Object event) {
//				eventOnEvent((String) event);
//			}
//		});
	}

	// subscribe http://semiwa.org/event
	private void eventOnEvent(String event) {
		// Log.d("eventOnEvent", event);

		SemiwaSensorData dataRecord = gson.fromJson(event, SemiwaSensorData.class);

		// if no entry exists for the
		// UID create an entry
		SemiwaManager.getInstance().addSemiwaSensorData(dataRecord);
	}

	// subscripe http://semiwa.org/register
	private void registerOnEvent(String event) {
		log("registerOnEvent", event);

		// creating a java object from json via GSON lib
		// creating a map entry for each new UID
		SemiwaSensor dataRecord = gson.fromJson(event, SemiwaSensor.class);

		DashboardValueObject vo = new DashboardValueObject(dataRecord.getDeviceUnit());
		DashboardDevice d = DashboardContentManager.getInstance().addDashboardDevice(
				new Sensor(dataRecord.getDeviceName(), dataRecord.getUId(), vo));
		SemiwaManager.getInstance().registerValueObject(dataRecord.getUId(), d.getValueObject());
	}

	private void log(String method, String message) {
		Log.d("SeMiWa::" + method, message);
	}

	private void setChangedAndNotifyObservers() {
		setChanged();
		notifyObservers();
	}
}
