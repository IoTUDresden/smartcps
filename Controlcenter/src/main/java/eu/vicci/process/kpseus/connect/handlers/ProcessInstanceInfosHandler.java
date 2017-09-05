package eu.vicci.process.kpseus.connect.handlers;

import eu.vicci.process.engine.core.ReplyState;
import eu.vicci.process.model.sofiainstance.ProcessInstance;
import eu.vicci.process.model.sofiainstance.SofiaInstanceFactory;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.JSONProcessStepInstance;
import ws.wamp.jawampa.Reply;

public class ProcessInstanceInfosHandler extends AbstractClientHandler {
	private JSONProcessStepInstance processInstanceInfos;
	private ProcessInstance processInstance;
	
	private String processInstanceId = "";
	
	public ProcessInstanceInfosHandler(String processInstanceId){
		this.processInstanceId = processInstanceId;
	}

	@Override
	public void onNext(Reply t) {
		processInstanceInfos = convertFromJson(t.arguments().get(0), JSONProcessStepInstance.class);		
		processInstance = (ProcessInstance)processInstanceInfos.makeProcessStepInstance(SofiaInstanceFactory.eINSTANCE);
	}
	
	@Override
	public void onCompleted() {
		state = ReplyState.SUCCESS;
		super.onCompleted();
		informListeners(processInstance);
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
}
