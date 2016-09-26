package eu.vicci.process.kpseus.connect.handlers;

import eu.vicci.process.model.sofiainstance.ProcessInstance;
import ws.wamp.jawampa.Reply;

public class ResumeInstanceHandler extends AbstractClientHandler{
	private ProcessInstance processInstance;
	private String instanceId;
	
	public ResumeInstanceHandler(ProcessInstance processInstance){
		this.processInstance = processInstance;
	}
	
	public ResumeInstanceHandler(String instanceId){
		this.instanceId = instanceId;
	}

	@Override
	public void onNext(Reply t) {
		state = t.arguments().get(0).asText();		
	}
	
	@Override
	public void onCompleted() {
		super.onCompleted();
		informListeners(instanceId);
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

}
