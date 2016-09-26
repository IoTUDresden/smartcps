package eu.vicci.turtlebot.navigationapp.helperclasses;

import java.util.ArrayList;
import java.util.List;

import eu.vicci.driver.robot.location.NamedLocation;

public class PoiManager {

	private static List<NamedLocation> pois;
	
	
	private PoiManager() {
		
	}
	
	public static void setPois(List<NamedLocation> pois_new) {
		pois = pois_new;
	}
	
	public static List<NamedLocation> getPois() {
		if(pois == null)
			return new ArrayList<NamedLocation>(); //avoid null check every time
		return pois;
	}
}
