package eu.vicci.ecosystem.standalone.controlcenter.android.content.device.mock;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import eu.vicci.ecosystem.standalone.controlcenter.android.content.DashboardContentManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardDevice;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardValueObject;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.Sensor;
import eu.vicci.ecosystem.standalone.controlcenter.android.semiwa.SemiwaSensorData;
import eu.vicci.ecosystem.standalone.controlcenter.android.visualization.VisualizationType;

/**
 * Definitions of Sensor Mocks
 * 
 * @author André Kühnert
 * 
 */
public class SensorMock {

	private static LinkedList<Timer> timers = new LinkedList<Timer>();
	
	/**
	 * adds some Sensors to the ContentManager
	 */
	public static void addSensorMocks(){
		DashboardValueObject valueObject1 = new DashboardValueObject("degC");
		DashboardValueObject valueObject2 = new DashboardValueObject("degC");
		DashboardValueObject valueObject3 = new DashboardValueObject("Barometer");
		DashboardValueObject valueObject4 = new DashboardValueObject("Barometer");
		DashboardValueObject valueObject5 = new DashboardValueObject("Humidity");
		DashboardValueObject valueObject6 = new DashboardValueObject("Lux");
		DashboardValueObject valueObject7 = new DashboardValueObject("Humidity");
		DashboardValueObject valueObject8 = new DashboardValueObject("Lux");
		DashboardValueObject valueObject9 = new DashboardValueObject("door");
		DashboardValueObject valueObject10 = new DashboardValueObject("window");
		DashboardContentManager dcm = DashboardContentManager.getInstance();
		DashboardDevice sensor1 = dcm.addDashboardDevice(new Sensor("Temperatur Bricklet", "tb1", valueObject1));
		DashboardDevice sensor2 = dcm.addDashboardDevice(new Sensor("Temperatur Bricklet", "tb2", valueObject2));
		DashboardDevice sensor3 = dcm.addDashboardDevice(new Sensor("Barometer Bricklet", "bb1", valueObject3));
		DashboardDevice sensor4 = dcm.addDashboardDevice(new Sensor("Barometer Bricklet", "bb2", valueObject4));
		DashboardDevice sensor5 = dcm.addDashboardDevice(new Sensor("Humidy Bricklet", "hb1", valueObject5));
		DashboardDevice sensor6 = dcm.addDashboardDevice(new Sensor("Ambient Light Bricklet", "alb1", valueObject6));
		DashboardDevice sensor7 = dcm.addDashboardDevice(new Sensor("Humidy Bricklet", "hb2", valueObject7));
		DashboardDevice sensor8 = dcm.addDashboardDevice(new Sensor("Ambient Light Bricklet", "alb2", valueObject8));
		DashboardDevice sensor9 = dcm.addDashboardDevice(new Sensor("Door", "d1", valueObject9));
		DashboardDevice sensor10 = dcm.addDashboardDevice(new Sensor("Window", "w1", valueObject10));
		sensor9.setVisualizationType(VisualizationType.DoorStateFlipper);
		sensor10.setVisualizationType(VisualizationType.WindowStateFlipper);
		addTimer(sensor1.getValueObject(), 24, 25, 6666);
		addTimer(sensor2.getValueObject(), 23, 24, 5960);
		addTimer(sensor3.getValueObject(), 1005, 1006, 7494);
		addTimer(sensor4.getValueObject(), 1005, 1006, 6232);
		addTimer(sensor5.getValueObject(), 34, 35, 9323);
		addTimer(sensor6.getValueObject(), 530, 550, 10222);
		addTimer(sensor7.getValueObject(), 34, 35, 9922);
		addTimer(sensor8.getValueObject(), 530, 550, 4000);
		addTimer(sensor9.getValueObject(), 0, 4, 10000);
		addTimer(sensor10.getValueObject(), 0, 4, 12000);
	}
	
	private static void addTimer(final DashboardValueObject vo, final int low, final int high, final int period){
		vo.addReceiver(new SemiwaSensorData(Double.valueOf(Math.random() * (high - low) + low).toString()));
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				vo.addReceiver(new SemiwaSensorData(Double.valueOf(Math.random() * (high - low) + low).toString()));
			}
		}, 1000, period/4);
		timers.add(timer);
	}

}
