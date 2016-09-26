package eu.vicci.ecosystem.processview.android.internal;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import eu.vicci.ecosystem.processview.android.internal.util.ProcessColors;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL) //should prevent that the group is insered, if the value is null
@JsonPropertyOrder({ "key", "name", "group", "stateColor", "leftArray", "rightArray"})
public class NodeModel extends GoModelBase  {	
	private String key;
	private String name;
	
	@JsonProperty("group")
	private String group;
	
	//default color for the process stroke. Is changed, if the state of a process changes
	private String stateColor = ProcessColors.process;
	private List<PortModel> leftArray = new ArrayList<PortModel>();
	private List<PortModel> rightArray = new ArrayList<PortModel>();
	
	public NodeModel() {

	}
	
	public NodeModel(String key, String name, String group) {
		this.key = key;
		this.name = name;
		this.group = group;
	}	
	
	public void addToLeftArray(PortModel portModel){
		leftArray.add(portModel);		
	}
	
	public void addToRightArray(PortModel portModel){
		rightArray.add(portModel);		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PortModel> getLeftArray() {
		return leftArray;
	}

	public void setLeftArray(List<PortModel> leftArray) {
		this.leftArray = leftArray;
	}

	public List<PortModel> getRightArray() {
		return rightArray;
	}

	public void setRightArray(List<PortModel> rightArray) {
		this.rightArray = rightArray;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@JsonProperty("group")
	public String getGroup() {
		return group;
	}

	@JsonProperty("group")
	public void setGroup(String group) {
		this.group = group;
	}

	public String getStateColor() {
		return stateColor;
	}

	public void setStateColor(String stateColor) {
		this.stateColor = stateColor;
	}

}
