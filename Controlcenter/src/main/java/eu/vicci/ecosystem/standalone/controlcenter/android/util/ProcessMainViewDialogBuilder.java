package eu.vicci.ecosystem.standalone.controlcenter.android.util;

import eu.vicci.ecosystem.standalone.controlcenter.android.activities.ProcessMainActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.ProcessListAdapter;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.ProcessMainViewFilterListAdapter;

public class ProcessMainViewDialogBuilder {
	private final ProcessMainActivity context;
	private final FilterDialogBuilder filterDialogBuilder;
	
	public ProcessMainViewDialogBuilder(ProcessMainActivity context) {
		this.context = context;
		filterDialogBuilder = new FilterDialogBuilder(context, new ProcessMainViewFilterListAdapter(context));
	}	
	
	public void showFilterDialog(final ProcessListAdapter listAdapter) {
		filterDialogBuilder.showFilterDialog(listAdapter, 
				new ProcessMainViewFilterListAdapter(context, listAdapter.getLastSelectedFilter()));
	}
	
}
