package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * The Class Common for helper methods.
 */
public class Common {
	
	/**
	 * Gets the current date time.
	 *
	 * @param pattern the pattern
	 * @return the date time
	 */
	public static String getDateTime(String pattern) {
		return new SimpleDateFormat(pattern, Locale.GERMANY).format(Calendar.getInstance().getTime());
	}

}
