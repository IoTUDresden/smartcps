package eu.vicci.ecosystem.standalone.controlcenter.android.util.webcam;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author dimitri
 *
 */
public class InputStreamProvider extends Thread {

	private URI uri;
	
	private MjpegInputStream in;
	
	private StreamState currentState;
	private StreamAccessingState streamAccessingState;
	private StreamReadingState streamReadingState;
	private final ReentrantLock lock = new ReentrantLock();
	private boolean hasNewFrame = false;

	private final static byte[] frameData = new byte[MjpegInputStream.FRAME_MAX_LENGTH];
	private int frameLength;
	
	private BitmapDecodingThread bitmapDecodingThread;
	private WebcamStreamListener streamListener;
	
	public InputStreamProvider(URI uri, WebcamStreamListener streamListener) {
		setDaemon(true);
		this.uri = uri;
		this.streamListener = streamListener;
		streamAccessingState = new StreamAccessingState(this);
		streamReadingState = new StreamReadingState(this);
		setCurrentState(streamAccessingState);
	}
	
	@Override
	public void run() {
		while(!isInterrupted()) {
			currentState.doBackgroundJob();
		}
		try {
			if(in!=null) in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(bitmapDecodingThread!=null) {
			frameLength = 0;
			bitmapDecodingThread.getBitmapOptions().requestCancelDecode();
			bitmapDecodingThread.interrupt();
		}
	}
	
	public URI getUri() {
		return uri;
	}
	

	public StreamAccessingState getStreamAccessingState() {
		return streamAccessingState;
	}

	public StreamReadingState getStreamReadingState() {
		return streamReadingState;
	}

	public void setCurrentState(StreamState currentState) {
		this.currentState = currentState;
	}
	
	public MjpegInputStream getMjpegInputStream() {
		return in;
	}
	
	public void setMjpegInputStream(MjpegInputStream in) {
		this.in = in;
	}

	public Lock getLock() {
		return lock;
	}

	public boolean getHasNewFrame() {
		return hasNewFrame;
	}

	public void setHasNewFrame(boolean value) {
		this.hasNewFrame = value;
	}

	public int getFrameLength() {
		return frameLength;
	}

	public void setFrameLength(int frameLength) {
		this.frameLength = frameLength;
	}

	public static byte[] getFramedata() {
		return frameData;
	}

	public BitmapDecodingThread getBitmapDecodingThread() {
		return bitmapDecodingThread;
	}

	public void setBitmapDecodingThread(BitmapDecodingThread bitmapDecodingThread) {
		this.bitmapDecodingThread = bitmapDecodingThread;
	}

	public WebcamStreamListener getStreamListener() {
		return streamListener;
	}	
	
}


