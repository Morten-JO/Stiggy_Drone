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
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import de.yadrone.apps.controlcenter.plugins.keyboard.KeyboardCommandManager;
import de.yadrone.apps.tutorial.TutorialAttitudeListener;
import de.yadrone.apps.tutorial.TutorialCommander;

import org.opencv.videoio.VideoCapture;

import BrannerAlgorithm.branner;
import de.yadrone.apps.tutorial.TutorialVideoListener;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.IExceptionListener;
import de.yadrone.base.navdata.BatteryListener;
import de.yadrone.base.video.ImageListener;
import org.opencv.imgcodecs.*;

public class Main {
	public static int speed = 20;
	public static int index = 0;
	public static BufferedImage img;
	static QRClass qrCode;
	static QRController qrControl;
	public static int preventLagCounter = 0;
	public static boolean userControl = true;
	//public static VideoCapture capture;
	
	public static void main(String[] args) {
		try
		{
			// Tutorial Section 1
			
			ARDrone drone = new ARDrone();
			
			
			drone.addExceptionListener(new IExceptionListener() {
				public void exeptionOccurred(ARDroneException exc)
				{
					exc.printStackTrace();
				}
			});
			
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			
			drone.start();
			
			VideoFrame vd = new VideoFrame();
			branner franner = new branner();
			//ColoredObjectTrack g = new ColoredObjectTrack();
			JFrame showvd = new JFrame();
			showvd.setTitle("Video shower.");
			showvd.setSize(640, 720);
			showvd.add(vd);
			showvd.setVisible(true);
			BasicController control = new BasicController(drone);
			control.start();
			
			drone.getVideoManager().addImageListener(new ImageListener() {
				
				@Override
				public void imageUpdated(BufferedImage arg0) {
					preventLagCounter++;
						
					if(preventLagCounter % 5 == 0){
				
				try{
					vd.updateImageTwo(Main.MatToBufferedImage(franner.getBranner(arg0), null));
					//qrControl.printCoordinates(qrCode.getResult(arg0));
					//qrControl.centerDrone(arg0, drone);
				}
				catch(Exception e){
					e.printStackTrace();
					System.out.println("error reading");
				}
					}
					control.updateImg(arg0);
					vd.update(arg0);
					
					
				}
			});
			
			JFrame frame = new JFrame();
			frame.setTitle("Control panel.");
			frame.setSize(500, 500);
			frame.setVisible(true);
			frame.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent arg0) {
				drone.getCommandManager().setVideoChannel(VideoChannel.NEXT);
					
				}
				
				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
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
					    
					    
					    
					    BufferedImage image = img;
				        Mat gg = bufferedImageToMat(image);
				  
				        CascadeClassifier faceDetector = new CascadeClassifier("resources/lbpcascade_frontalface.xml");
				        
				        
				        // Detect faces in the image.
				        // MatOfRect is a special container class for Rect.
				        MatOfRect faceDetections = new MatOfRect();
				        faceDetector.detectMultiScale(gg, faceDetections);

				        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
				        Mat noob = new Mat();
				        Mat pleb = new Mat();
				        
				        Imgproc.cvtColor(gg, noob, Imgproc.COLOR_BGR2HSV);
				        Imgproc.Canny(noob, pleb, 10,100,3,true);
				       //Imgproc.HoughCircles(gg, circles, method, dp, minDist);
				       Imgcodecs.imwrite("okmorten.png", pleb);
				       
				       
				       Mat gray = new Mat();
				       gg.copyTo(gray);
				       Imgproc.cvtColor(gray,gray,Imgproc.COLOR_BGR2GRAY);
				       Mat circles = new Mat();
				       int minRadius = 1;
				       int maxRadius = 18;
				       Imgproc.HoughCircles(gray, circles, Imgproc.CV_HOUGH_GRADIENT, 1, minRadius, 120, 10, minRadius, maxRadius);
						System.out.println(circles);
						 
						Imgcodecs.imwrite("okmorten111.png", circles);
						
						
				       
				       
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
					} 
				}
			});
			
			
			
			
			
			//capture.open();
			
			

			// Tutorial Section x
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
	
	public static BufferedImage MatToBufferedImage(Mat matrix, BufferedImage bimg)
	{
	    if ( matrix != null ) { 
	        int cols = matrix.cols();  
	        int rows = matrix.rows();  
	        int elemSize = (int)matrix.elemSize();  
	        byte[] data = new byte[cols * rows * elemSize];  
	        int type;  
	        matrix.get(0, 0, data);  
	        switch (matrix.channels()) {  
	        case 1:  
	            type = BufferedImage.TYPE_BYTE_GRAY;  
	            break;  
	        case 3:  
	            type = BufferedImage.TYPE_3BYTE_BGR;  
	            // bgr to rgb  
	            byte b;  
	            for(int i=0; i<data.length; i=i+3) {  
	                b = data[i];  
	                data[i] = data[i+2];  
	                data[i+2] = b;  
	            }  
	            break;  
	        default:  
	            return null;  
	        }  

	        // Reuse existing BufferedImage if possible
	        if (bimg == null || bimg.getWidth() != cols || bimg.getHeight() != rows || bimg.getType() != type) {
	            bimg = new BufferedImage(cols, rows, type);
	        }        
	        bimg.getRaster().setDataElements(0, 0, cols, rows, data);
	    } else { // mat was null
	        bimg = null;
	    }
	    return bimg;  
	} 
	
	public static BufferedImage MatToBufferedImage(Mat m, int type){
		// Create an empty image in matching format
		BufferedImage gray = new BufferedImage(m.width(), m.height(), type);

		// Get the BufferedImage's backing array and copy the pixels directly into it
		byte[] data = ((DataBufferByte) gray.getRaster().getDataBuffer()).getData();
		m.get(0, 0, data);
		return gray;
	}
	
	/** canny
	 * img = arg0;
//					 nr++;
//					 if(!(nr % 20 == 0)){
//						 return ;
//					 }		
					BufferedImage image = img;
			        Mat gg = bufferedImageToMat(image);
//			  
//			        CascadeClassifier faceDetector = new CascadeClassifier("resources/lbpcascade_frontalface.xml");
//			        
//			        
//			        // Detect faces in the image.
//			        // MatOfRect is a special container class for Rect.
//			        MatOfRect faceDetections = new MatOfRect();
//			        faceDetector.detectMultiScale(gg, faceDetections);
//
//			        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
			       
			      
				
			        Mat gray = new Mat();
			        Imgproc.cvtColor(gg, gray, Imgproc.COLOR_BGR2GRAY);
			        Imgproc.blur(gray, gray, new Size(3, 3));

			        Mat edges = new Mat();
			        int lowThreshold = 40;
			        int ratio = 3;
			        Imgproc.Canny(gray, edges, lowThreshold, lowThreshold * ratio);

			        Mat circles = new Mat();
			        Vector<Mat> circlesList = new Vector<Mat>();

			        Imgproc.HoughCircles(edges, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 60, 200, 20, 30, 0 );
			        System.out.println("#rows " + circles.rows() + " #cols " + circles.cols());
			        double x = 0.0;
			        double y = 0.0;
			        int r = 0;

			        for( int i = 0; i < circles.rows(); i++ )
			        {
			          double[] data = circles.get(i, 0);
			          for(int j = 0 ; j < data.length ; j++){
			               x = data[0];
			               y = data[1];
			               r = (int) data[2];
			          }
			          
			          Point center = new Point(x,y);
			          // circle center
			          
			          Imgproc.circle( gg, center, 3, new Scalar(0,255,0), -1);
			          // circle outline
			          Imgproc.circle( gg, center, r, new Scalar(0,0,255), 1);

			        // Imshow im1 = new Imshow("Hough");
			        // im1.showImage(gg); 

			         Rect bbox = new Rect((int)Math.abs(x-r), (int)Math.abs(y-r), (int)2*r, (int)2*r);
			         Mat croped_image = new Mat(gg, bbox);
			         Imgproc.resize(croped_image, croped_image, new Size(160,160));
			         circlesList.add(croped_image);
			         //Imshow m2 = new Imshow("cropedImage");
			         //m2.showImage(croped_image);
			         Imgcodecs.imwrite("Testokmorten.png", croped_image);
		
			         
			         File outputfile = new File("resources/saved"+index+".png");
						index++;
					    try {
					    	
							ImageIO.write(img, "png", outputfile);
						} catch (IOException l) {
							l.printStackTrace();
						}
					  
					    
					    BufferedImage image = img;
				        Mat gg = bufferedImageToMat(image);
				  
				        CascadeClassifier faceDetector = new CascadeClassifier("resources/lbpcascade_frontalface.xml");
				        
				        
				        // Detect faces in the image.
				        // MatOfRect is a special container class for Rect.
				        MatOfRect faceDetections = new MatOfRect();
				        faceDetector.detectMultiScale(gg, faceDetections);

				        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
				        Mat noob = new Mat();
				        Mat pleb = new Mat();
				        
				        Imgproc.cvtColor(gg, noob, Imgproc.COLOR_BGR2HSV);
				        Imgproc.Canny(noob, pleb, 10,100,3,true);
				       //Imgproc.HoughCircles(gg, circles, method, dp, minDist);
				       Imgcodecs.imwrite("okmorten.png", pleb);
				       
				       
				       Mat gray = new Mat();
				       gg.copyTo(gray);
				       Imgproc.cvtColor(gray,gray,Imgproc.COLOR_BGR2GRAY);
				       Mat circles = new Mat();
				       int minRadius = 1;
				       int maxRadius = 18;
				       Imgproc.HoughCircles(gray, circles, Imgproc.CV_HOUGH_GRADIENT, 1, minRadius, 120, 10, minRadius, maxRadius);
						System.out.println(circles);
						 
						Imgcodecs.imwrite("okmorten111.png", circles);
	 * 
	 */
	

	
}



