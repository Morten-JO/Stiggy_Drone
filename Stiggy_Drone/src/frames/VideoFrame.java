package frames;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.opencv.core.KeyPoint;

import controllers.BasicController;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.navdata.BatteryListener;
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
	private int speed = 20;
	
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
        
        addKeyListener();
		addMouseListener();
		
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
	    g.setColor(Color.RED);
	    g.drawString("Current State: "+this.getNameOfState(), 20, 50);
	    g.drawString("Current instruction: "+CommandManager.currentlyDoing, 20, 80);
	    g.setColor(Color.GREEN);
	    Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
	    if(point != null){
	    	g.drawRect(320-25, 180-25, 50, 50);
	    	for(int i = 0; i < point.length; i++){
	    		if(point[i] != null){
	    			int back = (int)(point[i].size/2*1.3);
	    			g2.drawOval((int)(point[i].pt.x), (int)(point[i].pt.y), back, back);
	    		}
	    	}
	    }
	    if(first != null && second != null && third != null){
	    	g2.drawLine((int)first.getX(), (int)first.getY(), (int)second.getX(), (int)second.getY());
	    	g2.drawLine((int)second.getX(), (int)second.getY(), (int)third.getX(), (int)third.getY());
	    	g2.drawLine((int)first.getX(), (int)first.getY(), (int)second.getX(), (int)second.getY());
	    }
    }
	
	private String getNameOfState(){
		switch(BasicController.currentState){
		case 0:
			return "Restart";
		case 1:
			return "On Ground";
		case 2:
			return "In Air";
		case 3:
			return "Search QR";
		case 6:
			return "FLYUP";
		case 7:
			return "Circledetection";
		case 8:
			return "Flythrough";
		case 9:
			return "CheckFlown";
		case 10:
			return "Finish";
		case 11:
			return "Stray";
		case 12:
			return "Spin";
		case 36:
			return "Error";
		case 666:
			return "Test";
		default:
			return "Mega error";
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
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private void addMouseListener(){
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				drone.getCommandManager().setVideoChannel(VideoChannel.NEXT);
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {}
		});
	}
	
	private void addKeyListener(){
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				final IARDrone temp = drone;
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					System.out.println("Take off");
					temp.getCommandManager().takeOff();
				} else if(e.getKeyCode() == KeyEvent.VK_X){
					System.out.println("Landing");
					temp.getCommandManager().landing();
				} else if(e.getKeyCode() == KeyEvent.VK_Y){
					speed += 10;
				} else if(e.getKeyCode() == KeyEvent.VK_H){
					speed -= 10;
				} else if(e.getKeyCode() == KeyEvent.VK_LEFT){
					temp.getCommandManager().hover();
				} else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					temp.getCommandManager().hover();
				} else if(e.getKeyCode() == KeyEvent.VK_UP){
					temp.getCommandManager().hover();
				} else if(e.getKeyCode() == KeyEvent.VK_DOWN){
					temp.getCommandManager().hover();
				} else if(e.getKeyCode() == KeyEvent.VK_A){
					temp.getCommandManager().hover();
				} else if(e.getKeyCode() == KeyEvent.VK_S){
					temp.getCommandManager().hover();
				} else if(e.getKeyCode() == KeyEvent.VK_D){
					temp.getCommandManager().hover();
				} else if(e.getKeyCode() == KeyEvent.VK_W){
					temp.getCommandManager().hover();
				}
				else if(e.getKeyCode() == KeyEvent.VK_E){
					temp.getNavDataManager().addBatteryListener(new BatteryListener() {
						
						@Override
						public void voltageChanged(int arg0) {
							// TODO Auto-generated method stub
						}
						
						@Override
						public void batteryLevelChanged(int arg0) {
							System.out.println("Battery currently at " + arg0 + "%");
							
						}
					});;
					
				}else if(e.getKeyCode() == KeyEvent.VK_Q){
					Main.userControl = !Main.userControl;
					System.out.println("User control = " + Main.userControl);
				} else if(e.getKeyCode() == KeyEvent.VK_T){
					Main.closeDrone();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				final IARDrone temp = drone;
				if(e.getKeyCode() == KeyEvent.VK_LEFT){
					temp.getCommandManager().goLeft(speed).doFor(500);						
				} else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					temp.getCommandManager().goRight(speed).doFor(500);
				} else if(e.getKeyCode() == KeyEvent.VK_UP){
					temp.getCommandManager().forward(speed).doFor(500);
				} else if(e.getKeyCode() == KeyEvent.VK_DOWN){
					temp.getCommandManager().backward(speed).doFor(500);
				} else if(e.getKeyCode() == KeyEvent.VK_A){
					temp.getCommandManager().spinLeft(70).doFor(500);
				} else if(e.getKeyCode() == KeyEvent.VK_S){
					temp.getCommandManager().down(speed).doFor(500);
				} else if(e.getKeyCode() == KeyEvent.VK_D){
					temp.getCommandManager().spinRight(70).doFor(500);						
				} else if(e.getKeyCode() == KeyEvent.VK_W){
					temp.getCommandManager().up(speed).doFor(500);
				} else if(e.getKeyCode() == KeyEvent.VK_O){
					BasicController.currentState = BasicController.ONGROUND;
				}
			}
		});
	}
	
	
}
