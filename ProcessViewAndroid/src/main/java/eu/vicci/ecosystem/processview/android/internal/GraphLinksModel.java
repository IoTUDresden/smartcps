package eu.vicci.ecosystem.processview.android.internal;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Model for the <a href=https://gojs.net/>GoJS</a> library
 */
@JsonPropertyOrder({ "copiesArrays", "copiesArrayObjects", "linkFromPortIdProperty", "linkToPortIdProperty", "nodeDataArray", "linkDataArray" })
public class GraphLinksModel extends GoModelBase {
	private boolean copiesArrays = true;
	private boolean copiesArrayObjects = true;
	private String linkFromPortIdProperty = "fromPort";
	private String linkToPortIdProperty = "toPort";
	
	private List<NodeModel> nodeDataArray = new ArrayList<NodeModel>();
	private List<LinkModel> linkDataArray = new ArrayList<LinkModel>();
	
	
	public void addNodeModel(NodeModel nodeModel){
		nodeDataArray.add(nodeModel);		
	}
	
	/**
	 * Only adds the link, if its not allready exists in the list
	 * @param linkModel
	 */
	public void addLinkModel(LinkModel linkModel){
		if(!linkDataArray.contains(linkModel))
			linkDataArray.add(linkModel);		
	}
	
	public boolean isCopiesArrays() {
		return copiesArrays;
	}
	public void setCopiesArrays(boolean copiesArrays) {
		this.copiesArrays = copiesArrays;
	}
	public boolean isCopiesArrayObjects() {
		return copiesArrayObjects;
	}
	public void setCopiesArrayObjects(boolean copiesArrayObjects) {
		this.copiesArrayObjects = copiesArrayObjects;
	}
	public String getLinkFromPortIdProperty() {
		return linkFromPortIdProperty;
	}
	public void setLinkFromPortIdProperty(String linkFromPortIdProperty) {
		this.linkFromPortIdProperty = linkFromPortIdProperty;
	}
	public String getLinkToPortIdProperty() {
		return linkToPortIdProperty;
	}
	public void setLinkToPortIdProperty(String linkToPortIdProperty) {
		this.linkToPortIdProperty = linkToPortIdProperty;
	}
	public List<NodeModel> getNodeDataArray() {
		return nodeDataArray;
	}
	public void setNodeDataArray(List<NodeModel> nodeDataArray) {
		this.nodeDataArray = nodeDataArray;
	}
	public List<LinkModel> getLinkDataArray() {
		return linkDataArray;
	}
	public void setLinkDataArray(List<LinkModel> linkDataArray) {
		this.linkDataArray = linkDataArray;
	}
	

}
