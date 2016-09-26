/**
 * 
 */
package eu.vicci.ecosystem.standalone.controlcenter.android.content;

import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardDevice;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.content.ContentManager;

/**
 * @author andreas1
 *
 */
public class DashboardContentManager extends ContentManager<DashboardDevice> {
	private static DashboardContentManager instance = null;
	
	private DashboardContentManager() {
		super();
	}

	public static DashboardContentManager getInstance() {
		if (instance==null)
			instance = new DashboardContentManager();
		return instance;
	}
}
