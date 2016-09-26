package eu.vicci.process.kpseus.connect.observers;

import java.util.Observable;
import java.util.Observer;

import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.handlers.HandlerFinishedListener;
import eu.vicci.process.model.sofia.Process;

/**
 * This class is necessary, when the {@link ProcessEngineClient} wants to return a new or updated {@link Process} 
 * @author Manuel
 *
 */
public abstract class ProcessObserver implements HandlerFinishedListener {
	/**
	 * This method is called from network-side to inform about a new or updated {@link Process}.
	 * @param o
	 * @param arg - new or updated Process
	 */
	public abstract void updateObserver(AbstractClientHandler o, Process arg);
	
	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		if (arg == null){
			updateObserver(handler, null);
		}
		if (arg instanceof Process) {
			updateObserver(handler, (Process) arg);
		}
		
	}
}
