package eu.vicci.ecosystem.standalone.controlcenter.android.util;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Dialog builder to show an filter dialog and filters within a listview with the help of callbacks
 */
public class FilterDialogBuilder {

	private final Context context;


	public FilterDialogBuilder(Context context, AbstractFilterDialogListAdapter adapter) {
		this.context = context;
	}

	/**
	 * An filter dialog is build and shown.
	 */
	public void showFilterDialog(final FilterableListViewAdaper listAdapter, final AbstractFilterDialogListAdapter adapter) {
		final StickyListHeadersListView listView = new StickyListHeadersListView(context);
		listView.setAdapter(adapter);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.ht_filter_filter)).setIconAttribute(R.attr.actionFilterDrawable)
				.setView(listView)
				.setPositiveButton(context.getString(R.string.ht_filter_apply), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						listAdapter.filterBy(adapter.getChooseFilter());
					}
				})
				.setNeutralButton(context.getString(R.string.ht_filter_remove), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						listAdapter.filterBy(null);
					}
				}).setNegativeButton(context.getString(R.string.ht_filter_cancel), null);

		builder.create().show();
	}

	/**
	 * Callback to use within a listadapter to filter with the help of the
	 * {@link FilterDialogBuilder}
	 */
	public static interface FilterableListViewAdaper {

		/**
		 * Filters to the given values or resets the filter.
		 * 
		 * @param selectedFilter
		 *            if null, the filter is reseted
		 */
		void filterBy(SparseArray<List<String>> selectedFilter);
		
		/**
		 * Gets the last selected filter
		 * @return
		 */
		SparseArray<List<String>> getLastSelectedFilter();
	}

}
