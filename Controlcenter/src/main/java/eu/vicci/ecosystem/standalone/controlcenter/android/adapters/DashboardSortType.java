package eu.vicci.ecosystem.standalone.controlcenter.android.adapters;

import java.util.Comparator;

import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardDevice;

/**
 * DashboardSortType Enum - für die Sortierung im Dashboard
 * @author Andreas Hippler
 */
public enum DashboardSortType {
	/**
	 * Sorts devices by name
	 */
	SORT_BY_NAME, 
	
	/**
	 * Sorts Devices by the type (e.g. Robot)
	 */
	SORT_BY_TYPE, 
	
	/**
	 * Sorts devices by the room
	 */
	SORT_BY_ROOM, 
	
	/**
	 * Not sorted
	 */
	NOT_SORTED;
	
	/**
	 * Gibt an ob sortiert ist
	 * @return true, wenn sortiert
	 */
	public boolean isSorted() {
		return !this.equals(NOT_SORTED);
	}
	
	/**
	 * Liefert einen Comparator für den gewählten Sortierungs Typ
	 * @return Comparator<DashboardDevice>
	 */
	public Comparator<DashboardDevice> getComperatorBySortType()
	{
		switch (this) {
		case SORT_BY_ROOM:
			return new Comparator<DashboardDevice>() {
				@Override
				public int compare(DashboardDevice lhs, DashboardDevice rhs) {
					String lhsString = lhs.getLocationName();
					String rhsString = rhs.getLocationName();
					return lhsString.compareToIgnoreCase(rhsString);
				}
			};
		case SORT_BY_TYPE:
			return new Comparator<DashboardDevice>() {
				@Override
				public int compare(DashboardDevice lhs, DashboardDevice rhs) {
					return lhs.getDeviceType().compareTo(rhs.getDeviceType());
				}
			};
		default:
			return new Comparator<DashboardDevice>() {
				@Override
				public int compare(DashboardDevice lhs, DashboardDevice rhs) {
					return lhs.getName().compareToIgnoreCase(rhs.getName());
				}
			};
		}
	}
}