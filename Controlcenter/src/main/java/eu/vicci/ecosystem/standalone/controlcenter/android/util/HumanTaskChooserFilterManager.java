package eu.vicci.ecosystem.standalone.controlcenter.android.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.widget.Toast;
import eu.vicci.ecosystem.standalone.controlcenter.android.humantask.HumanTaskDataManager;
import eu.vicci.process.model.sofia.HumanTaskType;
import eu.vicci.process.model.sofia.HumanTaskUseCase;
import eu.vicci.process.model.util.messages.core.IHumanTaskRequest;

/**
 * Filter for HumanTaskChooser
 * 
 * 
 */
public class HumanTaskChooserFilterManager {
	private static Context context;
	private static SparseArray<List<String>> lastSelectedFilter;

	/**
	 * Filters HumanTaskChooser
	 * 
	 * @param selectedFilter
	 *            - criteria
	 * @param context
	 *            - ActivityContext
	 * @return filtered list
	 */
	public static ArrayList<IHumanTaskRequest> filterBy(SparseArray<List<String>> selectedFilter, Context context) {
		HumanTaskChooserFilterManager.context = context;
		HumanTaskChooserFilterManager.lastSelectedFilter = selectedFilter;
		ArrayList<IHumanTaskRequest> tempList = new ArrayList<IHumanTaskRequest>(HumanTaskDataManager.getInstance().getRequestsList());

		if (selectedFilter != null) {
			for (IHumanTaskRequest req : new ArrayList<IHumanTaskRequest>(tempList))
				for (HTFilterType filterType : HTFilterType.values())
					filter(filterType, selectedFilter.get(filterType.ordinal()), tempList, req);
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

	private static void filter(HTFilterType filterType, List<String> values, List<IHumanTaskRequest> tempList,
			IHumanTaskRequest request) {
		switch (filterType) {
		case USECASE:
			filterUseCase(values, tempList, request);
			break;
		case TYPE:
			filterType(values, tempList, request);
			break;
		case HISTORY:
			filterHistory(values, tempList, request);
			break;
		default:
			Toast.makeText(context, "FilterGroup not found", Toast.LENGTH_LONG).show();
			break;
		}
	}

	private static void filterUseCase(List<String> values, List<IHumanTaskRequest> tempList, IHumanTaskRequest request) {
		if (values.isEmpty())
			return;
		boolean hasAnyCondition = false;
		for (String value : values)
			if (!hasAnyCondition && hasUseCaseCondition(request, value) && tempList.contains(request))
				hasAnyCondition = true;

		if (!hasAnyCondition)
			tempList.remove(request);
	}

	private static void filterType(List<String> values, List<IHumanTaskRequest> tempList, IHumanTaskRequest request) {
		if (values.isEmpty())
			return;
		boolean hasAnyCondition = false;
		for (String value : values)
			if (!hasAnyCondition && hasTypeCondition(request, value) && tempList.contains(request))
				hasAnyCondition = true;

		if (!hasAnyCondition)
			tempList.remove(request);
	}

	private static void filterHistory(List<String> values, List<IHumanTaskRequest> tempList, IHumanTaskRequest request) {
		if (values.isEmpty())
			return;
//		if (!device.exceedsIdealValues())
//			tempList.remove(device);
	}

	// Bedingung für UseCaseFilter
	private static boolean hasUseCaseCondition(IHumanTaskRequest request, String criteria) {
		return request.getHumanTaskUseCase().equals(HumanTaskUseCase.valueOf(criteria));
	}

	// Bedingung für Typen-Filter
	private static boolean hasTypeCondition(IHumanTaskRequest request, String criteria) {
		return request.getHumanTaskType().equals(HumanTaskType.valueOf(criteria));
	}

	/**
	 * filter type
	 * 
	 * 
	 */
	public enum HTFilterType {
		/**
		 * The UseCase
		 */
		USECASE,
		/** 
		 * the device types (error, warning...)
		 */
		TYPE,
		/**
		 * history
		 */
		HISTORY;

		// muss die selbe Reihenfolge wie dieses Enum haben
		private static String[] headerNames = new String[] { "Use Case", "Type", "History" };

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
