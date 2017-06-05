package dao;

import de.yadrone.base.ARDrone;

public class CircleARObject {
	
	public static final boolean DEBUG_MODE = false;
	public static double screenWidth = 640;
	public static double screenHeight = 360;
	public static double xIntervals = 30;
	public static double yIntervals = 30;
	
	
	public static boolean moveBasedOnLocation(ARDrone drone, double x, double y, boolean height){
			if(x > (screenWidth/2+xIntervals)){
				if(!DEBUG_MODE){
					System.out.println("RIIIIIIIIGHT");
					drone.getCommandManager().goRight(20).doFor(150);
					
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					drone.getCommandManager().hover();
					
				} else{
					System.out.println("CENTER | GO RIGHT!");
				}
			} else if(x < (screenWidth/2-xIntervals)){
				if(!DEBUG_MODE){
					System.out.println("LEEEFT");
					drone.getCommandManager().goLeft(20).doFor(150);
					
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					drone.getCommandManager().hover();
					
				} else{
					System.out.println("CENTER | GO LEFT!");
				}
			} else if(y > (screenHeight/2+yIntervals)){
				if(!DEBUG_MODE){
					if(height){
						return true;
					}
					System.out.println("UPPPPPPPP");
					drone.getCommandManager().up(20).doFor(100);
					try {
						Thread.sleep(120);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					drone.getCommandManager().hover();
					
				} else{
					System.out.println("CENTER | GO UP!");
				}
			} else if(y < (screenHeight/2-yIntervals)){
				if(!DEBUG_MODE){
					if(height){
						return true;
					}
					System.out.println("DOWWWWWWN");
					drone.getCommandManager().down(20).doFor(80);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					drone.getCommandManager().hover();
					
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