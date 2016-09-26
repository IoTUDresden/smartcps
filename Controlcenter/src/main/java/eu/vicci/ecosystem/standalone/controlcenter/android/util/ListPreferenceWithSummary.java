package eu.vicci.ecosystem.standalone.controlcenter.android.util;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

/**
 * Extending the default implementation of {@link ListPreference} cause the summary 
 * of the default ListPreference will not update, if you change a value in the list.
 * <br>
 * An good example how this will work is 'usersettings_active_user' in '_settings_usersettings.xml'.
 * <br><br>
 * <code>android:summary="%s"</code>  
 * <br> <br>
 * will update the summary to the current selected entry
 * 
 * @see 
 * <a href=http://stackoverflow.com/a/16661022>
 * Change the summary of a ListPreference with the new value (Android)</a>
 * 
 */
public class ListPreferenceWithSummary extends ListPreference {

	public ListPreferenceWithSummary(Context context) {
		super(context);
	}
	
	public ListPreferenceWithSummary(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setValue(String value) {
	    super.setValue(value);
	    setSummary(value);
	}

	@Override
	public void setSummary(CharSequence summary) {
	    super.setSummary(getEntry());
	}
}
