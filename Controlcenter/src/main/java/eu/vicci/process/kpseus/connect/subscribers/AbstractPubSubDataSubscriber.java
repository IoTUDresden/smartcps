package eu.vicci.process.kpseus.connect.subscribers;

import java.util.ArrayList;
import java.util.List;

import ws.wamp.jawampa.PubSubData;

public abstract class AbstractPubSubDataSubscriber extends AbstractSubscriber<PubSubData> {
	
	private List<SubscribedMessageReceiver> receivers = new ArrayList<SubscribedMessageReceiver>();

	/**
	 * onCompleted is called, when the connection to the router is closed.
	 * Otherwise only {@link #onNext(PubSubData)} is called after each message
	 */
	@Override
	public void onCompleted() {	
		//only after disconnect called!
	}
	
	protected void informReceiver(Object arg){
		for (SubscribedMessageReceiver receiver : receivers) {
			receiver.onSubscribedMessage(this, arg);
		}
	}
	
	public void addSubscribedMessageReceiver(SubscribedMessageReceiver receiver){
		receivers.add(receiver);
	}

}
