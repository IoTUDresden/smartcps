package eu.vicci.process.kpseus.connect.observers;

import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.handlers.HandlerFinishedListener;
import eu.vicci.process.kpseus.connect.subscribers.AbstractPubSubDataSubscriber;
import eu.vicci.process.kpseus.connect.subscribers.SubscribedMessageReceiver;
import eu.vicci.process.model.sofiainstance.ProcessInstance;

/**
 * This class is necessary, when the {@link ProcessEngineClient} wants to return a {@link ProcessInstance}.
 * @author Manuel
 *
 */
public abstract class ProcessInstanceObserver implements HandlerFinishedListener, SubscribedMessageReceiver{
	/**
	 * This method is called from network-side to inform about a new or updated {@link ProcessInstance}.
	 * @param o
	 * @param arg - new or updated ProcessInstance
	 */
	public abstract void updateObserver(AbstractClientHandler handler, ProcessInstance arg);
	
	/**
	 * This method is called from network-side to inform about a new or updated {@link ProcessInstance}.
	 * @param o
	 * @param arg - new or updated ProcessInstance
	 */
	public abstract void updateObserver(AbstractPubSubDataSubscriber subscriber, ProcessInstance arg);
	
	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		if (arg == null){
			updateObserver(handler, null);
		}
		if (arg instanceof ProcessInstance){
			updateObserver(handler, (ProcessInstance)arg);
		}		
	}
	
	@Override
	public void onSubscribedMessage(AbstractPubSubDataSubscriber subscriber, Object arg) {
		if (arg == null){
			updateObserver(subscriber, null);
		}
		if (arg instanceof ProcessInstance){
			updateObserver(subscriber, (ProcessInstance)arg);
		}		
		
	}
}
