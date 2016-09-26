package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import eu.vicci.driver.robot.location.NamedLocation;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientMap;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientRobot;

public interface FragmentCallback {
	public void addRobot(ClientRobot clientRobot);
	public void editRobot(int index, ClientRobot clientRobot);
	
	public void addPoi(NamedLocation poi);
	public void editPoi(int index, NamedLocation poi);
	
	public void addMap(ClientMap clientMap);
	public void editMap(int index, ClientMap clientMap);
}
