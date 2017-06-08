package fuk;

import de.yadrone.base.ARDrone;

public class CircleARObject {
	
	public static final boolean DEBUG_MODE = false;
	public static double screenWidth = 640;
	public static double screenHeight = 360;
	public static double xIntervals = 35;
	public static double yIntervals = 30;
	
	
	public static boolean moveBasedOnLocation(ARDrone drone, double x, double y, boolean height){
			if(x > ((screenWidth/2)+xIntervals)){
				if(!DEBUG_MODE){
					System.out.println("To center, go right.");
					drone.getCommandManager().goRight(20).doFor(230);
					drone.getCommandManager().hover().doFor(500);
				} else{
					System.out.println("CENTER | GO RIGHT!");
				}
			} else if(x < ((screenWidth/2)-xIntervals)){
				if(!DEBUG_MODE){
					System.out.println("To center, go left.");
					drone.getCommandManager().goLeft(20).doFor(230);
					drone.getCommandManager().hover().doFor(500);
				} else{
					System.out.println("CENTER | GO LEFT!");
				}
			} else if(y > ((screenHeight/2)+yIntervals)){
				if(!DEBUG_MODE){
					if(height){
						return true;
					}
					System.out.println("To center, go up.");
					drone.getCommandManager().up(20).doFor(150);
					drone.getCommandManager().hover().doFor(500);
				} else{
					System.out.println("CENTER | GO UP!");
				}
			} else if(y < ((screenHeight/2)-yIntervals)){
				if(!DEBUG_MODE){
					if(height){
						return true;
					}
					System.out.println("To center, go down.");
					drone.getCommandManager().down(20).doFor(120);
					drone.getCommandManager().hover().doFor(500);
				} else{
					System.out.println("CENTER | GO UP!");
				}
			}else{
				if(!DEBUG_MODE){
					System.out.println("WE GOING FORWARD NOW BOIS!");
					drone.getCommandManager().hover().doFor(200);
					return true;
				} else{
					System.out.println("CENTER | GO CENTER!");
					return true;
				}
			}
			return false;
	}
}