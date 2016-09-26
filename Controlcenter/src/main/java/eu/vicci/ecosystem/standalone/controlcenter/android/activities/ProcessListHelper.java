package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import eu.vicci.process.engine.core.IProcessInfo;
import eu.vicci.process.engine.core.IProcessInstanceInfo;
import eu.vicci.process.model.sofiainstance.State;

public class ProcessListHelper {

	private State state;
	private String name;
	private String type;
	private String instanceId;
	private String description;
	private String id;

	// helper for the ui
	private boolean isVisible = true;

	public ProcessListHelper(String name, String type, String id, String description) {
		this.name = name;
		this.type = type;
		this.id = id;
		this.description = description;
	}

	public ProcessListHelper(IProcessInstanceInfo pii) {
		this(pii.getProcessName(), pii.getProcessType(), pii.getProcessId(), pii.getProcessDescription());
		this.instanceId = pii.getProcessInstanceId();
		this.state = pii.getState();
	}

	public ProcessListHelper(IProcessInfo pi) {
		this(pi.getProcessName(), pi.getProcessType(), pi.getProcessId(), pi.getProcessDescription());
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String id) {
		this.instanceId = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets a value indicating if this helper is visible in the ui. Default is
	 * true.
	 * 
	 * @return
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Sets a value indicating if this helper is visible in the ui. Default is
	 * true.
	 * 
	 * @param isVisible
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(!(o instanceof ProcessListHelper))return false;
		ProcessListHelper helper = (ProcessListHelper)o;
		
		boolean idsEqual = id != null && id.equals(helper.getId());
		boolean instanceIdsEqual = (instanceId != null && instanceId.equals(helper.getInstanceId()))
				|| helper.getInstanceId() == instanceId;
		
		return  idsEqual && instanceIdsEqual;
	}
	
	@Override
	public int hashCode() {
		if(instanceId == null)
			return id.hashCode();
		return (id + instanceId).hashCode();
	}

}
