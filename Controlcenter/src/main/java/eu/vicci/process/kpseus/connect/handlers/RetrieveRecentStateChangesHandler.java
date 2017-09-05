package eu.vicci.process.kpseus.connect.handlers;

import java.util.List;

import eu.vicci.process.engine.core.ReplyState;
import eu.vicci.process.kpseus.connect.subscribers.StateChangeMessageHandler;
import eu.vicci.process.model.util.messages.StateChangeMessage;
import eu.vicci.process.model.util.messages.core.IStateChangeMessage;
import ws.wamp.jawampa.Reply;

public class RetrieveRecentStateChangesHandler extends AbstractClientHandler {
	private List<IStateChangeMessage> recentStateChanges;

	@SuppressWarnings("unchecked")
	@Override
	public void onNext(Reply t) {
		recentStateChanges = convertFromJsonToCollectionType(t.arguments().get(0), List.class, 
				StateChangeMessage.class);
	}
	
	@Override
	public void onCompleted() {
		state = ReplyState.SUCCESS;
		super.onCompleted();
		StateChangeMessageHandler.getInstance().addMessages(recentStateChanges);
	}

	public List<IStateChangeMessage> getRecentStateChanges() {
		return recentStateChanges;
	}
}
