package eu.vicci.ecosystem.standalone.controlcenter.android.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import eu.vicci.process.model.util.messages.core.IHumanTaskRequest;


/**
 * The Service should handle background listening stuff, like receiving {@link IHumanTaskRequest}s
 * 
 */
public class BackgroundMessageListenerService extends Service {

	//TODO maybe a connection service is a good idea?
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
