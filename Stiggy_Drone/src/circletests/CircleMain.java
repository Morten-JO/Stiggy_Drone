package circletests;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import helpers.Toolkit;

public class CircleMain {

	public static void main(String[] args) {
		
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		ImageFrame frame = new ImageFrame();
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("circle.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("COULD NOT LOAD IMG");
			System.exit(0);
		}
		frame.setImage(img);
		long startTime = System.currentTimeMillis();
		int threshold = 0;
		KeyPoint pointToReturn = null;
		if(img != null){
		    Mat gg = Toolkit.bufferedImageToMat(img);
		    Mat gray = new Mat();
		    Imgproc.cvtColor(gg, gray, Imgproc.COLOR_BGR2GRAY);
		    Imgproc.blur(gray, gray, new Size(3, 3));
		    Mat edges = new Mat();
		    int lowThreshold = threshold;
		    int ratio = 3;
		    
		    Imgproc.Canny(gray, edges, lowThreshold, lowThreshold * ratio);
		    
		    Mat circles = new Mat();
		    Vector<Mat> circlesList = new Vector<Mat>();
		    Imgproc.HoughCircles(edges, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 60, 200, 20, 120, 200);
		    System.out.println("Time taken before loop: "+((System.currentTimeMillis()-startTime)));
		    long beforeFor = System.currentTimeMillis();
		    double x = 0.0;
		    double y = 0.0;
		    int r = 0;
		    KeyPoint[] points = new KeyPoint[circles.rows()];
		    for(int i = 0; i < circles.rows(); i++){
		    	double[] data = circles.get(i, 0);
		    	for(int j = 0; j < data.length; j++){
		    		x = data[0];
		    		y = data[1];
		    		r = (int) data[2];
		    	}
		    	points[i] = new KeyPoint((float)x, (float)y, (float)r);
		    	
		    }
		    System.out.println("Time taken for loop: "+((System.currentTimeMillis()-beforeFor)));
		    frame.setPoints(points);
		    
		}
		System.out.println();
	}
}
