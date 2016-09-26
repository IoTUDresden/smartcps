package eu.vicci.process.client.android;

import java.util.Observable;

import eu.vicci.process.kpseus.connect.subscribers.HumanTaskRequestHandler;
import eu.vicci.process.kpseus.connect.subscribers.HumanTaskResponseHandler;
import eu.vicci.process.kpseus.connect.subscribers.StateChangeMessageHandler;

public class ObservableSession extends Observable implements ConnectionHandler {
	
	@Override
	public void onClose() {
		triggerObservers();
	}

	@Override
	public void onOpen() {						
		HumanTaskRequestHandler htmh = new HumanTaskRequestHandler();
		HumanTaskResponseHandler htrh = new HumanTaskResponseHandler();
		StateChangeMessageHandler scmh = StateChangeMessageHandler.getInstance();
		ProcessEngineClient pec = ProcessEngineClient.getInstance();
		
		pec.subscribeToHumanTask(htmh);
		pec.subscribeToHumanTaskResponse(htrh);		
		pec.subscribeToStateChange(scmh);
		
		triggerObservers();		
	}
	
	private void triggerObservers() {
		setChanged();
		notifyObservers();
	}
}
