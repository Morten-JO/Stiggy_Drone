package frames;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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

import controllers.BasicController;
import de.yadrone.base.ARDrone;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.video.ImageListener;

public class VideoFrame extends JFrame{

	private BufferedImage img;
	public static BufferedImage img2;
	public static KeyPoint[] point;
	
	public VideoFrame(final ARDrone drone, BasicController control){
		super("YADrone Tutorial");
        
        setSize(640, 800);
        setVisible(true);
        
        drone.getVideoManager().addImageListener(new ImageListener() {
            public void imageUpdated(BufferedImage newImage)
            {
            	control.updateImg(newImage);
            	img = newImage;
        		SwingUtilities.invokeLater(new Runnable() {
        			public void run()
        			{
        				repaint();
        			}
        		});
            }
        });
	}
	
	
	
	public void paint(Graphics g)
    {
	    if(img != null){
	    	g.drawImage(img, 0, 0, this);
	    }
	    if(img2 != null){
	    	g.drawImage(img2, 0, 360, this);
	    } 
	    g.setColor(Color.GREEN);
	    g.drawString("Current State: "+this.getNameOfState(), 20, 20);
	    g.setColor(Color.GREEN);
	    if(point != null){
	    	//g.drawOval((int)(point.pt.x), (int)(point.pt.y), back, back);
	    	for(int i = 0; i < point.length; i++){
	    		if(point[i] != null){
	    			int back = (int)(point[i].size/2*1.3);
	    			g.drawOval((int)(point[i].pt.x), (int)(point[i].pt.y), back, back);
	    		}
	    	}
	    }
    }
	
	private String getNameOfState(){
		switch(BasicController.currentState){
		case 1:
			return "On Ground";
		case 2:
			return "In Air";
		case 3:
			return "Search QR";
		case 6:
			return "Branner";
		case 7:
			return "Circledetection";
		case 8:
			return "Flythrough";
		case 9:
			return "CheckFlown";
		default:
			return "Error";
		}
	}
	
	
}
