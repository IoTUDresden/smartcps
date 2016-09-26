package eu.vicci.process.kpseus.connect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;

import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.observers.MapObserver;
import eu.vicci.process.kpseus.connect.observers.ProcessIdListObserver;
import eu.vicci.process.kpseus.connect.observers.ProcessInstanceObserver;
import eu.vicci.process.kpseus.connect.observers.ProcessObserver;
import eu.vicci.process.kpseus.connect.subscribers.AbstractPubSubDataSubscriber;
import eu.vicci.process.kpseus.instances.ProcessInstanceKPImpl;
import eu.vicci.process.model.sofia.Process;
import eu.vicci.process.model.sofiainstance.ProcessInstance;
import eu.vicci.process.model.sofiainstance.State;

public class DatabaseDummy {
	private static DatabaseDummy instance;
	private Map<String, Process> processes;
	private Map<String, ProcessInstance> instances;
	private Map<String, State> instanceStates;
	
	public static DatabaseDummy getInstance(){
		if (instance == null){
			instance = new DatabaseDummy();
		}
		return instance;
	}
	
	private DatabaseDummy(){
		processes =	new HashMap<String, Process>();
		instances = new HashMap<String, ProcessInstance>();
		instanceStates = new HashMap<String, State>();
	}

	public Map<String, Process> getProcesses(){
		return processes;
	}
	
	public Map<String, ProcessInstance> getProcessInstances(){
		return instances;
	}

	public void init(){
		final ProcessObserver po = new ProcessObserver() {
			@Override
			public void updateObserver(AbstractClientHandler o, Process arg) {
				getProcesses().put(arg.getId(), arg);
			}
		};
		ProcessIdListObserver pilo = new ProcessIdListObserver() {		
			@Override
			public void updateObserver(AbstractClientHandler o, List<String> arg) {
				for (String s: arg){
					ProcessDeployManager.getInstance().getProcess(s, po);
				}
				
			}
		};
		ProcessDeployManager.getInstance().getProcessIds(pilo);
		MapObserver mo = new MapObserver() {
			
			@Override
			public void updateObserver(Observable o, Map<String, String> arg) {
				for (Entry<String, String> e : arg.entrySet()){
					Process p = getProcesses().get(e.getValue());
					ProcessInstance pi = new ProcessInstanceKPImpl(p);
					pi.setProcessInstanceID(e.getKey());
					getProcessInstances().put(pi.getProcessInstanceID(), pi);
				}
				
			}
		};
		//ProcessDeployManager.getInstance().getInstanceProcessTable(mo);
		ProcessInstanceObserver pio = new ProcessInstanceObserver() {
			@Override
			public void updateObserver(AbstractClientHandler handler, ProcessInstance arg) {
				getProcessInstances().put(arg.getProcessInstanceID(), arg);				
			}

			@Override
			public void updateObserver(AbstractPubSubDataSubscriber subscriber, ProcessInstance arg) {
				getProcessInstances().put(arg.getProcessInstanceID(), arg);
				
			}
		};
		ProcessControlManager.getInstance().subscribe(pio);
	}
}
