package eu.vicci.process.kpseus.connect.observers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.model.sofia.Process;

/**
 * This class is necessary, when the {@link ProcessEngineClient} wants to return
 * a {@link List} of IDs of {@link Process}es.
 * 
 * @author Manuel
 * 
 */
public abstract class MapObserver implements Observer {
	/**
	 * This method is called from network-side to inform about a list of process
	 * IDs. The class type of the argument and its elements is checked by
	 * {@link #update(Observable, Object)} before.
	 * 
	 * @param o
	 * @param arg
	 *            - list of process IDs
	 */
	public abstract void updateObserver(Observable o, Map<String, String> arg);

	@Override
	public final void update(Observable o, Object arg) {
		if (arg == null) {
			updateObserver(o, null);
		}
		if (arg instanceof Map) {
			HashMap<String, String> ipt = new HashMap<String, String>();
			for (Object e : ((Map) arg).entrySet()) {
				if (e instanceof Entry<?, ?>) {
					Entry<?,?> me = (Entry<?,?>) e;
					if ((me.getKey() instanceof String)
							&& (me.getValue() instanceof String)) {
						ipt.put((String) me.getKey(), (String) me.getValue());
					} else {
						return;
					}
				}
			}
			updateObserver(o, ipt);
		}

	}
}
