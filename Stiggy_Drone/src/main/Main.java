package main;

import de.yadrone.apps.tutorial.TutorialAttitudeListener;
import de.yadrone.apps.tutorial.TutorialCommander;
import de.yadrone.apps.tutorial.TutorialVideoListener;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.IExceptionListener;

public class Main {

	public static void main(String[] args) {
		IARDrone drone = null;
		try {
			drone = new ARDrone();
			drone.addExceptionListener(new IExceptionListener() {
			public void exeptionOccurred(ARDroneException exc)
			{
				exc.printStackTrace();
			}
		});
		
		drone.start();
		
		TutorialCommander commander = new TutorialCommander(drone);
		commander.customPro();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
			if (drone != null) {
				drone.stop();
			}
			System.exit(0);
		}
	}
	
}
