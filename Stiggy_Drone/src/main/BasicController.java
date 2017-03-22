package main;

import de.yadrone.base.IARDrone;

public class BasicController {

	private BasicMovements movement;
	
	public BasicController(IARDrone drone){
		this.movement = new BasicMovements(drone);
	}
	
	public void moveBasedOnCircleObject(CircleARObject obj){
		if(obj.horizontal > 5){
			movement.goLeft(500);
		} else if(obj.horizontal < -5){
			movement.goRight(500);
		} else if(obj.vertical > 5){
			movement.goDown(500);
		} else if(obj.vertical < -5){
			movement.goUp(500);
		}
	}
	
}
