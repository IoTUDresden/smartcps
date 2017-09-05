package eu.vicci.process.kpseus.connect.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.vicci.process.engine.core.ReplyState;
import rx.Subscriber;
import ws.wamp.jawampa.Reply;

public abstract class AbstractClientHandler extends Subscriber<Reply>{
	private ObjectMapper mapper = new ObjectMapper();
	private boolean isFinished = false;
	private boolean hasError = false;
	private CountDownLatch countDownLatch;
	private Throwable lastError;
	
	private List<HandlerFinishedListener> listeners = new ArrayList<HandlerFinishedListener>();
	
	protected String state = ReplyState.WAITING;
	
	@Override
	public void onCompleted() {
		finished();		
	}
	
	@Override
	public void onError(Throwable arg0) {
		arg0.printStackTrace();
		lastError = arg0;
		hasError = true;
		state = ReplyState.ERROR;
		finished();
		informListeners(null);
	}
	
	public boolean isFinished(){
		return isFinished;
	}
	
	public Throwable getLastError(){
		return lastError;
	}
	
	/**
	 * informs all {@link HandlerFinishedListener}s for this handler
	 * @param arg
	 */
	protected void informListeners(Object arg){
		for (HandlerFinishedListener handlerFinishedListener : listeners)
			handlerFinishedListener.onHandlerFinished(this, arg);
	}
	
	/**
	 * Set isFinsihed to true and count down the countDownLatch if one exists
	 */
	protected void finished(){
		isFinished = true;
		if(countDownLatch != null)
			countDownLatch.countDown();
	}
	
	/**
	 * Adds a countdownlatch which is count down, when the replay was handled.
	 * So you can avoid busy waiting.
	 * @param countDownLatch
	 */
	public void addCountDownLatch(CountDownLatch countDownLatch){
		this.countDownLatch = countDownLatch;
	}
	
	/**
	 * Adds a {@link HandlerFinishedHandler} to get informed, 
	 * if an {@link AbstractClientHandler} has received its values.
	 * @param listener
	 */
	public void addHandlerFinishedListener(HandlerFinishedListener listener){
		listeners.add(listener);
	}
	
	public boolean hasError(){
		return hasError;
	}
	
	public String getState(){
		return state;
	}
	
	protected final <T> T convertFromJson(String json, Class<T> clazz){
		try {
			return mapper.readValue(json, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected final <T> T convertFromJson(JsonNode json, Class<T> clazz){
		try {
			return mapper.readValue(json.toString(), clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected final <T> Object unsafeConvertFromJson(JsonNode json, TypeReference<T> reference){
		try {
			return mapper.readValue(json.toString(), reference);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return null;		
	}
	
	protected final <T> T convertFromJson(JsonNode json, TypeReference<T> reference){
		try {
			return mapper.readValue(json.toString(), reference);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	protected final <T extends Collection<?>> T convertFromJsonToCollectionType(JsonNode json, 
			Class<T> collectionClass, Class<?> elementClass){		
		try {
			JavaType type = mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
			return mapper.readValue(json.toString(), type);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	protected ObjectMapper getObjectMapper(){
		return mapper;
	}
	
}
