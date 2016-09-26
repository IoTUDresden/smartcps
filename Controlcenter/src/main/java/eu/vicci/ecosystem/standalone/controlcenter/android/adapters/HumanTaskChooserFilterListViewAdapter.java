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
import eu.vicci.ecosystem.standalone.controlcenter.android.humantask.HumanTaskDataManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.AbstractFilterDialogListAdapter;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.FilterElement;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.HumanTaskChooserFilterManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.HumanTaskChooserFilterManager.HTFilterType;
import eu.vicci.process.model.util.messages.core.IHumanTaskRequest;

/**
 * ListViewAdapter für den FilterDialog im HumanTask
 * 
 *
 */
public class HumanTaskChooserFilterListViewAdapter extends AbstractFilterDialogListAdapter {

	/**
	 * @param context
	 * 
	 */
	public HumanTaskChooserFilterListViewAdapter(Context context) {
		this(context, null);
	}
	
	public HumanTaskChooserFilterListViewAdapter(Context context, SparseArray<List<String>> lastSelectedFilter){
		super(context, HumanTaskChooserFilterManager.HTFilterType.getHeaderNames(), lastSelectedFilter);
		HashSet<FilterElement> set = new HashSet<FilterElement>();

		for (IHumanTaskRequest request : HumanTaskDataManager.getInstance().getRequestsList()){
			set.add(new FilterElement(request.getHumanTaskUseCase().toString(), HTFilterType.USECASE.ordinal(), isFilterEntrySelected(
					request, HTFilterType.USECASE)));
			set.add(new FilterElement(request.getHumanTaskType().toString(), HTFilterType.TYPE.ordinal(),
					isFilterEntrySelected(request, HTFilterType.TYPE)));
		}

		this.list.addAll(set);
		Collections.sort(this.list);		
	}

	// bei vorher aktiven Filter, werden die aktuell gesetzten Werte
	// vorausgewählt
	private boolean isFilterEntrySelected(IHumanTaskRequest request, HTFilterType type) {
		switch (type) {
		case USECASE:
			return request != null
					&& HumanTaskChooserFilterManager.getLastSelectedFilter() != null
					&& HumanTaskChooserFilterManager.getLastSelectedFilter().get(HTFilterType.USECASE.ordinal())
							.contains(request.getHumanTaskUseCase().name());
		case TYPE:
			return request != null
					&& HumanTaskChooserFilterManager.getLastSelectedFilter() != null
					&& HumanTaskChooserFilterManager.getLastSelectedFilter().get(HTFilterType.TYPE.ordinal())
							.contains(request.getHumanTaskType().name());
		case HISTORY:
//			return HumanTaskChooserFilterManager.getLastSelectedFilter() != null
//					&& !HumanTaskChooserFilterManager.getLastSelectedFilter().get(HTFilterType.HISTORY.ordinal())
//							.isEmpty();
		default:
			return false;
		}
	}

	@Override
	public SparseArray<List<String>> getChooseFilter() {
		SparseArray<List<String>> map = new SparseArray<List<String>>(HTFilterType.values().length);
		for (HTFilterType filterType : HTFilterType.values())
			map.put(filterType.ordinal(), new LinkedList<String>());

		for (FilterElement element : list) {
			if (element.isSelected())
				map.get(element.getHeaderID()).add(element.getName());
		}
		return map;
	}
}
