package eu.vicci.process.kpseus.connect.handlers;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

import eu.vicci.process.engine.core.IProcessInfo;
import eu.vicci.process.engine.core.ReplyState;
import eu.vicci.process.model.util.ProcessInfo;
import ws.wamp.jawampa.Reply;

public class ProcessListHandler extends AbstractClientHandler {
	private List<IProcessInfo> processInfos;


	@SuppressWarnings("unchecked")
	@Override
	public void onNext(Reply t) {
		processInfos = (List<IProcessInfo>) unsafeConvertFromJson(t.arguments().get(0), new TypeReference<List<ProcessInfo>>() {});
	}
	
	@Override
	public void onCompleted() {
		state = ReplyState.SUCCESS;
		super.onCompleted();
		informListeners(processInfos);
	}

	public List<IProcessInfo> getProcessInfos() {
		return processInfos;
	}
}
