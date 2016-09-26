package eu.vicci.ecosystem.processview.android.internal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"from", "to", "fromPort", "toPort", "color"})
public class LinkModel extends GoModelBase  {
	@JsonIgnore
	public static final String DEFAULT_COLOR = "gray";
	@JsonIgnore
	public static final String DEFAULT_TRUE_COLOR = "green";
	@JsonIgnore
	public static final String DEFAULT_FALSE_COLOR = "red";
	
	private String from;
	private String to;
	private String fromPort;
	private String toPort;
	private String color = DEFAULT_COLOR;
	
	public LinkModel() {
	}
	
	public LinkModel(String from, String to, String fromPort, String toPort) {
		this(from, to, fromPort, toPort, DEFAULT_COLOR);		
	}
	
	public LinkModel(String from, String to, String fromPort, String toPort, String color) {
		this.from = from;
		this.to = to;
		this.fromPort = fromPort;
		this.toPort = toPort;
		this.color = color;		
	}	
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFromPort() {
		return fromPort;
	}
	public void setFromPort(String fromPort) {
		this.fromPort = fromPort;
	}
	public String getToPort() {
		return toPort;
	}
	public void setToPort(String toPort) {
		this.toPort = toPort;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof LinkModel))
			return false;
		LinkModel lm = (LinkModel)o;
		boolean fromTo = from != null && from.equals(lm.from) && to != null && to.equals(lm.to);
		boolean portTo = fromPort != null && fromPort.equals(lm.fromPort) && toPort != null && toPort.equals(lm.toPort);
		return fromTo && portTo;
	}

}
