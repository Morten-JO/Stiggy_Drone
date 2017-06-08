package main;

import java.awt.event.WindowEvent;
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
		loadDrone();
	}
	
	public static void loadDrone(){
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
			
			if(Main.control != null){
				Main.control.slowStop();
			}
			Main.control = new BasicController(drone);			
			//Make videoframe.
			Main.vd = new VideoFrame(drone, control);

			//Create basic controller
			controlFrame = new ControlFrame(drone);
			control.run();
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
	
	public static void closeDrone(){
		System.out.println("Closing drone.");
		control.slowStop();
		control = null;
		drone.hover();
		drone = null;
		controlFrame.dispatchEvent(new WindowEvent(controlFrame, WindowEvent.WINDOW_CLOSING));
		controlFrame = null;
		showvd.dispatchEvent(new WindowEvent(showvd, WindowEvent.WINDOW_CLOSING));
		showvd = null;
		vd = null;
	}
	
}