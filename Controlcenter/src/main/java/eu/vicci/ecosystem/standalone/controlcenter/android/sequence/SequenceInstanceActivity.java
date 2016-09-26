package eu.vicci.ecosystem.standalone.controlcenter.android.sequence;

import java.util.Map;
import java.util.Observable;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.ProcessInstanceHistoryActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.SequenceActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.SmartCPSActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.processview.ItemDetailFragment;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessInfosHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessInstanceInfosHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessInstanceInfosReducedHandler;
import eu.vicci.process.kpseus.connect.subscribers.AbstractPubSubDataSubscriber;
import eu.vicci.process.kpseus.connect.subscribers.StateChangeMessageHandler;
import eu.vicci.process.kpseus.connect.subscribers.SubscribedMessageReceiver;
import eu.vicci.process.model.sofia.Process;
import eu.vicci.process.model.sofiainstance.ProcessInstance;

public class SequenceInstanceActivity extends SmartCPSActivity implements SubscribedMessageReceiver {

	public static Process model = null;
	private String modelId;
	private String instanceId;
	private ProcessInfosHandler pih;
	private ProcessInstanceInfosReducedHandler piirh;
	private boolean[] modelAndInstanceInfosReceived = { false, false };
	boolean isInstance;
	private String activeId;
	private Map<String, String> modelToInstanceMapping;
	private ProcessInstanceInfosHandler piih;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		modelId = intent.getStringExtra(ItemDetailFragment.EXTRA_MESSAGE_MODEL_ID);
		instanceId = intent.getStringExtra(ItemDetailFragment.EXTRA_MESSAGE_PROCESS_INSTANCEID);
		activeId = intent.getStringExtra(ItemDetailFragment.EXTRA_MESSAGE_ACTIVE_ID);

		// //Comment out these three lines if you do not want the server to get
		// the process model.
		pih = new ProcessInfosHandler(modelId);
		pih.addHandlerFinishedListener(this);
		piih = new ProcessInstanceInfosHandler(instanceId);
//		piih.addObserver(this);
		piirh = new ProcessInstanceInfosReducedHandler(instanceId);
		piirh.addHandlerFinishedListener(this);
		StateChangeMessageHandler.getInstance().addSubscribedMessageReceiver(this);
		ProcessEngineClient.getInstance().getProcessInfos(pih);
		ProcessEngineClient.getInstance().getProcessInstanceInfosReduced(piirh);
		ProcessEngineClient.getInstance().getProcessInstanceInfos(piih);
	}

	@Override
	public void update(Observable observable, Object data) {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.process_instance_history, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.showHistory: {
			
			Intent intent = new Intent(this, ProcessInstanceHistoryActivity.class);
			intent.putExtra(ProcessInstanceHistoryActivity.PROCESS_INSTANCE_ID, instanceId);
			startActivity(intent);
			break;
		}
		
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		if (handler instanceof ProcessInfosHandler) {
			model = pih.getProcess();
			ProcessEngineClient.getInstance().getLocalProcesses().put(model.getId(), model);

			setContentView(R.layout.sequence_view_holder);
			FrameLayout sequenceViewContainer = (FrameLayout) findViewById(R.id.sequenceViewContainer);
			final RelativeLayout propertiesHolder = (RelativeLayout) findViewById(R.id.properties);
			ImageButton bCloseProperties = (ImageButton) findViewById(R.id.properties_close_button);
			bCloseProperties.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SequenceActivity.animateProperties(propertiesHolder, false);
				}
			});
			modelAndInstanceInfosReceived[0] = true;
			if (modelAndInstanceInfosReceived[0] && modelAndInstanceInfosReceived[1])
				sequenceViewContainer.addView(new SequenceView(model, getApplicationContext(), propertiesHolder, activeId, modelToInstanceMapping));

		}

		if (handler instanceof ProcessInstanceInfosHandler){
			ProcessInstance inst = piih.getProcessInstance();
			inst.getInstanceId();
		}
		
		if (handler instanceof ProcessInstanceInfosReducedHandler) {
			modelToInstanceMapping = (Map<String, String>) arg;
			FrameLayout sequenceViewContainer = (FrameLayout) findViewById(R.id.sequenceViewContainer);
			final RelativeLayout propertiesHolder = (RelativeLayout) findViewById(R.id.properties);
			modelAndInstanceInfosReceived[1] = true;
			if (modelAndInstanceInfosReceived[0] && modelAndInstanceInfosReceived[1])
				sequenceViewContainer.addView(new SequenceView(model, getApplicationContext(), propertiesHolder, activeId, modelToInstanceMapping));

		}		
	}

	@Override
	public void onSubscribedMessage(AbstractPubSubDataSubscriber subscriber, Object arg) {
		//statemessages etc		
	}
}
