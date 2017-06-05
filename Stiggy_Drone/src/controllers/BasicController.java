package controllers;

import java.awt.image.BufferedImage;

import javax.swing.text.html.HTML.Tag;

import org.opencv.core.KeyPoint;

import com.google.zxing.Result;

import dao.CircleARObject;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;
import de.yadrone.base.navdata.AttitudeListener;
import helpers.ToolkitKit;
import main.Main;
import movement.BasicMovements;
import scanners.CircleEdgeDetection;
import scanners.CustomQRScanner;

public class BasicController implements Runnable {

	private BasicMovements movement;
	private Thread thread;
	private CustomQRScanner qrScanner;
	private boolean running = true;
	
	//States
	public static final int RESTART = 0;
	public static final int ONGROUND = 1;
	public static final int INAIR = 2;
	public static final int SEARCHQR = 3;
	public static final int BRANNER = 6;
	public static final int CIRCLEEDGEDETECTION = 7;
	public static final int FLYTHROUGH = 8;
	public static final int CHECKFLOWN = 9;
	public static final int FINISH = 10;
	public static final int ERROR = 37;
	//States end
	private BufferedImage imgi2;
	private BufferedImage imgi;
	
	public static int currentState = CIRCLEEDGEDETECTION;
	
	private int alti = 1;
	private int oldalti;
	
	private int tries = 0;
	
	public boolean imageUpdated = false;
	
	public BasicController(ARDrone drone){
		this.movement = new BasicMovements(drone);
		qrScanner = new CustomQRScanner();
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
	
	public BufferedImage getImigi2(){
		return imgi2;
	}

	@Override
	public void run() {
		while(running){
			if(Main.userControl == false){
				if(imageUpdated){
					switch(currentState){
						case RESTART:
							Main.closeDrone();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e2) {
								e2.printStackTrace();
							}
							Main.loadDrone();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e2) {
								e2.printStackTrace();
							}
							currentState = ONGROUND;
							break;
						case ONGROUND:
							oldalti = alti;
							movement.getDrone().getCommandManager().takeOff();
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							currentState = INAIR;
							break;
						case INAIR:
							if(oldalti != alti){
								currentState = SEARCHQR;
								tries = 0;
							} else{
								currentState = ONGROUND;
							}
							break;
						case SEARCHQR:
							try {
									boolean jensen = qrScanner.applyFilters(ToolkitKit.bufferedImageToMat(imgi),movement.getDrone());
									//currentState = BRANNER;
									if(jensen){
										System.out.println("SWITCHED STATE!");
										currentState = CIRCLEEDGEDETECTION;
									}
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						case BRANNER:
							System.out.println("GOT TO BRANNER");
							movement.getDrone().getCommandManager().landing();
							break;
						case CIRCLEEDGEDETECTION:
							movement.getDrone().getCommandManager().hover();
							try {
								Thread.sleep(200);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							KeyPoint point = CircleEdgeDetection.checkForCircle(imgi);
							if(point != null){
								if(CircleARObject.moveBasedOnLocation(movement.getDrone(), point.pt.x, point.pt.y, false)){
									System.out.println("SWITCHED THAT FUCKING STATE");
									
									//currentState = FLYTHROUGH; // just to land
								}
							}
							
							
							break;
						case FLYTHROUGH:
							movement.getDrone().getCommandManager().forward(50).doFor(4500);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							movement.getDrone().getCommandManager().hover();
							//check if in air and standing still
							break;
						case CHECKFLOWN:
							//What to do in here?
							break;
						case FINISH:
							movement.getDrone().getCommandManager().landing();
							break;
						case ERROR:
							System.out.println("ERROR END");
							System.exit(0);
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
	
	public void slowStop(){
		running = false;
	}
}
	

