package RedCircle;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;  
/*
 * Lavet med hjælp fra : https://stackoverflow.com/questions/43820441/track-specific-color-in-a-video-with-opencv-java
 */

public class ColoredObjectTrack {  
  public Mat getCircle(BufferedImage image){  
	Mat img =  bufferedImageToMat(image);
  
	  System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
   
      
    Mat inputImage= img;  
    Mat hsvImgi=new Mat();  
    Mat thresholdValue1=new Mat();  
    Mat thresholdValue2=new Mat();
    Mat array255=new Mat(inputImage.height(),inputImage.width(),CvType.CV_8UC1);  
    array255.setTo(new Scalar(255));  
    Mat distance=new Mat(inputImage.height(),inputImage.width(),CvType.CV_8UC1);  
    
    List<Mat> lhsv = new ArrayList<Mat>(3);      
    Mat circlesInImgi = new Mat(); 
    Scalar hsv_min = new Scalar(0, 30, 20);  
    Scalar hsv_max = new Scalar(0, 255, 255);  
    Scalar hsv_min2 = new Scalar(170, 70, 50, 0);  
    Scalar hsv_max2 = new Scalar(180, 255, 255, 0);   
    double[] data=new double[3];  
    
        
         Imgproc.cvtColor(inputImage, hsv_image, Imgproc.COLOR_BGR2HSV);  
         Core.inRange(hsvImgi, hsv_min, hsv_max, thresholdValue1);           
         Core.inRange(hsvImgi, hsv_min2, hsv_max2, thresholdValue2);  
         Core.bitwise_or(thresholdValue1, thresholdValue2, thresholdValue1);  
      
         Core.split(hsvImgi, lhsv);
         Mat S = lhsv.get(1);  
         Mat V = lhsv.get(2);  
         Core.subtract(array255, S, S);  
         Core.subtract(array255, V, V);  
         S.convertTo(S, CvType.CV_32F);  
         V.convertTo(V, CvType.CV_32F);  
         Core.magnitude(S, V, distance);  
         Core.inRange(distance,new Scalar(0.0), new Scalar(200.0), thresholdValue2);  
         Core.bitwise_and(thresholdValue1, thresholdValue2, thresholdValue1);  
       
         Imgproc.GaussianBlur(thresholdValue1, thresholdValue1, new Size(9,9),0,0);  
         Imgproc.HoughCircles(thresholdValue1, circlesInImgi, Imgproc.CV_HOUGH_GRADIENT, 2, thresholdValue1.height()/4, 500, 50, 0, 0);   
         
         Imgproc.line(inputImage, new Point(150,50), new Point(202,200), new Scalar(100,10,10)/*CV_BGR(100,10,10)*/, 3);  
         Imgproc.circle(inputImage, new Point(210,210), 10, new Scalar(100,10,10),3);  
         data=inputImage.get(210, 210);  
         Imgproc.putText(inputImage,String.format("("+String.valueOf(data[0])+","+String.valueOf(data[1])+","+String.valueOf(data[2])+")"),new Point(30, 30) , 3 //FONT_HERSHEY_SCRIPT_SIMPLEX  
              ,1.0,new Scalar(100,10,10,255),3);  
    
          int rows = circlesInImgi.rows();  
          int elemSize = (int)circlesInImgi.elemSize();  
          float[] data2 = new float[rows * elemSize/4];  
          if (data2.length>0){  
            circlesInImgi.get(0, 0, data2); / 
                           
            for(int i=0; i<data2.length; i=i+3) {  
              Point center= new Point(data2[i], data2[i+1]);  
               
              double[] dato = circlesInImgi.get(i, 0);
             int r = (int) dato[2];
              Imgproc.circle(inputImage, center, r, new Scalar(0,255,0),5);
              System.out.println("Radius er " + r);
            }  
          }  
          Imgproc.line(hsvImgi, new Point(150,50), new Point(202,200), new Scalar(100,10,10), 3);  
          Imgproc.circle(hsvImgi, new Point(210,210), 10, new Scalar(100,10,10),3);  
         data=hsv_image.get(210, 210);  
         Imgproc.putText(hsvImgi,String.format("("+String.valueOf(data[0])+","+String.valueOf(data[1])+","+String.valueOf(data[2])+")"),new Point(30, 30) , 3 //FONT_HERSHEY_SCRIPT_SIMPLEX  
              ,1.0,new Scalar(100,10,10,255),3);  
         distance.convertTo(distance, CvType.CV_8UC1);  
         Imgproc.line(distance, new Point(150,50), new Point(202,200), new Scalar(100)/*CV_BGR(100,10,10)*/, 3);  
         Imgproc.circle(distance, new Point(210,210), 10, new Scalar(100),3);  
         data=(double[])distance.get(210, 210);  
         Imgproc.putText(distance,String.format("("+String.valueOf(data[0])+")"),new Point(30, 30) , 3 //FONT_HERSHEY_SCRIPT_SIMPLEX  
              ,1.0,new Scalar(100),3);   
     
    return inputImage ;  
    
  }

  
} 