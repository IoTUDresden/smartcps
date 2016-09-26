package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.ContextManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.AgeContext;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.HandicapsContext;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.TechnicalSkillsContext;

//import eu.vicci.ecosystem.standalone.controlcenter.android.persistence.AndroidPersistence;

/**
 * The routing activity (like a routing controller in web development).
 */
public class MainActivity extends Activity {

	public static final String DB_EXTENSION = ".db";
	private static Boolean restartApp = false;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Common.setActivityTheme(this);
		super.onCreate(savedInstanceState);		
		
		restartApp = true;
		restartApp();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		restartApp();
	}

	/**
	 * Restart the whole app by removing activities from the stack
	 */
	private void restartApp() {
		if (restartApp) {
			
			List<String> relevantContextClasses = new ArrayList<String>();
			relevantContextClasses.add(AgeContext.CLASS_NAME);
			relevantContextClasses.add(TechnicalSkillsContext.CLASS_NAME);
			relevantContextClasses.add(HandicapsContext.CLASS_NAME);
			
			/* never saved

			// load possibly saved ContextModel instance including all of the
			// user's context properties
			Object contextModel = AndroidPersistence.loadObject(
					ContextModel.class.getName() + DB_EXTENSION, this
							.getApplicationContext().getFilesDir().toString());
			ContextManager.setContextModel((ContextModel) contextModel);
			*/

			// initialize the framework with the relevant contexts
			ContextManager.initialize(relevantContextClasses);

			Intent mainIntent;
			//if (contextModel != null) {
				// Common.startBreadcrumbsActivity(this, HomeActivity.class);
				mainIntent = new Intent(this, HomeActivity.class);
			//} else {
				// Common.startBreadcrumbsActivity(this,
				// TutWelcomeActivity.class);
			//	mainIntent = new Intent(this, HomeActivity.class);
			//}
			mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mainIntent);
		}
		finish();
		restartApp = false;
	}

	public static Boolean getRestartApp() {
		return restartApp;
	}

	public static void setRestartApp(Boolean restartApp) {
		MainActivity.restartApp = restartApp;
	}
}
