package eu.vicci.ecosystem.standalone.controlcenter.android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;

/**
 * The fragment for the technical skills context.
 */
public class TutEvaluationTechnicalSkillsFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Spinner spnTechnicalSkills = (Spinner) getView().findViewById(
				R.id.tutEvaluation_spnTechnicalSkills);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.tutEvaluation_spnTechnicalSkillsChoices,
				R.layout._spinner_tutorial);
		adapter.setDropDownViewResource(R.layout._spinner_dropdown_item_tutorial);
		spnTechnicalSkills.setAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(
				R.layout._fragment_tut_evaluation_technical_skills, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

}
