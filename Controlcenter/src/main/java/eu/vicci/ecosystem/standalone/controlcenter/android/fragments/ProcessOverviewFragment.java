package eu.vicci.ecosystem.standalone.controlcenter.android.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.ProcessListItemAdapter;

public class ProcessOverviewFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        String[] dummyProcesses = new String[] {
                "Waschmaschine",
                "Spülmaschine",
                "Roboterstaubsauger",
                "Dummy",
                "Kaffekocher",
                "Heizung",
                "Heizung2",
                "Wasserkocher",
                "Dummy",
        };

        ProcessListItemAdapter adapter = new ProcessListItemAdapter(getActivity(), dummyProcesses);
        setListAdapter(adapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }


    @Override
    public View onCreateView(LayoutInflater infl, ViewGroup vGrp, Bundle savedInstanceState){
        View view = infl.inflate(R.layout.fragment_process_overview, vGrp, false);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){
        Toast.makeText(getActivity(), "not implemented yet", Toast.LENGTH_SHORT).show();
    }
}
