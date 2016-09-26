package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * The Persistence class.
 */
public class Persistence {
	
	/**
	 * Deserializes an object from file.
	 *
	 * @param filename the filename
	 * @param directory the directory
	 * @return the object
	 */
	public static Object loadObject(String filename, String directory) {
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(directory, filename)));
			Object object = objectInputStream.readObject();
			objectInputStream.close();
			return object;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
