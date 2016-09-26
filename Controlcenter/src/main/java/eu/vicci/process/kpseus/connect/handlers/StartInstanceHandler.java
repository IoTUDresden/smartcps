package eu.vicci.process.kpseus.connect.handlers;

import eu.vicci.process.model.sofiainstance.ProcessInstance;
import ws.wamp.jawampa.Reply;

public class StartInstanceHandler extends AbstractClientHandler {
	private ProcessInstance processInstance;
	private String processInstanceId = "";
	
	public StartInstanceHandler(ProcessInstance processInstance){
		this.processInstance = processInstance;
	}
	
	public StartInstanceHandler(String processInstanceId){
		this.processInstanceId = processInstanceId;
	}

	@Override
	public void onNext(Reply t) {
		state = t.arguments().get(0).asText();		
	}
	
	@Override
	public void onCompleted() {
		super.onCompleted();
		informListeners(processInstanceId);
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

}
