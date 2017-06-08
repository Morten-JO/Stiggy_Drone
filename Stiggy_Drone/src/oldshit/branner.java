package oldshit;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class branner {
	

	public Mat getBranner(BufferedImage image){ 
		Mat img =  bufferedImageToMat(image);
		
		Mat mat = img;

		/* convert to grayscale */
		int colorChannels = (mat.channels() == 3) ? Imgproc.COLOR_BGR2GRAY
		        : ((mat.channels() == 4) ? Imgproc.COLOR_BGRA2GRAY : 1);

		Mat grayMat = new Mat();
		;
		Imgproc.cvtColor(mat, grayMat, colorChannels);
		
        
       

		/* reduce the noise so we avoid false circle detection */
		Imgproc.GaussianBlur(grayMat, grayMat, new Size(9, 9), 2, 2);

		// accumulator value
		double dp = 1.2d;
		// minimum distance between the center coordinates of detected circles in pixels
		double minDist = 100;

		// min and max radii (set these values as you desire)
		int minRadius = 50, maxRadius = 0;

		// param1 = gradient value used to handle edge detection
		// param2 = Accumulator threshold value for the
		// cv2.CV_HOUGH_GRADIENT method.
		// The smaller the threshold is, the more circles will be
		// detected (including false circles).
		// The larger the threshold is, the more circles will
		// potentially be returned.
		double param1 = 70, param2 = 72;

		/* create a Mat object to store the circles detected */
		Mat circles = new Mat(mat.width(),
		        mat.height(), CvType.CV_8UC1);

		/* find the circle in the image */
		Imgproc.HoughCircles(grayMat, circles,
		        Imgproc.CV_HOUGH_GRADIENT, dp, minDist, param1,
		        param2, minRadius, maxRadius);
		
		   
	        

		/* get the number of circles detected */
		int numberOfCircles = (circles.rows() == 0) ? 0 : circles.cols();

		/* draw the circles found on the image */
		for (int i=0; i<numberOfCircles; i++) {


		/* get the circle details, circleCoordinates[0, 1, 2] = (x,y,r)
		 * (x,y) are the coordinates of the circle's center
		 */
		    double[] circleCoordinates = circles.get(0, i);


		    int x = (int) circleCoordinates[0], y = (int) circleCoordinates[1];

		    Point center = new Point(x, y);

		    int radius = (int) circleCoordinates[2];

		    /* circle's outline */
		    Imgproc.circle(mat, center, radius, new Scalar(0,
		            255, 0), 4);
		    
System.out.println("Radius" + radius);


		    /* circle's center outline */
		    Imgproc.rectangle(mat, new Point(x - 5, y - 5),
		            new Point(x + 5, y + 5),
		            new Scalar(0, 128, 255), -1);
		}

	      
		return mat;   
}
	
	 public static Mat bufferedImageToMat(BufferedImage bi) {
		  Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		  byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		  mat.put(0, 0, data);
		  return mat;
		}
	 
	
}
