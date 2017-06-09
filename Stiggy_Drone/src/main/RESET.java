package main;

import de.yadrone.base.ARDrone;

public class RESET {

	public static void main(String[] args) {
		ARDrone drone = new ARDrone();
		drone.start();
		drone.getCommandManager().setSSIDSinglePlayer(""+System.nanoTime());
	}
	
}
