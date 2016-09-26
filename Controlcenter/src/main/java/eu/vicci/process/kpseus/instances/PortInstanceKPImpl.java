package eu.vicci.process.kpseus.instances;

import eu.vicci.process.model.sofia.Port;
import eu.vicci.process.model.sofiainstance.DataTypeInstance;
import eu.vicci.process.model.sofiainstance.MappingUtil;
import eu.vicci.process.model.sofiainstance.ProcessStepInstance;
import eu.vicci.process.model.sofiainstance.State;
import eu.vicci.process.model.sofiainstance.impl.PortInstanceImpl;

public class PortInstanceKPImpl extends PortInstanceImpl {
	public PortInstanceKPImpl(Port port, ProcessStepInstance psi){
		super();
		setPortType(port);
		setProcessStepInstance(psi);
	}
	
	public void deploy(MappingUtil mapper) {
		// TODO: implement this method
		
	}

	public boolean activate(DataTypeInstance parameter) {
		if (parameter == null){
			if (getTypeId() == null){
				return true;
			}
			throw new IllegalArgumentException("this instance needs a data type instance");
		}
		// TODO: check concrete data type
		setExecutionState(State.ACTIVE);
		return true;
	}
}
