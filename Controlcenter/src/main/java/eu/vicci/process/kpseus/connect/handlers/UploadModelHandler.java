package eu.vicci.process.kpseus.connect.handlers;

import ws.wamp.jawampa.Reply;

public class UploadModelHandler extends AbstractClientHandler {	
	private String processId = "";

	@Override
	public void onNext(Reply t) {
		state = t.arguments().get(0).asText();
	}
	
	@Override
	public void onCompleted() {
		super.onCompleted();
		informListeners(processId);
	}
	
	public void setProcessId(String processId){
		this.processId = processId;
	}
	
	public String getProcessId(){
		return processId;
	} 
}
