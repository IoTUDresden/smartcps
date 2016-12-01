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
    //must be an arrayList, so we can easily get the last message for a given process
	private List<IStateChangeMessage> events = new ArrayList<>();
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


    /**
     * Gets the last received message for the given process.
     * @param processInstanceId the instance id of the (executing) root process
     * @param processId the processId of the process within the root process or the root process self
     * @return null if no message was found
     */
    public IStateChangeMessage getLastStateMessageForProcess(String processInstanceId, String processId){
        //array lists backwards
        for (int i = events.size() - 1; i > -1 ; i--) {
            IStateChangeMessage message = events.get(i);
            if(message.getProcessInstanceId().equals(processInstanceId) && message.getProcessId().equals(processInstanceId))
                return message;
        }
        return  null;
    }

	/**
	 * Gets all received messages for the given processInstanceId (root process)
	 * @param processInstanceID
	 * @return
     */
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
