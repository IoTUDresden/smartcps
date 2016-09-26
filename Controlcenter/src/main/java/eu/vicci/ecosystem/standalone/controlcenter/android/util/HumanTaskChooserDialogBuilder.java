package eu.vicci.ecosystem.standalone.controlcenter.android.util;

import android.app.AlertDialog;
import android.content.Context;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.DashboardListAdapter;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.HumanTaskChooserFilterListViewAdapter;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.HumanTaskChooserListAdapter;

/**
 * Helper to build {@link AlertDialog}s for the HumanTaskChooserActivity
 * 
 */
public class HumanTaskChooserDialogBuilder {
	private final Context context;
	private final FilterDialogBuilder filterDialogBuilder;

	/**
	 * @param context
	 */
	public HumanTaskChooserDialogBuilder(Context context) {
		this.context = context;
		filterDialogBuilder = new FilterDialogBuilder(context, new HumanTaskChooserFilterListViewAdapter(context));
	}

	/**
	 * An filter dialog is build and shown.
	 * 
	 * @param listAdapter
	 *            the {@link DashboardListAdapter}, used to get all possible
	 *            options
	 * 
	 * @see DashBoardFilterManager
	 */
	public void showFilterDialog(final HumanTaskChooserListAdapter listAdapter) {
		filterDialogBuilder.showFilterDialog(listAdapter, new HumanTaskChooserFilterListViewAdapter(context));
	}

}
