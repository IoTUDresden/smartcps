package eu.vicci.process.kpseus.connect.handlers;

import java.util.ArrayList;
import java.util.List;

import eu.vicci.process.engine.core.IProcessInstanceInfo;
import eu.vicci.process.model.util.ProcessInstanceInfo;
import ws.wamp.jawampa.Reply;

public class ProcessInstanceInfoListHandler extends AbstractClientHandler{
	private List<IProcessInstanceInfo> processInstanceList = new ArrayList<IProcessInstanceInfo>();


	@SuppressWarnings("unchecked")
	@Override
	public void onNext(Reply arg0) {
		processInstanceList = convertFromJsonToCollectionType(arg0.arguments().get(0), 
				List.class, ProcessInstanceInfo.class);		
	}
	
	@Override
	public void onCompleted() {
		super.onCompleted();
		informListeners(processInstanceList);
	}

	public List<IProcessInstanceInfo> getProcessInstanceList() {
		return processInstanceList;
	}
}
