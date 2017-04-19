package main;

import java.awt.image.BufferedImage;

import de.yadrone.base.IARDrone;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;
import de.yadrone.base.navdata.AttitudeListener;

public class BasicController implements Runnable {

	private BasicMovements movement;
	private Thread thread;
	private QRController troller;
	
	//States
	public static final int ONGROUND = 1;
	public static final int INAIR = 2;
	public static final int SEARCHQR = 3;
	public static final int FLYQR = 4;
	public static final int ADJUSTQR = 5;
	public static final int BRANNER = 6;
	public static final int HJORTEN = 7;
	public static final int FLYTHROUGH = 8;
	public static final int CHECKFLOWN = 9;
	public static final int ERROR = 37;
	//States end
	
	private BufferedImage imgi;
	
	public static int currentState = ONGROUND;
	
	private int alti = 1;
	private int oldalti;
	
	
	public BasicController(IARDrone drone){
		this.movement = new BasicMovements(drone);
		troller = new QRController();
		drone.getNavDataManager().addAltitudeListener(new AltitudeListener() {
			
			@Override
			public void receivedExtendedAltitude(Altitude arg0) {
				
			}
			
			@Override
			public void receivedAltitude(int arg0) {
				alti = arg0;
			}
		});
	}
	
	public void updateImg(BufferedImage img1){
		this.imgi = img1;
	}
	
	public void start(){
		thread = new Thread(this);
		thread.start();
	}
	
	public void moveBasedOnCircleObject(CircleARObject obj){
		if(obj.horizontal > 5){
			movement.goLeft(500);
		} else if(obj.horizontal < -5){
			movement.goRight(500);
		} else if(obj.vertical > 5){
			movement.goDown(500);
		} else if(obj.vertical < -5){
			movement.goUp(500);
		}
	}

	@Override
	public void run() {
		while(!Main.userControl){
			switch(currentState){
				case ONGROUND:
					movement.getDrone().getCommandManager().takeOff();
					break;
				case INAIR:
					if(oldalti == alti){
						currentState = SEARCHQR;
					} else{
						oldalti = alti;
					}
					//check if in air and standing still
					break;
				case SEARCHQR:
					
					break;
				case FLYQR:
					
					break;
				case ADJUSTQR:
					
					break;
				case BRANNER:
					
					break;
				
			}
		}
	}
	
}
