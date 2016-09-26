package eu.vicci.ecosystem.standalone.controlcenter.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;

/**
 * The fragment for the age context.
 */
public class TutEvaluationAgeFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Spinner spnAge = (Spinner) getView().findViewById(
				R.id.tutEvaluation_spnAge);
		List<String> ageArray = new ArrayList<String>();
		for (Integer i = 10; i <= 140; i++)
			ageArray.add(i.toString());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout._spinner_tutorial, ageArray);
		adapter.setDropDownViewResource(R.layout._spinner_dropdown_item_tutorial);
		spnAge.setAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout._fragment_tut_evaluation_age,
				container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

}
