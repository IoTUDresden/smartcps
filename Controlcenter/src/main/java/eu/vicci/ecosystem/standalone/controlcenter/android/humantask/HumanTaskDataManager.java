package eu.vicci.ecosystem.standalone.controlcenter.android.humantask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.SettingsActivity;
import eu.vicci.process.model.util.messages.core.IHumanTaskMessage;
import eu.vicci.process.model.util.messages.core.IHumanTaskRequest;
import eu.vicci.process.model.util.messages.core.IHumanTaskResponse;

/**
 * The HumanTaskDataManager holds all {@link IHumanTaskRequest}s
 */
public class HumanTaskDataManager {
	public static final String HUMAN_TASK_ID = "humantaskindex";
	public static final String HUMAN_TASK_MESSAGE_TYPE = "humantaskmessagetype";
	
	public static final String HUMAN_TASK_RESPONSE = "humantaskrepsonse";
	public static final String HUMAN_TASK_REQUEST = "humantaskrequest";
	
	private List<HumanTaskDataManagerListener> listeners = new ArrayList<HumanTaskDataManagerListener>();
	private Map<String, IHumanTaskRequest> requests = new HashMap<String, IHumanTaskRequest>();
	private Map<String, IHumanTaskResponse> response = new HashMap<String, IHumanTaskResponse>();
	private HashMap<String, Integer> notificationIds = new HashMap<String, Integer>(); 	
	
	private static HumanTaskDataManager instance;

	private HumanTaskDataManager() {

		// mocks for demo
		if (isMockingEnabled()) {
			HTMockUtil.addHTMocks(requests);
			HTMockUtil.addHTResponseMocks(response);
		}
	}
	
	private boolean isMockingEnabled(){
		Context context = SmartCPS_Impl.getAppContext();
		if(context == null)
			return false;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean(SettingsActivity.KEY_PREF_MOCKING, false);
	}

	public static synchronized HumanTaskDataManager getInstance() {
		if (instance == null)
			instance = new HumanTaskDataManager();
		return instance;
	}

	/**
	 * @deprecated should not be used anymore - if you want a request by its id
	 *             use {@link #getHumanTaskRequestByInstanceId(String)}
	 * @return
	 */
	@Deprecated
	public Map<String, IHumanTaskRequest> getRequests() {
		return requests;
	}

	/**
	 * Gets a HumanTask by its instance id
	 * 
	 * @param instanceId
	 * @return
	 */
	public IHumanTaskRequest getHumanTaskRequestByInstanceId(String instanceId) {
		return requests.get(instanceId);
	}

	/**
	 * Gets a HumanTask by its instance id
	 * 
	 * @param instanceId
	 * @return
	 */
	public IHumanTaskResponse getHumanTaskResponseByInstanceId(String instanceId) {
		return response.get(instanceId);
	}
	/**
	 * Informs this manager, if a request was handled. The specific request is
	 * removed from the manager and all listeners will be informed.
	 * 
	 * @param response
	 */
	public void humanTaskHandled(IHumanTaskResponse response) {
		IHumanTaskRequest old = requests.remove(response.getHumanTaskInstanceId());
		if (old != null)
			informListenersForRemove(old);
	}

	/**
	 * Adds an {@link IHumanTaskRequest}. Listeners will be informed about
	 * adding. The same request cannot be added twice.
	 * 
	 * @param humanTaskRequest
	 */
	public void addHumanTask(IHumanTaskRequest humanTaskRequest) {
		// human task is already in this manager
		if (requests.containsKey(humanTaskRequest.getHumanTaskInstanceId()))
			return;
		requests.put(humanTaskRequest.getHumanTaskInstanceId(), humanTaskRequest);
		informListenersForAdd(humanTaskRequest);
	}
	

	/**
	 * Gets all {@link IHumanTaskRequest}s as list
	 * 
	 * @return
	 */
	public List<IHumanTaskRequest> getRequestsList() {
		ArrayList<IHumanTaskRequest> result = new ArrayList<IHumanTaskRequest>();
		result.addAll(requests.values());
		return result;
	}
	/**
	 * Gets all {@link IHumanTaskResponse}s as list
	 * 
	 * @return
	 */
	public List<IHumanTaskResponse> getResponseList() {
		ArrayList<IHumanTaskResponse> result = new ArrayList<IHumanTaskResponse>();
		result.addAll(response.values());
		return result;
	}
	/**
	 * Adds an {@link HumanTaskDataManagerListener} to this manager.
	 * 
	 * @param listener
	 */
	public void addHumanTaskDataManagerListener(HumanTaskDataManagerListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes the {@link HumanTaskDataManagerListener} from this manager.
	 * 
	 * @param listener
	 */
	public void removeHumanTaskDataManagerListener(HumanTaskDataManagerListener listener) {
		listeners.remove(listener);
	}
	
	public Integer createNotificationId(IHumanTaskMessage message){
		if(!notificationIds.containsKey(message.getHumanTaskInstanceId()))
			notificationIds.put(message.getHumanTaskInstanceId(), Integer.valueOf(message.hashCode()));
		return notificationIds.get(message.getHumanTaskInstanceId());		
	}
	
	public Integer getNotificationId(IHumanTaskMessage message){
		return notificationIds.remove(message.getHumanTaskInstanceId());		
	}

	private void informListenersForAdd(IHumanTaskRequest request) {
		for (HumanTaskDataManagerListener listener : listeners) {
			listener.onHumanTaskAdded(request);
		}
	}

	private void informListenersForRemove(IHumanTaskRequest request) {
		for (HumanTaskDataManagerListener listener : listeners) {
			listener.onHumanTaskHandledByOther(request);
		}
	}	
}
