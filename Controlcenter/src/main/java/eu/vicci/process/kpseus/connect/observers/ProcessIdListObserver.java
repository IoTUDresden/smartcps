package eu.vicci.process.kpseus.connect.observers;

import java.util.List;
import java.util.Observable;

import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.handlers.HandlerFinishedListener;
import eu.vicci.process.model.sofia.Process;

/**
 * This class is necessary, when the {@link ProcessEngineClient} wants to return
 * a {@link List} of IDs of {@link Process}es.
 * 
 * @author Manuel
 * 
 */
public abstract class ProcessIdListObserver implements HandlerFinishedListener {
	/**
	 * This method is called from network-side to inform about a list of process
	 * IDs. The class type of the argument and its elements is checked by
	 * {@link #update(Observable, Object)} before.
	 * 
	 * @param o
	 * @param arg
	 *            - list of process IDs
	 */
	public abstract void updateObserver(AbstractClientHandler o, List<String> arg);
	
	@SuppressWarnings("unchecked")
	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		if (arg == null) {
			updateObserver(handler, null);
		}
		if (arg instanceof List<?>) {
			List<?> resultList = (List<?>) arg;
			//
			for (Object e : resultList) {
				if (!(e instanceof String)) {
					updateObserver(handler, null);
					return;
				}
			}
			updateObserver(handler, (List<String>)resultList);
		}
		
	}
}
