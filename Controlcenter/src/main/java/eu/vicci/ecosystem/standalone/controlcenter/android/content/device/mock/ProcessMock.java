package eu.vicci.ecosystem.standalone.controlcenter.android.content.device.mock;

import eu.vicci.ecosystem.standalone.controlcenter.android.content.DashboardContentManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.Process;
import eu.vicci.process.model.sofiainstance.State;
import eu.vicci.process.model.util.ProcessInstanceInfo;

/**
 * Definition of some Mock Data
 * @author André Kühnert
 *
 */
public class ProcessMock {
	
	/**
	 * Add Mocks to the ContentManager.
	 */
	public static void addProcessMocks(){
		ProcessInstanceInfo proc1 = new ProcessInstanceInfo(
				"inst01", "id01", "Waschmaschine", "CleaningType", "Waschmaschine im Keller", 
				State.ACTIVE);
		ProcessInstanceInfo proc2 = new ProcessInstanceInfo(
				"inst02", "id02", "Kaffeemaschine", "KitchenType", "Kaffeautomat in Küche", 
				State.EXECUTING);
		DashboardContentManager.getInstance().addDashboardDevice(new Process(proc1));
		DashboardContentManager.getInstance().addDashboardDevice(new Process(proc2));
	}

}
