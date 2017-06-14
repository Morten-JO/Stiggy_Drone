package frames;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import controllers.BasicController;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.navdata.BatteryListener;
import main.Main;

public class ControlFrame extends JFrame{
	
	private int speed = 20;
	private ARDrone drone;
	
	public ControlFrame(ARDrone drone){
		super("Control panel.");
		this.setSize(150, 150);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX() - this.getWidth();
        int y = 0;
        this.setLocation(x, y);
		
		this.setVisible(true);
		this.drone = drone;
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                Main.closeDrone();
            }
        });
        
		addKeyListener();
		addMouseListener();
		
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
					temp.getCommandManager().goLeft(speed);						
				} else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					temp.getCommandManager().goRight(speed);
				} else if(e.getKeyCode() == KeyEvent.VK_UP){
					temp.getCommandManager().forward(speed);
				} else if(e.getKeyCode() == KeyEvent.VK_DOWN){
					temp.getCommandManager().backward(speed);
				} else if(e.getKeyCode() == KeyEvent.VK_A){
					temp.getCommandManager().spinLeft(70);
				} else if(e.getKeyCode() == KeyEvent.VK_S){
					temp.getCommandManager().down(speed);
				} else if(e.getKeyCode() == KeyEvent.VK_D){
					temp.getCommandManager().spinRight(70);						
				} else if(e.getKeyCode() == KeyEvent.VK_W){
					temp.getCommandManager().up(speed);
				} else if(e.getKeyCode() == KeyEvent.VK_O){
					BasicController.currentState = BasicController.ONGROUND;
				}
			}
		});
	}

}
