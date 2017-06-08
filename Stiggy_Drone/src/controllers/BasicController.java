package controllers;

import java.awt.image.BufferedImage;

import org.opencv.core.KeyPoint;

import de.yadrone.base.ARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.FlyingMode;
import de.yadrone.base.command.UltrasoundFrequency;
import de.yadrone.base.command.VideoBitRateMode;
import de.yadrone.base.command.VideoCodec;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;
import fuk.CircleARObject;
import helpers.ToolkitKit;
import main.Main;
import movement.BasicMovements;
import scanners.CircleEdgeDetection;
//import scanners.CustomQRScanner;

public class BasicController {

	private BasicMovements movement;
	//private CustomQRScanner qrScanner;
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
	public static final int TEST = 666;
	//States end
	private BufferedImage imgi2;
	private BufferedImage imgi;
	
	public static int currentState = ONGROUND;
	
	private int alti = 1;
	private int oldalti;
	
	private int tries = 0;
	
	public boolean imageUpdated = false;
	
	private long privateTimer;
	
	public BasicController(ARDrone drone){
		this.movement = new BasicMovements(drone);
		//qrScanner = new CustomQRScanner();
		AltitudeListener lis = new AltitudeListener() {
			
			@Override
			public void receivedExtendedAltitude(Altitude arg0) {
				
			}
			
			@Override
			public void receivedAltitude(int arg0) {
				alti = arg0;
			}
		};
		drone.getNavDataManager().addAltitudeListener(lis);
		drone.setMaxAltitude(1900);
		/*drone.getCommandManager().setOutdoor(false, false);
		drone.getCommandManager().setFlyingMode(FlyingMode.FREE_FLIGHT);
		drone.getCommandManager().setVideoCodecFps(20);
		drone.getCommandManager().setVideoBitrate(2000);
		drone.getCommandManager().setUltrasoundFrequency(UltrasoundFrequency.F25Hz);
		drone.getCommandManager().setVideoCodec(VideoCodec.H264_360P);
		*/
	}
	
	public void updateImg(BufferedImage img1){
		this.imgi = img1;
		imageUpdated = true;
	}
	
	public BufferedImage getImigi2(){
		return imgi2;
	}

	public void updateImigi2(BufferedImage img){
		imgi2 = img;
		
	}
	
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
							movement.getDrone().getCommandManager().takeOff().doFor(5000);
							currentState = INAIR;
							break;
						case INAIR:
							if(oldalti != alti){
								currentState = BRANNER;
								tries = 0;
							} else{
								currentState = ONGROUND;
							}
							break;
						case SEARCHQR:
							try {
									//boolean jensen = qrScanner.applyFilters(ToolkitKit.bufferedImageToMat(imgi),movement.getDrone());
									//currentState = BRANNER;
									//if(jensen){
										System.out.println("SWITCHED STATE!");
										//currentState = CIRCLEEDGEDETECTION;
									//}
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						case BRANNER:
							if(alti < 1700){
								movement.getDrone().getCommandManager().up(20).doFor(300);
								movement.getDrone().getCommandManager().hover().doFor(1000);
							} else{
								currentState = CIRCLEEDGEDETECTION;
							}
							break;
						case CIRCLEEDGEDETECTION:
							movement.getDrone().getCommandManager().hover().doFor(500);
							
							KeyPoint point = CircleEdgeDetection.checkForCircle(imgi, this);
							if(point != null){
								if(CircleARObject.moveBasedOnLocation(movement.getDrone(), point.pt.x, point.pt.y, false)){
									System.out.println("SWITCHED THAT FUCKING STATE");
									currentState = FLYTHROUGH; // just to land
									movement.getDrone().getCommandManager().hover().doFor(200);
									privateTimer = System.currentTimeMillis();
								}
							}
							
							
							break;
						case FLYTHROUGH:
							movement.getDrone().getCommandManager().forward(13);
							if((System.currentTimeMillis()-privateTimer) > 5000){
								currentState = FINISH;
								movement.getDrone().getCommandManager().hover().doFor(500);
							}
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
						case TEST:
							CommandManager cmd = movement.getDrone().getCommandManager();
							
							int speed = 30;
							long start = System.currentTimeMillis();
							cmd.takeOff().doFor(5000);
							
							//cmd.goLeft(speed).doFor(1000);
							System.out.println("FIRST HOVER");
							//cmd.hover().doFor(5000);
							
							//cmd.goRight(speed).doFor(1000);
							System.out.println("SECOND HOVER");
							//cmd.hover().doFor(5000);
							
							cmd.forward(speed).doFor(3000);
							System.out.println("THIRD HOVER");
							cmd.hover().doFor(5000);
							
							cmd.backward(speed).doFor(3000);
							System.out.println("FOURTH HOVER");
							cmd.hover().doFor(5000);
							System.out.println("TIME TKAEN: "+(System.currentTimeMillis()-start));
							
							cmd.landing();
							currentState = ERROR;
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
	

