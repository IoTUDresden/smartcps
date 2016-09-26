package eu.vicci.turtlebot.navigationapp.model;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.vicci.driver.robot.util.rosmsg.MapMetaData;
import eu.vicci.driver.robot.util.rosmsg.MapMsg;
import eu.vicci.driver.robot.util.rosmsg.OrientationMsg;
import eu.vicci.driver.robot.util.rosmsg.PoseMsg;
import eu.vicci.driver.robot.util.rosmsg.PositionMsg;
import eu.vicci.driver.robot.util.rosmsg.ServiceResponseMsg;
import eu.vicci.turtlebot.navigationapp.helperclasses.Map;

// Starts a Thread and loads a Map from ROS topic
public class MapLoader extends WebSocketClient{

	//private static final String TAG = MapLoader.class.getSimpleName();
	
	static final Draft_10 draft = new Draft_10();
		
	private Map map= null;
	private boolean connectionTest=false;
	private boolean connected = false;
	
	public MapLoader(String ip, String port) {
		super(URI.create("ws://" + ip + ":" + port), draft);
	}

	public MapLoader(String ip_port) {
		super(URI.create("ws://" + ip_port), draft);
	}
	
	public MapLoader(String ip, String port, boolean connectionTest) {
		super(URI.create("ws://" + ip + ":" + port), draft);
		this.connectionTest = connectionTest;
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
	}

	@Override
	public void onError(Exception e) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(String message) {
		Gson gson = new Gson();
		Type msgType = new TypeToken<ServiceResponseMsg<MapMsg>>(){}.getType();
		MapMsg msg = (MapMsg) ((ServiceResponseMsg<MapMsg>)gson.fromJson(message, msgType)).getValues();
		map = getMapFromMsg(msg);
	}
	
	private Map getMapFromMsg(MapMsg rosMsg) {
		MapMetaData meta = rosMsg.getMap().getInfo();
		PoseMsg pose = meta.getOrigin();
		PositionMsg p = pose.getPosition();
		OrientationMsg o = pose.getOrientation();
		
		int index=0;
		int width = (int)meta.getWidth();
		int height = (int)meta.getHeight();

		int[] pixels = new int[width*height];
		
		List<Integer> pixelData = rosMsg.getMap().getData();
		for(int i = 0; i< pixels.length; i++) {
			index = pixels.length-1-(width -1 - i%width + width*(i/width));		// mirror picture
			if(pixelData.get(index)==-1) {
				pixels[i] = Color.GRAY;
			} else if(pixelData.get(index)==0){
				pixels[i] = Color.WHITE;
			} else {
				pixels[i] = Color.BLACK;
			}
		}
		Bitmap bitmap;
		bitmap = Bitmap.createBitmap(width,height, Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		
		Map map = new Map(bitmap, p.getX(), p.getY(), p.getZ(), o.getX(), o.getY(), o.getZ(), o.getW(), width, height, meta.getResolution());
		
		return map;
	}
	
	
	public Map getMap() {
		return map;
	}

	@Override
	public void onOpen(ServerHandshake shs) {
		connected = true;
		Log.v("Debug", "Connected to Turtle");
		if(!connectionTest)	callService("/static_map");
	}

	public void callService(String service_name){
		String message = "{'op': 'call_service', 'service': '"+service_name+"'}";
		message = message.replace("'", "\"");
		send(message);
	}

	public boolean isConnected() {
		return connected;
	}
	
	
	
}	
