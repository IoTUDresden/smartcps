package eu.vicci.ecosystem.standalone.controlcenter.android.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.widget.Toast;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.DashboardContentManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardDevice;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DeviceType;

/**
 * Filter for dashboard
 * 
 * @author André Kühnert
 */
public class DashBoardFilterManager {
	private static Context context;
	private static SparseArray<List<String>> lastSelectedFilter;

	/**
	 * Filters Dashboarddevices
	 * 
	 * @param selectedFilter
	 *            - criteria
	 * @param context
	 *            - ActivityContext
	 * @return filtered list
	 */
	public static ArrayList<DashboardDevice> filterBy(SparseArray<List<String>> selectedFilter, Context context) {
		DashBoardFilterManager.context = context;
		DashBoardFilterManager.lastSelectedFilter = selectedFilter;
		ArrayList<DashboardDevice> tempList = new ArrayList<DashboardDevice>(DashboardContentManager.getInstance()
				.getVisibleDashboardDevices());

		if (selectedFilter != null) {
			for (DashboardDevice device : new ArrayList<DashboardDevice>(tempList))
				for (FilterType filterType : FilterType.values())
					filter(filterType, selectedFilter.get(filterType.ordinal()), tempList, device);
		}

		return tempList;
	}

	/**
	 * Gets the last selected filter values
	 * 
	 * @return Filter - NULL, if no filter was set
	 */
	public static SparseArray<List<String>> getLastSelectedFilter() {
		return lastSelectedFilter;
	}

	private static void filter(FilterType filterType, List<String> values, List<DashboardDevice> tempList,
			DashboardDevice device) {
		switch (filterType) {
		case ROOM:
			filterRoom(values, tempList, device);
			break;
		case TYPE:
			filterType(values, tempList, device);
			break;
		case BOUNDARYVALUES:
			filterBoundaryValue(values, tempList, device);
			break;
		default:
			Toast.makeText(context, "FilterGroup not found", Toast.LENGTH_LONG).show();
			break;
		}
	}

	private static void filterRoom(List<String> values, List<DashboardDevice> tempList, DashboardDevice device) {
		if (values.isEmpty())
			return;
		boolean hasAnyCondition = false;
		for (String value : values)
			if (!hasAnyCondition && hasRoomCondition(device, value) && tempList.contains(device))
				hasAnyCondition = true;

		if (!hasAnyCondition)
			tempList.remove(device);
	}

	private static void filterType(List<String> values, List<DashboardDevice> tempList, DashboardDevice device) {
		if (values.isEmpty())
			return;
		boolean hasAnyCondition = false;
		for (String value : values)
			if (!hasAnyCondition && hasTypeCondition(device, value) && tempList.contains(device))
				hasAnyCondition = true;

		if (!hasAnyCondition)
			tempList.remove(device);
	}

	private static void filterBoundaryValue(List<String> values, List<DashboardDevice> tempList, DashboardDevice device) {
		if (values.isEmpty())
			return;
		if (!device.exceedsIdealValues())
			tempList.remove(device);
	}

	// Bedingung für RaumFilter
	private static boolean hasRoomCondition(DashboardDevice device, String criteria) {
		return device.getLocationName().equals(criteria);
	}

	// Bedingung für Typen-Filter
	private static boolean hasTypeCondition(DashboardDevice device, String criteria) {
		return device.getDeviceType().equals(DeviceType.valueOf(criteria));
	}

	/**
	 * filter type
	 * 
	 * @author André Kühnert
	 */
	public enum FilterType {
		/**
		 * The rooms
		 */
		ROOM,
		/**
		 * the device types
		 */
		TYPE,
		/**
		 * ideal values
		 */
		BOUNDARYVALUES;

		// muss die selbe Reihenfolge wie dieses Enum haben
		private static String[] headerNames = new String[] { "Raum", "Typ", "Grenzwerte" };

		/**
		 * Gets a String[] with the header names. order must be the same as in
		 * this enum
		 * 
		 * @return the names of the headers for displaying in the filter dialog
		 */
		public static String[] getHeaderNames() {
			return headerNames;
		}

	}
}
