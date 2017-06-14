package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.opencv.core.KeyPoint;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import centering.CircleARObject;
import de.yadrone.base.ARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.UltrasoundFrequency;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;
import helpers.Toolkit;
import main.Main;
import movement.BasicMovements;
import scanners.CircleEdgeDetection;
import scanners.CustomQRScanner;
import scanners.SimpleQR;


public class BasicController {

	private BasicMovements movement;
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
	public static final int TEST = 666;
	public static final int STRAY = 11;
	public static final int SPIN = 12;
	//States end
	private BufferedImage imgi2;
	private BufferedImage imgi;
	
	public static int currentState = ONGROUND;
	
	private int alti = 1;
	private int oldalti;
	
	private int tries = 0;
	
	public boolean imageUpdated = false;
	
	private long privateTimer;
	
	
	//Just spin and stray things
	private int oldState = 0;
	private int triesBeforeSpin;
	private int triesBeforeStray;
	
	//Just circle things
	private int triesOnCircle = 0;
	
	public BasicController(ARDrone drone){
		this.movement = new BasicMovements(drone);
		qrScanner = new CustomQRScanner();
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
		drone.getCommandManager().setOutdoor(false, true);
		drone.getCommandManager().setUltrasoundFrequency(UltrasoundFrequency.F25Hz);
		drone.getCommandManager().setVideoCodecFps(15);
		drone.getCommandManager().setVideoBitrate(1000);
		
		//drone.getCommandManager().setSSIDSinglePlayer("testflight MonkaS");
		/*
		drone.getCommandManager().setFlyingMode(FlyingMode.FREE_FLIGHT);
		drone.getCommandManager().setVideoCodecFps(20);
		
		
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
						case ONGROUND:
							tries++;
							if(tries > 5){
								currentState = RESTART;
							}
							oldalti = alti;
							movement.getDrone().getCommandManager().flatTrim();
							movement.getDrone().getCommandManager().takeOff().doFor(5000);
							
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
							/*
							movement.getDrone().getCommandManager().hover().doFor(1000);
							int morten = SimpleQR.moveQR(imgi, movement.getDrone());
							if(morten == 1){
								System.out.println("Switched to state : CIRCLEDETECTION!");
								currentState = BRANNER;
							} else if(morten == -1){
								triesBeforeSpin++;
								if(triesBeforeSpin > 5){
									triesBeforeSpin = 0;
									triesBeforeStray = 0;
									currentState = SPIN;
									oldState = SEARCHQR;
								}
							} else if(morten == 0){
								triesBeforeSpin = 0;
								triesBeforeStray = 0;
							} else{
								//Checksumexception
								triesBeforeStray++;
								if(triesBeforeStray > 5){
									triesBeforeStray = 0;
									triesBeforeSpin = 0;
									currentState = STRAY;
									oldState = SEARCHQR;
								}
							}
							
							*/
							boolean jensen = qrScanner.applyFilters(Toolkit.bufferedImageToMat(imgi),movement.getDrone());
							if(jensen){
								System.out.println("Switched to state : CIRCLEDETECTION!");
								currentState = CIRCLEEDGEDETECTION;
							}
							break;
						case BRANNER:
							if(alti < 1700){
								movement.getDrone().getCommandManager().up(20).doFor(300);
								movement.getDrone().getCommandManager().hover().doFor(1000);
							} else{
								movement.getDrone().getCommandManager().hover().doFor(2000);
								currentState = CIRCLEEDGEDETECTION;
							}
							break;
						case CIRCLEEDGEDETECTION:
							movement.getDrone().getCommandManager().hover().doFor(2000);
							KeyPoint point = CircleEdgeDetection.checkForCircle(imgi, this);
							if(point != null){
								if(CircleARObject.moveBasedOnLocation(movement.getDrone(), point.pt.x, point.pt.y, false, currentState)){
									System.out.println("Switched to flythrough state.");
									currentState = FLYTHROUGH; // just to land
									movement.getDrone().getCommandManager().hover().doFor(2000);
									privateTimer = System.currentTimeMillis();
								}
							} 
							break;
						case FLYTHROUGH:
							movement.getDrone().getCommandManager().forward(13);
							if((System.currentTimeMillis()-privateTimer) > 3000){
								currentState = FINISH;
								movement.getDrone().getCommandManager().hover().doFor(500);
							}
							break;
						case CHECKFLOWN:
						    
						    
						    
						 
							break;
						case FINISH:
							movement.getDrone().getCommandManager().landing();
							break;
						case ERROR:
							System.out.println("ERROR END");
							Main.closeDrone();
							break;
						case TEST:
							currentState = ERROR;
							break;
						case STRAY:
							//Should only be applied if spin couldnt do jack
							int direction = new Random().nextInt() % 2;
							switch(direction)
							{
								case 0 : movement.getDrone().getCommandManager().goLeft(15); break;
								case 1 : movement.getDrone().getCommandManager().goRight(15); break;
							}
							currentState = oldState;
							break;
						case SPIN:
							movement.getDrone().getCommandManager().spinRight(15).doFor(200);
							movement.getDrone().getCommandManager().hover().doFor(1000);
							currentState = oldState;
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