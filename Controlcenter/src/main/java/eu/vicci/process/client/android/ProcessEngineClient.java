package eu.vicci.process.client.android;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.emf.ecore.xmi.util.XMLProcessor;
import org.xml.sax.InputSource;

import android.content.Context;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.util.Log;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.process.engine.core.IProcessInfo;
import eu.vicci.process.engine.core.IProcessInstanceInfo;
import eu.vicci.process.kpseus.connect.handlers.DeployInstanceHandler;
import eu.vicci.process.kpseus.connect.handlers.DeployModelHandler;
import eu.vicci.process.kpseus.connect.handlers.ModelListHandler;
import eu.vicci.process.kpseus.connect.handlers.PauseInstanceHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessInfosHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessInstanceInfoListHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessInstanceInfosHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessInstanceInfosReducedHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessListHandler;
import eu.vicci.process.kpseus.connect.handlers.ResumeInstanceHandler;
import eu.vicci.process.kpseus.connect.handlers.RetrieveRecentStateChangesHandler;
import eu.vicci.process.kpseus.connect.handlers.SemanticPersonHandler;
import eu.vicci.process.kpseus.connect.handlers.StartInstanceHandler;
import eu.vicci.process.kpseus.connect.handlers.StartProcessHandler;
import eu.vicci.process.kpseus.connect.handlers.StopInstanceHandler;
import eu.vicci.process.kpseus.connect.handlers.UploadModelHandler;
import eu.vicci.process.kpseus.connect.subscribers.HumanTaskRequestHandler;
import eu.vicci.process.kpseus.connect.subscribers.HumanTaskResponseHandler;
import eu.vicci.process.kpseus.connect.subscribers.StateChangeHandler;
import eu.vicci.process.kpseus.connect.subscribers.StateChangeMessageHandler;
import eu.vicci.process.model.sofia.Process;
import eu.vicci.process.model.sofia.SofiaPackage;
import eu.vicci.process.model.sofiainstance.DataTypeInstance;
import eu.vicci.process.model.sofiainstance.SofiaInstancePackage;
import eu.vicci.process.model.util.ProcessStartRequest;
import eu.vicci.process.model.util.UploadModelRequest;
import eu.vicci.process.model.util.configuration.RpcId;
import eu.vicci.process.model.util.configuration.TopicId;
import eu.vicci.process.model.util.messages.core.IHumanTaskResponse;
import eu.vicci.process.model.util.serialization.jsontypeinstances.JSONTypeInstanceSerializer;
import eu.vicci.process.model.util.serialization.jsontypeinstances.core.IJSONTypeInstance;
import rx.functions.Action1;
import ws.wamp.jawampa.WampClient;
import ws.wamp.jawampa.WampClient.State;
import ws.wamp.jawampa.WampClientBuilder;
import ws.wamp.jawampa.transport.netty.NettyWampClientConnectorProvider;

// FIXME remove getter and setters for the handlers
// One rpc handler should not be used across the app, this will lead to concurrent problems
public class ProcessEngineClient {
	public static final String DEFAULT_REALM = "vicciRealm";
	public static final String DEFAULT_NAMESPACE = "vicciWs";
	public static final int DEFAULT_RECONNECT_INT_SEC = 3;
	
	private static final String PREFIX = "ws://";
	private String URL = null;	
	
	private static ProcessEngineClient pec;
	private String ip;
	private String port;
	private WampClient wampClient;
	private boolean connected = false;
	private ObservableSession os = null;
	
	private ProcessListHandler processListHandler = null;
	private ProcessInstanceInfoListHandler processInstanceInfoListHandler = null;
	
	private Map<String, Process> localProcesses;

	public static synchronized ProcessEngineClient getInstance() {
		if (pec == null) {
			pec = new ProcessEngineClient();
		}
		return pec;
	}

	private ProcessEngineClient() {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("sofia", new XMIResourceFactoryImpl());
		m.put("sofiainstance", new XMIResourceFactoryImpl());
		SofiaPackage.eINSTANCE.eClass();
		SofiaInstancePackage.eINSTANCE.eClass();
		os = new ObservableSession();
		localProcesses = new HashMap<String, Process>();
	}

	public boolean connect() {
		if(connected){
			Log.e(getClass().getSimpleName(), "already connected. please disconnect first");
			return false;
		}
			
		URL = PREFIX + PreferenceManager.getDefaultSharedPreferences(SmartCPS_Impl.getAppContext()).getString("prefProcess", "192.168.56.1:8081");
		URL = URL + "/" + DEFAULT_NAMESPACE;
		NettyWampClientConnectorProvider connectorProvider = new NettyWampClientConnectorProvider();
		WampClientBuilder clientBuilder = new WampClientBuilder();
		Log.d(getClass().getSimpleName(), "connecting to process engine...");
		
		try {
			clientBuilder
			.withConnectorProvider(connectorProvider)
			.withRealm(DEFAULT_REALM)
			.withUri(URL)
			.withInfiniteReconnects()
			.withReconnectInterval(3, TimeUnit.SECONDS);
			
			wampClient = clientBuilder.build();
			addObservableSessionToClient();			
			wampClient.open();
		} catch (Exception e) {
			Log.wtf(ProcessEngineClient.class.getSimpleName(), e);
			return false;
		}		
		
		return true;
	}
	
