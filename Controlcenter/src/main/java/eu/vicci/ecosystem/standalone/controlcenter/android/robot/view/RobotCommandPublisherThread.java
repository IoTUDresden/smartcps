package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import java.util.concurrent.locks.ReentrantLock;

import eu.vicci.driver.robot.Robot;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.exception.UnknownRobotException;
import eu.vicci.driver.robot.util.Speed;

public class RobotCommandPublisherThread extends Thread {

	public static final int WAIT_ON_ERROR_IN_MS = 2000;
	public static final int DEFAULT_PUBLISHING_DELAY_IN_MS = 100;
	
	private ReentrantLock lock;
	private Robot robot;
	private int publishingDelay = DEFAULT_PUBLISHING_DELAY_IN_MS;
	
	private boolean sendInstruction = false;
	private double linearSpeed,angularSpeed;
	
	public RobotCommandPublisherThread(String host,int port) throws UnknownRobotException{
		System.out.println("R "+host+"  "+port);
		robot = new Robot(host,port);
		if(robot.getEngine()==null) throw new UnknownRobotException();
		lock = new ReentrantLock();
	}
	
	@Override
	public void run() {
		System.out.println("R connected");
		try {
			robot.connect();
		} catch (NotConnectedException e1) {
			e1.printStackTrace();
		}
		while(!isInterrupted()){
			try {
				doRun();
			} catch(Exception e){
				sleep(WAIT_ON_ERROR_IN_MS);				
			}
		}
		if(isConnected()) {
			try {
				robot.disconnect();
			} catch (NotConnectedException e) {
			}
		}
	}
	
	private void sleep(int duration){
		try {
			Thread.sleep(duration);
		} catch(InterruptedException ie){
			interrupt();
		}
	}

	
	private void doRun() throws Exception {
		if(!isConnected()) {
			try {
				robot.connect();
			} catch(NullPointerException e){
				sleep(WAIT_ON_ERROR_IN_MS);
				return;
			}
		} else if(sendInstruction) {
			System.out.println("R send insruction");
			lock.lock();
				double linSpeed = linearSpeed;
				double angSpeed = angularSpeed;
				sendInstruction = false;
			lock.unlock();
			boolean backwards = Math.signum(-linSpeed)==-1;
			System.out.println("move");
			robot.move(new Speed(Math.abs(linSpeed), backwards), angSpeed);
			sleep(publishingDelay);
		}
	}
	
	public boolean isConnected(){
		try{
			robot.getIsConnected();
		} catch(NullPointerException e){
			return false;
		}
		return true;
	}

	public void setPublishingDelay(int publishingDelay) {
		this.publishingDelay = publishingDelay;
	}
	
	public void tryPublishingVelocityCommand(double linearSpeed, double angularSpeed){
		if(lock.tryLock()){
			this.linearSpeed = linearSpeed;
			this.angularSpeed = angularSpeed;
			sendInstruction = true;
			lock.unlock();
		}
	}
	
}
