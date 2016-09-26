package eu.vicci.ecosystem.standalone.controlcenter.android.persistence;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;

/**
 * The Android Persistence.
 */
public class AndroidPersistence {

	/**
	 * Persist the object on the android device.
	 * 
	 * @param object
	 *            the object
	 * @param filename
	 *            the filename
	 * @param activity
	 *            the activity
	 */
	public static void persistObject(Object object, String filename,
			Activity activity) {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					activity.getApplicationContext().openFileOutput(filename,
							android.content.Context.MODE_PRIVATE));
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
			objectOutputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Loads an object which was persist from the Android device.
	 * 
	 * @param filename
	 * @param context
	 * @return the loaded object
	 */
	public static Object loadObject(String filename, Context context) {
		Object object = null;
		try {
			FileInputStream fis = context.openFileInput(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			object = ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return object;
	}

}
