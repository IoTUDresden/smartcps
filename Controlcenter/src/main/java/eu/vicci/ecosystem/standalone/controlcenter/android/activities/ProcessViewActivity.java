package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import eu.vicci.ecosystem.processview.android.IProcessView;
import eu.vicci.ecosystem.processview.android.IProcessView.ProcessClickedListener;
import eu.vicci.ecosystem.processview.android.IProcessView.ProcessViewReadyListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.SmartCPSActivity;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.handlers.HandlerFinishedListener;
import eu.vicci.process.kpseus.connect.handlers.ProcessInfosHandler;
import eu.vicci.process.kpseus.connect.subscribers.AbstractPubSubDataSubscriber;
import eu.vicci.process.kpseus.connect.subscribers.StateChangeMessageHandler;
import eu.vicci.process.kpseus.connect.subscribers.SubscribedMessageReceiver;
import eu.vicci.process.model.sofiainstance.State;
import eu.vicci.process.model.util.messages.core.IStateChangeMessage;

// TODO unsubscripe from statechangeshandler if activity is not vissible
public class ProcessViewActivity extends SmartCPSActivity implements HandlerFinishedListener, SubscribedMessageReceiver {
	public static final String EXTRA_MESSAGE_MODEL_ID = "processview_model_id";
	public static final String EXTRA_MESSAGE_IS_INSTANCE = "processview_is_instance";
	public static final String EXTRA_MESSAGE_INSTANCE_ID = "processview_instance_id";
	
	private static final String E_NO_MODEL_ID = "No model id was found. Cant show process";
	private static final String E_NO_INSTANCE_ID = "Instance ID was given with null. Can't subscribe to statechanges.";
	
	private ProcessEngineClient pec = ProcessEngineClient.getInstance();
	
	private IProcessView processView;
	
	private boolean isInstance;
	private String modelId;
	private String instanceId;
	
	private boolean viewIsReady;
	private boolean setInitialStates;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_view);
		
		processView = (IProcessView)findViewById(R.id.processViewActivity_processView);	
		processView.setProcessClickedListener(processClickedListener);	
		processView.setProcessViewReadyListener(processViewReadyListener);
		
		isInstance = getIntent().getBooleanExtra(EXTRA_MESSAGE_IS_INSTANCE, false);
		modelId = getIntent().getStringExtra(EXTRA_MESSAGE_MODEL_ID);
		instanceId = getIntent().getStringExtra(EXTRA_MESSAGE_INSTANCE_ID);
		
		loadProcessModel();
	}
	
	private void loadProcessModel(){
		if(modelId == null){
			showErrorToast(E_NO_MODEL_ID);			
			return;
		}
		if(isInstance && instanceId == null){
			isInstance = false;
			showErrorToast(E_NO_INSTANCE_ID);
		}
		viewIsReady = false;
		setInitialStates = true;
		ProcessInfosHandler handler = new ProcessInfosHandler(modelId);
		handler.addHandlerFinishedListener(this);
		pec.getProcessInfos(handler);
	}
	
	@Override
	public void onHandlerFinished(final AbstractClientHandler handler, Object arg) {
		super.onHandlerFinished(handler, arg);
		if(handler instanceof ProcessInfosHandler){
			runOnUiThread(new Runnable() {				
				@Override
				public void run() {
					processView.showProcess(((ProcessInfosHandler) handler).getProcess());
					if(isInstance)
						listenToStatechanges();					
				}
			});
		}
	}
	
	private ProcessClickedListener processClickedListener = new ProcessClickedListener() {		
		@Override
		public void onProcessClicked(String processId) {
			showProcessDialog(processId);
		}
	};
	
	private ProcessViewReadyListener processViewReadyListener = new ProcessViewReadyListener() {		
		@Override
		public void onProcessViewIsReady() {
			viewIsReady = true;	
			if(isInstance)
				setInitialStates();
		}
	};
	
	private void showErrorToast(String text){
		Log.e("ProcessViewActivity", text);
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();		
	}
	
	private void listenToStatechanges(){
		StateChangeMessageHandler.getInstance().addSubscribedMessageReceiver(this);		
	}
	
	private void showProcessDialog(final String id){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Callback")
		.setMessage("Example for process clicked dialog.\nTODO this should show the current values for this process.\nChange the state of: ''" + id == null ? "null" : id)
		.setPositiveButton("active", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(processView != null)
					processView.changeProcessState(id, State.ACTIVE);				
			}
		}).setNegativeButton("executed", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(processView != null)
					processView.changeProcessState(id, State.EXECUTED);	
				
			}
		}).create().show();		
	}

	@Override
	public void onSubscribedMessage(AbstractPubSubDataSubscriber subscriber, Object arg) {
		if(!viewIsReady || !(subscriber instanceof StateChangeMessageHandler))
			return;	
		setInitialStates();
		if(!(arg instanceof IStateChangeMessage))
			return;
		IStateChangeMessage message = (IStateChangeMessage)arg;
		//only accept the state changes for this process
		if(message.getProcessInstanceId().equals(instanceId)){
			processView.changeProcessState(message.getProcessId(), message.getState());
		}
	}
	
	private void setInitialStates(){
		if(!setInitialStates)
			return;	
		setInitialStates = false;
		// TODO we need the states from the engine. if the app is restarted, these list will be empty
		List<IStateChangeMessage> messages = StateChangeMessageHandler.getInstance().getMessages(instanceId);
		for (IStateChangeMessage message : messages) {
			processView.changeProcessState(message.getProcessId(), message.getState());
		}
	}

}
