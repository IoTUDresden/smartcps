package eu.vicci.ecosystem.standalone.controlcenter.android.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpContent;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpItem;
import eu.vicci.ecosystem.standalone.controlcenter.android.listeners.TwoPaneItemSelectListener;

/**
 * A list fragment representing a list of Helps. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link HelpDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the
 * {@link TwoPaneItemSelectListener} interface.
 */
public class HelpListFragment extends ListFragment {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	private TwoPaneItemSelectListener twoPaneItemSelectListener = dummyTwoPaneItemSelectListener;
	// only used on tablets
	private int selectedPosition = ListView.INVALID_POSITION;

	/**
	 * A dummy implementation of the {@link TwoPaneItemSelectListener} interface
	 * that does nothing. Used only when this fragment is not attached to an
	 * activity.
	 */
	private static TwoPaneItemSelectListener dummyTwoPaneItemSelectListener = new TwoPaneItemSelectListener() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public HelpListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<HelpItem>(getActivity(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, HelpContent.getHelpItems()));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof TwoPaneItemSelectListener)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		twoPaneItemSelectListener = (TwoPaneItemSelectListener) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		twoPaneItemSelectListener = dummyTwoPaneItemSelectListener;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);
		// notify the listeners
		twoPaneItemSelectListener.onItemSelected(HelpContent.getHelpItems()
				.get(position).getId());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (selectedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, selectedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically give
		// items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	/**
	 * Sets the activated position.
	 * 
	 * @param position
	 *            the new activated position
	 */
	public void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(selectedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		selectedPosition = position;
	}
}
