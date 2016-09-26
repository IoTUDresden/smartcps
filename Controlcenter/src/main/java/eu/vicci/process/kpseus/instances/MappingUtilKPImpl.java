package eu.vicci.process.kpseus.instances;

import java.util.Map;

import eu.vicci.process.model.sofia.DataType;
import eu.vicci.process.model.sofia.Port;
import eu.vicci.process.model.sofia.ProcessStep;
import eu.vicci.process.model.sofia.Transition;
import eu.vicci.process.model.sofiainstance.DataTypeInstance;
import eu.vicci.process.model.sofiainstance.PortInstance;
import eu.vicci.process.model.sofiainstance.ProcessStepInstance;
import eu.vicci.process.model.sofiainstance.TransitionInstance;
import eu.vicci.process.model.sofiainstance.impl.MappingUtilImpl;

public class MappingUtilKPImpl extends MappingUtilImpl {
	private Map<Port, PortInstance> ports;
	public PortInstance mapPort(Port port) {
		PortInstance pi = ports.get(port);
		if (pi == null){
			pi = new PortInstanceKPImpl(port, null);
			ports.put(port, pi);
		}
		return pi;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProcessStepInstance mapProcessStep(ProcessStep processStep) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransitionInstance mapTransition(Transition transition) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void addProcessStepInstance(ProcessStepInstance instance) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataTypeInstance mapDataType(DataType dataType) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}
}
