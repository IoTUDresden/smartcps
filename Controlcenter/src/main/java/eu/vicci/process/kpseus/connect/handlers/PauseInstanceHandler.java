package eu.vicci.process.kpseus.connect.handlers;

import eu.vicci.process.model.sofiainstance.ProcessInstance;
import ws.wamp.jawampa.Reply;

public class PauseInstanceHandler extends AbstractClientHandler {
	private String instanceId = "";
	
	public PauseInstanceHandler(String instanceId) {
		this.instanceId = instanceId;
	}
	
	public PauseInstanceHandler(ProcessInstance processInstance) {
		this.instanceId = processInstance.getInstanceId();
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

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
}
