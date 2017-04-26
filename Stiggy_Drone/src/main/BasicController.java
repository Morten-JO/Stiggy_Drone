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
	private QRCodeScanner troller;
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
	
	public static int currentState = ONGROUND;
	
	private int alti = 1;
	private int oldalti;
	
	private int tries = 0;
	
	public boolean imageUpdated = false;
	
	public BasicController(ARDrone drone){
		this.movement = new BasicMovements(drone);
		troller = new QRCodeScanner();
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
								System.out.println("I FOUND THE FUCKING CIRCLE");
								if(point.pt.x > 350){
									movement.getDrone().getCommandManager().goRight(35);
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									movement.getDrone().getCommandManager().hover();
									try {
										Thread.sleep(50);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} else if(point.pt.x < 290){
									movement.getDrone().getCommandManager().goLeft(35);
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									movement.getDrone().getCommandManager().hover();
									try {
										Thread.sleep(50);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} else if(point.pt.y > 210){
									movement.getDrone().getCommandManager().up(35);
									try {
										Thread.sleep(80);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									movement.getDrone().getCommandManager().hover();
									try {
										Thread.sleep(50);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} else if(point.pt.y < 150){
									movement.getDrone().getCommandManager().down(5);
									try {
										Thread.sleep(20);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									movement.getDrone().getCommandManager().hover();
									try {
										Thread.sleep(50);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								if((point.pt.x) > 290 && (point.pt.x) < 350){
									if((point.pt.y) < 210 && (point.pt.y ) > 150){
										System.out.println("WE GOING FORWARD NOW BOIS!");
										movement.getDrone().getCommandManager().forward(30);
										try {
											Thread.sleep(5000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										movement.getDrone().getCommandManager().hover();
										this.currentState = ERROR;
									}
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
	

