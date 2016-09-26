package eu.vicci.process.kpseus.connect.subscribers;

import eu.vicci.ecosystem.standalone.controlcenter.android.humantask.HumanTaskDataManager;
import eu.vicci.process.model.util.messages.HumanTaskRequest;
import eu.vicci.process.model.util.messages.core.IHumanTaskRequest;
import ws.wamp.jawampa.PubSubData;

public class HumanTaskRequestHandler extends AbstractPubSubDataSubscriber {
	private IHumanTaskRequest request;

	@Override
	public void onNext(PubSubData arg0) {
		request = convertFromJson(arg0.arguments().get(0), HumanTaskRequest.class);		
		HumanTaskDataManager.getInstance().addHumanTask(request);
		informReceiver(request);
	}

}
