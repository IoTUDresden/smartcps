package eu.vicci.ecosystem.standalone.controlcenter.android.util.webcam;

import java.io.IOException;

import org.apache.http.conn.HttpHostConnectException;

public class StreamReadingState implements StreamState {

	private InputStreamProvider isp;
	
	public StreamReadingState(InputStreamProvider isp) {
		this.isp = isp;
	}
	
	@Override
	public void doBackgroundJob() {
		try {
			isp.getMjpegInputStream().readMjpegFrame();
		} catch (HttpHostConnectException e) {
			isp.setCurrentState(isp.getStreamAccessingState());			
		} catch (IOException e) {
			isp.setCurrentState(isp.getStreamAccessingState());
		}
	}

}
