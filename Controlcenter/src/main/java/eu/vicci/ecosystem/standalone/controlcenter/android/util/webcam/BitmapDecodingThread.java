package eu.vicci.ecosystem.standalone.controlcenter.android.util.webcam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapDecodingThread extends Thread{

	private InputStreamProvider isp;
	BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
	private final static byte[] frameData = new byte[MjpegInputStream.FRAME_MAX_LENGTH];
	private int contentLength;

	public BitmapDecodingThread(InputStreamProvider isp) {
		setDaemon(true);
		this.isp = isp;
		bitmapOptions.inSampleSize = 1;
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inPreferQualityOverSpeed = false;
		bitmapOptions.inPurgeable = true;
	}
	
	@Override
	public void run() {
		while(!isInterrupted()) {
			isp.getLock().lock();
			contentLength = isp.getFrameLength();
			System.arraycopy(InputStreamProvider.getFramedata() , 0, frameData, 0, contentLength);
			isp.getLock().unlock();
			if(contentLength>0){
				Bitmap bm = BitmapFactory.decodeByteArray(frameData, 0, contentLength);
				if(bm!=null) isp.getStreamListener().onBitmapReceived(bm);	
			}
		}
	}

	public BitmapFactory.Options getBitmapOptions() {
		return bitmapOptions;
	}
	
}
