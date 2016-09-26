package eu.vicci.ecosystem.standalone.controlcenter.android.util.webcam;

import android.graphics.Bitmap;

public interface WebcamStreamListener {

	public void onBitmapReceived(Bitmap bitmap);
	
}
