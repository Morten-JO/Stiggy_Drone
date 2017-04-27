package main;

import de.yadrone.base.ARDrone;

public class CircleARObject {
	
	public static final boolean DEBUG_MODE = true;
	
	public static boolean moveBasedOnLocation(ARDrone drone, double x, double y){
			if(x > 350){
				if(!DEBUG_MODE){
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
				} else{
					System.out.println("CENTER | GO RIGHT!");
				}
			} else if(x < 290){
				if(!DEBUG_MODE){
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
				} else{
					System.out.println("CENTER | GO LEFT!");
				}
			} else if(y > 210){
				if(!DEBUG_MODE){
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
				} else{
					System.out.println("CENTER | GO UP!");
				}
			} else if(y < 150){
				if(!DEBUG_MODE){
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
				} else{
					System.out.println("CENTER | GO UP!");
				}
			}
			if((x) > 290 && (x) < 350){
				if((y) < 210 && (y ) > 150){
					if(!DEBUG_MODE){
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
					} else{
						System.out.println("CENTER | GO CENTER!");
						return true;
					}
				}
			}
			return false;
	}
}