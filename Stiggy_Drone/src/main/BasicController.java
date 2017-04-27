package main;

import java.awt.image.BufferedImage;

import javax.swing.text.html.HTML.Tag;

import org.opencv.core.KeyPoint;

import com.google.zxing.Result;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;
import de.yadrone.base.navdata.AttitudeListener;

public class BasicController implements Runnable {

	private BasicMovements movement;
	private Thread thread;
	private CustomQRScanner troller;
	private boolean running = true;
	
	//States
	public static final int ONGROUND = 1;
	public static final int INAIR = 2;
	public static final int SEARCHQR = 3;
	public static final int BRANNER = 6;
	public static final int HJORTEN = 7;
	public static final int FLYTHROUGH = 8;
	public static final int CHECKFLOWN = 9;
	public static final int ERROR = 37;
	//States end
	private BufferedImage imgi2;
	private BufferedImage imgi;
	
	public static int currentState = SEARCHQR;
	
	private int alti = 1;
	private int oldalti;
	
	private int tries = 0;
	
	public boolean imageUpdated = false;
	
	public BasicController(ARDrone drone){
		this.movement = new BasicMovements(drone);
		troller = new CustomQRScanner();
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
		imageUpdated = true;
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
	
	public BufferedImage getImigi2(){
		return imgi2;
	}

	@Override
	public void run() {
		while(running){
			if(Main.userControl == false){
				if(imageUpdated){
					switch(currentState){
						case ONGROUND:
							movement.getDrone().getCommandManager().takeOff();
							oldalti = alti;
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							currentState = INAIR;
							
							break;
						case INAIR:
							tries++;
							if(oldalti != alti){
								currentState = HJORTEN;
								tries = 0;
							} else{
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							if(tries > 4){
								currentState = ONGROUND;
							}
							break;
						case SEARCHQR:
							System.out.println("SEARCHQR");
							try {
								if(true){
									boolean jensen = troller.applyFilters(Main.bufferedImageToMat(imgi),movement.getDrone());
									System.out.println(jensen);
									//currentState = BRANNER;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						case BRANNER:
							System.out.println("GOT TO BRANNER");
							movement.getDrone().getCommandManager().landing();
							break;
						case HJORTEN:
							movement.getDrone().getCommandManager().hover();
							try {
								Thread.sleep(200);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							KeyPoint point = Hjorten.checkForCircle(imgi);
							if(point != null){
								CircleARObject obj = new CircleARObject();
								obj.horizontal = point.pt.x;
								obj.vertical = point.pt.y;
								if(obj.moveBasedOnLocation(movement.getDrone())){
									this.currentState = ERROR; // just to land
								}
							}
							
							
							break;
						case FLYTHROUGH:
							movement.getDrone().getCommandManager().forward(20);
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
						if(tries > 4){
							currentState = ONGROUND;
						}
						//check if in air and standing still
						break;
					
					
					}
					imageUpdated = false;
				} else{
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
	

