package eu.vicci.process.kpseus.connect.subscribers;

import eu.vicci.ecosystem.standalone.controlcenter.android.humantask.HumanTaskDataManager;
import eu.vicci.process.model.util.messages.HumanTaskResponse;
import eu.vicci.process.model.util.messages.core.IHumanTaskResponse;
import ws.wamp.jawampa.PubSubData;

public class HumanTaskResponseHandler extends AbstractPubSubDataSubscriber {
	private IHumanTaskResponse response;

	@Override
	public void onNext(PubSubData arg0) {
		response = convertFromJson(arg0.arguments().get(0), HumanTaskResponse.class);
		HumanTaskDataManager.getInstance().humanTaskHandled(response);
		informReceiver(response);
	}

}
