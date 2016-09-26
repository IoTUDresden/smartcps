package eu.vicci.ecosystem.system.app.impl;

import eu.vicci.ecosystem.system.app.App;
import eu.vicci.ecosystem.system.appruntime.impl.AppRuntime;

public abstract class AppImpl extends ElementImpl implements App {
	@Override
	public boolean start() {
		boolean started = super.start();
		
		if (started) {
			AppRuntime.getAppRuntime().addApp(this);
		}
		
		return started;
	}

	@Override
	public void run() {
	}

	@Override
	public boolean stop() {
		boolean stopped = super.stop();
		
		if (stopped) {
			AppRuntime.getAppRuntime().removeApp(this);
		}
		
		return stopped;
	}
}
