package eu.vicci.ecosystem.system.app.impl;

import eu.vicci.ecosystem.system.app.Element;

public abstract class ElementImpl implements Element {
	private String id;
	private String name;
	
	private boolean stopRequested;
	
	public ElementImpl() {
		stopRequested = false;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean start() {
		stopRequested = false;
		
		return doStart();
	}
	
	protected boolean doStart() {
		return true;
	}

	@Override
	public boolean stop() {
		boolean stopped = doStop();
		if (stopped) {
			stopRequested = false;
			
			return true;
		}
		
		return false;
	}
	
	protected boolean doStop() {
		return true;
	}
	
	@Override
	public void requestStop() {
		stopRequested = true;
	}
	
	protected boolean isStopRequested() {
		return stopRequested;
	}
}
