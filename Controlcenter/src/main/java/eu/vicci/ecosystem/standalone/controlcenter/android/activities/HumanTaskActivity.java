package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.SmartCPSActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.fragments.HumanTaskPortListFragment;
import eu.vicci.ecosystem.standalone.controlcenter.android.humantask.HumanTaskDataManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.humantask.HumanTaskResponseUtil;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.model.util.messages.core.IHumanTaskRequest;
import eu.vicci.process.model.util.messages.core.IHumanTaskResponse;

/**
 * This activity needs the humantask instance id {@link HumanTaskDataManager#HUMAN_TASK_ID}
 * and the human task type (request or response) {@link HumanTaskDataManager#HUMAN_TASK_MESSAGE_TYPE}
 */
public class HumanTaskActivity extends SmartCPSActivity {
	private IHumanTaskResponse response;
	private Button submitButton;
	private boolean isAlreadyCommitted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_human_task);
		submitButton = (Button)findViewById(R.id.HTsubmit);		
		setHumanTaskMessage();
		
		
		TextView textView = (TextView) findViewById(R.id.HTName);
		textView.setText(response == null ? "Response not found" : response.getName());
		if(response == null){
			return;
		}
		
		registerClickListeners();		
	
		if(savedInstanceState != null)
			return;
		setInputFragment();
		setOutputFragment();
	}
	
	private void setHumanTaskMessage(){
		String humanTaskId = getIntent().getStringExtra(HumanTaskDataManager.HUMAN_TASK_ID);
		String htType = getIntent().getStringExtra(HumanTaskDataManager.HUMAN_TASK_MESSAGE_TYPE);
		if(HumanTaskDataManager.HUMAN_TASK_REQUEST.equals(htType)){
			IHumanTaskRequest request = HumanTaskDataManager.getInstance()
					.getHumanTaskRequestByInstanceId(humanTaskId);
			if(request == null){
				return;
			}
			response = HumanTaskResponseUtil.createResponseFromRequest(request);			
		}
		else if(HumanTaskDataManager.HUMAN_TASK_RESPONSE.equals(htType)){
			response = HumanTaskDataManager.getInstance().getHumanTaskResponseByInstanceId(humanTaskId);		
		}
		else {
			String msg = "IHumanTaskMessage type not found: " + htType;
			showToastShort(msg);
			throw new IllegalArgumentException(msg);
		}
	}
	
	private void setInputFragment(){
		HumanTaskPortListFragment fragment = new HumanTaskPortListFragment(response.getStartDataPorts().values(), 
				R.string.humanTask_inputFrag);
		getFragmentManager().beginTransaction().add(R.id.HT_Input, fragment, "Input").commit();
	}
	
	private void setOutputFragment(){
		HumanTaskPortListFragment fragment = new HumanTaskPortListFragment(response.getEndDataPorts().values(), 
				R.string.humanTask_outputFrag);
		getFragmentManager().beginTransaction().add(R.id.HT_Output, fragment, "Output").commit();	
	}
	
	private void registerClickListeners(){
		findViewById(R.id.infoButton).setOnClickListener(showHumanTaskDescription);	
		submitButton.setOnClickListener(onSubmitClicked);
	}
	
	private OnClickListener onSubmitClicked = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			if(!ProcessEngineClient.getInstance().isConnected()){
				showToastShort("please connect to the process engine first");
				return;
			}			
			if(isAlreadyCommitted)
				return;
			afterSubmitClicked();
		}
	};
	
	private void afterSubmitClicked(){
		ProcessEngineClient.getInstance().publishHumanTaskResponse(response);
		HumanTaskDataManager.getInstance().humanTaskHandled(response);
		isAlreadyCommitted = true;
		submitButton.setEnabled(false);
		submitButton.setVisibility(Button.INVISIBLE);
		finish();
	}
	
	private OnClickListener showHumanTaskDescription = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HumanTaskActivity.this);
			alertDialogBuilder.setTitle(response.getName());
			alertDialogBuilder.setMessage(response.getDescription());
			alertDialogBuilder.setCancelable(true);
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();			
		}
	};
	
	private void showToastShort(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();		
	}
}
