package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class VideoFrame extends JPanel{

	private static BufferedImage img;
	private static BufferedImage img2;
	
	public void update(BufferedImage img){
		this.img = img;
		this.img2 = img;
		//doWhatever();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
	    if (img == null || img2 == null){
	    	super.paintComponents(g);
	    } else{
	        g.drawImage(img, 0, 0, this);
	    	g.drawImage(img, 0, img.getHeight(), this);
	    }
	}
	
	public void doWhatever(){
		Mat mat;
		BufferedImage image = img;
		Mat gg = Main.bufferedImageToMat(image);
						  
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
				       
						       
		Mat gray = new Mat();
		gg.copyTo(gray);
		Imgproc.cvtColor(gray,gray,Imgproc.COLOR_BGR2GRAY);
		Mat circles = new Mat();
		int minRadius = 1;
		int maxRadius = 18;
		Imgproc.HoughCircles(gray, circles, Imgproc.CV_HOUGH_GRADIENT, 1, minRadius, 120, 10, minRadius, maxRadius);
		System.out.println(circles);
		img2 = Main.MatToBufferedImage(circles, BufferedImage.TYPE_BYTE_GRAY);
	}
}
