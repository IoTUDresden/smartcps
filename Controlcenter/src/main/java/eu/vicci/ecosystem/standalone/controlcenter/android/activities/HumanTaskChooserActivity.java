package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import java.util.Map;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.SmartCPSActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.HumanTaskChooserListAdapter;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.HumanTaskChooserSortType;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.HumanTaskChooserStickyGridHeadersAdapterWrapper;
import eu.vicci.ecosystem.standalone.controlcenter.android.humantask.HumanTaskDataManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.humantask.HumanTaskResponseUtil;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.HumanTaskChooserDialogBuilder;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.model.util.messages.core.IHumanTaskMessage;
import eu.vicci.process.model.util.messages.core.IHumanTaskRequest;
import eu.vicci.process.model.util.messages.core.IHumanTaskResponse;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.IJSONDataPortInstance;
import eu.vicci.process.model.util.serialization.jsontypeinstances.core.IJSONBooleanTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypes.core.IJSONBooleanType;
import eu.vicci.process.model.util.serialization.jsontypes.core.IJSONType;

public class HumanTaskChooserActivity extends SmartCPSActivity {
	private HumanTaskChooserListAdapter listAdapter;
	private StickyGridHeadersGridView gridView;
	private static final HumanTaskChooserSortType SORTBY = HumanTaskChooserSortType.NOT_SORTED;
	private HumanTaskChooserDialogBuilder dialogBuilder = new HumanTaskChooserDialogBuilder(this);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_human_task_chooser);
		setTitle(R.string.humanTask_name);

		listAdapter = new HumanTaskChooserListAdapter(this, SORTBY);

		gridView = (StickyGridHeadersGridView) findViewById(R.id.htchooser_gridview);
		gridView.setAdapter(new HumanTaskChooserStickyGridHeadersAdapterWrapper(listAdapter));
		gridView.setAdapter(listAdapter);
		buildAdapter();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.human_task_sort, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_filter:
			dialogBuilder.showFilterDialog(listAdapter);
			return true;
		case R.id.menu_sort_by_name_e:
			listAdapter.sortBy(HumanTaskChooserSortType.SORT_BY_NAME);
			return true;
		case R.id.menu_sort_by_type_e:
			listAdapter.sortBy(HumanTaskChooserSortType.SORT_BY_TYPE);
			return true;
		case R.id.menu_sort_by_date_e:
			listAdapter.sortBy(HumanTaskChooserSortType.SORT_BY_DATE);
			return true;
		case R.id.menu_sort_by_none_e:
			listAdapter.sortBy(HumanTaskChooserSortType.NOT_SORTED);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    // Make sure to call the super method so that the states of our views are saved
	    super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		listAdapter.unregisterListeners();
		super.onDestroy();
	}

	private void showDialogForControlPorts(IHumanTaskMessage req) {
		if(req instanceof IHumanTaskResponse){
			openHumanTaskActivity(req);
		} else if(req instanceof IHumanTaskRequest) {
			AlertDialog dialog = getShortDialog((IHumanTaskRequest)req);
			if(dialog != null)
				dialog.show();			
		}	
		else {
			Toast.makeText(this, "type of task '" + req.getClass().getSimpleName()+ "' is not handled", 
					Toast.LENGTH_SHORT).show();				
		}
	}

	private void buildAdapter() {
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				IHumanTaskMessage tmpMessage = listAdapter.getItem(position);
				if (tmpMessage == null)
					return;

				if (shouldShowShortDialog(tmpMessage))
					showDialogForControlPorts(tmpMessage);
				else 
					openHumanTaskActivity(tmpMessage);				
			}
		});
	}
	
	private void openHumanTaskActivity(IHumanTaskMessage message){
		Intent intent = new Intent(getApplicationContext(), HumanTaskActivity.class);
		String instanceId = message.getHumanTaskInstanceId();
		String htType = getHTType(message);		
		intent.putExtra(HumanTaskDataManager.HUMAN_TASK_ID, instanceId);
		intent.putExtra(HumanTaskDataManager.HUMAN_TASK_MESSAGE_TYPE, htType);
		startActivity(intent);				
	}
	
	private static String getHTType(IHumanTaskMessage message){
		if(message instanceof IHumanTaskRequest)
			return HumanTaskDataManager.HUMAN_TASK_REQUEST;
		if(message instanceof IHumanTaskResponse)
			return HumanTaskDataManager.HUMAN_TASK_RESPONSE;		
		return null;
	}
	
	private AlertDialog getShortDialog(IHumanTaskRequest message){
		IHumanTaskResponse response = HumanTaskResponseUtil.createResponseFromRequest(message);
		IJSONDataPortInstance pi = getFirstData(response.getEndDataPorts());
		
		if(pi == null){
			return buildOkDialog(response);		
		}
		
		if(!(pi.getDataTypeInstance() instanceof IJSONBooleanTypeInstance)){
			String dt = pi.getDataTypeInstance() == null ? "null" : 
				pi.getDataTypeInstance().getClass().getSimpleName();
			Toast.makeText(this, "cant make short dialog for port datatype '" + dt + "'", 
					Toast.LENGTH_SHORT).show();	
			return null;
		}
		
		IJSONBooleanTypeInstance typeInstance = (IJSONBooleanTypeInstance)pi.getDataTypeInstance();
		return buildYesNoDialog(response, typeInstance);			
	}
	
	private AlertDialog buildOkDialog(final IHumanTaskResponse response){
		return new AlertDialog.Builder(this)
		.setTitle(response.getName())
		.setMessage(response.getDescription())
		.setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				sendResponse(response);				
			}			
		} ).create();		
	}
	
	private AlertDialog buildYesNoDialog(final IHumanTaskResponse response, final IJSONBooleanTypeInstance typeInstance){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(response.getDescription())
		.setTitle(response.getName())
		.setCancelable(true)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						typeInstance.setValue(true);
						dialog.cancel();
						sendResponse(response);

					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						typeInstance.setValue(false);
						dialog.cancel();
						sendResponse(response);
					}
				});
		return alertDialogBuilder.create();			
	}
	
	private boolean shouldShowShortDialog(IHumanTaskMessage message){
		boolean hasStartDataPorts = !message.getStartDataPorts().isEmpty();
		boolean hasEndDataPorts = !message.getEndDataPorts().isEmpty();
		IJSONType type = getDataTypeOfEndport(message.getEndDataPorts());
		boolean hasOneBooleanEndPort = message.getEndDataPorts().size() == 1 
				&& (type != null && type instanceof IJSONBooleanType);
		
		return (!hasStartDataPorts && !hasEndDataPorts) ||	hasOneBooleanEndPort;	
	}
	
	private void sendResponse(IHumanTaskResponse response){
		ProcessEngineClient pec = ProcessEngineClient.getInstance();
		if(!pec.isConnected()){
			new AlertDialog.Builder(this)
			.setMessage("Please connect to PROtEUS first.")
			.setTitle("Not connected")
			.create().show();
			return;
		}
		pec.publishHumanTaskResponse(response);
		HumanTaskDataManager.getInstance().humanTaskHandled(response);
	}
	
	private IJSONDataPortInstance getFirstData(Map<String, IJSONDataPortInstance> endports){
		for (IJSONDataPortInstance pi : endports.values()) {
			return pi;					
		}
		return null;
	}
	
	// we only use the first value	
	private IJSONType getDataTypeOfEndport(Map<String, IJSONDataPortInstance> map){
		if(map == null)
			return null;
		for (IJSONDataPortInstance pi : map.values()) {
			return pi.getPortType().getDataType();					
		}
		return null;		
	}

}
