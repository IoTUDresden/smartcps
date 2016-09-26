package eu.vicci.ecosystem.standalone.controlcenter.android.content.device;

import java.util.List;

import eu.vicci.ecosystem.standalone.controlcenter.android.visualization.VisualizationType;
import eu.vicci.process.engine.core.IProcessInstanceInfo;

/**
 * Process device for dashboard.
 * Encapsulates a ProcessInstanceInfo.
 * 
 * @author André Kühnert
 */
public final class Process extends DashboardDevice {
	private static final long serialVersionUID = 5933195457460592280L;
	
	private static List<String> allowedOptions = DeviceOption.getOptionsArray(
			DeviceOption.CAN_CHANGE_VISUALISATION, DeviceOption.CAN_HIDE);
	private static String[] optionsStrings = allowedOptions.toArray(
			new String[allowedOptions.size()]);
	
	private final IProcessInstanceInfo processInstanceInfo;

	/**
	 * instantiates a new dashboard process device.
	 * {@link eu.vicci.process.model.util.ProcessInstanceInfo}
	 * @param processInstanceInfo - can not be null.
	 */
	public Process(IProcessInstanceInfo processInstanceInfo) {
		super(processInstanceInfo.getProcessName() , processInstanceInfo.getProcessId(), 
				null);
		this.processInstanceInfo = processInstanceInfo;
	}

	@Override
	public DeviceType getDeviceType() {
		return DeviceType.Process;
	}

	@Override
	public boolean hasDeviceOption(String option) {
		return allowedOptions.contains(option);
	}

	@Override
	public String[] getAllowedOptions() {
		return optionsStrings;
	}

	@Override
	public VisualizationType getDefaultVisualizationType() {
		return VisualizationType.ProcessInfo;
	}

	@Override
	public VisualizationType getDetailVisualizationType() {
		return null;
	}
	
	/**
	 * gets the process state as a string
	 * @return process state string
	 */
	public String getProcessStateString(){
		return processInstanceInfo.getState().toString();
	}
	
	/**
	 * gets the process state description
	 * @return process state description
	 */
	public String getProcessDescription(){
		return processInstanceInfo.getProcessDescription();
	}
	
	/**
	 * gets the process type
	 * @return process type
	 */
	public String getProcessType(){
		return processInstanceInfo.getProcessType();
	}
	
	/**
	 * gets the process instance id
	 * @return process instance id
	 */
	public String getProcessInstanceId(){
		return processInstanceInfo.getProcessInstanceId();		
	}

}
