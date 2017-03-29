package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class VideoFrame extends JPanel{

	private BufferedImage img;
	private BufferedImage img2;
	
	public void update(BufferedImage img){
		this.img = img;
		checkForCircle();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
	    if (img == null && img2 == null){
	    	super.paintComponents(g);
	    	return;
	    }
	    if(img != null){
	    	g.drawImage(img, 0, 0, this);
	    }
	    if(img2 != null){
	    	g.drawImage(img2, 0, img.getHeight(), this);
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
		    this.img2 = Main.MatToBufferedImage(croped_image, BufferedImage.TYPE_BYTE_GRAY);
		}
	}
}
