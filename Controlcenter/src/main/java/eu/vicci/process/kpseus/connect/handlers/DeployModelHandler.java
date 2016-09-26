package eu.vicci.process.kpseus.connect.handlers;

import ws.wamp.jawampa.Reply;

public class DeployModelHandler extends AbstractClientHandler {
	
	private String processId = "";
	
	public DeployModelHandler(String processId){
		this.processId = processId;
	}
	
	@Override
	public void onNext(Reply arg0) {
		state = arg0.arguments().get(0).asText();
	}
	
	@Override
	public void onCompleted() {
		super.onCompleted();
		informListeners(processId);
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

}
