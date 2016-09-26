package eu.vicci.ecosystem.standalone.controlcenter.android.fragments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.HumanTaskPortListAdapter;
import eu.vicci.ecosystem.standalone.controlcenter.android.fragments.HumanTaskSimpleTypePortDialog.HumanTaskBooleanTypePortDialog;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.IJSONDataPortInstance;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.IJSONPortInstance;

public class HumanTaskPortListFragment extends ListFragment implements RefreshableActivity {
	private TextView titleText;
	private View view;
	private int titleResourceId;
	private String title;
	private List<IJSONDataPortInstance> tmpPorts = new ArrayList<IJSONDataPortInstance>();
	private HumanTaskPortListAdapter listAdapter;
	
	public HumanTaskPortListFragment(Collection<IJSONDataPortInstance> portList, int titleResourceId){
		tmpPorts.addAll(portList);
		this.titleResourceId = titleResourceId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_humantask_data, container, false);
		titleText = (TextView) view.findViewById(R.id.HTtitle);
		title = getString(titleResourceId);
		titleText.setText(title);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Context context = getActivity().getApplicationContext();
		listAdapter = new HumanTaskPortListAdapter(context, tmpPorts);
		setListAdapter(listAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		IJSONPortInstance portInstance = tmpPorts.get(position);
		HumanTaskPortDialogFragment<?> dialog = getPortSpecificFragment(portInstance);
		dialog.setTargetFragment(this, 1);
		dialog.show(getFragmentManager(), "type in some data");
	}
	
	@Override
	public void refresh() {
		listAdapter.notifyDataSetChanged();		
	}

	private HumanTaskPortDialogFragment<?> getPortSpecificFragment(IJSONPortInstance portInstance) {
		switch (portInstance.getPortInstanceType()) {
		case StartDataPortInstance:
			return getTypeSpecificFragment((IJSONDataPortInstance) portInstance, true);
		case EndDataPortInstance:
			return getTypeSpecificFragment((IJSONDataPortInstance) portInstance, false);
		default:
			return new HumanTaskSimpleTypePortDialog.HumanTaskStringTypePortDialog(null, this);																									
		}
	}

	// TODO complete with set
	private HumanTaskPortDialogFragment<?> getTypeSpecificFragment(IJSONDataPortInstance dataPortInstance,
			boolean isReadonly) {
		switch (dataPortInstance.getPortType().getDataType().getDataTypeType()) {
		case StringType:
			return new HumanTaskSimpleTypePortDialog.HumanTaskStringTypePortDialog(dataPortInstance, isReadonly, this);
		case IntegerType:
			return new HumanTaskSimpleTypePortDialog.HumanTaskIntTypePortDialog(dataPortInstance, isReadonly, this);
		case DoubleType:
			return new HumanTaskSimpleTypePortDialog.HumanTaskDoubleTypePortDialog(dataPortInstance, isReadonly, this);
		case ListType:
			return new HumanTaskListPortDialogFragment(dataPortInstance, isReadonly, this);
		case BooleanType:
			return new HumanTaskBooleanTypePortDialog(dataPortInstance, isReadonly, this);
		case ComplexType:
			return new HumanTaskComplexPortDialog(dataPortInstance, isReadonly, this);
		default:
			return new HumanTaskSimpleTypePortDialog.HumanTaskStringTypePortDialog(dataPortInstance, isReadonly, this);
		}
	}
}
