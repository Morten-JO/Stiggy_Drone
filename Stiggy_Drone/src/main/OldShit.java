package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

public class OldShit {

	private int count = 0;
	private int threshold = 140;
	private int ratio = 1;
	private int relax = 0;
	private List<KeyPoint> points;
	private BufferedImage img2;
	/**
	 * g.setColor(Color.RED);
	    	for(int i = 0 ; i < this.points.size(); i++){
	    		double size = 2 * points.get(i).size;
	    		g.drawOval((int)points.get(i).pt.x - (int)(0.5f * size), (int)points.get(i).pt.y - (int)(0.5f * size), (int)size, (int)size);
	    	}
	 */
	
	public OldShit(){
		points = new ArrayList<KeyPoint>();
	}
	
	public void checkForCircle(BufferedImage img){
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
		}
	}
	
}
