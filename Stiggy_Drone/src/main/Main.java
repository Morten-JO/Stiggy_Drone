package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import de.yadrone.apps.controlcenter.plugins.keyboard.KeyboardCommandManager;
import de.yadrone.apps.tutorial.TutorialAttitudeListener;
import de.yadrone.apps.tutorial.TutorialCommander;

import org.opencv.videoio.VideoCapture;

import controllers.BasicController;
import controllers.QRController;
import de.yadrone.apps.tutorial.TutorialVideoListener;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.IExceptionListener;
import de.yadrone.base.navdata.BatteryListener;
import de.yadrone.base.video.ImageListener;
import de.yadrone.base.video.VideoManager;
import frames.ControlFrame;
import frames.VideoFrame;
import helpers.ToolkitKit;
import oldshit.branner;

import org.opencv.imgcodecs.*;

public class Main {
	public static int speed = 20;
	public static int index = 0;
	public static BufferedImage img;
	static QRController qrControl;
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
			
			if(Main.control == null){
				Main.control = new BasicController(drone);
				control.start();
			}
			
			//Make videoframe.
			Main.vd = new VideoFrame(drone, control);
			
			//Create basic controller
			
			
			controlFrame = new ControlFrame(drone);
			
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