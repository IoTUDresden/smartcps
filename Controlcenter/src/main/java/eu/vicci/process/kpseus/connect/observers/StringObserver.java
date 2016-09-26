package eu.vicci.process.kpseus.connect.observers;

import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.handlers.HandlerFinishedListener;

/**
 * This class is necessary, when the {@link ProcessEngineClient} wants to return a new or updated {@link String}.<br>
 * FOR INTERN PACKAGE USAGE ONLY!
 * @author Manuel
 *
 */
public abstract class StringObserver implements HandlerFinishedListener {
	/**
	 * This method is called from network-side to inform about a returned String.
	 * @param o
	 * @param arg - new or updated Process
	 */
	public abstract void updateObserver(AbstractClientHandler o, String arg);
	
	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		if (arg == null){
			updateObserver(handler, null);
		}
		if (arg instanceof String) {
			updateObserver(handler, (String) arg);
		}		
	}

}
