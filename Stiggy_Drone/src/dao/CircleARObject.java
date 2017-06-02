package dao;

import de.yadrone.base.ARDrone;

public class CircleARObject {
	
	public static final boolean DEBUG_MODE = true;
	public static double screenWidth = 640;
	public static double screenHeight = 360;
	public static double xIntervals = 30;
	public static double yIntervals = 30;
	
	
	public static boolean moveBasedOnLocation(ARDrone drone, double x, double y){
			if(x > (screenWidth/2+xIntervals)){
				if(!DEBUG_MODE){
					drone.getCommandManager().goRight(35);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					drone.getCommandManager().hover();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else{
					System.out.println("CENTER | GO RIGHT!");
				}
			} else if(x < (screenWidth/2-xIntervals)){
				if(!DEBUG_MODE){
					drone.getCommandManager().goLeft(35);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					drone.getCommandManager().hover();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else{
					System.out.println("CENTER | GO LEFT!");
				}
			} else if(y > (screenHeight/2+yIntervals)){
				if(!DEBUG_MODE){
					drone.getCommandManager().up(35);
					try {
						Thread.sleep(80);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					drone.getCommandManager().hover();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else{
					System.out.println("CENTER | GO UP!");
				}
			} else if(y < (screenHeight/2-yIntervals)){
				if(!DEBUG_MODE){
					drone.getCommandManager().down(5);
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					drone.getCommandManager().hover();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else{
					System.out.println("CENTER | GO UP!");
				}
			}else{
				if(!DEBUG_MODE){
					System.out.println("WE GOING FORWARD NOW BOIS!");
					drone.getCommandManager().hover();
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				} else{
					System.out.println("CENTER | GO CENTER!");
					return true;
				}
			}
			return false;
	}
}