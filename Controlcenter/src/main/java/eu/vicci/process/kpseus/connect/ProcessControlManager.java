package eu.vicci.process.kpseus.connect;

import java.util.Map;

import eu.vicci.process.kpseus.connect.handlers.PauseInstanceHandler;
import eu.vicci.process.kpseus.connect.handlers.ResumeInstanceHandler;
import eu.vicci.process.kpseus.connect.handlers.StartInstanceHandler;
import eu.vicci.process.kpseus.connect.handlers.StopInstanceHandler;
import eu.vicci.process.kpseus.connect.observers.ProcessInstanceObserver;
import eu.vicci.process.kpseus.connect.subscribers.StateChangeHandler;
import eu.vicci.process.model.sofiainstance.DataTypeInstance;
import eu.vicci.process.model.sofiainstance.ProcessInstance;
import eu.vicci.process.model.sofiainstance.State;

/**
 * This class allows to send control commands to the server. The {@link ServerConnector} has to be instantiated before.
 * The controls base on the {@link ProcessInstance#getInstanceId()} instead of the {@link ProcessInstance#getProcessInstanceID()}!
 * @author Manuel
 *
 */
public class ProcessControlManager {
	private static ProcessControlManager instance;
	
	private ProcessControlManager(){
			
	}
	
	public static synchronized ProcessControlManager getInstance(){
		if (instance == null){
			instance = new ProcessControlManager();
		}
		return instance;
	}
	
	/**
	 * starts a {@link ProcessInstance} with these input values
	 * @param pi - the {@link ProcessInstance}
	 * @param parameters - a map for input values
	 */
	public void start(ProcessInstance pi, Map<String, DataTypeInstance> parameters, ProcessInstanceObserver pio){
		StartInstanceHandler sih = new StartInstanceHandler(pi);
		sih.addHandlerFinishedListener(pio);
		if (ServerConnector.getInstance() != null){
			ServerConnector.getInstance().getProcessEngineClient().startProcessInstance(sih, parameters, null);
		}
	}

	/**
	 * stops a running {@link ProcessInstance}
	 * @param p - the {@link ProcessInstance} which shall be stopped
	 */
	public void stop(ProcessInstance p, ProcessInstanceObserver pio){
		StopInstanceHandler sih = new StopInstanceHandler(p);
		sih.addHandlerFinishedListener(pio);
		if (ServerConnector.getInstance() != null){
			ServerConnector.getInstance().getProcessEngineClient().stopProcessInstance(sih);
		}
	}
	
	/**
	 * pauses a running {@link ProcessInstance}
	 * @param p - the {@link ProcessInstance} which shall be stopped
	 */
	public void pause(ProcessInstance p, ProcessInstanceObserver pio){
		PauseInstanceHandler pih = new PauseInstanceHandler(p);
		pih.addHandlerFinishedListener(pio);
		if (ServerConnector.getInstance() != null && p.getExecutionState().equals(State.EXECUTING)){
			ServerConnector.getInstance().getProcessEngineClient().pauseProcessInstance(pih);
		}
	}
	
	/**
	 * resumes a paused {@link ProcessInstance}
	 * @param p - paused {@link ProcessInstance}
	 */
	public void resume(ProcessInstance p, ProcessInstanceObserver pio){
		ResumeInstanceHandler rih = new ResumeInstanceHandler(p);
		rih.addHandlerFinishedListener(pio);
		ServerConnector.getInstance().getProcessEngineClient().resumeProcessInstance(rih);
	}
	
	public void subscribe(ProcessInstanceObserver pio){
		StateChangeHandler sch = new StateChangeHandler();
		sch.addSubscribedMessageReceiver(pio);
		ServerConnector.getInstance().getProcessEngineClient().subscribeToStateChange(sch);
	}

}
