package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class VideoFrame extends JPanel{

	private BufferedImage img;
	private BufferedImage img2;
	
	private int count = 0;
	private int threshold = 140;
	private int ratio = 1;
	private int relax = 0;
	private List<KeyPoint> points;
	
	public VideoFrame(){
		points = new ArrayList<KeyPoint>();
	}
	
	public void update(BufferedImage img){
		this.img = img;
		count++;
		if(count > 10){
			checkForCircle();
			count = 0;
		}
		
		
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
	    if(img != null){
	    	g.drawImage(img, 0, 0, this);
	    }
	    if(img2 != null){
	    	g.drawImage(img2, 0, img.getHeight(), img.getWidth(), img.getHeight(), this);
	    	g.setColor(Color.RED);
	    	for(int i = 0 ; i < this.points.size(); i++){
	    		double size = 2 * points.get(i).size;
	    		g.drawOval((int)points.get(i).pt.x - (int)(0.5f * size), (int)points.get(i).pt.y - (int)(0.5f * size), (int)size, (int)size);
	    	}
	    }
	}
	
	public void checkForCircle(){
		if(img != null){
			BufferedImage image = img;
		    Mat gg = Main.bufferedImageToMat(image);
		    Mat gray = new Mat();
		    Imgproc.cvtColor(gg, gray, Imgproc.COLOR_BGR2GRAY);
		    Imgproc.blur(gray, gray, new Size(3, 3));
		    Mat edges = new Mat();
		    int lowThreshold = this.threshold;
		    int ratio = 1;
		    Imgproc.Canny(gray, edges, lowThreshold, lowThreshold * ratio);
		    Mat circles = new Mat();
		    Imgproc.HoughCircles(edges, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 60, 200, 20, 30, 0 );
		    
		    FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
		    detector.read("whatevs.xml");
		    MatOfKeyPoint keypoints = new MatOfKeyPoint();
		    detector.detect(edges, keypoints);
		    Mat out = new Mat();
		    org.opencv.core.Scalar cores = new org.opencv.core.Scalar(0, 255, 0);
		    org.opencv.features2d.Features2d.drawKeypoints(edges, keypoints, out);
		    this.img2 = Main.MatToBufferedImage(out, null);
		    KeyPoint[] points = keypoints.toArray();
		    this.points.clear();
		    if(points.length > 0){
		    	//System.out.println("KeyPOINTS!");
			    for(int i = 0; i < points.length; i++){
			    	this.points.add(points[i]);
			    	//System.out.println("Point #"+i+": "+points[i].pt.x+","+points[i].pt.y);

			    }
			    //System.out.println();
		    }
		    
		    
		    
		    
		    /*
		    Mat circles = new Mat();
		    Vector<Mat> circlesList = new Vector<Mat>();
		    
		  
		    double x = 0.0;
		    double y = 0.0;
		    int r = 0;
		    for( int i = 0; i < circles.rows(); i++ ) {
		         double[] data = circles.get(i, 0);
		         for(int j = 0 ; j < data.length ; j++){
		              x = data[0];
		              y = data[1];
		              r = (int) data[2];
		         }
		    } 
		    Point center = new Point(x,y); 
		    Imgproc.circle( gg, center, 3, new Scalar(0,255,0), -1);
		    Imgproc.circle( gg, center, r, new Scalar(0,0,255), 1);
		    Rect bbox = new Rect((int)Math.abs(x-r), (int)Math.abs(y-r), (int)2*r, (int)2*r);
		    Mat croped_image = new Mat(gg, bbox);
		    Imgproc.resize(croped_image, croped_image, new Size(160,160));
		    circlesList.add(croped_image);
		    this.img2 = Main.MatToBufferedImage(croped_image, null);
		    */
		}
	}
}
