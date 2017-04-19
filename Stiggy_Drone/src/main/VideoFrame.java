package main;

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
	}
	
	
}
