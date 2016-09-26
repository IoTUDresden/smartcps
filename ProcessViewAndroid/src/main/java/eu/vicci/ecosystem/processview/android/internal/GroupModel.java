package eu.vicci.ecosystem.processview.android.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "key", "name", "isGroup", "leftArray", "rightArray"})
public class GroupModel extends NodeModel {
	@JsonProperty("isGroup")
	private boolean isGroup = true;
	
	public GroupModel() {
	}
	
	public GroupModel(String key, String name) {
		this(key, name, null);		
	}
	
	public GroupModel(String key, String name, String group) {
		super(key, name, group);		
	}

	@JsonProperty("isGroup")
	public boolean isGroup() {
		return isGroup;
	}

	@JsonProperty("isGroup")
	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

}
