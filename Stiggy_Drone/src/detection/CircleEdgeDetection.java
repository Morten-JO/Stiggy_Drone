package detection;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.opencv.core.CvType;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import controllers.BasicController;
import frames.VideoFrame;
import helpers.Toolkit;

public class CircleEdgeDetection {

	private static int threshold = 53;
	
	/**
	 * Checks for a circle based on edges, takes the biggest circle into account
	 * @param img
	 * @return Keypoint with center of circle, or null if error.
	 */
	public static KeyPoint checkForCircle(BufferedImage img, BasicController controller){
		KeyPoint pointToReturn = null;
		if(img != null){
			Mat gg = Toolkit.bufferedImageToMat(img);
			if(gg != null){
			    Mat gray = new Mat();
			    Imgproc.cvtColor(gg, gray, Imgproc.COLOR_BGR2GRAY);
			    //Imgproc.blur(gray, gray, new Size(3, 3));
			    Imgproc.GaussianBlur(gray, gray, new Size(9,9), 2,2);
			    Mat edges = new Mat();
			    int lowThreshold = threshold;
			    int ratio = 3;
			    
			    Imgproc.Canny(gray, edges, lowThreshold, lowThreshold * ratio);
			    
			    Mat circles = new Mat();
			    Vector<Mat> circlesList = new Vector<Mat>();
			    Imgproc.HoughCircles(edges, circles, Imgproc.CV_HOUGH_GRADIENT, 1, gray.rows()/8, 600, 50, 60, 200);
			    List<MatOfPoint> removedContoursList = new ArrayList<MatOfPoint>(); 
			    for (int id=0;id<circlesList.size();id++){                     
		            MatOfPoint2f mop2f = new MatOfPoint2f();
		            circlesList.get(id).convertTo(mop2f,CvType.CV_32F);
		            RotatedRect rectangle = Imgproc.minAreaRect(mop2f);
		            if (rectangle.boundingRect().height<10){
		                removedContoursList.add((MatOfPoint) circlesList.get(id));
		                System.out.println("removing: "+rectangle.boundingRect());
		                circlesList.remove(id);
		                id--;
		            }
		        }
			    
			    
			    double x = 0.0;
			    double y = 0.0;
			    int r = 0;
			    KeyPoint[] points = new KeyPoint[circles.rows()];
			    VideoFrame.point = points;
			    for(int i = 0; i < circles.rows(); i++){
			    	double[] data = circles.get(i, 0);
			    	for(int j = 0; j < data.length; j++){
			    		x = data[0];
			    		y = data[1];
			    		r = (int) data[2];
			    	}
			    	points[i] = new KeyPoint((float)x, (float)y, (float)r);
			    	pointToReturn = points[i];
			    	
			    }
			     
			    for (int i = 0; i < points.length; i++) {
					Imgproc.circle(edges, new Point(points[i].pt.x, points[i].pt.y), (int)points[i].size,new Scalar(100,10,10),3);
				}
			    BufferedImage img2 = null;
			    img2 = Toolkit.MatToBufferedImage(edges, BufferedImage.TYPE_BYTE_GRAY);
			    if(img2 == null){
			    	System.out.println("IMGI IS NULL");
			    } else{
			    	VideoFrame.img2 = img2;
			    }
			}
		}
		return pointToReturn;
	}
}
