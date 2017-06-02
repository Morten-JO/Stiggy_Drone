package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

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
	

	public static void main(String[] args) {
		try
		{
			//Load drone things.
			ARDrone drone = new ARDrone();
			drone.addExceptionListener(new IExceptionListener() {
				public void exeptionOccurred(ARDroneException exc)
				{
					exc.printStackTrace();
				}
			});
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			drone.start();
			
			//Make videoframe.
			VideoFrame vd = new VideoFrame();
			JFrame showvd = new JFrame();
			showvd.setTitle("Video shower.");
			showvd.setSize(640, 720);
			showvd.add(vd);
			showvd.setVisible(true);
			
			//Create basic controller
			BasicController control = new BasicController(drone);
			control.start();
			
			
			drone.getVideoManager().addImageListener(new ImageListener() {
				@Override
				public void imageUpdated(BufferedImage arg0) {
					control.updateImg(arg0);
					vd.update(arg0);
					vd.updateImageTwo(control.getImigi2());
				}
			});
			
			//Manual override control panel
			JFrame frame = new JFrame();
			frame.setTitle("Control panel.");
			frame.setSize(500, 500);
			frame.setVisible(true);
			frame.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent arg0) {}
				
				@Override
				public void mousePressed(MouseEvent arg0) {
					drone.getCommandManager().setVideoChannel(VideoChannel.NEXT);
				}
				
				@Override
				public void mouseExited(MouseEvent arg0) {}
				
				@Override
				public void mouseEntered(MouseEvent arg0) {}
				
				@Override
				public void mouseClicked(MouseEvent arg0) {}
			});
			
			frame.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {}
				
				@Override
				public void keyReleased(KeyEvent e) {
					final IARDrone temp = drone;
					if(e.getKeyCode() == KeyEvent.VK_ENTER){
						System.out.println("Take off");
						temp.getCommandManager().takeOff();
					} else if(e.getKeyCode() == KeyEvent.VK_X){
						System.out.println("Landing");
						temp.getCommandManager().landing();
					} else if(e.getKeyCode() == KeyEvent.VK_Y){
						speed += 10;
					} else if(e.getKeyCode() == KeyEvent.VK_H){
						speed -= 10;
					} else if(e.getKeyCode() == KeyEvent.VK_L){
						ToolkitKit.saveImage(img);
					} else if(e.getKeyCode() == KeyEvent.VK_LEFT){
						temp.getCommandManager().hover();
					} else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
						temp.getCommandManager().hover();
					} else if(e.getKeyCode() == KeyEvent.VK_UP){
						temp.getCommandManager().hover();
					} else if(e.getKeyCode() == KeyEvent.VK_DOWN){
						temp.getCommandManager().hover();
					} else if(e.getKeyCode() == KeyEvent.VK_A){
						temp.getCommandManager().hover();
					} else if(e.getKeyCode() == KeyEvent.VK_S){
						temp.getCommandManager().hover();
					} else if(e.getKeyCode() == KeyEvent.VK_D){
						temp.getCommandManager().hover();
					} else if(e.getKeyCode() == KeyEvent.VK_W){
						temp.getCommandManager().hover();
					}
					else if(e.getKeyCode() == KeyEvent.VK_E){
						temp.getNavDataManager().addBatteryListener(new BatteryListener() {
							
							@Override
							public void voltageChanged(int arg0) {
								// TODO Auto-generated method stub
							}
							
							@Override
							public void batteryLevelChanged(int arg0) {
								System.out.println("Battery currently at " + arg0 + "%");
								
							}
						});;
						
					}else if(e.getKeyCode() == KeyEvent.VK_Q){
						userControl = !userControl;
						System.out.println("User control = " + userControl);
					}
				
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					final IARDrone temp = drone;
					if(e.getKeyCode() == KeyEvent.VK_LEFT){
						temp.getCommandManager().goLeft(speed);						
					} else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
						temp.getCommandManager().goRight(speed);
					} else if(e.getKeyCode() == KeyEvent.VK_UP){
						temp.getCommandManager().forward(speed);
					} else if(e.getKeyCode() == KeyEvent.VK_DOWN){
						temp.getCommandManager().backward(speed);
					} else if(e.getKeyCode() == KeyEvent.VK_A){
						temp.getCommandManager().spinLeft(70);
					} else if(e.getKeyCode() == KeyEvent.VK_S){
						temp.getCommandManager().down(speed);
					} else if(e.getKeyCode() == KeyEvent.VK_D){
						temp.getCommandManager().spinRight(70);						
					} else if(e.getKeyCode() == KeyEvent.VK_W){
						temp.getCommandManager().up(speed);
					} else if(e.getKeyCode() == KeyEvent.VK_O){
						BasicController.currentState = BasicController.ONGROUND;
					}
				}
			});
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
	
	
}