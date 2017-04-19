package main;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;

public class BasicMovements {

	private ARDrone drone;
	private int speed;
	
	public BasicMovements(ARDrone drone){
		this.drone = drone;
	}
	
	public void goLeft(long millis){
		if(millis > 0){
			drone.getCommandManager().goLeft(speed).doFor(millis);
		} else{
			drone.getCommandManager().goLeft(speed);
		}
		
	}
	
	public void goRight(long millis){
		if(millis > 0){
			drone.getCommandManager().goRight(speed).doFor(millis);
		} else{
			drone.getCommandManager().goRight(speed);
		}
	}
	
	public void goBackward(long millis){
		if(millis > 0){
			drone.getCommandManager().backward(speed).doFor(millis);
		} else{
			drone.getCommandManager().backward(speed);
		}
	}
	
	public void goForward(long millis){
		if(millis > 0){
			drone.getCommandManager().forward(speed).doFor(millis);
		} else{
			drone.getCommandManager().forward(speed);
		}
	}
	
	public void spinRight(long millis){
		if(millis > 0){
			drone.getCommandManager().spinRight(speed).doFor(millis);
		} else{
			drone.getCommandManager().spinRight(speed);
		}
	}
	
	public void spinLeft(long millis){
		if(millis > 0){
			drone.getCommandManager().spinLeft(speed).doFor(millis);
		} else{
			drone.getCommandManager().spinLeft(speed);
		}
	}
	
	public void goUp(long millis){
		if(millis > 0){
			drone.getCommandManager().up(speed).doFor(millis);
		} else{
			drone.getCommandManager().up(speed);
		}
	}
	
	public void goDown(long millis){
		if(millis > 0){
			drone.getCommandManager().down(speed).doFor(millis);
		} else{
			drone.getCommandManager().down(speed);
		}
	}
	
	public void setSpeed(int speed){
		if(speed > 100 || speed < 0){
			return;
		}
		this.speed = speed;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	public ARDrone getDrone(){
		return drone;
	}
}
