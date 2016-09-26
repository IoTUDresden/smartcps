package eu.vicci.ecosystem.standalone.controlcenter.android.processview;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.handlers.HandlerFinishedListener;
import eu.vicci.process.kpseus.connect.handlers.ProcessInfosHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessInstanceInfosHandler;
import eu.vicci.process.kpseus.connect.handlers.ResumeInstanceHandler;
import eu.vicci.process.model.sofia.Process;
import eu.vicci.process.model.sofiainstance.ProcessInstance;

public class OnStartClickListener implements OnClickListener, HandlerFinishedListener {

	private String id;
	private Context mContext;
	private Process process;
	private ProcessInfosHandler pih;
	private ProcessInstanceInfosHandler piih;
	private Button bPause;
	private Button bStop;
	private boolean isInstance;
	private ProcessInstance processInstance;

	public OnStartClickListener(String id, Context mContext, Button buttonPause, Button buttonStop, boolean isInstance) {
		super();
		this.id = id;
		this.isInstance = isInstance;
		this.mContext = mContext;
		bPause = buttonPause;
		bStop = buttonStop;
	}

	@Override
	public void onClick(View v) {
		if (isInstance) {
			piih = new ProcessInstanceInfosHandler(id);
			piih.addHandlerFinishedListener(this);
			ProcessEngineClient.getInstance().getProcessInstanceInfos(piih);
		} else {
			pih = new ProcessInfosHandler(id);
			pih.addHandlerFinishedListener(this);
			ProcessEngineClient.getInstance().getProcessInfos(pih);
		}
	}

	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		if (handler instanceof ProcessInfosHandler) {
			process = pih.getProcess();
			ProcessEngineClient.getInstance().getLocalProcesses().put(process.getId(), process);
			StartDialog dialogFragment = new StartDialog();
			FragmentManager fm = ((Activity) mContext).getFragmentManager();
			dialogFragment.show(fm, id);
		}
		if(handler instanceof ProcessInstanceInfosHandler){
			processInstance = piih.getProcessInstance();
			ProcessEngineClient.getInstance().resumeProcessInstance(new ResumeInstanceHandler(processInstance));			
		}		
	}

}
