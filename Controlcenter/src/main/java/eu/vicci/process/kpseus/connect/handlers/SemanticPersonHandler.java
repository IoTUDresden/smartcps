package eu.vicci.process.kpseus.connect.handlers;

import java.util.ArrayList;
import java.util.List;

import eu.vicci.process.model.util.messages.core.SemanticPerson;
import ws.wamp.jawampa.Reply;

public final class SemanticPersonHandler extends AbstractClientHandler {
	private List<SemanticPerson> received;

	@SuppressWarnings("unchecked")
	@Override
	public void onNext(Reply arg0) {
		received = convertFromJsonToCollectionType(arg0.arguments().get(0), 
				ArrayList.class, SemanticPerson.class);				
	}
	
	@Override
	public void onCompleted() {
		super.onCompleted();
		informListeners(received);
	}
}
