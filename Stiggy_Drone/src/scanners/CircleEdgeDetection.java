package scanners;

import java.awt.image.BufferedImage;

import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import helpers.ToolkitKit;

public class CircleEdgeDetection {

	private static int threshold = 140;
	
	/**
	 * Checks for a circle based on edges, takes the biggest circle into account
	 * @param img
	 * @return Keypoint with center of circle, or null if error.
	 */
	public static KeyPoint checkForCircle(BufferedImage img){
		if(img != null){
			BufferedImage image = img;
		    Mat gg = ToolkitKit.bufferedImageToMat(image);
		    Mat gray = new Mat();
		    Imgproc.cvtColor(gg, gray, Imgproc.COLOR_BGR2GRAY);
		    Imgproc.blur(gray, gray, new Size(3, 3));
		    Mat edges = new Mat();
		    int lowThreshold = threshold;
		    int ratio = 1;
		    Imgproc.Canny(gray, edges, lowThreshold, lowThreshold * ratio);
		    Mat circles = new Mat();
		    Imgproc.HoughCircles(edges, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 60, 200, 20, 30, 0 );
		    
		    FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
		    detector.read("whatevs.xml");
		    MatOfKeyPoint keypoints = new MatOfKeyPoint();
		    try{
		    	detector.detect(edges, keypoints);
		    } catch(Exception e){
		    }
		    
		    Mat out = new Mat();
		    org.opencv.core.Scalar cores = new org.opencv.core.Scalar(0, 255, 0);
		    org.opencv.features2d.Features2d.drawKeypoints(edges, keypoints, out);
		    KeyPoint[] points = keypoints.toArray();
		    if(points.length > 0){
			    for(int i = 0; i < points.length; i++){
			    	if(points[i].size > 90f){
			    		System.out.println("Point #"+i+": "+points[i].pt.x+","+points[i].pt.y+" with size: "+points[i].size);
			    		System.out.println("FOUND BIG ENOUGH CIRCLE!!!!! IT MUST BE WHAT WE NEED TO GO THROUGH");
			    		System.out.println("ANGLE: "+points[i].angle);
			    		System.out.println("OCTAVE: "+ points[i].octave);
			    		System.out.println("RESPONSE: "+points[i].response);
			    		System.out.println("CLASS ID: "+points[i].class_id);
			    		System.out.println("   ");
			    		return points[i];
			    	}
			    }
			    //System.out.println();
		    }
		}
		return null;
	}
	
}
