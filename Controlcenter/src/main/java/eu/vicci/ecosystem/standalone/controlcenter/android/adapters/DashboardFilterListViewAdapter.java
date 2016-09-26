/**
 * 
 */
package eu.vicci.ecosystem.standalone.controlcenter.android.adapters;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.DashboardContentManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardDevice;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.AbstractFilterDialogListAdapter;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.DashBoardFilterManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.DashBoardFilterManager.FilterType;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.FilterElement;

/**
 * ListViewAdapter für den FilterDialog im Dashboard
 * 
 * @author Andreas Hippler
 */
public class DashboardFilterListViewAdapter extends AbstractFilterDialogListAdapter {

	/**
	 * @param context
	 */
	public DashboardFilterListViewAdapter(Context context) {
		this(context, null);
	}
	
	public DashboardFilterListViewAdapter(Context context, SparseArray<List<String>> lastSelectedFilter){
		super(context, DashBoardFilterManager.FilterType.getHeaderNames(), lastSelectedFilter);
		HashSet<FilterElement> set = new HashSet<FilterElement>();

		for (DashboardDevice device : DashboardContentManager.getInstance().getVisibleDashboardDevices()) {
			set.add(new FilterElement(device.getLocationName(), FilterType.ROOM.ordinal(), isFilterEntrySelected(
					device, FilterType.ROOM)));
			set.add(new FilterElement(device.getDeviceType().toString(), FilterType.TYPE.ordinal(),
					isFilterEntrySelected(device, FilterType.TYPE)));
		}

		set.add(new FilterElement(context.getString(R.string.dashboard_filter_exceededBoundaryValues),
				FilterType.BOUNDARYVALUES.ordinal(), isFilterEntrySelected(null, FilterType.BOUNDARYVALUES)));

		this.list.addAll(set);
		Collections.sort(this.list);
		
	}

	// bei vorher aktiven Filter, werden die aktuell gesetzten Werte
	// vorausgewählt
	private boolean isFilterEntrySelected(DashboardDevice device, FilterType type) {
		switch (type) {
		case ROOM:
			return device != null
					&& DashBoardFilterManager.getLastSelectedFilter() != null
					&& DashBoardFilterManager.getLastSelectedFilter().get(FilterType.ROOM.ordinal())
							.contains(device.getLocationName());
		case TYPE:
			return device != null
					&& DashBoardFilterManager.getLastSelectedFilter() != null
					&& DashBoardFilterManager.getLastSelectedFilter().get(FilterType.TYPE.ordinal())
							.contains(device.getDeviceType().toString());
		case BOUNDARYVALUES:
			return DashBoardFilterManager.getLastSelectedFilter() != null
					&& !DashBoardFilterManager.getLastSelectedFilter().get(FilterType.BOUNDARYVALUES.ordinal())
							.isEmpty();
		default:
			return false;
		}
	}

	/**
	 * Liefert gewählte Filterelemente
	 * 
	 * @return Filterwerte
	 */
	public SparseArray<List<String>> getChooseFilter() {
		SparseArray<List<String>> map = new SparseArray<List<String>>(FilterType.values().length);
		for (FilterType filterType : FilterType.values())
			map.put(filterType.ordinal(), new LinkedList<String>());

		for (FilterElement element : list) {
			if (element.isSelected())
				map.get(element.getHeaderID()).add(element.getName());
		}
		return map;
	}
}
