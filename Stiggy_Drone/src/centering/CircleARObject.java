package centering;

import controllers.BasicController;
import de.yadrone.base.ARDrone;
import de.yadrone.base.command.CalibrationCommand;
import de.yadrone.base.command.Device;
import helpers.Values;

public class CircleARObject {
	
	public static final boolean DEBUG_MODE = false;
	public static double screenWidth = 640;
	public static double screenHeight = 360;
	public static double xIntervals = 25;
	public static double yIntervals = 25;
	public static double orientation = 0;
	public static double oldTheta = 1 ;
	
	
	public static boolean moveBasedOnLocation(ARDrone drone, double x, double y, boolean height, int state){
			
		oldTheta = orientation;
		orientation = Values.THETA;
		
		if(x > ((screenWidth/2)+xIntervals)) {
			System.out.println("To center, go right.");
			drone.getCommandManager().goRight(Values.BASE_SPEED).doFor(200);
			drone.getCommandManager().hover().doFor(Values.BASE_SLEEP);
		} else if(x < ((screenWidth/2)-xIntervals)) {
			System.out.println("To center, go left.");
			drone.getCommandManager().goLeft(Values.BASE_SPEED).doFor(200);
			drone.getCommandManager().hover().doFor(Values.BASE_SLEEP);
		} else if(y > ((screenHeight/2)+yIntervals)) {
			if(height){
				return true;
			}
			System.out.println("To center, go down.");
			drone.getCommandManager().down(Values.BASE_SPEED).doFor(200);
			drone.getCommandManager().hover().doFor(Values.BASE_SLEEP);
		} else if(y < ((screenHeight/2)-yIntervals)){
			if(height){
				return true;
			}
			System.out.println("To center, go up.");
			drone.getCommandManager().up(Values.BASE_SPEED).doFor(200);
			drone.getCommandManager().hover().doFor(Values.BASE_SLEEP);
		} /*else if (orientation < 88 && state == BasicController.SEARCHQR && oldTheta != orientation) {
			System.out.println("To center, Spin left");
			drone.getCommandManager().spinLeft(Values.BASE_SPEED).doFor(200);
			drone.getCommandManager().hover().doFor(Values.BASE_SLEEP);
			drone.getCommandManager().goRight(Values.BASE_SPEED).after(500);
			drone.getCommandManager().hover().doFor(Values.BASE_SLEEP);
		} else if (orientation > 91   && state == BasicController.SEARCHQR && oldTheta != orientation) {
			System.out.println("To center, Spin right");
			drone.getCommandManager().spinRight(Values.BASE_SPEED).doFor(200);
			drone.getCommandManager().hover().doFor(Values.BASE_SLEEP);
			drone.getCommandManager().goLeft(Values.BASE_SPEED).after(500);
			drone.getCommandManager().hover().doFor(Values.BASE_SLEEP);
		} */else{
			System.out.println("Center is just infront of us!");
			drone.getCommandManager().hover().doFor(Values.BASE_SLEEP);
			return true;
		}
		return false;
	}
}