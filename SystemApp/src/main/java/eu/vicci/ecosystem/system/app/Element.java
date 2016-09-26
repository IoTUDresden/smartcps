package eu.vicci.ecosystem.system.app;

public interface Element {
	public void setId(String id);
	public String getId();
	
	public void setName(String name);
	public String getName();
	
	public boolean start();
	public boolean stop();
	
	public void requestStop();
}
