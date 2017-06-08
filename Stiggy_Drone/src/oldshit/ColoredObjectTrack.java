package oldshit;

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
class Panel extends JPanel{  
  private static final long serialVersionUID = 1L;  
  private BufferedImage image;    
  // Create a constructor method  
  public Panel(){  
    super();  
  }  
  private BufferedImage getimage(){  
    return image;  
  }  
  public void setimage(BufferedImage newimage){  
    image=newimage;  
    return;  
  }  
  public void setimagewithMat(Mat newimage){  
    image=this.matToBufferedImage(newimage);  
    return;  
  }  
  /**  
   * Converts/writes a Mat into a BufferedImage.  
   *  
   * @param matrix Mat of type CV_8UC3 or CV_8UC1  
   * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY gg 
   */  
  public BufferedImage matToBufferedImage(Mat matrix) {  
    int cols = matrix.cols();  
    int rows = matrix.rows();  
    int elemSize = (int)matrix.elemSize();  
    byte[] data = new byte[cols * rows * elemSize];  
    int type;  
    matrix.get(0, 0, data);  
    switch (matrix.channels()) {  
      case 1:  
        type = BufferedImage.TYPE_BYTE_GRAY;  
        break;  
      case 3:  
        type = BufferedImage.TYPE_3BYTE_BGR;  
        // bgr to rgb  
        byte b;  
        for(int i=0; i<data.length; i=i+3) {  
          b = data[i];  
          data[i] = data[i+2];  
          data[i+2] = b;  
        }  
        break;  
      default:  
        return null;  
    }  
    BufferedImage image2 = new BufferedImage(cols, rows, type);  
    image2.getRaster().setDataElements(0, 0, cols, rows, data);  
    return image2;  
  }  
  @Override  
  protected void paintComponent(Graphics g){  
     super.paintComponent(g);  
     //BufferedImage temp=new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);  
     BufferedImage temp=getimage();  
     //Graphics2D g2 = (Graphics2D)g;
     if( temp != null)
       g.drawImage(temp,10,10,temp.getWidth(),temp.getHeight(), this);  
  }  
}  
public class ColoredObjectTrack {  
  public Mat getCircle(BufferedImage image){  
	Mat img =  bufferedImageToMat(image);
    // Load the native library.  
	  System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    // It is better to group all frames together so cut and paste to  
    // create more frames is easier  
    
    //-- 2. Read the video stream  
    //org.opencv.videoio.VideoCapture capture = new org.opencv.videoio.VideoCapture(0);
      
    Mat webcam_image= img;  
    Mat hsv_image=new Mat();  
    Mat thresholded=new Mat();  
    Mat thresholded2=new Mat();  
     //capture.read(webcam_image);  
    Mat array255=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);  
    array255.setTo(new Scalar(255));  
    /*Mat S=new Mat();  
    S.ones(new Size(hsv_image.width(),hsv_image.height()),CvType.CV_8UC1);  
    Mat V=new Mat();  
    V.ones(new Size(hsv_image.width(),hsv_image.height()),CvType.CV_8UC1);  
        Mat H=new Mat();  
    H.ones(new Size(hsv_image.width(),hsv_image.height()),CvType.CV_8UC1);*/  
    Mat distance=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);  
    //new Mat();//new Size(webcam_image.width(),webcam_image.height()),CvType.CV_8UC1);  
    List<Mat> lhsv = new ArrayList<Mat>(3);      
    Mat circles = new Mat(); // No need (and don't know how) to initialize it.  
                 // The function later will do it... (to a 1*N*CV_32FC3)  
    Scalar hsv_min = new Scalar(0, 30, 20);  
    Scalar hsv_max = new Scalar(0, 255, 255);  
    Scalar hsv_min2 = new Scalar(170, 70, 50, 0);  
    Scalar hsv_max2 = new Scalar(180, 255, 255, 0);   
    double[] data=new double[3];  
    
         // One way to select a range of colors by Hue  
         Imgproc.cvtColor(webcam_image, hsv_image, Imgproc.COLOR_BGR2HSV);  
         Core.inRange(hsv_image, hsv_min, hsv_max, thresholded);           
         Core.inRange(hsv_image, hsv_min2, hsv_max2, thresholded2);  
         Core.bitwise_or(thresholded, thresholded2, thresholded);  
         // Notice that the thresholds don't really work as a "distance"  
         // Ideally we would like to cut the image by hue and then pick just  
         // the area where S combined V are largest.  
         // Strictly speaking, this would be something like sqrt((255-S)^2+(255-V)^2)>Range  
         // But if we want to be "faster" we can do just (255-S)+(255-V)>Range  
         // Or otherwise 510-S-V>Range  
         // Anyhow, we do the following... Will see how fast it goes...  
         Core.split(hsv_image, lhsv); // We get 3 2D one channel Mats  
         Mat S = lhsv.get(1);  
         Mat V = lhsv.get(2);  
         Core.subtract(array255, S, S);  
         Core.subtract(array255, V, V);  
         S.convertTo(S, CvType.CV_32F);  
         V.convertTo(V, CvType.CV_32F);  
         Core.magnitude(S, V, distance);  
         Core.inRange(distance,new Scalar(0.0), new Scalar(200.0), thresholded2);  
         Core.bitwise_and(thresholded, thresholded2, thresholded);  
         // Apply the Hough Transform to find the circles  
         Imgproc.GaussianBlur(thresholded, thresholded, new Size(9,9),0,0);  
         Imgproc.HoughCircles(thresholded, circles, Imgproc.CV_HOUGH_GRADIENT, 2, thresholded.height()/4, 500, 50, 0, 0);   
         //Imgproc.Canny(thresholded, thresholded, 500, 250);  
         //-- 4. Add some info to the image  
         Imgproc.line(webcam_image, new Point(150,50), new Point(202,200), new Scalar(100,10,10)/*CV_BGR(100,10,10)*/, 3);  
         Imgproc.circle(webcam_image, new Point(210,210), 10, new Scalar(100,10,10),3);  
         data=webcam_image.get(210, 210);  
         Imgproc.putText(webcam_image,String.format("("+String.valueOf(data[0])+","+String.valueOf(data[1])+","+String.valueOf(data[2])+")"),new Point(30, 30) , 3 //FONT_HERSHEY_SCRIPT_SIMPLEX  
              ,1.0,new Scalar(100,10,10,255),3);  
         //int cols = circles.cols();  
          int rows = circles.rows();  
          int elemSize = (int)circles.elemSize(); // Returns 12 (3 * 4bytes in a float)  
          float[] data2 = new float[rows * elemSize/4];  
          if (data2.length>0){  
            circles.get(0, 0, data2); // Points to the first element and reads the whole thing  
                          // into data2  
            for(int i=0; i<data2.length; i=i+3) {  
              Point center= new Point(data2[i], data2[i+1]);  
              //Core.ellipse( this, center, new Size( rect.width*0.5, rect.height*0.5), 0, 0, 360, new Scalar( 255, 0, 255 ), 4, 8, 0 );  
             // Imgproc.ellipse( webcam_image, center, new Size((double)data2[i+2], (double)data2[i+2]), 0, 0, 360, new Scalar( 255, 0, 255 ), 4, 8, 0 );  
              double[] dato = circles.get(i, 0);
             int r = (int) dato[2];
              Imgproc.circle(webcam_image, center, r, new Scalar(0,255,0),5);
              System.out.println("Radius er " + r);
            }  
          }  
          Imgproc.line(hsv_image, new Point(150,50), new Point(202,200), new Scalar(100,10,10)/*CV_BGR(100,10,10)*/, 3);  
          Imgproc.circle(hsv_image, new Point(210,210), 10, new Scalar(100,10,10),3);  
         data=hsv_image.get(210, 210);  
         Imgproc.putText(hsv_image,String.format("("+String.valueOf(data[0])+","+String.valueOf(data[1])+","+String.valueOf(data[2])+")"),new Point(30, 30) , 3 //FONT_HERSHEY_SCRIPT_SIMPLEX  
              ,1.0,new Scalar(100,10,10,255),3);  
         distance.convertTo(distance, CvType.CV_8UC1);  
         Imgproc.line(distance, new Point(150,50), new Point(202,200), new Scalar(100)/*CV_BGR(100,10,10)*/, 3);  
         Imgproc.circle(distance, new Point(210,210), 10, new Scalar(100),3);  
         data=(double[])distance.get(210, 210);  
         Imgproc.putText(distance,String.format("("+String.valueOf(data[0])+")"),new Point(30, 30) , 3 //FONT_HERSHEY_SCRIPT_SIMPLEX  
              ,1.0,new Scalar(100),3);   
         //-- 5. Display the image  
        
    return webcam_image ;  
    
  }
  public static Mat bufferedImageToMat(BufferedImage bi) {
	  Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
	  byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
	  mat.put(0, 0, data);
	  return mat;
	}
  
} 