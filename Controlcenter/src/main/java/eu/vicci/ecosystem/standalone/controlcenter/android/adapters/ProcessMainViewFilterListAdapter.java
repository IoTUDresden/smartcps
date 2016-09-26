package eu.vicci.ecosystem.standalone.controlcenter.android.adapters;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.util.SparseArray;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.ProcessMainActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.AbstractFilterDialogListAdapter;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.FilterElement;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.ProcessMainViewFilterManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.ProcessMainViewFilterManager.FilterType;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.ProcessMainViewFilterManager.GroupTypes;
import eu.vicci.process.model.sofiainstance.State;

/**
 * Adapter for the filter dialog in the {@link ProcessMainActivity}
 *
 */
public class ProcessMainViewFilterListAdapter extends AbstractFilterDialogListAdapter {
	
	public ProcessMainViewFilterListAdapter(Context context, SparseArray<List<String>> lastSelected){
		super(context, ProcessMainViewFilterManager.FilterType.getHeaderNames(), lastSelected);
		addGroupedStates();
		addStates();
	}
	
	public ProcessMainViewFilterListAdapter(Context context){
		this(context, null);
	}

	@Override
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
	
	//let choose between finished (e.g. failed, executed) and unfinished (e.g. waiting, paused) states
	private void addGroupedStates() {
		list.add(new FilterElement(GroupTypes.Finished.name().toLowerCase(Locale.ENGLISH), 
				FilterType.GroupedState.ordinal(), 
				isFilterSelected(FilterType.GroupedState, GroupTypes.Finished)));
		list.add(new FilterElement(GroupTypes.Unfinished.name().toLowerCase(Locale.ENGLISH), 
				FilterType.GroupedState.ordinal(), 
				isFilterSelected(FilterType.GroupedState, GroupTypes.Unfinished)));	
	}

	private void addStates(){
		for (int i = 0; i < State.values().length; i++) {
			State state = State.values()[i];
			list.add(new FilterElement(
					state.getName().toLowerCase(Locale.ENGLISH), 
					FilterType.State.ordinal(), 
					isFilterSelected(FilterType.State, state)));
		}
	}
	
	private boolean isFilterSelected(FilterType type, GroupTypes group){
		if(lastSelectedFilter == null) return false;
		String name = group.name().toLowerCase(Locale.ENGLISH);
		
		for(String val : lastSelectedFilter.get(type.ordinal()))
			if(name.equals(val))return true;
				
		return false;
	}
	
	private boolean isFilterSelected(FilterType type, State state){
		if(lastSelectedFilter == null) return false;
		String name = state.name().toLowerCase(Locale.ENGLISH);
		
		for(String val : lastSelectedFilter.get(type.ordinal()))
			if(name.equals(val))return true;
				
		return false;
	}
}
