package frames;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.opencv.core.KeyPoint;

import controllers.BasicController;
import de.yadrone.base.ARDrone;
import de.yadrone.base.video.ImageListener;
import main.Main;

public class VideoFrame extends JFrame implements Runnable{

	private BufferedImage img;
	public static BufferedImage img2;
	public static KeyPoint[] point;
	private ARDrone drone;
	private BasicController control;
	private boolean updated;
	public boolean running = true;
	public static Point first;
	public static Point second;
	public static Point third;
	
	public VideoFrame(final ARDrone drone, BasicController control){
		super("YADrone Tutorial");
        
        setSize(640, 360);
        setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                Main.closeDrone();
            }
        });
        this.drone = drone;
        this.control = control;
        drone.getVideoManager().addImageListener(new ImageListener() {
            public void imageUpdated(BufferedImage newImage)
            {
            	updated = true;
            	img = newImage;
        		SwingUtilities.invokeLater(new Runnable() {
        			public void run()
        			{
        				repaint();
        			}
        		});
            }
        });
        Thread thread = new Thread(this);
        thread.start();
        
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
	    	g.drawRect(320-25, 180-25, 50, 50);
	    	//g.drawOval((int)(point.pt.x), (int)(point.pt.y), back, back);
	    	for(int i = 0; i < point.length; i++){
	    		if(point[i] != null){
	    			int back = (int)(point[i].size/2*1.3);
	    			g.drawOval((int)(point[i].pt.x), (int)(point[i].pt.y), back, back);
	    		}
	    	}
	    }
	    if(first != null && second != null && third != null){
	    	g.drawLine((int)first.getX(), (int)first.getY(), (int)second.getX(), (int)second.getY());
	    	g.drawLine((int)second.getX(), (int)second.getY(), (int)third.getX(), (int)third.getY());
	    	g.drawLine((int)first.getX(), (int)first.getY(), (int)second.getX(), (int)second.getY());
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



	@Override
	public void run() {
		while(running){
			if(updated){
				control.updateImg(img);
				updated = false;
			} else{
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
}
