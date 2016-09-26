package eu.vicci.process.kpseus.connect.subscribers;

/**
 * Receives updates for received messages on {@link AbstractSubscriber}
 */
public interface SubscribedMessageReceiver {
	
	/**
	 * This method is called if a message was received, to which this receiver is subscribed to
	 * @param subscriber
	 * @param arg
	 */
	void onSubscribedMessage(AbstractPubSubDataSubscriber subscriber, Object arg);

}
