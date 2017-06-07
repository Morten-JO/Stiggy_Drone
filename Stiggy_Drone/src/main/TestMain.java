package main;

import de.yadrone.base.ARDrone;
import de.yadrone.base.command.CommandManager;

public class TestMain {

	public static void main(String[] args) {
		ARDrone drone = new ARDrone();
		drone.start();
		
		CommandManager cmd = drone.getCommandManager();
		drone.getCommandManager().setNavDataDemo(true);
		
		int speed = 50;
		long start = System.currentTimeMillis();
		cmd.takeOff().doFor(5000);
		
		//cmd.goLeft(speed).doFor(1000);
		System.out.println("FIRST HOVER");
		//cmd.hover().doFor(5000);
		
		//cmd.goRight(speed).doFor(1000);
		System.out.println("SECOND HOVER");
		//cmd.hover().doFor(5000);
		
		cmd.forward(speed).doFor(7000);
		System.out.println("THIRD HOVER");
		//cmd.hover().doFor(5000);
		
		//cmd.backward(speed).doFor(7000);
		System.out.println("FOURTH HOVER");
		//cmd.hover().doFor(5000);
		System.out.println("TIME TKAEN: "+(System.currentTimeMillis()-start));
		
		cmd.landing();
		
	}
	
}
