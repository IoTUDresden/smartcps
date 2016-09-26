package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.SmartCPSActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.sequence.SequenceInstanceActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.sequence.history.HistoryListAdapter;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.subscribers.StateChangeMessageHandler;
import eu.vicci.process.model.sofia.ProcessStep;

public class ProcessInstanceHistoryActivity extends SmartCPSActivity {
	public static final String PROCESS_INSTANCE_ID = "processInstanceId";

	public static final String PROCESS_STEP = "processStep";

	private String processInstanceID;

	private ProcessStep model; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		processInstanceID = (String) intent.getExtras().get(PROCESS_INSTANCE_ID);
		model = SequenceInstanceActivity.model;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_instance_history);
		LinearLayout listView = (LinearLayout) findViewById(R.id.process_instance_history_listview);
		listView.getParent().requestDisallowInterceptTouchEvent(true);
		HistoryListAdapter adapter = new HistoryListAdapter(this, 0, StateChangeMessageHandler.getInstance().getMessages(processInstanceID), processInstanceID, model);
		listView.removeAllViews();
		for(int i=0; i<adapter.getCount(); i++){
			View newView = adapter.getView(i, null, null);
			listView.addView(newView);
			
		}
		HorizontalScrollView scroll = (HorizontalScrollView) listView.getParent();
		fillList();
	}

	private void fillList() {
		StateChangeMessageHandler.getInstance().getMessages(processInstanceID);
	}

	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		
	}
	
	
}
