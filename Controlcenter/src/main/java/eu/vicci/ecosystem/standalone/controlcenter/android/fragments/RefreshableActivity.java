package eu.vicci.ecosystem.standalone.controlcenter.android.fragments;

public interface RefreshableActivity {
	
	/**
	 * Refreshes the given ui. e.g. if the data in background has changed, with the help of a dialog fragment.
	 * @see {@link HumanTaskListPortDialogFragment} and {@link HumanTaskPortListFragment}
	 */
	void refresh();

}
