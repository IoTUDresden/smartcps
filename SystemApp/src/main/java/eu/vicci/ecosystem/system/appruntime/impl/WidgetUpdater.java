package eu.vicci.ecosystem.system.appruntime.impl;

import java.util.ArrayList;
import java.util.List;

import eu.vicci.ecosystem.system.app.Widget;

public class WidgetUpdater {
	private static final int updateInterval = 500;
	private static WidgetUpdater instance = null;
	private static boolean shutdown = false;
	
	private static List<Widget> widgets;
	
	// Singleton
	private WidgetUpdater() {
		widgets = new ArrayList<Widget>();
	}

	synchronized public void addWidget(Widget widget) {
		widgets.add(widget);
	}
	
	synchronized public void removeWidget(Widget widget) {
		widgets.remove(widget);
	}
	
	synchronized protected void update() {
		for (Widget widget : widgets) {
			widget.update();
		}
	}
	
	public static WidgetUpdater getWidgetUpdater() {
		if (instance != null) {
			return instance;
		}
		
		instance = new WidgetUpdater();
		
		Thread widgetUpdaterThread = new Thread() {
			@Override
			public void run() {
				while(!shutdown) {
					instance.update();
					
					try {
						Thread.sleep(updateInterval);
					} catch(InterruptedException e) {
					}
				}
				
				getWidgetUpdater().doShutdown();
			}
		};
		
		widgetUpdaterThread.start();
		
		return instance;
	}
	
	public static void shutdown() {
		shutdown = true;
	}
	
	private synchronized void doShutdown() {
		for (Widget widget : widgets) {
			widget.stop();
		}
	}

	public static List<Widget> getWidgets() {
		return widgets;
	}
	
}