	private void addObservableSessionToClient(){
		wampClient.statusChanged().subscribe(new Action1<State>() {
			@Override
			public void call(State state) {
				if(state instanceof WampClient.ConnectedState){
					connected = true;
					Log.d(ProcessEngineClient.class.getSimpleName(), "connected to: " + URL);
					os.onOpen();
				}
				if(state instanceof WampClient.DisconnectedState){
					connected = false;
					Log.d(ProcessEngineClient.class.getSimpleName(), "disconnected from: " + URL);
					os.onClose();				
				}
			}
		});
	}

	public boolean disconnect() {
		if(!connected){
			Log.e(getClass().getSimpleName(), "Client not connected. Cant disconnect");
			return false;
		}
			
		wampClient.close();
		return true;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public boolean isConnected() {
		return connected;
	}

	public ObservableSession getOs() {
		return os;
	}

	/**
	 * Upload from local assets
	 * 
	 * @param filename
	 * @param context
	 * @param umh
	 */
	public void uploadProcessDefinition(String filename, Context context, UploadModelHandler umh) {
		XMLResourceImpl res = new XMLResourceImpl();
		AssetManager am = context.getAssets();

		try {
			res.load(new InputSource(am.open(filename)), null);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Process model = (Process) res.getContents().get(0);

		XMLResourceImpl resource2 = new XMLResourceImpl();
		XMLProcessor processor = new XMLProcessor();

		String text = "";

		resource2.getContents().add(model);

		try {
			text = processor.saveToString(resource2, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		UploadModelRequest input = new UploadModelRequest(model.getId(), text, false);

		umh.setProcessId(model.getId());
		wampClient.call(RpcId.UPLOAD_MODEL, input).subscribe(umh);
	}

	/**
	 * Upload from file system
	 * 
	 * @param path
	 * @param umh
	 */

	public void uploadProcessDefinition(String path, UploadModelHandler umh) {
		XMLResourceImpl res = new XMLResourceImpl();

		try {
			res.load(new BufferedInputStream(new FileInputStream(path)), null);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Process model = (Process) res.getContents().get(0);

		XMLResourceImpl resource2 = new XMLResourceImpl();
		XMLProcessor processor = new XMLProcessor();

		String text = "";

		resource2.getContents().add(model);

		try {
			text = processor.saveToString(resource2, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		UploadModelRequest input = new UploadModelRequest(model.getId(), text, false);

		umh.setProcessId(model.getId());
		wampClient.call(RpcId.UPLOAD_MODEL, input).subscribe(umh);
	}

	public void deployProcess(DeployModelHandler dmh) {
		wampClient.call(RpcId.DEPLOY_MODEL, dmh.getProcessId()).subscribe(dmh);
	}

	public void deployProcessInstance(DeployInstanceHandler dih) {
		wampClient.call(RpcId.DEPLOY_INSTANCE, dih.getProcessId()).subscribe(dih);
	}

	public String configureProcessInstance(String processInstanceId, String configuration) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public void startProcessInstance(StartInstanceHandler sih, Map<String, DataTypeInstance> inputParameters, String processId) {
		Map<String, IJSONTypeInstance> ports = new HashMap<String, IJSONTypeInstance>();

		if (inputParameters != null) {

			for (String portName : inputParameters.keySet()) {
				DataTypeInstance dti = inputParameters.get(portName);
				IJSONTypeInstance json = JSONTypeInstanceSerializer.makeJSONTypeInstance(dti);
				ports.put(portName, json);
			}

		} else {
			ports = null;
		}

		ProcessStartRequest input = new ProcessStartRequest(sih.getProcessInstanceId(), processId, ports, false);
		wampClient.call(RpcId.START_INSTANCE, input).subscribe(sih);
	}

	public void stopProcessInstance(StopInstanceHandler stih) {
		wampClient.call(RpcId.STOP_INSTANCE, stih.getInstanceId()).subscribe(stih);
	}

	public void pauseProcessInstance(PauseInstanceHandler pih) {
		wampClient.call(RpcId.PAUSE_INSTANCE, pih.getInstanceId()).subscribe(pih);
	}

	public void resumeProcessInstance(ResumeInstanceHandler rih) {
		wampClient.call(RpcId.RESUME_INSTANCE, rih.getProcessInstance().getInstanceId()).subscribe(rih);
	}

	/**
	 * List of process models uploaded to the server (only in file system)
	 * 
	 * @param mlh
	 */
	public void listUploadedProcessDefinitions(ModelListHandler mlh) {
		wampClient.call(RpcId.LIST_MODELS, "").subscribe(mlh);
	}

	/**
	 * List of process models deployed on the server (loaded into memory)
	 * 
	 * @param plh
	 */
	public void listDeployedProcesses(ProcessListHandler plh) {	
		processListHandler = plh;
		wampClient.call(RpcId.PROCESS_LIST, "").subscribe(plh);
	}

	public void getProcessInfos(ProcessInfosHandler pih) {
		wampClient.call(RpcId.PROCESS_INFOS, pih.getProcessId()).subscribe(pih);
	}

	public void getProcessInstanceInfos(ProcessInstanceInfosHandler piih) {
		wampClient.call(RpcId.PROCESS_INSTANCE_INFOS, piih.getProcessInstanceId()).subscribe(piih);
	}

	public void getProcessInstanceInfosReduced(ProcessInstanceInfosReducedHandler piirh) {
		wampClient.call(RpcId.PROCESS_INSTANCE_INFOS_REDUCED, piirh.getInstanceId()).subscribe(piirh);
	}

	/**
	 * Calls the RPC with the given handler. After the handler isCompleted, it can not be reused!
	 * @param pilh
	 */
	public void listProcessInstances(ProcessInstanceInfoListHandler pilh) {
		processInstanceInfoListHandler = pilh;
		wampClient.call(RpcId.PROCESS_INSTANCE_LIST, "").subscribe(pilh);
	}
	
	/**
	 * Retreives all Semantic persons, if proteus holds such informations
	 * @param handler
	 */
	public void listSemanticPersons(SemanticPersonHandler handler){
		wampClient.call(RpcId.SEMANTIC_PERSONS, "").subscribe(handler);
	}

	public void uploadProcess(Process process, UploadModelHandler umh) {
		Process model = process;

		String processId = model.getId();
		umh.setProcessId(processId);

		XMLResourceImpl resource2 = new XMLResourceImpl();
		XMLProcessor processor = new XMLProcessor();

		String text = "";

		resource2.getContents().add(model);

		try {
			text = processor.saveToString(resource2, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		UploadModelRequest input = new UploadModelRequest(processId, text, false);
		wampClient.call(RpcId.UPLOAD_MODEL, input).subscribe(umh);
	}

	public WampClient getWampClient() {
		return wampClient;
	}
	
	public void publishHumanTaskResponse(IHumanTaskResponse response){
		wampClient.publish(TopicId.HUMAN_TASK_RESP, response);
	}

	public void publishMessage(String topic, Object payload) {
		wampClient.publish(topic, payload);
	}

	public void subscribeToStateChange(StateChangeHandler sch) {
		wampClient.makeSubscription(TopicId.STATE_CHANGE).subscribe(sch);
	}
	
	public void subscribeToStateChange(StateChangeMessageHandler scmh) {
		wampClient.makeSubscription(TopicId.STATE_CHANGE).subscribe(scmh);
	}
	
	public void subscribeToHumanTask(HumanTaskRequestHandler htmh){
		wampClient.makeSubscription(TopicId.HUMAN_TASK_REQ).subscribe(htmh);
	}
	
	/**
	 * Subscribe to {@link IHumanTaskResponse}s to check if a request was already handled
	 * @param htrh
	 */
	public void subscribeToHumanTaskResponse(HumanTaskResponseHandler htrh){
		wampClient.makeSubscription(TopicId.HUMAN_TASK_RESP).subscribe(htrh);
	}
	
	public void getRecentStateChanges(RetrieveRecentStateChangesHandler handler){
		wampClient.call(RpcId.RECENT_STATE_CHANGES, "").subscribe(handler);
	}

	/**
	 * Gets the last received processes
	 * @return
	 */
	public List<IProcessInfo> getProcesslist() {
		if(processListHandler != null)
			processListHandler.getProcessInfos();
		return null;
	}

	public List<IProcessInstanceInfo> getProcessInstanceList() {
		if(processInstanceInfoListHandler != null)
			return processInstanceInfoListHandler.getProcessInstanceList();
		return null;
	}

	public void startProcess(StartProcessHandler psh, Map<String, DataTypeInstance> inputData) {
		psh.setInputParameters(inputData);
		psh.startInstance();
	}

	public Map<String, Process> getLocalProcesses() {
		return localProcesses;
	}

	public void setLocalProcesses(Map<String, Process> localProcesses) {
		this.localProcesses = localProcesses;
	}
}