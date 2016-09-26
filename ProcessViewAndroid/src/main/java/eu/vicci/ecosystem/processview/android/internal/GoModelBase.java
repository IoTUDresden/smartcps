package eu.vicci.ecosystem.processview.android.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.util.Log;

public abstract class GoModelBase {
	
	public String exportJson(){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			Log.e("GoModelCreating", e.getMessage(), e);
			return null;
		}
	}

}
