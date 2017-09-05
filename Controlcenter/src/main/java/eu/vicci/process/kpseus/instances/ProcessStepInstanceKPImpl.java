package eu.vicci.process.kpseus.instances;

import eu.vicci.process.model.sofia.ProcessStep;
import eu.vicci.process.model.sofiainstance.Configuration;
import eu.vicci.process.model.sofiainstance.MappingUtil;
import eu.vicci.process.model.sofiainstance.State;
import eu.vicci.process.model.sofiainstance.impl.ProcessStepInstanceImpl;

public class ProcessStepInstanceKPImpl extends ProcessStepInstanceImpl {

	
	public ProcessStepInstanceKPImpl(ProcessStep process){
		super();
		setProcessStepType(process);
		setExecutionState(State.UNDEPLOYED);
	}
	
	public void kill(){
		switch(getExecutionState()){
		case WAITING:
		case EXECUTING:
		case PAUSED:
			setExecutionState(State.KILLED);
			break;
		default:
			break;
		}
	}
	
	public void deploy(MappingUtil mapper){
		setExecutionState(State.INACTIVE);
	}
	
	public boolean execute(){
		return false;
	}
	
	public void stop(){
		switch(getExecutionState()){
		case WAITING:
		case EXECUTING:
		case PAUSED:
			setExecutionState(State.STOPPED);
			break;
		default:
			break;
		}
	}
	
	public void pause(){
		switch(getExecutionState()){
		case WAITING:
		case EXECUTING:
			setExecutionState(State.PAUSED);
			break;
		default:
			break;
		}
	}
	
	public String configure(Configuration config){
		return null;
	}
	
	public void escalate(){
		setExecutionState(State.ESCALATED);
	}

}
