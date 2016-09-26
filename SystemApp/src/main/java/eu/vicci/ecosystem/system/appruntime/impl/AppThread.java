package eu.vicci.ecosystem.system.appruntime.impl;

import eu.vicci.ecosystem.system.app.App;


public class AppThread extends Thread {
	private static final ThreadGroup appThreadGroup = new ThreadGroup("Apps");
	
	private App app;
	
	public AppThread(App app) {
		super(appThreadGroup, createName(app));
		
		this.app = app;
	}
	
	private static String createName(App app) {
		//Create a name for this thread with at least some meaning.
		return AppThread.class.getSimpleName() + ": " + app.getId();
	}
	
	@Override
	public void run() {
		app.start();
		app.run();
		app.stop();
	}

	public App getApp() {
		return app;
	}
}
