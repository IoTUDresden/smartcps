package eu.vicci.process.kpseus.connect.subscribers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.vicci.process.model.sofia.EndDataPort;
import eu.vicci.process.model.sofia.Port;
import eu.vicci.process.model.sofia.StartDataPort;
import eu.vicci.process.model.util.messages.StateChangeMessage;
import eu.vicci.process.model.util.messages.core.IStateChangeMessage;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.IJSONDataPortInstance;
import eu.vicci.process.model.util.serialization.jsontypeinstances.core.IJSONTypeInstance;
import ws.wamp.jawampa.PubSubData;

public class StateChangeMessageHandler extends AbstractPubSubDataSubscriber {
	private static StateChangeMessageHandler instance = null;
	private List<IStateChangeMessage> events = new ArrayList<IStateChangeMessage>();
	private IStateChangeMessage payload;
	
	private StateChangeMessageHandler() {	}
	
	public static synchronized StateChangeMessageHandler getInstance() {
		if (instance == null)
			instance = new StateChangeMessageHandler();
		return instance;
	}	

	@Override
	public void onNext(PubSubData arg0) {
		payload = convertFromJson(arg0.arguments().get(0), StateChangeMessage.class);
		
		events.add(payload);	
		informReceiver(payload);
	}	
	
	public void addMessages(List<IStateChangeMessage> messages) {
		events.addAll(messages);
	}
	
	public String getPortValue(Port port, String processInstanceID, String messageID) {
		String result = "";
		long latest = 0;
		for (IStateChangeMessage event : events) {
			if (event.getTimeStamp() < latest || (messageID!=null && !messageID.equals(event.getMessageId())))
				continue;
			Map<String, IJSONDataPortInstance> ports = new HashMap<String, IJSONDataPortInstance>();

			if (port instanceof StartDataPort)
				ports = event.getStartDataPorts();
			if (port instanceof EndDataPort)
				ports = event.getEndDataPorts();
			for (String key : ports.keySet()) {
				if (key.equals(port.getId()) && event.getProcessInstanceId().equals(processInstanceID)) {
					IJSONTypeInstance typeInstance = ports.get(key).getDataTypeInstance();
					if (typeInstance != null && typeInstance.getValueString().length() > 0) {
						result = typeInstance.getValueString();
						latest = event.getTimeStamp();
					}
				}
			}
		}
		return result;
	}

	public List<IStateChangeMessage> getMessages(String processInstanceID) {
		ArrayList<IStateChangeMessage> result = new ArrayList<IStateChangeMessage>();
		ArrayList<String> messageIDs = new ArrayList<String>();
		for (IStateChangeMessage event : events) {
			if (event.getProcessInstanceId().equals(processInstanceID) && !messageIDs.contains(event.getMessageId())) {
				result.add(event);
				messageIDs.add(event.getMessageId());
			}
		}
		return result;
	}
}
