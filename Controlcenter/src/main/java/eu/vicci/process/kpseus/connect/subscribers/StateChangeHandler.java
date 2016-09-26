package eu.vicci.process.kpseus.connect.subscribers;

import eu.vicci.process.kpseus.connect.DatabaseDummy;
import eu.vicci.process.model.sofia.ProcessStep;
import eu.vicci.process.model.sofiainstance.ProcessInstance;
import eu.vicci.process.model.sofiainstance.ProcessStepInstance;
import eu.vicci.process.model.sofiainstance.impl.SofiaInstanceFactoryImpl;
import eu.vicci.process.model.util.messages.StateChangeMessage;
import eu.vicci.process.model.util.messages.core.IStateChangeMessage;
import ws.wamp.jawampa.PubSubData;

public class StateChangeHandler extends AbstractPubSubDataSubscriber {

	@Override
	public void onCompleted() {
		
	}

	@Override
	public void onNext(PubSubData arg0) {
		IStateChangeMessage sm = convertFromJson(arg0.arguments().get(0), StateChangeMessage.class);
		ProcessStepInstance psi = null;
		ProcessInstance pi = DatabaseDummy.getInstance().getProcessInstances().get(sm.getProcessInstanceId());
		if (pi == null){
			// TODO: request for ProcessInstance from Server
			return;
		}
		for (ProcessStepInstance psiCurrent : pi.getSubSteps()){
			if (psiCurrent.getInstanceId().equals(sm.getProcessInstanceId())){
				psi = psiCurrent;
			}
		}
		if (psi == null){
			psi = SofiaInstanceFactoryImpl.init().createProcessStepInstance();
			psi.setInstanceId(sm.getInstanceId());
			for (ProcessStep ps : pi.getProcessStepType().getParentstep().getSubSteps()){
				if (ps.getId().equals(sm.getModelId())){
					psi.setProcessStepType(ps);
				}
			}
			psi.setProcessInstanceID(sm.getProcessInstanceId());
		}
		psi.setExecutionState(sm.getState());
		informReceiver(pi);		
	}

}
