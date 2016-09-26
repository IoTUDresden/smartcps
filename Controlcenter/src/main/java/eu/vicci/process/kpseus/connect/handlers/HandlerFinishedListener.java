package eu.vicci.process.kpseus.connect.handlers;

/**
 * Listener, to get informed, if a {@link AbstractClientHandler} has finished
 */
public interface HandlerFinishedListener {	
	
	/**
	 * This method is called, after a {@link AbstractClientHandler} has finished
	 * @param arg
	 */
	void onHandlerFinished(AbstractClientHandler handler, Object arg);

}
