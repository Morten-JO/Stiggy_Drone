package centering;

import controllers.BasicController;
import de.yadrone.base.ARDrone;
import helpers.Values;

public class CircleARObject {
	
	public static final boolean DEBUG_MODE = false;
	public static double screenWidth = 640;
	public static double screenHeight = 360;
	public static double xIntervals = 35;
	public static double yIntervals = 35;
	public static double orientation = 0;
	public static double oldTheta = 1 ;
	
	
	public static boolean moveBasedOnLocation(ARDrone drone, double x, double y, boolean height, int state){
			
			oldTheta = orientation ;
		orientation = Values.THETA;
		
		System.out.println("BIAS:"+orientation);
		
		if (orientation < 88 && state == BasicController.SEARCHQR && oldTheta != orientation)
		{
			System.out.println("PaperChaseAutoController: Spin left");
			drone.getCommandManager().spinLeft(20).doFor(200);
			drone.getCommandManager().hover().doFor(500);
			drone.getCommandManager().goRight(20).after(700);
			drone.getCommandManager().hover().doFor(500);
		}
		else if (orientation > 91   && state == BasicController.SEARCHQR && oldTheta != orientation)
		{
			System.out.println("PaperChaseAutoController: Spin right");
			drone.getCommandManager().spinRight(20).doFor(200);
			drone.getCommandManager().hover().doFor(500);
			drone.getCommandManager().goLeft(20).after(700);
			drone.getCommandManager().hover().doFor(500);
		}
			
			else if(x > ((screenWidth/2)+xIntervals))
			{
			System.out.println("To center, go right.");
			drone.getCommandManager().goRight(10).doFor(200);
			drone.getCommandManager().hover().doFor(500);
		} 
			else if(x < ((screenWidth/2)-xIntervals))
		{
			System.out.println("To center, go left.");
			drone.getCommandManager().goLeft(10).doFor(200);
			drone.getCommandManager().hover().doFor(500);
		} 
			else if(y > ((screenHeight/2)+yIntervals))
			{
			if(height){
				return true;
			}
			System.out.println("To center, go down.");
			drone.getCommandManager().down(10).doFor(120);
			
			drone.getCommandManager().hover().doFor(500);
		} else if(y < ((screenHeight/2)-yIntervals)){
			if(height){
				return true;
			}
			System.out.println("To center, go up.");
			drone.getCommandManager().down(10).doFor(120);
			drone.getCommandManager().hover().doFor(500);
		}else{
			System.out.println("WE GOING FORWARD NOW BOIS!");
			if (Values.DISTANCE > 200){
				drone.getCommandManager().forward(20).doFor(300);
			}
			drone.getCommandManager().hover().doFor(1000);
			return true;
		}
		return false;
	}
}