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
				"process1234_instance_1", 1, State.ACTIVE , "process1234", "Waschmaschine im Keller", "CleaningType", "Die Wäsche wird gewaschen");

		ProcessInstanceInfo proc2 = new ProcessInstanceInfo(
		"process4321_instance_1", 1, State.EXECUTING , "process4321", "Kaffeemaschine", "KitchenType", "Es wird Kaffee gekocht.");

		DashboardContentManager.getInstance().addDashboardDevice(new Process(proc1));
		DashboardContentManager.getInstance().addDashboardDevice(new Process(proc2));
	}

}
