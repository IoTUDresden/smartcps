package eu.vicci.process.kpseus.connect.handlers;

import java.util.Map;

import eu.vicci.process.model.util.configuration.ReplyState;
import ws.wamp.jawampa.Reply;

public class ProcessInstanceInfosReducedHandler extends AbstractClientHandler {
	private Map<String,String> processStepInstances;
	private String instanceId = "";
	
	public ProcessInstanceInfosReducedHandler(String instanceId) {
		this.instanceId = instanceId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onNext(Reply t) {
		processStepInstances = convertFromJson(t.arguments().get(0), Map.class);		
	}
	
	@Override
	public void onCompleted() {
		state = ReplyState.SUCCESS;
		super.onCompleted();
		informListeners(processStepInstances);
	}

	public Map<String,String> getProcessStepInstances() {
		return processStepInstances;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
}
