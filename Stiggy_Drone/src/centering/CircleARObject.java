package centering;

import de.yadrone.base.ARDrone;

public class CircleARObject {
	
	public static final boolean DEBUG_MODE = false;
	public static double screenWidth = 640;
	public static double screenHeight = 360;
	public static double xIntervals = 35;
	public static double yIntervals = 30;
	
	
	public static boolean moveBasedOnLocation(ARDrone drone, double x, double y, boolean height){
		if(x > ((screenWidth/2)+xIntervals)){
			System.out.println("To center, go right.");
			drone.getCommandManager().goRight(10).doFor(300);
			drone.getCommandManager().hover().doFor(500);
		} else if(x < ((screenWidth/2)-xIntervals)){
			System.out.println("To center, go left.");
			drone.getCommandManager().goLeft(10).doFor(300);
			drone.getCommandManager().hover().doFor(500);
		} else if(y > ((screenHeight/2)-yIntervals)){
			if(height){
				return true;
			}
			System.out.println("To center, go up.");
			drone.getCommandManager().up(10).doFor(200);
			drone.getCommandManager().hover().doFor(500);
		} else if(y < ((screenHeight/2)+yIntervals)){
			if(height){
				return true;
			}
			System.out.println("To center, go down.");
			drone.getCommandManager().down(10).doFor(170);
			drone.getCommandManager().hover().doFor(500);
		}else{
			System.out.println("WE GOING FORWARD NOW BOIS!");
			drone.getCommandManager().hover().doFor(2000);
			return true;
		}
		return false;
	}
}