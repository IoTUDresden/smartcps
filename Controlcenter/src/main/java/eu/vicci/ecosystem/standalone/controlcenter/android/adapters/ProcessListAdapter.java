package eu.vicci.ecosystem.standalone.controlcenter.android.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.ProcessListHelper;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.ProcessMainActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.processview.OnStartClickListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.FilterDialogBuilder.FilterableListViewAdaper;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.ProcessMainViewFilterManager.FilterType;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.ProcessMainViewFilterManager.GroupTypes;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.engine.core.IProcessInfo;
import eu.vicci.process.engine.core.IProcessInstanceInfo;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.handlers.HandlerFinishedListener;
import eu.vicci.process.kpseus.connect.handlers.PauseInstanceHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessInstanceInfoListHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessListHandler;
import eu.vicci.process.kpseus.connect.handlers.StopInstanceHandler;
import eu.vicci.process.kpseus.connect.subscribers.AbstractPubSubDataSubscriber;
import eu.vicci.process.kpseus.connect.subscribers.StateChangeMessageHandler;
import eu.vicci.process.kpseus.connect.subscribers.SubscribedMessageReceiver;
import eu.vicci.process.model.sofiainstance.State;
import eu.vicci.process.model.util.messages.core.IStateChangeMessage;

/**
 * This adapter can act as {@link HandlerFinishedListener} and handle events
 * received from the process engine e.g. receive all deployed processes or all
 * process instances. In case of instances, the list supports also filtering
 */
