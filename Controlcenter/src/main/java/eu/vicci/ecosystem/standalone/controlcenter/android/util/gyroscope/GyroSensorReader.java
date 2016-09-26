package eu.vicci.ecosystem.standalone.controlcenter.android.util.gyroscope;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GyroSensorReader implements SensorEventListener{

	private SensorManager sensorManager;
	private Context context;
	private GyroSensorListener gyroSensorListener;
	
	public GyroSensorReader(Context context) {
		this.context = context;
	}
	
	public void start(){
		sensorManager = (SensorManager) context.getSystemService(android.content.Context.SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
	}
	
	public void stop(){
		if(sensorManager!=null) sensorManager.unregisterListener(this);
	}
	
	public void setGyroSensorListner(GyroSensorListener gyroSensorListener){
		this.gyroSensorListener = gyroSensorListener;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {}

	private float[] mGravity;
	private float[] mMagnetic;
	float[] rot_mat = new float[9];
	float[] vec = new float[3];
	float[] vecZ = { 0f, 0f, 1f };
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			mGravity = event.values.clone();
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			mMagnetic = event.values.clone();
			break;
		default:
			return;
		}
		if (mGravity != null && mMagnetic != null) {
			synchronized (this) {
				SensorManager.getRotationMatrix(rot_mat, null, mGravity,
						mMagnetic);
				vec[0] = rot_mat[0];
				vec[1] = rot_mat[3];
				vec[2] = rot_mat[6];
				double xValue = getAngle(vec, vecZ);
				vec[0] = -rot_mat[1];
				vec[1] = -rot_mat[4];
				vec[2] = -rot_mat[7];
				double yValue = getAngle(vec, vecZ);
				gyroSensorListener.onSensorData(xValue, yValue);
			}
		}
	}
		
	private float getAngle(float[] vec1, float[] vec2) {
		float abs1 = (float) Math.sqrt((double) (vec1[0] * vec1[0] + vec1[1]
				* vec1[1] + vec1[2] * vec1[2]));
		float abs2 = (float) Math.sqrt((double) (vec2[0] * vec2[0] + vec2[1]
				* vec2[1] + vec2[2] * vec2[2]));

		float skalar = vec1[0] * vec2[0] + vec1[1] * vec2[1] + vec1[2]
				* vec2[2];

		float erg = skalar / (abs1 * abs2);
		erg = (float) Math.acos((double) erg);
		erg = (float) ((double) erg - (Math.PI / 2.0));
		return erg;
	}

}
