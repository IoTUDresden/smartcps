package eu.vicci.ecosystem.system.app.impl;

import eu.vicci.ecosystem.system.app.Widget;
import eu.vicci.ecosystem.system.appruntime.impl.WidgetUpdater;

public class WidgetImpl extends ElementImpl implements Widget {
	@Override
	public boolean start() {
		boolean started = super.start();
		
		if (started) {
			WidgetUpdater.getWidgetUpdater().addWidget(this);
		}
		
		return started;
	}

	@Override
	public boolean stop() {
		boolean stopped = super.stop();
		
		if (stopped) {
			WidgetUpdater.getWidgetUpdater().removeWidget(this);
		}
		
		return stopped;
	}

	@Override
	public void update() {
	}
}
