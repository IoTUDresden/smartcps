package eu.vicci.process.kpseus.connect.handlers;

import java.util.Map;

import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.model.sofiainstance.DataTypeInstance;
import ws.wamp.jawampa.Reply;

public class StartProcessHandler extends AbstractClientHandler implements HandlerFinishedListener {
	private String processId;
	private String processInstanceId;
	private Map<String, DataTypeInstance> inputParameters;
	private DeployInstanceHandler dih;
	
	public StartProcessHandler(String processId) {
		this.processId = processId;
		dih = new DeployInstanceHandler(processId);
		this.setDih(dih);
		dih.addHandlerFinishedListener(this);
	}
	

	@Override
	public void onNext(Reply arg0) {
		// DO nothing ?
		
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Map<String, DataTypeInstance> getInputParameters() {
		return inputParameters;
	}

	public void setInputParameters(Map<String, DataTypeInstance> inputParameters) {
		this.inputParameters = inputParameters;
	}

	public DeployInstanceHandler getDih() {
		return dih;
	}

	public void setDih(DeployInstanceHandler dih) {
		this.dih = dih;
	}

	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		if (handler instanceof DeployInstanceHandler) {
			String instanceId = (String)arg;
			StartInstanceHandler sih = new StartInstanceHandler(instanceId);
			ProcessEngineClient.getInstance().startProcessInstance(sih, inputParameters, processId);
		}		
	}
	
	public void startInstance() {
		ProcessEngineClient.getInstance().deployProcessInstance(this.getDih());
	}

}
