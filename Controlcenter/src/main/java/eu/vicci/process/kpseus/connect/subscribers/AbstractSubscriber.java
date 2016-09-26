package eu.vicci.process.kpseus.connect.subscribers;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.util.Log;
import rx.Subscriber;
import ws.wamp.jawampa.PubSubData;

public abstract class AbstractSubscriber<K extends PubSubData> extends Subscriber<K>{
	private ObjectMapper mapper = new ObjectMapper();
	
	private String topicId;
	
	
	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}	
	
	@Override
	public void onError(Throwable arg0) {
		Log.wtf(getClass().getSimpleName(), arg0);
	}
	
	@Override
	public void onCompleted() {
		//onCompleted is never called - dont know why
		
	}
	
	protected <T> T convertFromJson(String json, Class<T> clazz){
		try {
			return mapper.readValue(json, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected <T> T convertFromJson(JsonNode json, Class<T> clazz){
		try {
			return mapper.readValue(json.toString(), clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected <T> T convertFromJson(JsonNode json, TypeReference<T> reference){
		try {
			return mapper.readValue(json.toString(), reference);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return null;
	}
}
