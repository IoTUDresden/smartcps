package eu.vicci.ecosystem.standalone.controlcenter.android.util.webcam;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class StreamAccessingState implements StreamState {

	private InputStreamProvider isp;
	private MjpegInputStream in;

	public StreamAccessingState(InputStreamProvider isp) {
		this.isp = isp;
	}

	@Override
	public void doBackgroundJob() {

		HttpResponse res = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// publishProgress("Sending http request");
		try {
			res = httpclient.execute(new HttpGet(isp.getUri()));
			// publishProgress("Request finished, status = "
			// + res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 401) {
				// publishProgress("Request failed - User Access Control is enabled");
				return;
			}

			in = new MjpegInputStream(res.getEntity().getContent(), isp);
			if (in != null) {
				isp.setMjpegInputStream(in);
				isp.setCurrentState(isp.getStreamReadingState());
				isp.setBitmapDecodingThread(new BitmapDecodingThread(isp));
				isp.getBitmapDecodingThread().start();
				return;
			}

		} catch (ClientProtocolException e) {
			//e.printStackTrace();
			// publishProgress("Request failed - ClientProtocolException");
			// Error connecting to camera
		} catch (IOException e) {
			//e.printStackTrace();
			// publishProgress("Request failed - IOException");
			// Error connecting to camera e.g due to missing internet
			// permission
			// for this app or no internet connection at all
		} catch (Exception e) {
			// publishProgress("Unexpected Exception");
			//e.printStackTrace();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			isp.interrupt();
		}
	}
}
