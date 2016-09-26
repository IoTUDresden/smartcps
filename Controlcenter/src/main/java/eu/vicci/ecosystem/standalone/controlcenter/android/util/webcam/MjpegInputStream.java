package eu.vicci.ecosystem.standalone.controlcenter.android.util.webcam;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MjpegInputStream extends DataInputStream{

	private final static int HEADER_MAX_LENGTH = 110;
	public final static int FRAME_MAX_LENGTH = 400000 + HEADER_MAX_LENGTH;
	protected final byte[] SOI_MARKER = { (byte) 0xFF, (byte) 0xD8 };
	protected final byte[] EOF_MARKER = { (byte) 0xFF, (byte) 0xD9 };
	private final String CONTENT_LENGTH = "Content-Length:";
	private final String CONTENT_END = "\r\n";

	private final static byte[] gHeader = new byte[HEADER_MAX_LENGTH];
	BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
	byte[] CONTENT_LENGTH_BYTES;
	byte[] CONTENT_END_BYTES;
	private InputStreamProvider isp;

	public MjpegInputStream(InputStream in,InputStreamProvider isp) {
		super(new BufferedInputStream(in, FRAME_MAX_LENGTH));
		this.isp = isp;
		bitmapOptions.inSampleSize = 1;
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inPreferQualityOverSpeed = false;
		bitmapOptions.inPurgeable = true;
		try {
			CONTENT_LENGTH_BYTES = CONTENT_LENGTH.getBytes("UTF-8");
			CONTENT_END_BYTES = CONTENT_END.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	

	public Bitmap readMjpegFrame() throws IOException {	
		mark(FRAME_MAX_LENGTH);
		int headerLen = getStartOfSequence(SOI_MARKER);
		
		if (headerLen > HEADER_MAX_LENGTH) return null;
		
		reset();
		readFully(gHeader, 0, headerLen);

		int contentLen;

		try {
			contentLen = parseContentLength(gHeader, headerLen);
		} catch (NumberFormatException nfe) {
			nfe.getStackTrace();
			contentLen = getEndOfSequence(EOF_MARKER);
		}

		reset();
		skipBytes(headerLen);
		if(isp.getLock().tryLock()){
			readFully(InputStreamProvider.getFramedata(), 0, contentLen);
			isp.setFrameLength(contentLen);
			isp.setHasNewFrame(true);
			isp.getLock().unlock();
		} else {
			skipBytes(contentLen);
		}

		return null;
	}

	protected int parseContentLength(byte[] headerBytes, int headerLen)
			throws IOException, NumberFormatException {
		ByteArrayInputStream headerIn = new ByteArrayInputStream(headerBytes);
		Properties props = new Properties();
		props.load(headerIn);
		return Integer.parseInt(props.getProperty(CONTENT_LENGTH));
	}
	
	protected int getEndOfSequence(byte[] sequence) throws IOException {
		int seqIndex = 0;
		byte c;
		for (int i = 0; i < FRAME_MAX_LENGTH; i++) {
			c = (byte) readUnsignedByte();
			if (c == sequence[seqIndex]) {
				seqIndex++;
				if (seqIndex == sequence.length) {
					return i + 1;
				}
			} else {
				seqIndex = 0;
			}
		}
		return -1;
	}

	protected int getStartOfSequence(byte[] sequence) throws IOException {
		int end = getEndOfSequence(sequence);
		return (end < 0) ? (-1) : (end - sequence.length);
	}

}
