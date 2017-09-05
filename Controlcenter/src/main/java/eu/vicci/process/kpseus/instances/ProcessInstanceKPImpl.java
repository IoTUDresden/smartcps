package eu.vicci.process.kpseus.instances;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import eu.vicci.process.model.sofia.ProcessStep;
import eu.vicci.process.model.sofiainstance.DataTypeInstance;
import eu.vicci.process.model.sofiainstance.ProcessInstance;
import eu.vicci.process.model.sofiainstance.ProcessStepInstance;
import eu.vicci.process.model.sofiainstance.SofiaInstancePackage;
import eu.vicci.process.model.sofiainstance.State;

public class ProcessInstanceKPImpl extends ProcessStepInstanceKPImpl implements
		ProcessInstance {

	public ProcessInstanceKPImpl(ProcessStep value) {
		super(value);
	}

	@Override
	public boolean start(Map<String, DataTypeInstance> parameters) {
		for (ProcessStepInstance psi : getSubSteps()){
			psi.setExecutionState(State.WAITING);
		}
		return true;
	}

	@Override
	protected EClass eStaticClass() {
		return SofiaInstancePackage.Literals.PROCESS_INSTANCE;
	}

	@Override
	public String getRunningForInstanceId() {
		return null;
	}

	@Override
	public void setRunningForInstanceId(String s) {

	}

}
