package frames;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

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

public class VideoFrame extends JPanel{

	private BufferedImage img;
	private BufferedImage img2;
	
	public VideoFrame(){
	}
	
	public void update(BufferedImage img){
		this.img = img;
		repaint();
	}
	
	public void updateImageTwo(BufferedImage img){
		this.img2 = img;
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
	    if(img != null){
	    	g.drawImage(img, 0, 0, this);
	    }
	    if(img2 != null){
	    	g.drawImage(img2, 0, img.getHeight(), img.getWidth(), img.getHeight(), this);
	    }
	    g.setColor(Color.RED);
	    g.drawString("Current State: "+this.getNameOfState(), 10, 10);
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
			return "Hjorten";
		case 8:
			return "Flythrough";
		case 9:
			return "CheckFlown";
		default:
			return "Error";
		}
	}
	
	
}
