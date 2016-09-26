package eu.vicci.ecosystem.processview.android.internal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"portId", "portColor"})
public class PortModel extends GoModelBase {
	@JsonIgnore
	private static final String DEFAULT_START_COLOR = "#00CC00";
	@JsonIgnore
	private static final String DEFAULT_END_COLOR = "#CC0000"; 
	
	private String portColor;
	private String portId;
	
	public PortModel() {
	}
	
	public PortModel(String portId) {
		this(portId, true);	
	}
	
	public PortModel(String portId, boolean isStartPort) {
		this.portId = portId;
		portColor = isStartPort ? DEFAULT_START_COLOR : DEFAULT_END_COLOR;
	}
	
	public String getPortColor() {
		return portColor;
	}
	
	public void setPortColor(String portColor) {
		this.portColor = portColor;
	}
	
	public String getPortId() {
		return portId;
	}
	
	public void setPortId(String portId) {
		this.portId = portId;
	}

}
