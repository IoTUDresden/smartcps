package eu.vicci.process.kpseus.connect.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import ws.wamp.jawampa.Reply;

/**
 * This handler expects a {@link List} of {@link String} as argument in
 * {@link #onResult(Object)}. The List represents the IDs of all available
 * process models on the server. It will notify its {@link Observer} with this
 * list.
 * 
 * @author Manuel, Andre Kuehnert
 */
public class ModelListHandler extends AbstractClientHandler {
	private List<String> modelList;

	@Override
	public void onCompleted() {
		state = "succes";
		super.onCompleted();
		informListeners(modelList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onNext(Reply t) {
		modelList = convertFromJson(t.arguments().get(0), ArrayList.class);
	}

	public List<String> getModelList() {
		return modelList;
	}
}
