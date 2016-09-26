package eu.vicci.ecosystem.standalone.controlcenter.android;

import java.util.Iterator;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.MainActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.DashboardContentManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardDevice;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DeviceType;
import eu.vicci.ecosystem.standalone.controlcenter.android.persistence.AndroidPersistence;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.Navigator;
import eu.vicci.ecosystem.standalone.controlcenter.android.semiwa.SemiwaConnection;
import eu.vicci.ecosystem.standalone.controlcenter.android.semiwa.SemiwaManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.ContentModel;
import eu.vicci.process.client.android.ProcessEngineClient;

/**
 * The Application Class SmartCPS_Impl.
 */
public class SmartCPS_Impl extends MultiDexApplication {

	private static Context context;
	private static Navigator navigator;
	private static SemiwaConnection semiwaConnection;
	private static ProcessEngineClient pec;

	/**
	 * Gets the app context (globally accessible).
	 * 
	 * @return the app context
	 */
	public static Context getAppContext() {
		return SmartCPS_Impl.context;
	}

	/**
	 * @return the Navigator for the Robots and POIs
	 */
	public static Navigator getNavigator() {
		return navigator;
	}

	/**
	 * @return the connection to the SeMiWa-Server
	 */
	public static SemiwaConnection getSemiwaConnection() {
		return semiwaConnection;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		SmartCPS_Impl.context = getApplicationContext();
		SmartCPS_Impl.navigator = new Navigator();

		loadDevicesFromFile();

		SmartCPS_Impl.semiwaConnection = SemiwaConnection.getInstance();
		SmartCPS_Impl.pec = ProcessEngineClient.getInstance();
		pec.connect();
	}

	@Override
	public void onTerminate() {
		semiwaConnection.disconnect();
		Log.d("SmartCPS_Impl", "semiwa disconnected");
		pec.disconnect();
		super.onTerminate();
	}

	/**
	 * Loads all devices from Android device and adds them to
	 * {@link DashboardContentManager}
	 */
	@SuppressWarnings("unchecked")
	private void loadDevicesFromFile() {
		List<DashboardDevice> devices = null;
		try {
			devices = (List<DashboardDevice>) AndroidPersistence.loadObject(ContentModel.class.getName()
					+ MainActivity.DB_EXTENSION, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (devices != null) {
			for (Iterator<DashboardDevice> iterator = devices.iterator(); iterator.hasNext();) {
				DashboardDevice d = (DashboardDevice) iterator.next();
				DashboardContentManager.getInstance().addDashboardDevice(d);

				if (d.getDeviceType() == DeviceType.Sensor)
					SemiwaManager.getInstance().registerValueObject(d.getId(), d.getValueObject());
			}
		}
	}
}
