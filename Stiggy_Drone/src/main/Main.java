package main;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.opencv.core.Core;

import controllers.BasicController;
//import controllers.QRController;
import de.yadrone.base.ARDrone;
import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.IExceptionListener;
import frames.ControlFrame;
import frames.VideoFrame;

public class Main {
	public static int speed = 20;
	public static int index = 0;
	public static BufferedImage img;
	//static QRController qrControl;
	public static int preventLagCounter = 0;
	public static boolean userControl = true;
	
	private static VideoFrame vd;
	private static JFrame showvd;
	private static ARDrone drone;
	private static BasicController control;
	private static ControlFrame controlFrame;

	public static void main(String[] args) {
		startDrone();
	}
	
	public static void startDrone(){
		try{
			//Load drone things.
			Main.drone = new ARDrone();
			drone.addExceptionListener(new IExceptionListener() {
				public void exeptionOccurred(ARDroneException exc)
				{
					System.out.println("EX:"+exc.getMessage());
				}
			});
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			drone.start();
			drone.getVideoManager().reinitialize();
			if(Main.control != null){
				Main.control.slowStop();
			}
			Main.control = new BasicController(drone);			
			//Make videoframe.
			Main.vd = new VideoFrame(drone, control);
			control.run();
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
	
	/**
	 * Closes everything and exits the program.
	 */
	public static void closeDrone(){
		System.out.println("Closing drone.");
		vd.running = false;
		if(control != null){
			control.slowStop();
			control = null;
		}
		if(drone != null){
			drone.hover();
			drone.getCommandManager().close();
			drone.getVideoManager().close();
			drone.getConfigurationManager().close();
			drone.getNavDataManager().close();
			drone.stop();
			drone.reset();
			drone = null;
		}
		System.exit(0);
	}
	
}