package eu.vicci.ecosystem.processview.android;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import eu.vicci.ecosystem.processview.android.internal.dialog.ProcessValuesListAdapter;
import eu.vicci.process.model.util.messages.core.IStateChangeMessage;

/**
 * Needs the argument {@link #ARG_MESSAGE} as {@link IStateChangeMessage}
 */

public class ProcessValuesDialog extends DialogFragment {
    public static final String ARG_MESSAGE = "message";

    private ProcessValuesListAdapter adapter;
    private IStateChangeMessage message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FIXME wont work
        message = (IStateChangeMessage)getArguments().get(ARG_MESSAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout view = new LinearLayout(getActivity());
        ListView list = new ListView(getActivity());
        adapter = new ProcessValuesListAdapter(getActivity());
        adapter.fillWith(message);
        list.setAdapter(adapter);
        view.addView(list);
        return view;
    }
}
