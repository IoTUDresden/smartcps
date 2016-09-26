package eu.vicci.ecosystem.system.app.impl;


public abstract class GuiAppImpl extends AppImpl {
	public GuiAppImpl() {
		assembleGui();
	}
	
	@Override
	public void run() {
		openGui();
		
		while(!isStopRequested()) {
			try {
				Thread.sleep(100);
			} catch(InterruptedException e) {
			}
		}
		
		closeGui();
	}
	
	@Override
	protected boolean doStop() {
		boolean stopped = super.doStop();
		
		if (stopped) {
			closeGui();
			
			return true;
		}
		
		return false;
	}
	
	protected abstract void assembleGui();
	protected abstract void openGui();
	protected abstract void closeGui();
}
