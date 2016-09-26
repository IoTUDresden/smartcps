package eu.vicci.process.kpseus.connect.handlers;

import eu.vicci.process.model.sofia.Process;
import eu.vicci.process.model.sofiainstance.ProcessInstance;
import eu.vicci.process.model.util.configuration.ReplyState;
import ws.wamp.jawampa.Reply;

/**
 * This handler expects a {@link String} as argument in
 * {@link #onResult(Object)}. This String should be equal to an ID of a
 * {@link ProcessInstance}.
 * 
 * @author Manuel, Andre Kuehnert
 *
 */
public class DeployInstanceHandler extends AbstractClientHandler {
	//the received instance id
	private String instanceId = "";
	private String processId = "";
	
	public DeployInstanceHandler(Process process){
		this.processId = process.getId();
	}
	
	public DeployInstanceHandler(String processId){
		this.processId = processId;
	}

	@Override
	public void onNext(Reply arg0) {		
		instanceId = arg0.arguments().get(0).asText();
	}
	
	@Override
	public void onCompleted() {
		state = ReplyState.SUCCESS;
		super.onCompleted();
		informListeners(instanceId);
	}
	
	public String getInstanceId() {
		return instanceId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}	

}
