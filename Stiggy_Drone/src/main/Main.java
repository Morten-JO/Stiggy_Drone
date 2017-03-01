package main;

import de.yadrone.apps.tutorial.TutorialAttitudeListener;
import de.yadrone.apps.tutorial.TutorialCommander;
import de.yadrone.apps.tutorial.TutorialVideoListener;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.IExceptionListener;
import de.yadrone.base.video.VideoManager;

public class Main {

	public static void main(String[] args) {
		IARDrone drone = null;
		try
		{
			// Tutorial Section 1
			drone = new ARDrone();
			drone.addExceptionListener(new IExceptionListener() {
				public void exeptionOccurred(ARDroneException exc)
				{
					exc.printStackTrace();
				}
			});
			
			drone.start();
			
			// Tutorial Section 2
			//new TutorialAttitudeListener(drone);
			
			// Tutorial Section 3
			new TutorialVideoListener(drone);
			
			// Tutorial Section 4
//			TutorialCommander commander = new TutorialCommander(drone);
//			commander.animateLEDs();
//			commander.takeOffAndLand();
//			commander.leftRightForwardBackward();
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
	
}
