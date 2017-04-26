package main;

import de.yadrone.base.ARDrone;

public class CircleARObject {

	public double horizontal = 0;
	public double vertical = 0;
	
	public boolean moveBasedOnLocation(ARDrone drone){
			if(horizontal > 350){
				drone.getCommandManager().goRight(35);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				drone.getCommandManager().hover();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(horizontal < 290){
				drone.getCommandManager().goLeft(35);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				drone.getCommandManager().hover();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(vertical > 210){
				drone.getCommandManager().up(35);
				try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				drone.getCommandManager().hover();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(vertical < 150){
				drone.getCommandManager().down(5);
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				drone.getCommandManager().hover();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if((horizontal) > 290 && (horizontal) < 350){
				if((vertical) < 210 && (vertical ) > 150){
					System.out.println("WE GOING FORWARD NOW BOIS!");
					drone.getCommandManager().forward(30);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					drone.getCommandManager().hover();
					return true;
				}
			}
			return false;
	}
}