public class ProcessListAdapter extends BaseAdapter
		implements ListAdapter, HandlerFinishedListener, SubscribedMessageReceiver, FilterableListViewAdaper {
	
	private LayoutInflater inflater;
	private boolean isInstance;

	private ProcessEngineClient pec;
	private ProcessMainActivity context;

	/**
	 * Object used for locks like in the arrayadapter.
	 * Lock any write operation on the list.
	 */	
	private Object mLock = new Object();
	
	private SparseArray<List<String>> lastSelectedFilter;
	
	private List<ProcessListHelper> list = new ArrayList<ProcessListHelper>();
	private List<ProcessListHelper> oldList;

	// prevents that the process list is requested multiple times
	private volatile boolean newListRequested = false;

	public ProcessListAdapter(ProcessMainActivity context, boolean isInstance) {
		this.isInstance = isInstance;
		this.context = context;

		pec = ProcessEngineClient.getInstance();

		if (isInstance) {
			StateChangeMessageHandler.getInstance().addSubscribedMessageReceiver(this);
			// TODO statemessage also for the not instance view to list all
			// processes
		} 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.process_item_detail_list_row, parent, false);
		} else {
			view = convertView;
		}
		final TextView textId = (TextView) view.findViewById(R.id.item_detail_id);
		final TextView textName = (TextView) view.findViewById(R.id.properties_port_name);
		final TextView textType = (TextView) view.findViewById(R.id.item_detail_type);
		final TextView textState = (TextView) view.findViewById(R.id.item_detail_status);
		final TextView textDescription = (TextView) view.findViewById(R.id.item_detail_description);

		ProcessListHelper item = getItem(position);
		State state = item.getState();
		textName.setText(item.getName());
		textType.setText(item.getType());
		textId.setText(isInstance ? item.getInstanceId() : item.getId());
		textState.setText(state == null ? "" : state.getLiteral());
		textDescription.setText(item.getDescription());

		setControlButtons(view, item);
		setStateVisibility(textState, state);
		view.setVisibility(getItem(position).isVisible() ? View.VISIBLE : View.INVISIBLE);		
		return view;
	}

	/**
	 * Makes a RPC to receive the deployed processes (isInstance == false) or
	 * the process instances (isInstance == true)
	 */
	public void getProcesses() {
		if (isInstance) {
			ProcessInstanceInfoListHandler handler = new ProcessInstanceInfoListHandler();
			handler.addHandlerFinishedListener(this);
			pec.listProcessInstances(handler);
		} else{
			ProcessListHandler handler = new ProcessListHandler();
			handler.addHandlerFinishedListener(this);
			pec.listDeployedProcesses(handler);
		}
	}

	/**
	 * Gets the process by its process id
	 * 
	 * @param processId
	 * @return null if not found
	 */
	public ProcessListHelper getHelperByProcessId(String processId) {
		for (ProcessListHelper helper : list) {
			if (helper.getId().equals(processId))
				return helper;
		}
		return null;
	}

	/**
	 * Gets the process by its process instance id
	 * 
	 * @param processId
	 * @return null if not found
	 */
	public ProcessListHelper getHelperByProcessInstanceId(String processInstanceId) {
		for (ProcessListHelper helper : list) {
			if (helper.getInstanceId().equals(processInstanceId))
				return helper;
		}
		return null;
	}

	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		if (handler instanceof ProcessListHandler)
			handleProcessList((ProcessListHandler) handler);
		else if (handler instanceof ProcessInstanceInfoListHandler)
			handleProcessInstanceInfoList((ProcessInstanceInfoListHandler) handler);
	}

	@Override
	public void onSubscribedMessage(AbstractPubSubDataSubscriber subscriber, final Object arg) {
		if (!(subscriber instanceof StateChangeMessageHandler))
			return;
		final IStateChangeMessage message = (IStateChangeMessage) arg;
		ProcessListHelper helper = getHelperByProcessInstanceId(message.getProcessInstanceId());
		if (helper == null) {
			requestNewInstances();
			return;
		}

		helper.setState(message.getState());
		notifyUi();
	}
	
	/**
	 * Searches in the list of processes
	 * @param processName
	 */
	public void searchByName(String processName){
		synchronized (mLock) {
			lastSelectedFilter = null;
			for (ProcessListHelper helper : list) {
				if(processName == null || processName.isEmpty())
					helper.setVisible(true);
				else if (!helper.getName().contains(processName))
					helper.setVisible(false);
			}			
		}
		notifyUi();
	}

	@Override
	public void filterBy(SparseArray<List<String>> selectedFilter){
		synchronized (mLock) {
			lastSelectedFilter = selectedFilter;
			//reset
			if(lastSelectedFilter == null){
				list.clear();
				if(oldList != null)
					list.addAll(oldList); 
				oldList = null;
			}else {
				//filter
				if(oldList == null)
					oldList = new ArrayList<ProcessListHelper>(list);
				list.clear();
				for (ProcessListHelper helper : oldList)
					for(FilterType type : FilterType.values())
						filter(type, lastSelectedFilter, helper);					
			}					
		}
		notifyUi();
	}
	
	private void filter(FilterType type, SparseArray<List<String>> selectedFilter, ProcessListHelper current){
		switch (type) {
		case GroupedState:
			filterGroupedState(selectedFilter.get(type.ordinal()), current);
			break;
		case State:
			filterState(selectedFilter.get(type.ordinal()), current);
			break;
		default:
			break;
		}
	}
	
	private void filterState(List<String> filter, ProcessListHelper current){
		boolean match = false;
		for (String val : filter) {
			match = current.getState().getName().toLowerCase(Locale.ENGLISH).equals(val);
			if(match) break;			
		}
		
		if(match && !list.contains(current))
			list.add(current);
	}
	
	private void filterGroupedState(List<String> filter, ProcessListHelper current){
		boolean match = false;
		String fName = GroupTypes.Finished.name().toLowerCase(Locale.ENGLISH);
		String uName = GroupTypes.Unfinished.name().toLowerCase(Locale.ENGLISH);
		
		for (String val : filter) {
			boolean finished = processIsFinished(current);
			match = finished && fName.equals(val) || !finished && uName.equals(val);
			if(match) break;
		}	
		
		if(match && !list.contains(current))
			list.add(current);
	}
	
	private boolean processIsFinished(ProcessListHelper current){
		State state = current.getState();
		if(state == State.EXECUTED || state == State.FAILED || state == State.STOPPED)
			return true;
		return false;
	}

	private void handleProcessList(final ProcessListHandler handler) {
		synchronized (mLock) {
			list.clear();
			for (IProcessInfo pi : handler.getProcessInfos())
				list.add(new ProcessListHelper(pi));			
		}
		notifyUi();
	}

	private void handleProcessInstanceInfoList(final ProcessInstanceInfoListHandler handler) {
		newListRequested = false;
		synchronized (mLock) {
			oldList = null;
			list.clear();
			for (IProcessInstanceInfo pi : handler.getProcessInstanceList())
				list.add(new ProcessListHelper(pi));
			if(lastSelectedFilter != null) filterBy(lastSelectedFilter);
		}
		notifyUi();
	}

	private void notifyUi() {
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
	}

	private void requestNewInstances() {
		if (!newListRequested) {
			newListRequested = true;
			final HandlerFinishedListener listener = this;
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					ProcessInstanceInfoListHandler handler = new ProcessInstanceInfoListHandler();
					handler.addHandlerFinishedListener(listener);
					pec.listProcessInstances(handler);
				}
			});
			t.start();
		}
	}

	private void setControlButtons(View processDetailRow, final ProcessListHelper item) {
		final Button bStart = (Button) processDetailRow.findViewById(R.id.start);
		final Button bStop = (Button) processDetailRow.findViewById(R.id.stop);
		final Button bPause = (Button) processDetailRow.findViewById(R.id.pause);
		State itemState = item.getState();

		bStart.setOnClickListener(new OnStartClickListener(item.getId(), context, bPause, bStop, isInstance));
		bPause.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PauseInstanceHandler pih = new PauseInstanceHandler(item.getId());
				ProcessEngineClient.getInstance().pauseProcessInstance(pih);
				// TODO: handle return values

				bPause.setVisibility(View.INVISIBLE);
				bStop.setVisibility(View.INVISIBLE);
				bStart.setVisibility(View.VISIBLE);
				Toast.makeText(context, "Pause request has been sent to the server", Toast.LENGTH_SHORT).show();

			}
		});

		bStop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				StopInstanceHandler stih = new StopInstanceHandler(item.getId());
				ProcessEngineClient.getInstance().stopProcessInstance(stih);
				Toast.makeText(context, "Stop request has been sent to the server", Toast.LENGTH_SHORT).show();
			}
		});
		setButtonVisibilizy(bStart, bStop, bPause, itemState);
	}

	private void setButtonVisibilizy(Button bStart, Button bStop, Button bPause, State itemState) {
		if (itemState == null) {
			bPause.setVisibility(View.INVISIBLE);
			bStop.setVisibility(View.INVISIBLE);
			bStart.setVisibility(View.VISIBLE);
		} else {
			if (itemState != State.FAILED && itemState != State.EXECUTED) {
				// running
				bStart.setVisibility(View.INVISIBLE);
				bPause.setVisibility(View.VISIBLE);
				bStop.setVisibility(View.VISIBLE);
			} else {
				// stopped, paused etc
				bStart.setVisibility(View.VISIBLE);
				bPause.setVisibility(View.INVISIBLE);
				bStop.setVisibility(View.INVISIBLE);
			}
		}
	}

	private void setStateVisibility(TextView textState, State state) {
		if (state != null)
			textState.setVisibility(View.VISIBLE);
		else
			textState.setVisibility(View.INVISIBLE);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ProcessListHelper getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public SparseArray<List<String>> getLastSelectedFilter() {
		return lastSelectedFilter;
	}
}
