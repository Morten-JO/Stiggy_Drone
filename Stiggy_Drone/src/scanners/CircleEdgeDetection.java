package scanners;

import java.awt.image.BufferedImage;

import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import frames.VideoFrame;
import helpers.ToolkitKit;

public class CircleEdgeDetection {

	private static int threshold = 120;
	
	/**
	 * Checks for a circle based on edges, takes the biggest circle into account
	 * @param img
	 * @return Keypoint with center of circle, or null if error.
	 */
	public static KeyPoint checkForCircle(BufferedImage img){
		KeyPoint pointToReturn = null;
		if(img != null){
			//threshold += 5;
			BufferedImage image = img;
		    Mat gg = ToolkitKit.bufferedImageToMat(image);
		    Mat gray = new Mat();
		    Imgproc.cvtColor(gg, gray, Imgproc.COLOR_BGR2GRAY);
		    Imgproc.blur(gray, gray, new Size(9, 9));
		    Mat edges = new Mat();
		    int lowThreshold = threshold;
		    int ratio = 1;
		    Imgproc.Canny(gray, edges, lowThreshold, lowThreshold * ratio);
		    Mat circles = new Mat();
		    Imgproc.HoughCircles(edges, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 1, 140, 80, 10, 300);
		    
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
		    BufferedImage imgfg = ToolkitKit.MatToBufferedImage(edges, BufferedImage.TYPE_BYTE_GRAY);
		    ToolkitKit.saveImage(imgfg, ""+threshold);
		    KeyPoint[] points = keypoints.toArray();
		    
		    if(points.length > 0){
			    for(int i = 0; i < points.length; i++){
			    	System.out.println("RADIUS OF CIRCLE #"+i+" : "+points[i].size);
			    	if(points[i].size > 90f){
			    		System.out.println("Point #"+i+": "+points[i].pt.x+","+points[i].pt.y+" with size: "+points[i].size);
			    		System.out.println("FOUND BIG ENOUGH CIRCLE!!!!! IT MUST BE WHAT WE NEED TO GO THROUGH");
			    		System.out.println("ANGLE: "+points[i].angle);
			    		System.out.println("OCTAVE: "+ points[i].octave);
			    		System.out.println("RESPONSE: "+points[i].response);
			    		System.out.println("CLASS ID: "+points[i].class_id);
			    		System.out.println("   ");
			    		if(pointToReturn == null){
			    			pointToReturn = points[i];
			    		} else if(pointToReturn.size < points[i].size){
				    		pointToReturn = points[i];
			    		}
			    	}
			    }
			    //System.out.println();
		    }
		}
		VideoFrame.point = pointToReturn;
		return pointToReturn;
	}
	
}
