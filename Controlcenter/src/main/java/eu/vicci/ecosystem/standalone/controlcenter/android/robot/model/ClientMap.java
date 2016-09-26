package eu.vicci.ecosystem.standalone.controlcenter.android.robot.model;

import eu.vicci.turtlebot.navigationapp.helperclasses.Map;

public class ClientMap {
	private String name;
	private String sourceDir;
	private String ip;
	private int port;
	private Map map;
	
	public ClientMap(String name, String sourceDir, String ip, int port, Map map) {
		this.name = name;
		this.sourceDir = sourceDir;
		this.ip = ip;
		this.port = port;
		this.map = map;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSourceDir() {
		return sourceDir;
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
}
