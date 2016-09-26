package eu.vicci.ecosystem.system.appruntime.impl;

import java.util.HashMap;
import java.util.Map;

import eu.vicci.ecosystem.system.app.App;

public class AppRuntime {
	private Map<String, AppThread> appThreads;
	
	private static AppRuntime instance = null;
	
	private AppRuntime() {
		appThreads = new HashMap<String, AppThread>();
	}
	
	synchronized public void addApp(App app) {
		String appId = app.getId();
		
		if (appThreads.containsKey(appId)) {
			return;
		}
		
		AppThread thread = new AppThread(app);
		appThreads.put(appId, thread);
		
		thread.start();
	}
	
	@SuppressWarnings("deprecation")
	synchronized public void removeApp(App app) {
		String appId = app.getId();
		
		if (!appThreads.containsKey(appId)) {
			return;
		}
		
		AppThread thread = appThreads.remove(app.getId());
		thread.stop();
	}
	
	public static AppRuntime getAppRuntime() {
		if (instance != null) {
			return instance;
		}
		
		instance = new AppRuntime();
		
		return instance;
	}
	
	public static void shutdown() {
		getAppRuntime().doShutdown();
	}
	
	synchronized private void doShutdown() {
		for (AppThread appThread : appThreads.values()) {
			App app = appThread.getApp();
			app.stop();
		}
	}
}
