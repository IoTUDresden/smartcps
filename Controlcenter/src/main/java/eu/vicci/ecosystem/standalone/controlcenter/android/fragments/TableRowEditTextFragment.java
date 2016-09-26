package eu.vicci.ecosystem.standalone.controlcenter.android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;

/**
 * The TableRowEditTextFragment to be inserted e.g. in add service activity..
 */
public class TableRowEditTextFragment extends Fragment {

	public static final String TEXT_KEY = "textKey";
	public static final String ID_KEY = "idKey";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup resultView = (ViewGroup) inflater.inflate(
				R.layout._fragment_tablerow_edittext, container, false);
		((TextView) resultView.getChildAt(0)).setText(getArguments().getString(
				TEXT_KEY));
		((EditText) resultView.getChildAt(1)).setId(getArguments().getInt(
				ID_KEY));
		return (View) resultView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

}
