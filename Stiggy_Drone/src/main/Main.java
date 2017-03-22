package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import de.yadrone.apps.controlcenter.plugins.keyboard.KeyboardCommandManager;
import de.yadrone.apps.tutorial.TutorialAttitudeListener;
import de.yadrone.apps.tutorial.TutorialCommander;
import de.yadrone.apps.tutorial.TutorialVideoListener;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.IExceptionListener;
import de.yadrone.base.video.ImageListener;
import de.yadrone.base.video.VideoManager;

public class Main {
	public static int speed = 20;
	public static int index = 0;
	public static BufferedImage img;
	public static void main(String[] args) {
		try
		{
			// Tutorial Section 1
			
			IARDrone drone = new ARDrone();
			drone.addExceptionListener(new IExceptionListener() {
				public void exeptionOccurred(ARDroneException exc)
				{
					exc.printStackTrace();
				}
			});
			
			drone.start();
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			// Tutorial Section 2
			//new TutorialAttitudeListener(drone);
			VideoFrame vd = new VideoFrame();
			JFrame showvd = new JFrame();
			showvd.setSize(640, 720);
			showvd.add(vd);
			showvd.setVisible(true);
			
			
			
			// Tutorial Section 3
			//new TutorialVideoListener(drone);
			drone.getVideoManager().addImageListener(new ImageListener() {
				
				@Override
				public void imageUpdated(BufferedImage arg0) {
					vd.update(arg0);
				}
			});
			
			/*JFrame frame = new JFrame();
			frame.setSize(500, 500);
			frame.setVisible(true);
			frame.addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
				
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
						speed -= 10
								;
					} else if(e.getKeyCode() == KeyEvent.VK_L){
						File outputfile = new File("resources/saved"+index+".png");
						index++;
					    try {
							ImageIO.write(img, "png", outputfile);
						} catch (IOException l) {
							l.printStackTrace();
						}
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
					} 
				}
			});
			*/
			// Tutorial Section 4
//			TutorialCommander commander = new TutorialCommander(drone);
//			commander.animateLEDs();
//			commander.takeOffAndLand();
//			commander.leftRightForwardBackward();
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
	
	public static Mat bufferedImageToMat(BufferedImage bi) {
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}
	
	public static BufferedImage MatToBufferedImage(Mat m, int type){
		// Create an empty image in matching format
		BufferedImage gray = new BufferedImage(m.width(), m.height(), type);

		// Get the BufferedImage's backing array and copy the pixels directly into it
		byte[] data = ((DataBufferByte) gray.getRaster().getDataBuffer()).getData();
		m.get(0, 0, data);
		return gray;
	}
	
	
	
}
