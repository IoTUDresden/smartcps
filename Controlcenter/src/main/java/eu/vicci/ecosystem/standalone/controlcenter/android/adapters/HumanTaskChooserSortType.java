package eu.vicci.ecosystem.standalone.controlcenter.android.adapters;

import java.sql.Date;
import java.util.Comparator;

import eu.vicci.process.model.util.messages.core.IHumanTaskMessage;

/**
 * DashboardSortType Enum - für die Sortierung im Dashboard
 * @author Andreas Hippler
 */
public enum HumanTaskChooserSortType {
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
	SORT_BY_DATE, 
	
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
	 * @return Comparator<IHumanTaskRequest>
	 */
	public Comparator<IHumanTaskMessage> getComperatorBySortType()
	{
		switch (this) {
		case SORT_BY_DATE:
			return new Comparator<IHumanTaskMessage>() {
				@Override
				public int compare(IHumanTaskMessage lhs, IHumanTaskMessage rhs) {
					Date lhsDate = new Date(lhs.getTimeStamp());
					Date rhsDate = new Date(rhs.getTimeStamp());;
					return lhsDate.compareTo(rhsDate);
				}
			};
		case SORT_BY_TYPE:
			return new Comparator<IHumanTaskMessage>() {
				@Override
				public int compare(IHumanTaskMessage lhs, IHumanTaskMessage rhs) {
					return lhs.getHumanTaskType().compareTo(rhs.getHumanTaskType());
				}
			};
		default:
			return new Comparator<IHumanTaskMessage>() {
				@Override
				public int compare(IHumanTaskMessage lhs, IHumanTaskMessage rhs) {
					return lhs.getName().compareToIgnoreCase(rhs.getName());
				}
			};
		}
	}
}